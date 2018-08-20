package com.marssvn.api.service.base;

import com.marssvn.api.model.dto.repository.response.RepositoryTreeDTO;
import com.marssvn.api.model.entity.SVNDirectory;
import com.marssvn.api.model.entity.SVNFile;
import com.marssvn.api.model.entity.SVNLockInfo;
import com.marssvn.utils.common.StringUtils;
import com.marssvn.utils.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class RepositoryBaseService extends BaseService {

    /**
     * Create empty directory
     *
     * @param svnurls SVNURL
     * @param comment String
     */
    public void createDirectory(SVNURL[] svnurls, String comment) throws BusinessException {

        try {
//            SVNRepository repository = SVNRepositoryFactory.create(svnurl);
//            repository.setAuthenticationManager(SVNWCUtil.createDefaultAuthenticationManager());
//            ISVNEditor editor = repository.getCommitEditor(comment, null);
//            editor.openRoot(-1L);
//            editor.addDir(directoryName, null, -1L);
//            editor.closeDir();
//            editor.closeDir();
//            editor.closeEdit();

//            FSRepositoryFactory.setup();
            DefaultSVNOptions svnOptions = SVNWCUtil.createDefaultOptions(true);
            SVNClientManager svnClientManager = SVNClientManager.newInstance(svnOptions, "marssvn", null);
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
            if (204899 == e.getErrorMessage().getErrorCode().getCode())
                throw new BusinessException(message.error("repository.name.duplicate.create", repositoryName));

            throw new BusinessException(message.error("repository.create.failed"));
        }
    }

    /**
     * get repository tree
     *
     * @param repositoryPath repository path
     * @param path           path
     * @return SVNDirectory
     */
    public RepositoryTreeDTO getRepositoryTree(String repositoryPath, String repositoryName, String path) {
        try {
            FSRepositoryFactory.setup();
            SVNURL svnurl = SVNURL.parseURIEncoded(repositoryPath);
            SVNRepository repository = SVNRepositoryFactory.create(svnurl);
            SVNDirectory directory = new SVNDirectory();
            directory.setName(repositoryName);
            directory.setRevision(repository.getLatestRevision());
            directory.setPath(path);
            String svnRootPath = repository.getRepositoryRoot(false).toString();
            directory.setFullPath(svnRootPath);

            // if path is not blank
            if (StringUtils.isNotBlank(path)) {
                SVNDirEntry svnDirEntry = repository.getDir(path, -1L, true, null);

                // if the path is a file
                if (svnDirEntry.getKind() == SVNNodeKind.FILE)
                    throw new BusinessException(message.error("repository.path.not_directory", path));

                // author, revision, commitMessage
                directory.copyPropertiesFrom(svnDirEntry);

                // date
                directory.setUpdatedAt(svnDirEntry.getDate());

                // name
                String[] strArr = path.split("/");
                directory.setName(strArr[strArr.length - 1]);

                // path and full path
                directory.setPath(path);
                directory.setFullPath(svnRootPath + "/" + directory.getPath());
            }

            // get sub directories
            _getDirectory(repository, directory);

            RepositoryTreeDTO repositoryTreeDTO = new RepositoryTreeDTO();
            repositoryTreeDTO.setUuid(repository.getRepositoryUUID(false));
            repositoryTreeDTO.setRoot(svnRootPath);
            repositoryTreeDTO.setProtocol(svnurl.getProtocol());
            repositoryTreeDTO.setTree(directory);
            return repositoryTreeDTO;
        } catch (SVNException e) {
            logger.error(e.getMessage());
            if (160013 == e.getErrorMessage().getErrorCode().getCode())
                throw new BusinessException(message.error("repository.path.not_exists", path));

            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * get directory
     *
     * @param repository repository
     * @param svnDirectory directory
     * @throws SVNException ex
     */
    private void _getDirectory(SVNRepository repository, SVNDirectory svnDirectory) throws SVNException {
        String path = svnDirectory.getPath();
        String svnRootPath = repository.getRepositoryRoot(false).toString();
        List<SVNDirectory> directoryList = new ArrayList<>();
        List<SVNFile> fileList = new ArrayList<>();

        // svn directories and files
        Collection entries = repository.getDir(path, -1L, null, (Collection) null);
        for (Object item : entries) {
            SVNDirEntry svnDirEntry = (SVNDirEntry) item;
            String realPath = StringUtils.isBlank(path) ? svnDirEntry.getName() : path + "/" + svnDirEntry.getName();

            // if entry is directory
            if (svnDirEntry.getKind() == SVNNodeKind.DIR) {
                SVNDirectory directory = new SVNDirectory();

                // name, author, revision, commitMessage
                directory.copyPropertiesFrom(svnDirEntry);

                // date
                directory.setUpdatedAt(svnDirEntry.getDate());

                // path and full path
                directory.setPath(realPath);
                directory.setFullPath(svnRootPath + "/" + realPath);

                // get sub directories
                _getDirectory(repository, directory);

                directoryList.add(directory);
            } else {
                SVNFile file = new SVNFile();

                // name, author, revision, size, commitMessage
                file.copyPropertiesFrom(svnDirEntry);

                // data
                file.setUpdatedAt(svnDirEntry.getDate());

                // extension
                String[] strArr = file.getName().split("\\.");
                file.setExtension(strArr[strArr.length - 1]);

                // path and full path
                file.setPath(realPath);
                file.setFullPath(svnRootPath + "/" + realPath);

                // lock owner
                SVNLock svnLock = repository.getLock(realPath);
                if (svnLock != null) {
                    SVNLockInfo lockInfo = new SVNLockInfo();
                    lockInfo.copyPropertiesFrom(svnLock);
                    lockInfo.setCreatedAt(svnLock.getCreationDate());
                    file.setLock(lockInfo);
                }
                fileList.add(file);
            }
        }
        svnDirectory.setChildren(directoryList);
        svnDirectory.setFiles(fileList);
    }
}
