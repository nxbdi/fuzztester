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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

/**
 * @author Tim Desjardins
 * @version $Rev: 22 $
 * $Id: FileFilterPrePostTest.java 22 2010-04-01 04:56:35Z agwego $
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
	 * An example of mixing in a regular JUnit test
	 */
	@Test
	public void includeJunitTests()
	{
		assertTrue( true );		
	}
}