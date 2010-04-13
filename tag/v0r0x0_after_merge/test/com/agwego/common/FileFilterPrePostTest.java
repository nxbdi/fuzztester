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

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Tim Desjardins
 * @version $Rev$
 * $Id$
 */
@RunWith( FuzzTester.class )
@Parameters( TestDirectory = "test/com/agwego/common", Prefix = "FileFilterPrePost" )
public class FileFilterPrePostTest
{
    /**
     * Test the accept
     *
     * @param dir - the directory for FileFilter accept (doesn't have to exist)
     * @param name - the file name to test for (prefix/postfix)
     * @param prefix - the file filter prefix
     * @param postfix - the file filter postfix
     */    
	@Fuzz
	public void accept( final String dir, final String name, final String prefix, final String postfix )
	{
		final FileFilterPrePost fpp = new FileFilterPrePost( prefix, postfix );
		assertTrue( fpp.accept( new File( dir ), name ) );
	}

    /**
     * Test constructor
     *
     * @param prefix - the file filter prefix
     * @param postfix - the file filter postfix
     */
	@Fuzz
	public void construtctor( final String prefix, final String postfix )
	{
		final FileFilterPrePost fpp = new FileFilterPrePost( prefix, postfix );
		assertNotNull( fpp );
	}
}