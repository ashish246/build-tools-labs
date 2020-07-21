package au.com.intershop.ant.git;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

public class GitCheckoutTask extends AbstractGitTask
{
	private String mBranch;

	private boolean mCreate = false;

	public void setBranch( String pBranch )
	{
		mBranch = pBranch;
	}

	public void setCreate( String pCreate )
	{
		mCreate = pCreate != null && pCreate.toLowerCase().equals( "true" );
	}

	@Override
	public void execute() throws BuildException
	{
		if ( mRepository == null || mRepository.trim().isEmpty() )
		{
			throw new BuildException( "Parameter 'respository' not set or is invalid." );
		}

		try
		{
			Git tGit = Git.open( new File( mRepository ) );

			// Check if the branch exists locally
			boolean tBranchExistsLocal = false;

			for ( Ref tRef : tGit.branchList().call() )
			{
				if ( tRef.getName().endsWith( "/".concat( mBranch ) ) )
				{
					tBranchExistsLocal = true;
				}
			}

			// Check if the branch exists remotely
			boolean tBranchExistsRemote = false;

			for ( Ref tRef : tGit.branchList().setListMode( ListMode.REMOTE ).call() )
			{
				if ( tRef.getName().endsWith( "/".concat( mBranch ) ) )
				{
					tBranchExistsRemote = true;
				}
			}

			if ( tBranchExistsLocal )
			{
				CheckoutCommand tCmd = tGit.checkout();
				tCmd.setName( mBranch ).setCreateBranch( mCreate ).call();
				echo( "Switched to local branch '".concat( mBranch ).concat( "'." ) );
			}
			else if ( tBranchExistsRemote )
			{
				CheckoutCommand tCmd = tGit.checkout();
				tCmd.setCreateBranch( true );
				tCmd.setName( mBranch );

				tCmd.setUpstreamMode( SetupUpstreamMode.TRACK );
				tCmd.setStartPoint( "origin/" + mBranch );

				tCmd.call();

				echo( "Created new local branch '".concat( mBranch ).concat( "' from remote branch 'origin/" ).concat( mBranch ).concat( "'." ) );
			}
			else if ( mCreate )
			{
				CheckoutCommand tCmd = tGit.checkout();
				tCmd.setName( mBranch ).setCreateBranch( true ).call();

				echo( "Created new local branch '".concat( mBranch ).concat( "'." ) );
			}
			else
			{
				throw new BuildException( "No new branch checked out or created as it is not available remotely and a local creation has not be permitted." );
			}
		}
		catch ( GitAPIException gitApiEx )
		{
			throw new BuildException( "Could not pull repository: " + gitApiEx.getMessage(), gitApiEx );
		}
		catch ( IOException ioEx )
		{
			throw new BuildException( "Could not pull repository: " + ioEx.getMessage(), ioEx );
		}
	}
}
