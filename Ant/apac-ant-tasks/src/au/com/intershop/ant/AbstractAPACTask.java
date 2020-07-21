package au.com.intershop.ant;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Echo;

public abstract class AbstractAPACTask extends Task
{
	protected void echo( String pMessage )
	{
		Echo tEcho = new Echo();
		tEcho.setMessage( pMessage );
		tEcho.execute();
	}

	/**
	 * Check if a {@link String} is not null and not trimmed empty.
	 * 
	 * @return
	 */
	protected static boolean isTrimmedEmpty( String pString )
	{
		if ( pString == null )
		{
			return true;
		}

		int length = pString.length();

		if ( length == 0 )
		{
			return true;
		}

		for ( int i = 0; i < length; i++ )
		{
			if ( !Character.isWhitespace( pString.charAt( i ) ) )
			{
				return false;
			}
		}

		return true;
	}
}
