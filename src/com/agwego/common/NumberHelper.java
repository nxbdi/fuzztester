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

/**
 * Number helpers
 * 
 * @author Tim Desjardins
 * @version $Rev$
 * <br/>
 * $Id$
 */
public class NumberHelper
{
	/**
	 * Parse an integer and return 0 no matter what if the string isn't an integer
	 *
	 * @param value string as an integer to parse
	 * @return the string to int all parse errors returns 0
	 */
	static public Integer parseInt( final String value )
	{
		return parseInt( value, 0 );
	}

	/**
	 * Parse an integer string and return, return the default (otherwise) if the string isn't an integer
	 *
	 * @param value  string as an integer to parse
	 * @param otherwise if the string isn't a number
	 * @return return the parsed string as an Integer, if any errors return otherwise
	 */
	static public Integer parseInt( final String value, final Integer otherwise )
	{
		try {
			return new Integer( StringHelper.nulled( value ) );
		} catch( Exception x ) {
			return otherwise;
		}
	}
}