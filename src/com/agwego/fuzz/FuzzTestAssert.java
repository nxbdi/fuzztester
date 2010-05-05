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

package com.agwego.fuzz;

import org.junit.Assert;

/**
 * Assert Helpers
 *
 * @author Tim Desjardins
 * @version $Rev$
 * <br/>
 * $Id$
 */
public class FuzzTestAssert
{
	/**
	 * Assert StringBuilders are equals with no message
	 *
	 * @param expected value
	 * @param actual the test value
	 */
	public static void assertEquals( StringBuilder expected, StringBuilder actual )
	{
		assertEquals( null, expected, actual );
	}

	/**
	 * Assert StringBuilders are equals with no message
	 *
	 * @param message message to present if there's an error
	 * @param expected value
	 * @param actual the test value
	 */
	public static void assertEquals( String message, StringBuilder expected, StringBuilder actual )
	{
		if( expected == null || actual == null )
			Assert.assertTrue( message, expected == actual );
		else
			Assert.assertEquals( message, expected.toString(), actual.toString() );
	}

}