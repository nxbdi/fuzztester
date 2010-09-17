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

import com.agwego.common.NumberHelper;
import com.agwego.fuzz.annotations.Fuzz;
import com.agwego.fuzz.annotations.Parameters;
import com.agwego.fuzz.exception.FuzzTestJsonError;
import com.agwego.fuzz.exception.ParametersError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;

import static org.junit.Assert.*;

/**
 * @author Tim Desjardins
 * @version $Rev$
 *
 * $Id$
 */
public class JsonFormatTest
{
	protected final Log log = LogFactory.getLog( getClass() );

    @Test
    public void noUnitTest()
    {
	    boolean exceptionCaught = false;
	    try {
	        new FuzzTester( JsonFormatTest.TestJsonNoUnitTest_.class );
	    } catch( ParametersError ex ) {
		    fail();
	    } catch( InitializationError ex ) {
		    fail();
	    } catch( FuzzTestJsonError ex ) {
			assertTrue( ex.getMessage().startsWith( "The \"unitTest\" element is missing check your JSON file" ));
		    exceptionCaught = true;
	    }

	    assertTrue( "No FuzzTestJsonError exception was thrown,", exceptionCaught );
    }

			@RunWith( FuzzTester.class )
			@Parameters( TestDirectory = "test/com/agwego/fuzz/bad_json_files", Prefix = "JSON_no_unitTest" )
			public class TestJsonNoUnitTest_
			{
				@Fuzz
				public void mockInteger( final String input, final Integer expected  )
				{
					int in = NumberHelper.parseInt( input, 0 );
					assertEquals( in, expected.intValue() );
				}
			}

    @Test
    public void missSpelledUnitTest()
    {
	    boolean exceptionCaught = false;
	    try {
	        new FuzzTester( JsonFormatTest.TestJsonMissingUnitTest_.class );
	    } catch( ParametersError ex ) {
		    fail();
	    } catch( InitializationError ex ) {
		    fail();
	    } catch( FuzzTestJsonError ex ) {
			assertTrue( ex.getMessage().startsWith( "The \"unitTest\" element is missing check your JSON file" ));
		    exceptionCaught = true;
	    }

	    assertTrue( "No FuzzTestJsonError exception was thrown,", exceptionCaught );
    }

			@RunWith( FuzzTester.class )
			@Parameters( TestDirectory = "test/com/agwego/fuzz/bad_json_files", Prefix = "JSON_misspelled_unitTest" )
			public class TestJsonMissingUnitTest_
			{
			}

    @Test
    public void missingBodyUnitTest()
    {
	    boolean exceptionCaught = false;
	    try {
	        new FuzzTester( JsonFormatTest.TestJsonMissingBodyUnitTest_.class );
	    } catch( ParametersError ex ) {
		    fail();
	    } catch( InitializationError ex ) {
		    fail();
	    } catch( FuzzTestJsonError ex ) {
			assertTrue( ex.getMessage().startsWith( "The \"unitTest\" element contains no tests" ) );
		    log.error( "FuzzTestJsonError= " + ex.getMessage() );
		    exceptionCaught = true;
	    }

	    assertTrue( "No FuzzTestJsonError exception was thrown,", exceptionCaught );
    }

			@RunWith( FuzzTester.class )
			@Parameters( TestDirectory = "test/com/agwego/fuzz/bad_json_files", Prefix = "JSON_missing_body_unitTest" )
			public class TestJsonMissingBodyUnitTest_
			{
			}

	@Test
	public void bodyAsEmptyObjectUnitTest()
	{
		boolean exceptionCaught = false;
		try {
		    new FuzzTester( JsonFormatTest.TestJsonUnitTestBodyAsEmptyObject.class );
		} catch( ParametersError ex ) {
			fail();
		} catch( InitializationError ex ) {
			fail();
		} catch( FuzzTestJsonError ex ) {
			assertTrue( ex.getMessage().startsWith( "The \"unitTest\" element is not an array" ));
			exceptionCaught = true;
		}

		assertTrue( "No FuzzTestJsonError exception was thrown,", exceptionCaught );
	}

