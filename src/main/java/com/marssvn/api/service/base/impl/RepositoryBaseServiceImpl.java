package com.marssvn.api.service.base.impl;

import com.marssvn.api.model.entity.SVNFile;
import com.marssvn.api.model.entity.SVNLockInfo;
import com.marssvn.api.model.entity.SVNTreeItem;
import com.marssvn.api.service.base.IRepositoryBaseService;
import com.marssvn.utils.common.StringUtils;
import com.marssvn.utils.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Repository Base Service
 * @author zhangkx
 */
@Service
public class RepositoryBaseServiceImpl extends BaseService implements IRepositoryBaseService {

    /**
     * Create empty directory
     *
     * @param svnurls SVNURL
     * @param comment String
     */
    @Override
    public void createDirectory(SVNURL[] svnurls, String comment) {

        try {
            FSRepositoryFactory.setup();
            DefaultSVNOptions svnOptions = SVNWCUtil.createDefaultOptions(true);
            SVNClientManager svnClientManager =
                    SVNClientManager.newInstance(svnOptions, "marssvn", null);
            svnClientManager.getCommitClient().doMkDir(svnurls, comment);
        } catch (SVNException e) {
            logger.error(e.getMessage());
            throw new BusinessException(message.error("repository.directory.add.failed"));
        }
    }

    /**
     * create svn repository on server
     *
     * @param repositoryName repository name
     * @return SVNURL
     */
    @Override
    public SVNURL createSvnRepository(String repositoryName) {
        try {
            // svn root path, here we use use home path
            String rootPath = System.getProperty("user.home");

            // svn repository folder path
            return SVNRepositoryFactory.createLocalRepository(
                    new File(rootPath + "/svn/" + repositoryName), true, false);

        } catch (SVNException e) {
            logger.error(e.getMessage());

            // svn: E204899: '/home/svn/#{repositoryName}' already exists; use 'force' to overwrite existing svnFiles
            if (204899 == e.getErrorMessage().getErrorCode().getCode()) {
                throw new BusinessException(message.error("repository.name.duplicate.create", repositoryName));
            }
            throw new BusinessException(message.error("repository.create.failed"));
        }
    }

