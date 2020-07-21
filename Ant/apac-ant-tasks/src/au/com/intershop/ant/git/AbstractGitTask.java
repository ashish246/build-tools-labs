package au.com.intershop.ant.git;

import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.OperationResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.StringUtils;

import au.com.intershop.ant.AbstractAPACTask;

/**
 * @author Sergey Bogutskiy
 */
public abstract class AbstractGitTask extends AbstractAPACTask
{

	protected String mUsername;

	protected String mPassword;

	protected String mRepository;

	protected String mRemote;

	public void setRepository( String pRepository )
	{
		mRepository = pRepository;
	}

	protected boolean isCredentialsValid()
	{
		return !StringUtils.isEmptyOrNull( mUsername ) && mPassword != null;
	}

	protected CredentialsProvider getDefaultCredentialsProvider()
	{
		if ( isCredentialsValid() )
		{
			return new UsernamePasswordCredentialsProvider( mUsername, mPassword );
		}
		else
		{
			return null;
		}
	}

	protected void logResults( Iterable < ? extends OperationResult > results )
	{
		echo( "Result: " );
		for ( OperationResult result : results )
		{
			echo( result.getMessages() );
		}
	}

	public void setUsername( String pUsername )
	{
		mUsername = pUsername;
	}

	public void setPassword( String pPassword )
	{
		mPassword = pPassword;
	}
	
	public void setRemote( String pRemote )
	{
		mRemote = pRemote;
	}
}
