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

package com.agwego.fuzz;

import com.agwego.fuzz.exception.ParametersError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * @author Tim Desjardins
 * @version $Rev: 8 $
 *
 * $Id: FileFilterPrePostTest.java 8 2010-03-26 03:45:50Z agwego $
 */
public class FuzzTesterTest
{
	protected final Log log = LogFactory.getLog( getClass() );

    @Test
    public void parameters()
    {
	    boolean exceptionCaught = false;
	    try {
	        new FuzzTester( FuzzTesterTest.TestParameters.class );
	    } catch( ParametersError ex ) {
		    log.info( "x Exception: " + ex.getMessage() );
		    fail();
	    } catch( InitializationError ex ) {
		    assertEquals( ex.getCauses().size(), 1 );
			assertEquals( "The dirName isn't a directory or there was an error reading the directory: test/com/agwego/no_directory", ex.getCauses().get( 0 ).getMessage() );
		    exceptionCaught = true;
	    }

	    assertTrue( exceptionCaught );
    }

	@RunWith( FuzzTester.class )
	@FuzzTester.Parameters( TestDirectory = "test/com/agwego/no_directory", Prefix = "NoFile" )
	private class TestParameters
	{
	}

	@Test
	public void noParameters()
	{
		boolean exceptionCaught = false;
		try {
		    new FuzzTester( FuzzTesterTest.TestNoParameters.class );
		} catch( ParametersError ex ) {
			assertEquals( "@Parameters( TestDirectory, Prefix, Suffix = '.json', TestDirectoryRootPropertyName = ''", ex.getMessage() );
			exceptionCaught = true;
		} catch( InitializationError ex ) {
			fail();
		}

		assertTrue( exceptionCaught );
	}

	@RunWith( FuzzTester.class )
	private class TestNoParameters
	{
	}

	@Test
	public void missingTestDirectory()
	{
		boolean exceptionCaught = false;
		try {
		    new FuzzTester( FuzzTesterTest.TestNoParameters.class );
		} catch( ParametersError ex ) {
			assertEquals( "@Parameters( TestDirectory, Prefix, Suffix = '.json', TestDirectoryRootPropertyName = ''", ex.getMessage() );
			exceptionCaught = true;
		} catch( InitializationError ex ) {
			fail();
		}

		assertTrue( exceptionCaught );
	}

	@RunWith( FuzzTester.class )
	@FuzzTester.Parameters( TestDirectory = "test/com/agwego/no_directory" )
	private class TestMissingTestDirectory
	{
	}
}