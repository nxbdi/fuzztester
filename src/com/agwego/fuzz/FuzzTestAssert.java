package com.agwego.fuzz;

import org.junit.Assert;

public class FuzzTestAssert
{
	public static void assertEquals( StringBuilder expected, StringBuilder actual )
	{
		assertEquals( null, expected, actual );
	}

	public static void assertEquals( String message, StringBuilder expected, StringBuilder actual )
	{
		if( expected == null || actual == null )
			Assert.assertTrue( message, expected == actual );
		else
			Assert.assertEquals( message, expected.toString(), actual.toString() );
	}

}