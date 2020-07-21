package au.com.intershop.ant.git;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.TransportException;

/**
 * @author Sergey Bogutskiy
 */
public class GitPullTask extends AbstractGitTask
{
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
			PullCommand tPullCommand = tGit.pull();

			tPullCommand.setRemote( mRemote );
			
			if ( isCredentialsValid() )
			{
				tPullCommand.setCredentialsProvider( getDefaultCredentialsProvider() );
			}

			tPullCommand.call();
		}
		catch ( TransportException transEx )
		{
			if ( transEx.getMessage().contains( "fetch" ) )
			{
				echo( "Fetch failed, check if .git/config contains a fetch configuration line for the 'origin' remote similar to" );
				echo( "fetch = +refs/heads/*:refs/remotes/origin/*" );
			}

			throw new BuildException( "Could not pull repository: " + transEx.getMessage(), transEx );
		}
		catch ( JGitInternalException jgiEx )
		{
			echo("WARNING: ".concat( jgiEx.getMessage() ) );
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