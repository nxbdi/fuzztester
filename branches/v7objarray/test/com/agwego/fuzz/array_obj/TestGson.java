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

package com.agwego.fuzz.array_obj;

import com.agwego.common.NumberHelper;
import com.agwego.fuzz.FuzzTester;
import com.agwego.fuzz.annotations.Fuzz;
import com.agwego.fuzz.annotations.Parameters;
import org.junit.Ignore;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith( FuzzTester.class )
@Parameters( TestDirectory = "test/com/agwego/fuzz/array_obj", Prefix = "GSON_01" )
public class TestGson
{
	//@Ignore
	@Fuzz
	public void mockInteger( final String input, final Integer expected  )
	{
		int in = NumberHelper.parseInt( input, 0 );
		assertEquals( in, expected.intValue() );
	}

	//@Ignore
	@Fuzz
	public void mockDouble( final String input, final Double expected ) throws Exception
	{
		Double in = new Double( input );
		assertEquals( in, expected );
	}

	//@Ignore
	@Fuzz
	public void mockBoolean( final String input, final Boolean expected ) throws Exception
	{
		Boolean in = new Boolean( input );
		assertEquals( in, expected );
	}

	@Fuzz
	public void mockTestObject( final Boolean input, final TestObject testObject ) throws Exception
	{
		assertFalse( input );
		assertEquals( testObject.getName(), "Test name" );
	}
}
