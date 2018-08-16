package com.marssvn.api.service.base;

import com.marssvn.utils.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.io.File;

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
            SVNURL svnurl = SVNRepositoryFactory.createLocalRepository(new File(svnPath), true, false);

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

            // svn: E204899: '/home/svn/#{repositoryName}' already exists; use 'force' to overwrite existing files
            if (204899 == e.getErrorMessage().getErrorCode().getCode())
                throw new BusinessException(message.error("repository.name.duplicate.create", repositoryName));

            throw new BusinessException(message.error("repository.create.failed"));
        }
    }
}
