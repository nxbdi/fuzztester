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

import com.agwego.common.FileHelper;
import com.agwego.common.GsonHelper;
import com.agwego.common.StringHelper;
import com.agwego.fuzz.annotations.Parameters;
import com.agwego.fuzz.exception.FuzzTestJsonError;
import com.agwego.fuzz.exception.ParametersError;
import com.google.gson.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

import java.io.File;
import java.util.*;

/**
 * The FuzzTester: The fuzz tester (also supports regular JUnit @Test annotations)
 *
 * @author Tim Desjardins
 * @version $Rev$
 * 
 * $Id$
 */
public class FuzzTester extends Suite
{
	protected final Log log = LogFactory.getLog( getClass() );
	private final ArrayList<Runner> runners= new ArrayList<Runner>();
	public static final String METHOD_NAME = "method";
	public static final String TEST_CASES = "testCases";
	public static final String TEST_UNIT = "unitTest";
	public static final String TEST_SKIP = "skip";
	public static final String TEST_FILE = "FuzzFile";

	/**
	 * Only called reflectively. Do not use programmatically.
     *
	 * @param klass the class of this test
	 * @throws com.agwego.fuzz.exception.ParametersError if any errors or otherwise
	 * @throws InitializationError -
	 * @throws FuzzTestJsonError - for any semantic errors in the JSON formatted test case
	 */
	public FuzzTester( Class<?> klass ) throws ParametersError, InitializationError, FuzzTestJsonError
	{
		super( klass, Collections.<Runner>emptyList() );

		if( ! klass.isAnnotationPresent( Parameters.class ) )
			throw new ParametersError( "@Parameters( TestDirectory, Prefix, Suffix = '.json', TestDirectoryRootPropertyName = ''" );

		Parameters params = klass.getAnnotation( Parameters.class );

		log.debug( "TestDirectory " + params.TestDirectory() );
        String prefix = StringHelper.unempty( params.Prefix(), klass.getSimpleName() );

		log.debug( "Prefix " + prefix );
		log.debug( "Suffix " + params.Suffix() );
		log.debug( "TestDirectoryRootPropertyName " + params.TestDirectoryRootPropertyName() );

		Map<String,List<FuzzTestCase>> testMethods = getTestMethods( params.TestDirectory(), prefix, params.Suffix(), klass );

		// add the Fuzz tests
		for( Map.Entry< String, List<FuzzTestCase>> ltc : testMethods.entrySet() )
			runners.add( new FuzzTestRunner( getTestClass().getJavaClass(), ltc.getKey(), ltc.getValue() ));

		// add the regular tests, optional
		try {
			runners.add( new BlockJUnit4ClassRunner( getTestClass().getJavaClass() ) );
		} catch( Exception ex ) {
			log.debug( ex );
		}
	}

	/**
	 * Get the test methods converting the JSON representation into FuzzTestCase(s)
	 *
	 * @param dirName List of JSONObjects containing tests
	 * @param prefix Look for files that start like
	 * @param suffix Look for files that end in
	 * @param testClass the class we're are testing
	 * @return the map of the TestCases
	 * @throws InitializationError if there are errors reading the file
	 * @throws FuzzTestJsonError - for any semantic errors in the JSON formatted test case
	 */
	protected Map<String,List<FuzzTestCase>> getTestMethods( String dirName, String prefix, String suffix, Class testClass ) throws InitializationError, FuzzTestJsonError
	{
		Map<String,List<FuzzTestCase>> testMethods = new HashMap<String,List<FuzzTestCase>>();
		List<JsonObject> tests = getTests( dirName, prefix, suffix );

		for( JsonObject test : tests ) {
			JsonArray only = GsonHelper.getAsArray( test, "only" );
			JsonArray testUnits;
			try {
				testUnits = test.getAsJsonArray( TEST_UNIT );
			} catch( ClassCastException ex ) {
				throw new FuzzTestJsonError( "The \"" + TEST_UNIT + "\" element is not an array, File = " + test.get( FuzzTester.TEST_FILE ) );
			}
			if( testUnits == null ) {
				throw new FuzzTestJsonError( "The \"" + TEST_UNIT + "\" element is missing check your JSON file, File = " + test.get( FuzzTester.TEST_FILE ) );
			} else if( testUnits.size() == 0 ) {
				throw new FuzzTestJsonError( "The \"" + TEST_UNIT + "\" element contains no tests, File = " + test.get( FuzzTester.TEST_FILE )  );
			}
			for( JsonElement x : testUnits ) {
				JsonObject unitTest = x.getAsJsonObject();
				// TODO add assertion of method_name existence
				boolean skip = GsonHelper.getAsBoolean( unitTest, TEST_SKIP, false );
				JsonArray testCases = unitTest.getAsJsonArray( TEST_CASES );
				int idx = 1;
				List<FuzzTestCase> fuzzTestCases = new ArrayList<FuzzTestCase>();
				for( JsonElement y : testCases ) {
					JsonObject tcj = y.getAsJsonObject();
					tcj.addProperty(
							TEST_SKIP,
							skip || ! in( unitTest.get( METHOD_NAME ).getAsString(), only ) || GsonHelper.getAsBoolean( tcj, TEST_SKIP, false )
					);
					FuzzTestCase fuzzTestCase;
					try {
						fuzzTestCase = FuzzTestCase.deserialize( tcj, idx, unitTest.get( METHOD_NAME).getAsString(), testClass );
					} catch( FuzzTestJsonError ex ) {
						throw new FuzzTestJsonError( ex.getMessage() + " see file: " + test.get( FuzzTester.TEST_FILE ), ex );	
					}
					fuzzTestCases.add( fuzzTestCase );
					idx++;
				}
				if( testMethods.containsKey( unitTest.get( METHOD_NAME ).getAsString() ))
					testMethods.get( unitTest.get( METHOD_NAME ).getAsString()).addAll( fuzzTestCases );
				else
					testMethods.put( unitTest.get( METHOD_NAME ).getAsString(), fuzzTestCases );				
			}
		}

		return testMethods;
	}

	protected boolean in( final String key, final JsonArray array )
	{
		return array.size() == 0 || GsonHelper.in( key, array );		
	}

	protected List<JsonObject> getTests( final String dirName, final String prefix, final String suffix ) throws InitializationError
	{
		List<JsonObject> js = new ArrayList<JsonObject>();
		JsonParser jParser = new JsonParser();

		try {
			for( File f: FileHelper.getFileList( dirName, prefix, suffix ) ) {
				log.debug( " FILE: " + f.getAbsolutePath() );
				JsonObject jObj = jParser.parse( FileHelper.readFile( f )).getAsJsonObject();
				jObj.addProperty( FuzzTester.TEST_FILE, f.getAbsolutePath() );
				js.add( jObj ) ;
			}
		} catch( Exception ex ) {
			log.error( ex );
			throw new InitializationError( ex );
		}

		return js;
	}

	/**
	 * Get the test runners
	 *
	 * @return List<Runner>
	 */
	@Override
	protected List<Runner> getChildren()
	{
		return runners;
	}
}