			@RunWith( FuzzTester.class )
			@Parameters( TestDirectory = "test/com/agwego/fuzz/bad_json_files", Prefix = "JSON_body_as_empty_object" )
			public class TestJsonUnitTestBodyAsEmptyObject
			{
			}

	@Test
	public void bodyAsObjectUnitTest()
	{
		boolean exceptionCaught = false;
		try {
		    new FuzzTester( JsonFormatTest.TestJsonUnitTestBodyAsObject.class );
		} catch( ParametersError ex ) {
			fail();
		} catch( InitializationError ex ) {
			fail();
		} catch( FuzzTestJsonError ex ) {
			assertTrue( ex.getMessage().startsWith( "The \"unitTest\" element is not an array" ));
			exceptionCaught = true;
		}

		assertTrue( "No FuzzTestJsonError exception was thrown,", exceptionCaught );
	}

			@RunWith( FuzzTester.class )
			@Parameters( TestDirectory = "test/com/agwego/fuzz/bad_json_files", Prefix = "JSON_body_as_object" )
			public class TestJsonUnitTestBodyAsObject
			{
			}

	@Test
	public void missingMethod()
	{
		boolean exceptionCaught = false;
		try {
		    new FuzzTester( JsonFormatTest.TestMissingMethod.class );
		} catch( ParametersError ex ) {
			fail();
		} catch( InitializationError ex ) {
			fail();
		} catch( FuzzTestJsonError ex ) {
			assertTrue( ex.getMessage().startsWith( "No test method 'mockInteger' with matching parameters signature" ));
			exceptionCaught = true;
		}

		assertTrue( "No FuzzTestJsonError exception was thrown,", exceptionCaught );
	}

			@RunWith( FuzzTester.class )
			@Parameters( TestDirectory = "test/com/agwego/fuzz/bad_json_files", Prefix = "JSON_missing_method" )
			public class TestMissingMethod
			{
			}

	@Test
	public void tooFewArgsForMethod()
	{
		boolean exceptionCaught = false;
		try {
		    new FuzzTester( JsonFormatTest.TestTooFewArgsForMethod.class );
		} catch( ParametersError ex ) {
			fail();
		} catch( InitializationError ex ) {
			fail();
		} catch( FuzzTestJsonError ex ) {
			assertTrue( ex.getMessage().startsWith( "Method 'mockInteger' has a mismatched parameters signature (too few)" ));
			exceptionCaught = true;
		}

		assertTrue( "No FuzzTestJsonError exception was thrown,", exceptionCaught );
	}

			@RunWith( FuzzTester.class )
			@Parameters( TestDirectory = "test/com/agwego/fuzz/bad_json_files", Prefix = "JSON_too_few_args_for_method" )
			public class TestTooFewArgsForMethod
			{
				@Fuzz
				public void mockInteger( final String input, final Integer expected  )
				{
					int in = NumberHelper.parseInt( input, 0 );
					assertEquals( in, expected.intValue() );
				}				
			}

	@Test
	public void tooManyArgsForMethod()
	{
		boolean exceptionCaught = false;
		try {
		    new FuzzTester( JsonFormatTest.TestTooManyArgsForMethod.class );
		} catch( ParametersError ex ) {
			fail();
		} catch( InitializationError ex ) {
			fail();
		} catch( FuzzTestJsonError ex ) {
			assertTrue( ex.getMessage().startsWith( "Method 'mockInteger' has a mismatched parameters signature (too many)" ));
			exceptionCaught = true;
		}

		assertTrue( "No FuzzTestJsonError exception was thrown,", exceptionCaught );
	}

			@RunWith( FuzzTester.class )
			@Parameters( TestDirectory = "test/com/agwego/fuzz/bad_json_files", Prefix = "JSON_too_many_args_for_method" )
			public class TestTooManyArgsForMethod
			{
				@Fuzz
				public void mockInteger( final String input, final Integer expected  )
				{
					int in = NumberHelper.parseInt( input, 0 );
					assertEquals( in, expected.intValue() );
				}
			}