    /**
     * get repository tree
     *
     * @param repositoryPath repository path
     * @param repositoryName repository name
     * @param path           path
     * @param getALl         get all
     * @return SVNDirectory
     */
    @Override
    public SVNTreeItem getRepositoryTree(String repositoryPath, String repositoryName, String path, Boolean getALl) {
        try {
            FSRepositoryFactory.setup();
            SVNURL svnurl = SVNURL.parseURIEncoded(repositoryPath);
            SVNRepository repository = SVNRepositoryFactory.create(svnurl);
            SVNTreeItem directory = new SVNTreeItem();
            directory.setName(repositoryName);
            directory.setRevision(repository.getLatestRevision());
            directory.setPath(path);
//            String svnRootPath = repository.getRepositoryRoot(false).toString();
//            directory.setFullPath(svnRootPath);

            // if path is not blank
            if (StringUtils.isNotBlank(path)) {
                SVNDirEntry svnDirEntry = repository.getDir(path, -1L, true, null);

                // if the path is a file
                if (svnDirEntry.getKind() == SVNNodeKind.FILE) {
                    throw new BusinessException(message.error("repository.path.not_directory", path));
                }

                // author, revision, commitMessage
                directory.copyPropertiesFrom(svnDirEntry);

                // date
                directory.setUpdatedAt(svnDirEntry.getDate());

                // name
                String[] strArr = path.split("/");
                directory.setName(strArr[strArr.length - 1]);

                // path and full path
                directory.setPath(path);
//                directory.setFullPath(svnRootPath + "/" + directory.getPath());

                // parent path
                directory.setParentPath(_getParentPath(path));
            }

            // get sub directories
            _getDirectory(repository, directory, getALl);

//            RepositoryTreeDTO repositoryTreeDTO = new RepositoryTreeDTO();
//            repositoryTreeDTO.setUuid(repository.getRepositoryUUID(false));
//            repositoryTreeDTO.setRoot(svnRootPath);
//            repositoryTreeDTO.setProtocol(svnurl.getProtocol());

            // root path = repository name
            if (StringUtils.isBlank(path)) {
                directory.setPath(repositoryName);
            }
//            repositoryTreeDTO.setTree(directory);
            return directory;
        } catch (SVNException e) {
            logger.error(e.getMessage());

            // svn: E160013: Attempted to open non-existent child node '#{path}'
            if (160013 == e.getErrorMessage().getErrorCode().getCode()) {
                throw new BusinessException(message.error("repository.path.not_exists", path));
            }
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public SVNFile getRepositoryFile(String repositoryPath, String filePath) {
        SVNFile svnFile = new SVNFile();
        try {
            FSRepositoryFactory.setup();
            SVNURL svnurl = SVNURL.parseURIEncoded(repositoryPath);
            SVNRepository repository = SVNRepositoryFactory.create(svnurl);

            // get file info
            SVNDirEntry svnDirEntry = repository.info(filePath, -1L);

            // if the path is a file
            if (svnDirEntry.getKind() != SVNNodeKind.FILE) {
                throw new BusinessException(message.error("repository.path.not_file", filePath));
            }

            // name, author, revision, size, commitMessage
            svnFile.copyPropertiesFrom(svnDirEntry);

            // date
            svnFile.setUpdatedAt(svnDirEntry.getDate());

            // extension
            String[] strArr = svnFile.getName().split("\\.");
            svnFile.setExtension(strArr[strArr.length - 1]);

            // path
            svnFile.setPath(filePath);

            // parent path
            svnFile.setParentPath(_getParentPath(filePath));

            // lock owner
            svnFile.setLock(_getLock(repository, filePath));

            // get content
            SVNProperties fileProperties = new SVNProperties();
            ByteArrayOutputStream contents = new ByteArrayOutputStream();
            repository.getFile(filePath, -1L, fileProperties, contents);
            String mimeType = fileProperties.getStringValue(SVNProperty.MIME_TYPE);
            if (SVNProperty.isTextMimeType(mimeType)) {
                svnFile.setContent(contents.toString());
            }

        } catch (SVNException e) {
            logger.error(e.getMessage());
        }
        return svnFile;
    }

    /**
     * get svn lock info
     * @param repository SVNRepository
     * @param filePath file path
     * @return SVNLockInfo
     */
    private SVNLockInfo _getLock(SVNRepository repository, String filePath) throws SVNException {
        SVNLock svnLock = repository.getLock(filePath);
        if (svnLock != null) {
            SVNLockInfo lockInfo = new SVNLockInfo();
            lockInfo.copyPropertiesFrom(svnLock);
            lockInfo.setCreatedAt(svnLock.getCreationDate());
            return lockInfo;
        }
        return null;
    }

    /**
     * get parent path
     * @param path repository path
     * @return string
     */
    private String _getParentPath(String path) {
        if (StringUtils.isBlank(path) || !path.contains("/")) {
            return "";
        }
        return path.substring(0, path.lastIndexOf("/"));
    }

    /**
     * get directory
     *
     * @param repository repository
     * @param svnDirectory directory
     * @param getALl       get all
     * @throws SVNException ex
     */
    private void _getDirectory(SVNRepository repository, SVNTreeItem svnDirectory, Boolean getALl) throws SVNException {
        String path = svnDirectory.getPath();
        String svnRootPath = repository.getRepositoryRoot(false).toString();
        List<SVNTreeItem> treeItemList = new ArrayList<>();
        List<SVNTreeItem> fileList = new ArrayList<>();

        // svn directories and files
        Collection entries = repository.getDir(path, -1L, null, (Collection) null);
        for (Object item : entries) {
            SVNDirEntry svnDirEntry = (SVNDirEntry) item;
            String realPath = StringUtils.isBlank(path) ? svnDirEntry.getName() : path + "/" + svnDirEntry.getName();
            logger.info(realPath + " (rev: " + svnDirEntry.getRevision() + ")");

            // if entry is directory
            if (svnDirEntry.getKind() == SVNNodeKind.DIR) {
                SVNTreeItem directory = new SVNTreeItem();

                // name, author, revision, commitMessage
                directory.copyPropertiesFrom(svnDirEntry);

                // date
                directory.setUpdatedAt(svnDirEntry.getDate());

                // path and full path
                directory.setPath(realPath);
//                directory.setFullPath(svnRootPath + "/" + realPath);

                // parent path
                directory.setParentPath(path);

                // get sub directories
                if (getALl) {
                    _getDirectory(repository, directory, getALl);
                }

                treeItemList.add(directory);
            } else {
                SVNTreeItem file = new SVNTreeItem();
                file.setType("file");

                // name, author, revision, size, commitMessage
                file.copyPropertiesFrom(svnDirEntry);

                // date
                file.setUpdatedAt(svnDirEntry.getDate());

                // extension
                String[] strArr = file.getName().split("\\.");
                file.setExtension(strArr[strArr.length - 1]);

                // path and full path
                file.setPath(realPath);
//                file.setFullPath(svnRootPath + "/" + realPath);

                // parent path
                file.setParentPath(path);

                // lock owner
                file.setLock(_getLock(repository, realPath));

                fileList.add(file);
            }
        }

        // sort by name asc
        treeItemList.sort(Comparator.comparing(SVNTreeItem::getName));
        fileList.sort(Comparator.comparing(SVNTreeItem::getName));

        treeItemList.addAll(fileList);
        svnDirectory.setChildren(treeItemList);
    }
}
