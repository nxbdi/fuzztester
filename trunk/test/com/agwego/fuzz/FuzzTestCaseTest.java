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

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * @author Tim Desjardins
 * @version $Rev:$
 *
 * $Id: $
 */
public class FuzzTestCaseTest
{
	@Test
	public void incorrectMethodSignature()
	{
        Boolean exceptionCaught = false;
        try {
		    FuzzTestCase.getMethodParams( com.agwego.fuzz.fuzz_tester_test.FuzzTestCaseTest.class, "mockTest", 2 );
        } catch( Throwable ex ) {
            exceptionCaught = true;
            assertEquals( "Method 'mockTest' with incorrect argument signature", ex.getMessage() );
        }
        assertTrue( exceptionCaught );
	}

    @Test
    public void tooManyMethodSignatures()
    {
        Boolean exceptionCaught = false;
        try {
            FuzzTestCase.getMethodParams( com.agwego.fuzz.fuzz_tester_test.FuzzTestCaseTest.class, "mockTest", 3 );
        } catch( Throwable ex ) {
            exceptionCaught = true;
            assertEquals( "Method 'mockTest' with incorrect argument signature", ex.getMessage() );
        }
        assertTrue( exceptionCaught );
    }


	@Test
	public void noMethodSignature()
	{
        Boolean exceptionCaught = false;
        try {
		    FuzzTestCase.getMethodParams( com.agwego.fuzz.fuzz_tester_test.FuzzTestCaseTest.class, "noMockTest", 2 );
        } catch( Throwable ex ) {
            exceptionCaught = true;
            assertEquals( "No test method 'noMockTest' with matching parameters signature", ex.getMessage() );
        }
        assertTrue( exceptionCaught );
	}

	@Test
	public void setComment()
	{
		FuzzTestCase f = new FuzzTestCase();
		f.setComment( "testing" );
		assertEquals( "testing", f.getComment() );
	}

	@Test
	public void setMethodName()
	{
		FuzzTestCase f = new FuzzTestCase();
		FuzzTestCase x = f.setMethodName( "testing" );
		assertEquals( "testing", f.getMethodName() );
		assertEquals( f, x );		
	}

	@Test
	public void setNumber()
	{
		FuzzTestCase f = new FuzzTestCase();
		FuzzTestCase x = f.setNumber( 87 );
		assertEquals( 87, f.getNumber() );
		assertEquals( f, x );
	}

	@Test
	public void setPass()
	{
		FuzzTestCase f = new FuzzTestCase();
		f.setPass( true );
		assertTrue( f.getPass() );
		assertTrue( f.isPass() );
		assertFalse( f.isFail() );		
	}
}