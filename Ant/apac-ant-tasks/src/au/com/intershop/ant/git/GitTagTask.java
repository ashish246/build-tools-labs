package au.com.intershop.ant.git;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.eclipse.jgit.api.DeleteTagCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TagCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;

public class GitTagTask extends AbstractGitTask
{
	private String mCreate;

	private String mDelete;

	public void setCreate( String pCreate )
	{
		mCreate = pCreate;
	}

	public void setDelete( String pDelete )
	{
		mDelete = pDelete;
	}

	@Override
	public void execute() throws BuildException
	{
		super.execute();

		if ( isTrimmedEmpty( mRepository ) )
		{
			throw new BuildException( "Parameter 'respository' not set or is invalid." );
		}

		if ( isTrimmedEmpty( mCreate ) && isTrimmedEmpty( mDelete ) )
		{
			throw new BuildException( "Neither parameter 'create' nor 'delete' is set." );
		}

		try
		{
			Git tGit = Git.open( new File( mRepository ) );

			try
			{
				if ( !isTrimmedEmpty( mDelete ) )
				{
					DeleteTagCommand tCmd = tGit.tagDelete();

					tCmd.setTags( mDelete );

					tCmd.call();

					echo( "Tag '".concat( mDelete ).concat( "' deleted." ) );
				}

				if ( !isTrimmedEmpty( mCreate ) )
				{
					TagCommand tCmd = tGit.tag();

					tCmd.setName( mCreate );

					tCmd.call();

					echo( "Tag '".concat( mCreate ).concat( "' created." ) );
				}
			}
			catch ( RefAlreadyExistsException raeEx )
			{
				echo( "WARNING: tag with name '".concat( mCreate ).concat( "' already exists. Skipping creation." ) );
			}
			catch ( GitAPIException gitApiEx )
			{
				throw new BuildException( "Could not tag repository: " + gitApiEx.getMessage(), gitApiEx );
			}
			finally
			{
				tGit.close();
			}
		}
		catch ( IOException ioEx )
		{
			throw new BuildException( "Could not tag repository: " + ioEx.getMessage(), ioEx );
		}
	}
}