	@Test
	public void missingArgs()
	{
		boolean exceptionCaught = false;
		try {
		    new FuzzTester( JsonFormatTest.TestMissingArgs.class );
		} catch( ParametersError ex ) {
			fail();
		} catch( InitializationError ex ) {
			fail();
		} catch( FuzzTestJsonError ex ) {
			assertTrue( ex.getMessage().startsWith( "Method 'mockInteger' has no argument list" ));
			exceptionCaught = true;
		}

		assertTrue( "No FuzzTestJsonError exception was thrown,", exceptionCaught );
	}

			@RunWith( FuzzTester.class )
			@Parameters( TestDirectory = "test/com/agwego/fuzz/bad_json_files", Prefix = "JSON_missing_args" )
			public class TestMissingArgs
			{
			}

	@Test
	public void emptyArgs()
	{
		boolean exceptionCaught = false;
		try {
		    new FuzzTester( JsonFormatTest.TestEmptyArgs.class );
		} catch( ParametersError ex ) {
			fail();
		} catch( InitializationError ex ) {
			fail();
		} catch( FuzzTestJsonError ex ) {
			assertTrue( ex.getMessage().startsWith( "Method 'mockInteger' has no argument list" ));
			exceptionCaught = true;
		}

		assertTrue( "No FuzzTestJsonError exception was thrown,", exceptionCaught );
	}

			@RunWith( FuzzTester.class )
			@Parameters( TestDirectory = "test/com/agwego/fuzz/bad_json_files", Prefix = "JSON_empty_args" )
			public class TestEmptyArgs
			{
			}

	@Test
	public void objectArgs()
	{
		boolean exceptionCaught = false;
		try {
		    new FuzzTester( JsonFormatTest.TestObjectArgs.class );
		} catch( ParametersError ex ) {
			fail();
		} catch( InitializationError ex ) {
			fail();
		} catch( FuzzTestJsonError ex ) {
			assertTrue( ex.getMessage(), ex.getMessage().startsWith( "Method 'mockInteger' argument list must be an array, see" ));
			exceptionCaught = true;
		}

		assertTrue( "No FuzzTestJsonError exception was thrown,", exceptionCaught );
	}

			@RunWith( FuzzTester.class )
			@Parameters( TestDirectory = "test/com/agwego/fuzz/bad_json_files", Prefix = "JSON_object_args" )
			public class TestObjectArgs
			{
			}

	@Test
	public void metaData()
	{
		boolean exceptionCaught = false;
		try {
		    new FuzzTester( JsonFormatTest.TestMetaData.class );
		} catch( ParametersError ex ) {
			fail();
		} catch( InitializationError ex ) {
			fail();
		} catch( FuzzTestJsonError ex ) {
			assertTrue( ex.getMessage(), ex.getMessage().startsWith( "Method 'mockInteger' argument list must be an array, see" ));
			exceptionCaught = true;
		}

		assertFalse( "An exception was thrown,", exceptionCaught );
	}

			@RunWith( FuzzTester.class )
			@Parameters( TestDirectory = "test/com/agwego/fuzz/bad_json_files", Prefix = "JSON_test_meta_data" )
			public class TestMetaData
			{
				@Fuzz
				public void mockInteger( final String input, final Integer expected  )
				{
					int in = NumberHelper.parseInt( input, 0 );
					assertEquals( in, expected.intValue() );
				}
			}

	@Test
	public void missingComma()
	{
		boolean exceptionCaught = false;
		try {
		    new FuzzTester( JsonFormatTest.TestMissingComma.class );
		} catch( ParametersError ex ) {
			fail();
		} catch( InitializationError ex ) {
			fail();
		} catch( FuzzTestJsonError ex ) {
			assertTrue( ex.getMessage(), ex.getMessage().startsWith( "Failed parsing JSON source" ));
			exceptionCaught = true;
		}

		assertTrue( "No FuzzTestJsonError exception was thrown,", exceptionCaught );
	}

