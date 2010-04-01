package com.agwego.fuzz;

import java.util.Collection;
import static org.junit.Assert.*;

public class Assert
{
	static public void assertNotEmpty( Collection collection )
	{
		assertNotEmpty( "Collection is not empty", collection );
	}

	static public void assertNotEmpty( String message, Collection collection )
	{
		assertTrue( message, ! collection.isEmpty() );
	}
}