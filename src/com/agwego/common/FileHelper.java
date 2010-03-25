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

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tim Desjardins
 * @version $Rev$
 * $Id: $
 */
public final class FileHelper
{
	public static final String FILE_SEP = System.getProperty( "file.separator", "/" );

	/**
	 * readFile read a file given a fq file name and return the contents as a String
	 *
	 * @param file_name the file to read
	 * @return the contents of file_name
	 * @throws IOException propagated
	 */
	public static String readFile( final String file_name ) throws java.io.IOException
	{
		return readFile( new File( file_name ) );
	}

	/**
	 * readFile read a file given File object and return the contents as a String
	 *
	 * @param file the file handle to read
	 * @return the contents of the file
	 * @throws IOException propagated
	 */
	public static String readFile( File file ) throws java.io.IOException
	{
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader( new FileReader( file ) );

		String str;
		while( ( str = br.readLine() ) != null )
			sb.append( str );

		br.close();

		return sb.toString();
	}

	/**
	 * @param dirName The directory to list
	 * @param prefix The prefix for any file
	 * @param postfix The postfix for the file
	 * @return List<File> the filtered list
	 * @throws Exception if the dirName doesn't exist or the file list throws an IO exception (see File.listFiles())
	 */
	public static List<File> getFileList( final String dirName, final String prefix, final String postfix ) throws Exception
	{
		final File dir = new File( dirName );
		final FilenameFilter filter = new FileFilterPrePost( prefix, postfix );
		File [] f = dir.listFiles( filter );

		if( f == null )
			throw new Exception( "The dirName isn't a directory or there was an error reading the directory: " + dirName );

		return Arrays.asList( f );
	}
}