			@RunWith( FuzzTester.class )
			@Parameters( TestDirectory = "test/com/agwego/fuzz/bad_json_files", Prefix = "JSON_missing_comma" )
			public class TestMissingComma
			{
			}

	@Test
	public void onlyEmpty()
	{
		boolean exceptionCaught = false;
		try {
		    new FuzzTester( JsonFormatTest.TestOnlyEmpty.class );
		} catch( ParametersError ex ) {
			fail();
		} catch( InitializationError ex ) {
			fail();
		} catch( FuzzTestJsonError ex ) {
			fail();
		}

		assertFalse( "An exception was thrown,", exceptionCaught );
	}

			@RunWith( FuzzTester.class )
			@Parameters( TestDirectory = "test/com/agwego/fuzz/bad_json_files", Prefix = "JSON_only_empty" )
			public class TestOnlyEmpty
			{
				@Fuzz
				public void mockInteger( final String input, final Integer expected  )
				{
					int in = NumberHelper.parseInt( input, 0 );
					assertEquals( in, expected.intValue() );
				}
			}

	@Test
	public void onlyAsObject()
	{
		boolean exceptionCaught = false;
		try {
		    new FuzzTester( JsonFormatTest.TestOnlyAsObject.class );
		} catch( ParametersError ex ) {
			fail();
		} catch( InitializationError ex ) {
			fail();
		} catch( FuzzTestJsonError ex ) {
			log.info(  "ex = " + ex.getMessage() );
			fail();
		}

		assertFalse( "An exception was thrown,", exceptionCaught );
	}

			@RunWith( FuzzTester.class )
			@Parameters( TestDirectory = "test/com/agwego/fuzz/bad_json_files", Prefix = "JSON_only_empty" )
			public class TestOnlyAsObject
			{
				@Fuzz
				public void mockInteger( final String input, final Integer expected  )
				{
					int in = NumberHelper.parseInt( input, 0 );
					assertEquals( in, expected.intValue() );
				}
			}

	@Test
	public void onlyAsString()
	{
		boolean exceptionCaught = false;
		try {
		    new FuzzTester( JsonFormatTest.TestOnlyAsString.class );
		} catch( ParametersError ex ) {
			fail();
		} catch( InitializationError ex ) {
			fail();
		} catch( FuzzTestJsonError ex ) {
			log.info(  "ex = " + ex.getMessage() );
			fail();
		}

		assertFalse( "An exception was thrown,", exceptionCaught );
	}

			@RunWith( FuzzTester.class )
			@Parameters( TestDirectory = "test/com/agwego/fuzz/bad_json_files", Prefix = "JSON_only_as_string" )
			public class TestOnlyAsString
			{
				@Fuzz
				public void mockInteger( final String input, final Integer expected  )
				{
					int in = NumberHelper.parseInt( input, 0 );
					assertEquals( in, expected.intValue() );
				}
			}

	@Test
	public void testCasesAsObject()
	{
		boolean exceptionCaught = false;
		try {
		    new FuzzTester( JsonFormatTest.TestTestCasesAsObject.class );
		} catch( ParametersError ex ) {
			fail();
		} catch( InitializationError ex ) {
			fail();
		} catch( FuzzTestJsonError ex ) {
			assertTrue( ex.getMessage(), ex.getMessage().startsWith( "The \"testCases\" element is not an array, see" ));
			exceptionCaught = true;
		}

		assertTrue( "No FuzzTestJsonError exception was thrown,", exceptionCaught );
	}

			@RunWith( FuzzTester.class )
			@Parameters( TestDirectory = "test/com/agwego/fuzz/bad_json_files", Prefix = "JSON_test_cases_as_object.json" )
			public class TestTestCasesAsObject
			{
				@Fuzz
				public void mockInteger( final String input, final Integer expected  )
				{
					int in = NumberHelper.parseInt( input, 0 );
					assertEquals( in, expected.intValue() );
				}
			}
}