package au.com.intershop.ant.git;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.eclipse.jgit.api.DeleteBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.CannotDeleteCurrentBranchException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NotMergedException;
import org.eclipse.jgit.transport.RefSpec;

public class GitRemoveBranchTask extends AbstractGitTask
{
	private String mRemove;

	private String mRemoteRemove;

	public void setRemove( String pRemove )
	{
		mRemove = pRemove;
	}

	public void setRemoteremove( String pRemoteRemove )
	{
		mRemoteRemove = pRemoteRemove;
	}

	@Override
	public void execute() throws BuildException
	{
		if ( isTrimmedEmpty( mRepository ) )
		{
			throw new BuildException( "Parameter 'respository' not set or is invalid." );
		}

		if ( isTrimmedEmpty( mRemote ) )
		{
			throw new BuildException( "Parameter 'remote' not set or is invalid." );
		}

		if ( isTrimmedEmpty( mRemove ) && isTrimmedEmpty( mRemoteRemove ) )
		{
			throw new BuildException( "Neither 'remove' nor 'remoteremove' has been specified." );
		}

		try
		{
			Git tGit = Git.open( new File( mRepository ) );

			try
			{
				if ( !isTrimmedEmpty( mRemove ) )
				{
					if ( mRemove.toLowerCase().equals( "master" ) )
					{
						throw new BuildException( "You can not remove the branch 'master'." );
					}

					DeleteBranchCommand tCmd = tGit.branchDelete();

					tCmd.setBranchNames( mRemove );

					tCmd.call();

					echo( "Local branch '".concat( mRemove ).concat( "' deleted." ) );
				}

				if ( !isTrimmedEmpty( mRemoteRemove ) )
				{
					if ( mRemove.toLowerCase().equals( "master" ) )
					{
						throw new BuildException( "You can not remove the remote branch 'master'." );
					}

					PushCommand tCmd = tGit.push();

					if ( isCredentialsValid() )
					{
						tCmd.setCredentialsProvider( getDefaultCredentialsProvider() );
					}

					RefSpec tRefSpec = new RefSpec().setSource( null ).setDestination( "refs/heads/".concat( mRemoteRemove ) );

					tCmd.setRefSpecs( tRefSpec );
					tCmd.setRemote( mRemote );
					tCmd.call();

					echo( "Remote branch '".concat( mRemoteRemove ).concat( "' deleted from remote '".concat( mRemote ).concat( "'." ) ) );
				}
			}
			catch ( CannotDeleteCurrentBranchException cdcbEx )
			{
				throw new BuildException( "You can not delete the current working branch. Checkout a different branch first.", cdcbEx );
			}
			catch ( NotMergedException nmEx )
			{
				throw new BuildException( "Branch cannot be deleted as it contains unmerged changes..", nmEx );
			}
			catch ( JGitInternalException jgiEx )
			{
				throw new BuildException( "Could not delete branch: " + jgiEx.getMessage(), jgiEx );
			}
			catch ( GitAPIException gitApiEx )
			{
				throw new BuildException( "Could not delete branch: " + gitApiEx.getMessage(), gitApiEx );
			}
			finally
			{
				tGit.close();
			}
		}
		catch ( IOException ioEx )
		{
			throw new BuildException( "Could not push repository: " + ioEx.getMessage(), ioEx );
		}
	}
}
