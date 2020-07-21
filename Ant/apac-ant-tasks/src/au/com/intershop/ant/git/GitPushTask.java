package au.com.intershop.ant.git;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RefSpec;

public class GitPushTask extends AbstractGitTask
{
	private boolean mDryRun = false;

	private String mRemoteBranch = null;
	
	public void setDryrun( String pDryRun )
	{
		mDryRun = pDryRun != null && pDryRun.toLowerCase().equals( "true" );
	}

	public void setRemotebranch(String pRemoteBranch)
	{
		mRemoteBranch = pRemoteBranch;
	}
	
	@Override
	public void execute() throws BuildException
	{
		if ( mRepository == null || mRepository.trim().isEmpty() )
		{
			throw new BuildException( "Parameter 'respository' not set or is invalid." );
		}

		if ( mRemote == null || mRemote.trim().isEmpty() )
		{
			throw new BuildException( "Parameter 'remote' not set or is invalid." );
		}

		try
		{
			Git tGit = Git.open( new File( mRepository ) );
			String tLocalBranch = tGit.getRepository().getBranch();

			if (mDryRun)
			{
				echo("This push is a dry run! No remote changes will be performed.");
			}
			
			try
			{
				PushCommand tPushCommand = tGit.push();

				tPushCommand.setRemote( mRemote );

				if ( isCredentialsValid() )
				{
					tPushCommand.setCredentialsProvider( getDefaultCredentialsProvider() );
				}

				if (mRemoteBranch == null || mRemoteBranch.trim().isEmpty())
				{
					mRemoteBranch = tLocalBranch;
				}
				
				tPushCommand.setRefSpecs( new RefSpec("refs/heads/".concat(tLocalBranch).concat(":refs/heads/").concat( mRemoteBranch )) );
				
				Iterable < PushResult > pushResults = tPushCommand.setForce( false ).setDryRun(mDryRun).call();
				logResults( pushResults );

			}
			catch ( TransportException transEx )
			{
				if ( transEx.getMessage().contains( "fetch" ) )
				{
					echo( "Push failed, check if .git/config contains a fetch configuration line for the 'origin' remote similar to" );
					echo( "push = refs/heads/".concat( tLocalBranch ).concat( ":refs/heads/" ).concat( tLocalBranch ) );
				}

				throw new BuildException( "Could not pull repository: " + transEx.getMessage(), transEx );
			}
			catch ( JGitInternalException jgiEx )
			{
				throw new BuildException( "Could not push repository: " + jgiEx.getMessage(), jgiEx );
			}
			catch ( GitAPIException gitApiEx )
			{
				throw new BuildException( "Could not push repository: " + gitApiEx.getMessage(), gitApiEx );
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
