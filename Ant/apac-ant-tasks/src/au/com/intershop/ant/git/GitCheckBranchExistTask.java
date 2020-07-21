package au.com.intershop.ant.git;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

public class GitCheckBranchExistTask extends AbstractGitTask
{
	private String mBranch;

	private String mPropertyName;

	public void setBranch( String pBranch )
	{
		mBranch = pBranch;
	}

	/**
	 * Set the name of the property in which the path to the GIT repository is saved.
	 * 
	 * @param pPropertyName
	 */
	public void setProperty( String pPropertyName )
	{
		mPropertyName = pPropertyName;
	}

	@Override
	public void execute() throws BuildException
	{
		super.execute();

		boolean tExists = false;

		try
		{
			Git tGit = Git.open( new File( mRepository ) );

			ListBranchCommand tCmd = tGit.branchList();

			List < Ref > tBranchList = tCmd.setListMode( ListMode.ALL ).call();

			for ( Ref tRef : tBranchList )
			{
				if ( tRef.getName().endsWith( "/".concat( mBranch ) ) )
				{
					tExists = true;
				}
			}

		}
		catch ( IOException | GitAPIException ioEx )
		{
			throw new BuildException( "Could not pull repository: " + ioEx.getMessage(), ioEx );
		}

		getProject().setNewProperty( mPropertyName, String.valueOf( tExists ) );

	}
}
