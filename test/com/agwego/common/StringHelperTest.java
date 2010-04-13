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

package com.agwego.common;

import com.agwego.fuzz.FuzzTester;
import com.agwego.fuzz.annotations.Fuzz;
import com.agwego.fuzz.annotations.Parameters;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Tim Desjardins
 * @version $Rev: 22 $
 * $Id: FileFilterPrePostTest.java 22 2010-04-01 04:56:35Z agwego $
 */
@RunWith( FuzzTester.class )
@Parameters( TestDirectory = "test/com/agwego/common", Prefix = "StringHelperTest" )
public class StringHelperTest
{
    /**
     * @param a the directory for FileFilter accept (doesn't have to exist)
     * @param b the file name to test for (prefix/postfix)
    */
	@Fuzz
	public void isSameIgnoreCase( final String a, final String b )
	{
		assertTrue( StringHelper.isSameIgnoreCase( a, b ));
	}

    /**
     * @param a the file filter prefix
     * @param b the file filter postfix
     */
	@Fuzz
	public void isSame( final String a, final String b )
	{
		assertTrue( StringHelper.isSame( a, b ));
	}

    /**
     * @param a the file filter prefix
     * @param otherwise the file filter postfix
     * @param expected the file filter postfix
     */
	@Fuzz
	public void unempty( final String a, final String otherwise, final String expected )
	{
		assertEquals( expected, StringHelper.unempty( a, otherwise ) );
	}

    /**
     * @param a the file filter prefix
     */
	@Fuzz
	public void isEmptyString( final String a )
	{
		assertTrue( StringHelper.isEmpty( a ) );
	}

    /**
     * @param a the file filter prefix
     */
	@Fuzz
	public void isEmptyByteArray( final String a )
	{
		byte [] b = null;
		if( a != null )
			b = a.getBytes();

		assertTrue( StringHelper.isEmpty( b ) );
	}

    /**
     * @param a the file filter prefix
     * @param expected the result
     */
	@Fuzz
	public void nulled( final String a, final String expected )
	{
		assertEquals( expected, StringHelper.nulled( a ) );
	}

    /**
     * @param a the file filter prefix
     * @param expected the result
     */
	@Fuzz
	public void nulledObject( final Object a, final Object expected )
	{
		assertEquals( expected, StringHelper.nulled( a ) );
	}

	/**
	 * @param a the test String
	 * @param expected the result
	 */
	@Fuzz
	public void unnulled( final String a, final String expected )
	{
		assertEquals( expected, StringHelper.unnulled( a ) );		
	}

	/**
	 * @param a the test String
	 * @param otherwise if null
	 * @param expected the result
	 */
	@Fuzz
	public void unnulledOtherwise( final String a, final String otherwise, final String expected )
	{
		assertEquals( expected, StringHelper.unnulled( a, otherwise ) );
	}

	/**
	 * @param a the test String
	 * @param expected the result
	 */
	@Fuzz
	public void unnulledObject( final Object a, final String expected )
	{
		assertEquals( expected, StringHelper.unnulled( a ) );
	}

	/**
	 * @param a test String
	 * @param otherwise if null
	 * @param expected the result
	 */
	@Fuzz
	public void unnulledObjectOtherwise( final Object a, final String otherwise, final String expected )
	{
		assertEquals( expected, StringHelper.unnulled( a, otherwise ) );
	}

	/**
	 *
	 * @param a test string
	 * @param begin index
	 * @param expected result
	 */
	@Fuzz
	public void substring( final String a, final String begin, final String expected )
	{
		final int beginIdx = NumberHelper.parseInt( begin );
		assertEquals( expected, StringHelper.substring( a, beginIdx ));
	}

	/**
	 *
	 * @param a test string
	 * @param begin index
     * @param end index
	 * @param expected result
	 */
	@Fuzz
	public void substringEnd( final String a, final String begin, final String end, final String expected )
	{
		final int beginIdx = NumberHelper.parseInt( begin );
        final int endIdx = NumberHelper.parseInt( end );
		assertEquals( expected, StringHelper.substring( a, beginIdx, endIdx ));
	}

	/**
	 *
	 * @param a test string
	 * @param begin index
	 * @param expected result
	 */
	@Fuzz
	public void substringBuilder( final String a, final String begin, final String expected )
	{
		final int beginIdx = NumberHelper.parseInt( begin );
		StringBuilder ex = new StringBuilder( expected );
		StringBuilder test = StringHelper.substringBuilder( a, beginIdx );
		assertTrue( new StringBuilder( expected ).toString().equals( StringHelper.substringBuilder( a, beginIdx ).toString() ));
	}

	/**
	 *
	 * @param a test string
	 * @param begin index
     * @param end index
	 * @param expected result
	 */
	@Fuzz
	public void substringBuilderEnd( final String a, final String begin, final String end, final String expected )
	{
		final int beginIdx = NumberHelper.parseInt( begin );
        final int endIdx = NumberHelper.parseInt( end );
		assertTrue( new StringBuilder( expected ).equals( StringHelper.substringBuilder( a, beginIdx, endIdx )));
	}
}