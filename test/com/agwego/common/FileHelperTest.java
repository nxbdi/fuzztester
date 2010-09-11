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
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Tim Desjardins
 * @version $Rev$
 * $Id$
 */
@RunWith( FuzzTester.class )
@Parameters( TestDirectory = "test/com/agwego/common", Prefix = "FileHelperTest" )
public class FileHelperTest
{
 	@Test
	public void classTest()
	{
		FileHelper nh = new FileHelper(); 
	}

	@Fuzz
	public void readFile( final String fileName, final String expected ) throws IOException
	{
		assertEquals( expected, FileHelper.readFile( fileName ));
	}

	@Fuzz
	public void getFileList( final String dirName, final String prefix, final String postfix, final Integer fileCount, final String [] expectedFiles ) throws Exception
	{
		List<File> files = FileHelper.getFileList( dirName, prefix, postfix );
		assertEquals( fileCount.longValue(), files.size() );
		assertFileListEquals( expectedFiles, files );
	}

	private void assertFileListEquals( final String [] expected, List<File> actual )
	{
		if( actual.size() == 0 )
			actual = null;

		if( actual == null || expected == null ) {
			if( actual != null || expected != null )
				fail();
			return ;
		}

		if( actual.size() != expected.length )
			fail();

		for( int idx = 0; idx < actual.size(); idx++ ) {
			if( ! actual.get( idx ).getName().equals( expected[ idx ] ))
				fail();
		}
	}
}