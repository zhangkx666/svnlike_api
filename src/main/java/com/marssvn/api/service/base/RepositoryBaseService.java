package com.marssvn.api.service.base;

import com.marssvn.api.model.dto.repository.RepositoryTreeDTO;
import com.marssvn.api.model.entity.SvnDirectory;
import com.marssvn.api.model.entity.SvnFile;
import com.marssvn.utils.common.StringUtils;
import com.marssvn.utils.exception.BusinessException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class RepositoryBaseService extends BaseService {

    /**
     * repository init comment
     */
    private static final String INIT_COMMENT = "init";

    /**
     * Create empty directory
     *
     * @param svnurl SVNURL
     * @param directoryName String
     */
    public void createDirectory(SVNURL svnurl, String directoryName, String comment) {
        try {
            SVNRepository repository = SVNRepositoryFactory.create(svnurl);
            repository.setAuthenticationManager(SVNWCUtil.createDefaultAuthenticationManager());
            ISVNEditor editor = repository.getCommitEditor(comment, null);
            editor.openRoot(-1L);
            editor.addDir(directoryName, null, -1L);
            editor.closeDir();
            editor.closeDir();
            editor.closeEdit();
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }

    /**
     * create svn repository on server
     *
     * @param repositoryName repository name
     * @return SVNURL
     */
    public SVNURL createSvnRepository(String repositoryName, boolean autoMakeDir) {
        try {
            // svn root path, here we use use home path
            String rootPath = System.getProperty("user.home");

            // svn repository folder path
            String svnPath = rootPath + "/svn/" + repositoryName;
            SVNURL svnurl = SVNRepositoryFactory.createLocalRepository(
                    new File(svnPath), true, false);

            // if autoMakeDir = true, create trunk, branches, tags directory
            if (autoMakeDir) {

                // create trunk
                createDirectory(svnurl, "trunk", INIT_COMMENT);

                // create branches
                createDirectory(svnurl, "branches", INIT_COMMENT);

                // create tags
                createDirectory(svnurl, "tags", INIT_COMMENT);
            }

            return svnurl;
        } catch (SVNException e) {
            logger.error(e.getMessage());

            // svn: E204899: '/home/svn/#{repositoryName}' already exists; use 'force' to overwrite existing svnFiles
            if (204899 == e.getErrorMessage().getErrorCode().getCode())
                throw new BusinessException(message.error("repository.name.duplicate.create", repositoryName));

            throw new BusinessException(message.error("repository.create.failed"));
        }
    }

    /**
     * getRepositoryTree
     * @param repositoryPath repository path
     * @param path path
     * @return SvnDirectory
     */
    public RepositoryTreeDTO getRepositoryTree(String repositoryPath, String repositoryName, String path) {
        try {
            SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(repositoryPath));
            repository.setAuthenticationManager(SVNWCUtil.createDefaultAuthenticationManager());

            SvnDirectory directory = new SvnDirectory();
            directory.setName(repositoryName);
            directory.setRevision(repository.getLatestRevision());
            directory.setPath(path);
            String svnRootPath = repository.getRepositoryRoot(false).toString();
            directory.setFullPath(svnRootPath);

            // if path is not blank
            if (StringUtils.isNotBlank(path)) {
                SVNDirEntry svnDirEntry = repository.getDir(path, -1L, true, null);

                String[] strArr = path.split("/");
                directory.setName(strArr[strArr.length - 1]);
                directory.setDate(svnDirEntry.getDate());
                directory.setAuthor(svnDirEntry.getAuthor());
                directory.setRevision(svnDirEntry.getRevision());
                directory.setPath(path);
                directory.setFullPath(svnRootPath + "/" + directory.getPath());
            }

            _getDirectory(repository, directory);

            RepositoryTreeDTO repositoryTreeDTO = new RepositoryTreeDTO();
            repositoryTreeDTO.setUuid(repository.getRepositoryUUID(false));
            repositoryTreeDTO.setRoot(svnRootPath);
            repositoryTreeDTO.setDirectory(directory);
            return repositoryTreeDTO;
        } catch (SVNException e) {
            e.printStackTrace();
            throw new BusinessException(message.error(e.getMessage()));
        }
    }

    private void _getDirectory(SVNRepository repository, SvnDirectory svnDirectory) throws SVNException {
        String path = svnDirectory.getPath();
        String svnRootPath = repository.getRepositoryRoot(false).toString();
        List<SvnDirectory> directoryList = new ArrayList<>();
        List<SvnFile> fileList = new ArrayList<>();

        // svn directories and files
        Collection entries = repository.getDir(path, -1L, null, (Collection) null);
        for (Object item : entries) {
            SVNDirEntry svnDirEntry = (SVNDirEntry) item;

            // if entry is directory
            if (svnDirEntry.getKind() == SVNNodeKind.DIR) {
                SvnDirectory directory = new SvnDirectory();
                directory.setName(svnDirEntry.getName());
                directory.setDate(svnDirEntry.getDate());
                directory.setAuthor(svnDirEntry.getAuthor());
                directory.setRevision(svnDirEntry.getRevision());
                directory.setPath(StringUtils.isBlank(path) ? svnDirEntry.getName() : path + "/" + svnDirEntry.getName());
                directory.setFullPath(svnRootPath + "/" + directory.getPath());

                // get sub directories
                _getDirectory(repository, directory);

                directoryList.add(directory);
            } else {
                SvnFile file = new SvnFile();
                file.setName(svnDirEntry.getName());
                String[] strArr = file.getName().split("\\.");
                file.setExtension(strArr[strArr.length - 1]);
                file.setDate(svnDirEntry.getDate());
                file.setAuthor(svnDirEntry.getAuthor());
                file.setRevision(svnDirEntry.getRevision());
                file.setPath(StringUtils.isBlank(path) ? svnDirEntry.getName() : path + "/" + svnDirEntry.getName());
                file.setFullPath(svnRootPath + "/" + file.getPath());
                file.setSize(svnDirEntry.getSize());

                SVNLock svnLock = svnDirEntry.getLock();
                if (svnLock != null) {
                    file.setLockOwner(svnLock.getOwner());
                }
                file.setCommitMessage(svnDirEntry.getCommitMessage());
                fileList.add(file);
            }
        }
        svnDirectory.setChildren(directoryList);
        svnDirectory.setFiles(fileList);
    }
}
