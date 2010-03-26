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

import java.io.File;
import java.io.FilenameFilter;

/**
 * Used to filter file names, specify a prefix and a postfix
 * to filter files.
 *
 * @see FilenameFilter
 *
 * @author Tim Desjardins
 * @version $Rev$
 * $Id$
 */
public class FileFilterPrePost implements FilenameFilter
{
	private final String prefix;
	private final String postfix;

	/**
	 * @param prefix the prefix to match for this filter
	 * @param postfix the postfix to match for this filter
	 */
	public FileFilterPrePost( final String prefix, final String postfix )
	{
		if( StringHelper.isEmpty( prefix ) )
			throw new RuntimeException( "Prefix is required" );

		if( StringHelper.isEmpty( postfix ) )
			throw new RuntimeException( "Postfix is required" );

		this.prefix = prefix;
		this.postfix = postfix;
	}

	/**
	 * @param dir the directory (File handle) to search
	 * @param name file name to match
	 * @return does the name match the prefix and postfix
	 */
	public boolean accept( final File dir, final String name )
	{
		return name.startsWith( prefix ) && name.endsWith( postfix );
	}
}
