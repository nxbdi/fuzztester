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

import com.agwego.fuzz.annotations.Fuzz;
import com.agwego.fuzz.annotations.Parameters;
import com.agwego.fuzz.exception.ParametersError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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
			@Parameters( TestDirectory = "test/com/agwego/no_directory", Prefix = "NoFile" )
			public class TestParameters
			{
			}

	@Test
	public void missingPrefix()
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
			@Parameters( TestDirectory = "test/com/agwego/no_directory" )
			public class TestMissingPrefix
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
			public class TestNoParameters
			{
			}

	@Test
	public void singleJsonFile() throws Exception
	{
		FuzzTester ft = new FuzzTester( FuzzTesterTest.TestSingleJsonFile.class );
		List<Runner> children = ft.getChildren();
		assertEquals( 1, children.size() );
		FuzzTestRunner runner = (FuzzTestRunner) children.get( 0 );
		List<FrameworkMethod> fms = runner.getChildren();
		assertEquals( 1, fms.size() );
		FuzzTestMethod ftm = (FuzzTestMethod) fms.get( 0 );
		assertEquals( "mockTest", ftm.getMethod().getName() );
	}

			@RunWith( FuzzTester.class )
			@Parameters( TestDirectory = "test/com/agwego/fuzz/fuzz_tester_test", Prefix = "FuzzTesterTest_01" )
			public class TestSingleJsonFile
			{
				@Fuzz
				public void mockTest( final String input, final String expected )
				{
					assertEquals( input, expected );
				}
			}

	@Test
	public void multipleJsonFiles() throws Exception
	{
		FuzzTester ft = new FuzzTester( FuzzTesterTest.TestMultipleJsonFiles.class );
		List<Runner> children = ft.getChildren();
		assertEquals( 2, children.size() );

		int count = 0;
		for( Runner runner : children ) {
			List<FrameworkMethod> fms = (( FuzzTestRunner ) runner).getChildren();
			assertTrue( ! fms.isEmpty() );
			if( fms.get( 0 ).getMethod().getName().equals( "noMethodExists" )) {
				assertEquals( 1, fms.size());
				count++;
			} if( fms.get( 0 ).getMethod().getName().equals( "mockTest" )) {
				assertEquals( 2, fms.size());
				count++;
			}
		}

		assertEquals( 2, count );

		/*
		FuzzTestRunner runner = (FuzzTestRunner) children.get( 0 );
		List<FrameworkMethod> fms = runner.getChildren();
		assertEquals( 1, fms.size() );
		FuzzTestMethod ftm = (FuzzTestMethod) fms.get( 0 );
		assertEquals( "noMethodExists", ftm.getMethod().getName() );

		runner = (FuzzTestRunner) children.get( 1 );
		fms = runner.getChildren();
		assertEquals( 2, fms.size() );
		ftm = (FuzzTestMethod) fms.get( 0 );
		assertEquals( "mockTest", ftm.getMethod().getName() );
			*/
	}

			@RunWith( FuzzTester.class )
			@Parameters( TestDirectory = "test/com/agwego/fuzz/fuzz_tester_test", Prefix = "FuzzTesterTest" )
			public class TestMultipleJsonFiles
			{
				@Fuzz
				public void mockTest( final String input, final String expected )
				{
					assertEquals( input, expected );
				}

				@Fuzz
				public void noMethodExists( final String input, final String expected )
				{
					assertEquals( input, expected );
				}
			}

	@Test
	public void noMethodExists() throws Exception
	{
		boolean exceptionCaught = false;

		FuzzTester ft = new FuzzTester( FuzzTesterTest.NoMethodExists.class );
		List<Runner> children = ft.getChildren();
		assertEquals( 1, children.size() );
		FuzzTestRunner runner = (FuzzTestRunner) children.get( 0 );
		try {
			runner.getChildren();
		} catch( Throwable ex ) {
			exceptionCaught = true;
		}

		assertTrue( exceptionCaught );
	}

			@RunWith( FuzzTester.class )
			@Parameters( TestDirectory = "test/com/agwego/fuzz/fuzz_tester_test", Prefix = "FuzzTesterTest_03" )
			public class NoMethodExists
			{
				@Fuzz
				public void mockTest( final String input, final String expected )
				{
					assertEquals( input, expected );
				}
			}

	@Test
	public void testMethod() throws Exception
	{
		FuzzTester ft = new FuzzTester( com.agwego.fuzz.TestMethod.class );
		List<Runner> children = ft.getChildren();
		assertEquals( 1, children.size() );
		FuzzTestRunner ftRunner = (FuzzTestRunner) children.get( 0 );
		TestNotifier rn = new TestNotifier();
		ftRunner.run( rn );
		assertEquals( 1, rn.getFailureCount() );
		assertEquals( "This assert should fail, as expected expected:<arghhh[x]> but was:<arghhh[]>", rn.getFailures().get( 0 ).getMessage() );
	}

	public class TestNotifier extends RunNotifier
	{
		private List<Failure> failures = new ArrayList<Failure>();

		public void fireTestFailure( final Failure failure )
		{
			failures.add( failure );
		}

		public int getFailureCount()
		{
			return failures.size();
		}

		public List<Failure> getFailures()
		{
			return failures;
		}
	}
}