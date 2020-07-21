package au.com.intershop.ant.git;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * This task provides an easy way to retrieve the short revision hash for a
 * given GIT repository.
 * 
 * @author Hendrik Brandt
 */
public class GitRevParseTask extends AbstractGitTask
{
    public static final int ABBREVIATE_LENGTH = 10;

    // private String mFolder;

    private String mPropertyName;

    /**
     * Set the name of the property in which the hash is saved.
     * 
     * @param pPropertyName
     */
    public void setProperty(String pPropertyName)
    {
        mPropertyName = pPropertyName;
    }

    /**
     * Set the GIT repository folder (.git) to retrieve the revision hash
     * from. If the provided folder path does not contain a .git entry the .git
     * is automatically appended to the string.
     * 
     * @param pFolder
     */
    /*
     * public void setFolder(String pFolder) { mFolder = pFolder; }
     */

    protected String createHash()
    {
        // final SimpleDateFormat tSDF = new SimpleDateFormat("yyyyMMddHHmm");

        // we use the current timestamp as a fall-back if GIT hash can not be
        // retrieved
        String tAbbreviatedHash = null;

        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try
        {
            File tRepositoryFolder = builder.findGitDir(new File(mRepository)).getGitDir();
            /*
             * Set the GIT repository folder (.git) to retrieve the revision
             * hash from. If the provided folder path does not contain a .git
             * entry the .git is automatically appended to the string.
             */
            Repository tRepository = builder.findGitDir(tRepositoryFolder).build();

            Ref tHeadRef = tRepository.getRef(tRepository.getFullBranch());

            if (tHeadRef == null)
            {
                log("HEAD revision is null. This indicates that the GIT repository could not be found.");
            }
            else
            {
                RevWalk tRevWalk = new RevWalk(tRepository);
                RevCommit tHeadCommit = tRevWalk.parseCommit(tRepository.resolve(Constants.HEAD));
                
                tAbbreviatedHash = tHeadRef.getObjectId().abbreviate(ABBREVIATE_LENGTH).name();

                echo("tAbbreviatedName -> " + tAbbreviatedHash);
                echo("FullName -> " + tHeadRef.getObjectId().getName());
                echo("HeadCommitID -> " + tHeadCommit.getId().toString());
                echo("HeadCommitName -> " + tHeadCommit.getName().toString());
            }
        }
        catch(IOException ioEx)
        {
            log(ioEx.getMessage());
        }

        return tAbbreviatedHash;
    }

    @Override
    public void execute() throws BuildException
    {
        if (mRepository == null || mRepository.trim().isEmpty())
        {
            throw new BuildException("Parameter 'respository' not set or is invalid.");
        }

        if (mRemote == null || mRemote.trim().isEmpty())
        {
            throw new BuildException("Parameter 'remote' not set or is invalid.");
        }

        getProject().setNewProperty(mPropertyName, createHash());
    }

    /*
     * public static void main(String[] args) { GitRevParseTask tSelf = new
     * GitRevParseTask(); tSelf.setFolder(
     * "/home/hbrandt/Projects/auspost-pcc/project-auspost-pcc/build/latest/");
     * tSelf.setProperty("testProperty");
     * System.out.println(tSelf.createHash()); }
     */

}
