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
import com.agwego.common.StringHelper;
import com.agwego.fuzz.annotations.Parameters;
import com.agwego.fuzz.exception.ParametersError;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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

	/**
	 * Only called reflectively. Do not use programmatically.
     *
	 * @param klass the class of this test
	 * @throws com.agwego.fuzz.exception.ParametersError if any errors or otherwise
	 * @throws InitializationError -
	 */
	public FuzzTester( Class<?> klass ) throws ParametersError, InitializationError
	{
		super( klass, Collections.<Runner>emptyList() );

		if( ! klass.isAnnotationPresent( Parameters.class ) )
			throw new ParametersError( "@Parameters( TestDirectory, Prefix, Suffix = '.json', TestDirectoryRootPropertyName = ''" );

		Parameters params = klass.getAnnotation( Parameters.class );

		log.debug( "TestDirectory " + params.TestDirectory() );
		String prefix = params.Prefix();
		if( StringHelper.isEmpty( prefix ) )
			prefix = klass.getName();

		log.debug( "Prefix " + prefix );
		log.debug( "Suffix " + params.Suffix() );
		log.debug( "TestDirectoryRootPropertyName " + params.TestDirectoryRootPropertyName() );

		Map<String,List<FuzzTestCase>> testMethods = getTestMethods( getTestJsonObjects( params.TestDirectory(), prefix, params.Suffix() ) );

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
	 * @return the map of the TestCases
	 */
	protected Map<String,List<FuzzTestCase>> getTestMethods( String dirName, String prefix, String suffix )
	{
		

		return null;
	}

	/**
	 * Get the list of TestCases from the JSON file on disk
	 * 
	 * @param dirName Name of directory of test files
	 * @param prefix Look for files that start like
	 * @param suffix Look for files that end in
	 * @return The list of JSONObject
	 * @throws InitializationError if there are errors reading the file
	 */
	protected List<JSONObject> getTestJsonObjects( final String dirName, final String prefix, final String suffix ) throws InitializationError
	{
		List<JSONObject> js = new ArrayList<JSONObject>();
		try {
			for( File f: FileHelper.getFileList( dirName, prefix, suffix ) )
				js.add( JSONObject.fromObject( FileHelper.readFile( f )) );
		} catch( Exception ex ) {
			log.info( ex );
			throw new InitializationError( ex );
		}

		return js;
	}
	
	/**
	 * Get the test methods converting the JSON representation into FuzzTestCase(s)
	 *
	 * @param unitTests List of JSONObjects containing tests
	 * @return the map of the TestCases
	 */
	protected Map<String,List<FuzzTestCase>> getTestMethods( List<JSONObject> unitTests )
	{
		Map<String,List<FuzzTestCase>> testMethods = new HashMap<String,List<FuzzTestCase>>();
		for( JSONObject jobj : unitTests ) {
			List tmpTestCases = jobj.getJSONArray( TEST_UNIT );
			for( Object x : tmpTestCases ) {
				JSONObject tcd = (JSONObject) x;
				JSONArray tests = tcd.getJSONArray( TEST_CASES );
				Iterator titr = tests.iterator();
				int idx = 1;
				List<FuzzTestCase> fuzzTestCases = new ArrayList<FuzzTestCase>();
				while( titr.hasNext() ) {
					JSONObject tcj = (JSONObject) titr.next();
					FuzzTestCase fuzzTestCase = FuzzTestCase.deserialize( tcj, idx, tcd.getString( METHOD_NAME ) );
					//FuzzTestCase fuzzTestCase = (FuzzTestCase) JSONObject.toBean( tcj, FuzzTestCase.class );
					//fuzzTestCase.setNumber( idx );
					//fuzzTestCase.setMethodName( tcd.getString( METHOD_NAME ) );
					fuzzTestCases.add( fuzzTestCase );
					idx++;
				}
				if( testMethods.containsKey( tcd.getString( METHOD_NAME ) ))
					testMethods.get( tcd.getString( METHOD_NAME )).addAll( fuzzTestCases );
				else
					testMethods.put( tcd.getString( METHOD_NAME ), fuzzTestCases );
			}
		}

		return testMethods;
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