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

/* **************************************************************************
 * NOTE TO SELF BEFORE ADDING A NEW METHOD LOOK IN APACHE.COMMONS.STRINGUTILS
 * **************************************************************************/

/**
 * String helpers
 *
 * @author Tim Desjardins
 * @version $Rev$
 * <br/>
 * $Id$
 */

public class StringHelper
{
	/**
	 * Return the sub-string of s from the beginning index to the end of the string
	 * No leak version of substring without all the exceptions
	 *
	 * @param s the subject string
	 * @param beginIndex start point
	 * @return the new substring of s from beginIndex
	 */
	static public String substring( String s, int beginIndex )
	{
		final String tempValue = StringHelper.unnulled( s );

        beginIndex = Math.min( beginIndex, tempValue.length() );

		return substring( s, beginIndex, tempValue.length() );
	}

	/**
	 * Return the sub-string of s between the beginning index and the end index
	 * No leak version of substring without all the exceptions
	 *
	 * @param s the subject string
	 * @param beginIndex start point
	 * @param endIndex end point
	 * @return the new substring of s between beginIndex and endIndex
	 */
	public static String substring( String s, int beginIndex, int endIndex )
	{
        if( s == null )
            return s;

        beginIndex = beginIndex < 0 ? 0 : beginIndex;
        endIndex = Math.min( s.length(), endIndex < 0 ? s.length() : endIndex );
        
		if( beginIndex > endIndex || ( beginIndex == 0 && endIndex == s.length() ) )
			return s;

		char[] rtnStr = new char[endIndex - beginIndex];
		System.arraycopy( s.toCharArray(), beginIndex, rtnStr, 0, endIndex - beginIndex );

		return new String( rtnStr );
	}

	/**
	 * StringBuilder version of substring See: {@link StringHelper#substring}
	 * 
	 * @param s the subject string
	 * @param beginIndex beginIndex start point
	 * @return the new substring of s from beginIndex as a StringBuilder object
	 */
	static public StringBuilder substringBuilder( String s, int beginIndex )
	{
		if( s == null )
			return null;

		s = StringHelper.unnulled( s );
		beginIndex = Math.min( beginIndex, s.length() );

		return substringBuilder( s, beginIndex, s.length() );
	}

	/**
	 * StringBuilder version of substring See: {@link StringHelper#substring}
	 *
	 * @param s the subject string
	 * @param beginIndex start point
	 * @param endIndex end point
	 * @return the new substring of s between beginIndex and endIndex as a StringBuilder object
	 */
	public static StringBuilder substringBuilder( String s, int beginIndex, int endIndex )
	{
		if( s == null )
			return null;

		return new StringBuilder( substring( s, beginIndex, endIndex ) );
	}

	/**
	 * is the byte array empty
	 *
	 * @param s byte array
	 * @return is it empty (null = empty)
	 */
	public static boolean isEmpty( byte[] s )
	{
		return s == null || s.length == 0;
	}

	/**
	 * is the String empty
	 *
	 * @param s byte array
	 * @return is it empty (null = empty)
	 */
	public static boolean isEmpty( String s )
	{
		return ( s == null ) || ( s.length() == 0 );
	}


	/**
	 * Nullify the String if the String is empty return null otherwise return the String s
	 *
	 * @param s the String to null
	 * @return the String or null
	 */
	public static String nulled( String s )
	{
		if( isEmpty( s ) )
			return null;
		else
			return s;
	}

	/**
	 * Nullify the Object if the Object.toString is empty return null otherwise return the object as a String
	 *
	 * @param s the String to null
	 * @return the String or null
	 */
	public static String nulled( Object s )
	{
		return s == null ? null : nulled( s.toString() );
	}

	/**
	 * Unnull the string, if the string is null return the empty string, otherwise return the original string
	 *
	 * @param s the String to unnull
	 * @return the unnulled string
	 */
	public static String unnulled( String s )
	{
		return unnulled( s, "" );
	}

	/**
	 * Unnull the string, if the string is null return the otherwise string, otherwise return the original string
	 *
	 * @param s the String to unnull
	 * @param otherwise return otherwise if the String is null
	 * @return the unnulled string
	 */
	public static String unnulled( String s, String otherwise )
	{
		return s == null ? otherwise : s;
	}

	/**
	 * Unnull the object, if the object is null return the empty string, otherwise return the original object as a string
	 *
	 * @param s the String to unnull
	 * @return the unnulled string
	 */
	public static String unnulled( Object s )
	{
		return s == null ? "" : unnulled( s.toString() );
	}

	/**
	 * Unnull the object, if the object is null return the otherwise string, otherwise return the original object as a string
	 *
	 * @param s the String to unnull
	 * @param otherwise return otherwise if the object is null
	 * @return the unnulled string
	 */
	public static String unnulled( Object s, String otherwise )
	{
		return s == null ? otherwise : unnulled( s.toString(), otherwise );
	}

	/**
	 * If the String is empty (or null) return otherwise, else return the String
	 *
	 * @param s the String to unempty
	 * @param otherwise return otherwise if the String is empty
	 * @return the String or otherwise
	 */
	public static String unempty( String s, String otherwise )
	{
		return isEmpty( s ) ? otherwise : s;
	}

	/**
	 * Compare two Strings, note: null = ""
	 *
	 * @param a string to compare
	 * @param b string to compare
	 * @return are the Strings the same
	 */
	public static boolean isSame( String a, String b )
	{
		return unnulled( a ).equals( unnulled( b ) );
	}

	/**
	 * Compare two Strings ignoring case, note: null = "" 
	 *
	 * @param a string to compare
	 * @param b string to compare
	 * @return are the Strings the same
	 */
	public static boolean isSameIgnoreCase( String a, String b )
	{
		return unnulled( a ).toLowerCase().equals( unnulled( b ).toLowerCase() );
	}
}