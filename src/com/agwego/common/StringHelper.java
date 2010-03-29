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

/*
 * **************************************************************************
 * NOTE TO SELF BEFORE ADDING A NEW METHOD LOOK IN APACHE.COMMONS.STRINGUTILS
 * **************************************************************************
 */

/**
 *  String helpers
 * @author Tim Desjardins
 * @version $Rev$
 *
 * $Id$
 */

public class StringHelper
{
	static public String substring( String value, int beginIndex )
	{
		value = StringHelper.unnulled( value );
		beginIndex = Math.min( beginIndex, value.length() );

		return substring( value, beginIndex, value.length() );
	}

	// no leak version of SubString without out all the god damned exceptions
	public static String substring( String s, int beginIndex, int endIndex )
	{
		if( s == null || beginIndex < 0 || endIndex > s.length() || beginIndex > endIndex || ( beginIndex == 0 && endIndex == s.length() ) )
			return s;

		char[] rtnStr = new char[endIndex - beginIndex];
		System.arraycopy( s.toCharArray(), beginIndex, rtnStr, 0, endIndex - beginIndex );

		return new String( rtnStr );
	}

	static public StringBuilder substringBuilder( String value, int beginIndex )
	{
		value = StringHelper.unnulled( value );
		beginIndex = Math.min( beginIndex, value.length() );

		return substringBuilder( value, beginIndex, value.length() );
	}

	// no leak version of SubString without out all the god damned exceptions
	public static StringBuilder substringBuilder( String s, int beginIndex, int endIndex )
	{
		return new StringBuilder( substring( s, beginIndex, endIndex ) );
	}	

	public static boolean isEmpty( byte[] s )
	{
		return s == null || s.length == 0;
	}

	public static boolean isEmpty( String s )
	{
		return ( s == null ) || ( s.length() == 0 );
	}

	public static String nulled( String s )
	{
		if( ( s == null ) || ( s.length() == 0 ) )
			return null;
		else
			return s;
	}

	public static String nulled( Object s )
	{
		return s == null ? null : nulled( s.toString() );
	}

	public static String unnulled( String s )
	{
		return unnulled( s, "" );
	}

	public static String unnulled( String s, String otherwise )
	{
		return s == null ? otherwise : s;
	}

	public static String unnulled( Object s )
	{
		return s == null ? "" : unnulled( s.toString() );
	}

	public static String unnulled( Object s, String otherwise )
	{
		return s == null ? otherwise : unnulled( s.toString(), otherwise );
	}

	public static String unempty( String s, String otherwise )
	{
		return isEmpty( s ) ? otherwise : s;
	}

	public static boolean isSame( String a, String b )
	{
		return unnulled( a ).equals( unnulled( b ) );
	}

	public static boolean isSameIgnoreCase( String a, String b )
	{
		return unnulled( a ).toLowerCase().equals( unnulled( b ).toLowerCase() );
	}
}