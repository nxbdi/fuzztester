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

/**
 * Getting Started
 *
 * @author Tim Desjardins
 * @version $Rev: $
 * <br/>
 * $Id: $
 */

public class GettingStarted
{
	/*
	 * Example truncate ...
	 * 
	 * @return String the substring
	 * @param str to truncate
	 * @param endIndex where to truncate
	 */
	public static String trunc( String str, int endIndex )
	{
		try {
			return str.substring( 0, endIndex );
		} catch( Exception ex ) {
			return str;
		}
	}

	/**
	 * Example is the String empty
	 *
	 * @param s a string
	 * @return is it empty (null = empty)
	 */
	public static boolean isEmpty( String s )
	{
		return ( s == null ) || ( s.length() == 0 );
	}

	/**
	 * Example throw execptions
	 *
	 * @return String the substring
	 * @param str to some string
	 * @throws Exception cause that's what we're demonstrating
	 */
	public static String except( String str ) throws Exception
	{
		if( str != null )
			throw new Exception( "This is a test exception: " + str );

		return str;
	}


	/**
	 * An example of an object with setters
	 *
	 * @param pl a simple object with setters
	 * @param x a multiplier for testing
	 * @return return a constructed object
	 */
	public SimpleObject update( final SimpleObject pl, final int x )
	{
		SimpleObject np = new SimpleObject( pl );
		np.setIntObj( pl.getIntObj() * x );
		np.setBoolObj( ! pl.getBoolObj() );
		return np;
	}

	public Double calculate( final Parent p, final Double multiplier )
	{
		return multiplier * p.getValue() + p.getChild().getValue();
	}

	/**
	 * An example of "final" object
	 *
	 * @param pl a simple object with a constructor and no setters
	 * @param x a multiplier for testing
	 * @return return an updated SimpleObjectConstructor
	 */
	public SimpleObjectConstructor updateConstructor( final SimpleObjectConstructor pl, int x )
	{
		return new SimpleObjectConstructor( pl.getStrObj(), pl.getIntObj() * x, pl.getBoolObj() );
	}
}
