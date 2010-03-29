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
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
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
	public static final String METHOD_NAME = "MethodName";
	public static final String TESTS = "Tests";
	public static final String TEST_CASES = "TestCases";

	@Retention( RetentionPolicy.RUNTIME )
	@Target( ElementType.TYPE )
	public @interface Parameters {
		String TestDirectory();
		String Prefix() default "";
		String Suffix() default ".json";
		String TestDirectoryRootPropertyName() default "";
	}

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

		Map<String,List<FuzzTestCase>> testMethods = null;
		testMethods = getTestMethods( getTestJsonObjects( params.TestDirectory(), prefix, params.Suffix() ) );

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
	 * Get the list of TestCases from the JSON file on disk
	 * 
	 * @param dirName Name of directory of test files
	 * @param prefix Look for files that start like
	 * @param postfix Look for files that end in
	 * @return The list of JSONObject
	 * @throws InitializationError if there are errors reading the file
	 */
	protected List<JSONObject> getTestJsonObjects( final String dirName, final String prefix, final String postfix ) throws InitializationError
	{
		List<JSONObject> js = new ArrayList<JSONObject>();
		try {
			for( File f: FileHelper.getFileList( dirName, prefix, postfix ) )
				js.add( JSONObject.fromObject( FileHelper.readFile( f )) );
		} catch( Exception ex ) {
			//log.error( "There was an error processing JSON files", ex );
			throw new InitializationError( ex );
			//throw new InitializationError( ex.getMessage() );

		}

		return js;
	}

	/**
	 * Get the test methods converting the JSON representation into FuzzTestCase(s)
	 *
	 * @param testCases List of JSONObjects containing tests
	 * @return the map of the TestCases
	 */
	protected Map<String,List<FuzzTestCase>> getTestMethods( List<JSONObject> testCases )
	{
		Map<String,List<FuzzTestCase>> testMethods = new HashMap<String,List<FuzzTestCase>>();
		for( JSONObject jobj : testCases ) {
			List tmpTestCases = jobj.getJSONArray( TEST_CASES );
			Iterator itr = tmpTestCases.iterator();
			while( itr.hasNext() ) {
				JSONObject tcd = (JSONObject) itr.next();
				JSONArray tests = tcd.getJSONArray( TESTS );
				Iterator titr = tests.iterator();
				int idx = 1;
				List<FuzzTestCase> fuzzTestCases = new ArrayList<FuzzTestCase>();
				while( titr.hasNext() ) {
					JSONObject tcj = (JSONObject) titr.next();
					FuzzTestCase fuzzTestCase = (FuzzTestCase) JSONObject.toBean( tcj, FuzzTestCase.class );
					fuzzTestCase.setNumber( idx );
					fuzzTestCase.setMethodName( tcd.getString( METHOD_NAME ) );
					fuzzTestCases.add( fuzzTestCase );
					idx++;
				}
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
