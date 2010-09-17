/*
 * Copyright (c) 2010. Agwego Enterprises Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * If you modify this software a credit would be nice
 */

package com.agwego.fuzz.examples;

import com.agwego.fuzz.FuzzTester;
import com.agwego.fuzz.annotations.Fuzz;
import com.agwego.fuzz.annotations.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

/**
 * Examples
 * 
 * @author Tim Desjardins
 * @version $Rev:$
 * <br>
 * $Id: $
 */
@RunWith( FuzzTester.class )
@Parameters( TestDirectory = "test/com/agwego/fuzz/examples", Prefix = "GettingStarted" )
public class GettingStartedTest
{
    /**
     * A normal @fuzz test
     *
     * @param input the test string
     * @param endIndex to truncate at (no primitives are allowed for parameters)
     * @param expected the result we're looking for
     */
	@Fuzz
	public void truncExample( final String input, final Integer endIndex, final String expected )
	{
		assertEquals( expected, GettingStarted.trunc( input, endIndex ));
	}

	/**
	 * Illustrate testing exceptions
	 *
	 * @param input the test string
	 * @param expected the result we're looking for
	 * @throws Exception to keep java happy
	 */
	@Fuzz
	public void exceptionExample( final String input, final String expected ) throws Exception
	{
		assertEquals( GettingStarted.except( input ), expected );
	}

	/**
	 * Simple objects
	 * 
	 * @param pl - a simple object, note that GSON requires a no-args constructor
	 * @param x - a multiplier
	 * @param intObjExpected - the expected int value
	 * @param boolObjExpected - the expected boolean value
	 */
	@Fuzz
	public void simpleObject( final SimpleObject pl, final Integer x, final Integer intObjExpected, final Boolean boolObjExpected )
	{
		GettingStarted gs = new GettingStarted();
		SimpleObject upl = gs.update( pl, x );
		assertEquals( intObjExpected, upl.getIntObj() );
		assertEquals( boolObjExpected, upl.getBoolObj() );
	}

	/**
	 * Simple final object
	 *
	 * @param pl - a simple object, note that GSON requires a no-args constructor
	 * @param x - a multiplier
	 * @param intObjExpected - the expected int value
	 * @param boolObjExpected - the expected boolean value
	 */
	@Fuzz
	public void simpleObjectConstructor( final SimpleObjectConstructor pl, final Integer x, final Integer intObjExpected, final Boolean boolObjExpected )
	{
		GettingStarted gs = new GettingStarted();
		SimpleObjectConstructor upl = gs.updateConstructor( pl, x );
		assertEquals( intObjExpected, upl.getIntObj() );
		assertEquals( boolObjExpected, upl.getBoolObj() );
	}

	/**
	 * Simple test of parent/child objects
	 *
	 * @param p - parent object
	 * @param x - multiplier
	 * @param expected - the expected result
	 */
	@Fuzz
	public void parentChild( final Parent p, final Double x, final Double expected )
	{
		GettingStarted gs = new GettingStarted();
		
		assertEquals( expected, gs.calculate( p, x ) );
	}

	/**
	 * An example of mixing in a regular JUnit test
	 */
	@Test
	public void includeJunitTests()
	{
		assertTrue( true );		
	}
}