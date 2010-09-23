package com.agwego.fuzz.examples;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

@RunWith( Parameterized.class )
public class FibonacciParameterizedTest
{
	@Parameterized.Parameters
	public static List<Object[]> data()
	{
		return Arrays.asList(
			new Object[][]
				{ { 0, 0 }, { 1, 1 }, { 2, 1 }, { 3, 2 }, { 4, 3 }, { 5, 5 }, { 6, 8 } } );
	}

	private int fInput;
	private int fExpected;

	public FibonacciParameterizedTest( int input, int expected )
	{
		fInput = input;
		fExpected = expected;
	}

	@Test
	public void testFib()
	{
		assertEquals( fExpected, GettingStarted.fibonacci( fInput ) );
	}
}