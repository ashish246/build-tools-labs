package au.com.intershop.ant.git;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class GitFindGitDirTask extends Task
{
	private String mFolder;

	private String mPropertyName;

	/**
	 * Set the name of the property in which the path to the GIT repository is saved.
	 * 
	 * @param pPropertyName
	 */
	public void setProperty( String pPropertyName )
	{
		mPropertyName = pPropertyName;
	}

	/**
	 * Set the path from where to find a git repository.
	 * 
	 * @param pFolder
	 */
	public void setFolder( String pFolder )
	{
		mFolder = pFolder;
	}

	private String findGitDir() throws BuildException
	{
		super.execute();

		if ( mFolder == null || mFolder.isEmpty() )
		{
			throw new BuildException( "Attribute 'folder' is mandatory and must not be empty." );
		}

		FileRepositoryBuilder builder = new FileRepositoryBuilder();

		File tRepository = null;

		tRepository = builder.findGitDir( new File( mFolder ) ).getGitDir();

		return tRepository.toString();
	}

	@Override
	public void execute() throws BuildException
	{
		getProject().setNewProperty( mPropertyName, findGitDir() );
	}
}
