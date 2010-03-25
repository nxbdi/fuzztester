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
import com.agwego.fuzz.annotations.Fuzz;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.rules.MethodRule;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.ParentRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.io.File;
import java.lang.annotation.*;
import java.util.*;

/**
 * The FuzzTester: The fuzz tester (also supports regular JUnit @Test annotations)
 *
 * @author Tim Desjardins
 * @version $Rev$
 * $Id: $
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
	 * @throws Throwable if any errors or otherwise
	 */
	public FuzzTester( Class<?> klass ) throws Throwable
	{
		super( klass, Collections.<Runner>emptyList() );

		if( ! klass.isAnnotationPresent( Parameters.class ) )
			throw new RuntimeException( "@Parameters( TestDirectory, Prefix, Suffix = '.json', TestDirectoryRootPropertyName = '' " );

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
		runners.add( new FuzzTestRunner( getTestClass().getJavaClass(), testMethods ));
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
	 */
	protected List<JSONObject> getTestJsonObjects( final String dirName, final String prefix, final String postfix )
	{
		List<JSONObject> js = new ArrayList<JSONObject>();
		try {
			for( File f: FileHelper.getFileList( dirName, prefix, postfix ) )
				js.add( JSONObject.fromObject( FileHelper.readFile( f )) );
		} catch( Exception ex ) {
			log.error( "There was an error processing JSON files", ex );
		}

		return js;
	}

	/**
	 * Get the test methods converting the JSON representation into FuzzTestCase(s)
	 * @param jobjs List of JSONObjects containing tests
	 * @return the map of the TestCases
	 */
	protected Map<String,List<FuzzTestCase>> getTestMethods( List<JSONObject> jobjs )
	{
		Map<String,List<FuzzTestCase>> testMethods = new HashMap<String,List<FuzzTestCase>>();
		for( JSONObject jobj : jobjs ) {
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
	 * @return List<Runner>
	 */
	@Override
	protected List<Runner> getChildren()
	{
		return runners;
	}

    /**
     * The fuzz test runner, do all the work
     * @see org.junit.runners.ParentRunner<T>
     */
	private class FuzzTestRunner extends ParentRunner<FrameworkMethod>
	{
		private Map<String,List<FuzzTestCase>> testCases;

		/**
		 *
		 * @param aClass - The generic class of this test
		 * @param testCases the Map of test cases
		 * @throws InitializationError - via super class
		 */
		public FuzzTestRunner( Class<?> aClass, Map<String,List<FuzzTestCase>> testCases ) throws InitializationError
		{
			super( aClass );
			this.testCases = testCases;
		}

		/**
		 * Create the test
		 * @return the test class
		 * @throws Exception per getTestClass
		 */
		public Object createTest() throws Exception
		{
			return getTestClass().getOnlyConstructor().newInstance();
		}

		/**
		 * @see org.junit.runners.ParentRunner<T>
		 *  
		 * @param notifier -
		 * @return Statement
		 */
		@Override
		protected Statement classBlock( RunNotifier notifier )
		{
			return childrenInvoker( notifier );
		}

		@Override
		protected List<FrameworkMethod> getChildren()
		{
			List<FrameworkMethod> methods = new ArrayList<FrameworkMethod>();

			for( FrameworkMethod fm : getTestClass().getAnnotatedMethods( Fuzz.class ) )
				for( FuzzTestCase tc : testCases.get( fm.getName() ))
					methods.add( new FuzzTestMethod( fm, tc ) );

			return methods;
		}

		@Override
		protected Description describeChild( FrameworkMethod method )
		{
			FuzzTestMethod m = (FuzzTestMethod) method;
			return Description.createTestDescription(
										getTestClass().getJavaClass(),
										m.getTestCase().toString(),
										method.getAnnotations()
			);
		}

		@Override
		protected void runChild( FrameworkMethod method, RunNotifier notifier )
		{
			EachTestNotifier eachNotifier = makeNotifier( method, notifier );
            FuzzTestMethod tTestMethod = (FuzzTestMethod) method;

			if( method.getAnnotation( Ignore.class ) != null || tTestMethod.getTestCase().isSkip() ) {
				eachNotifier.fireTestIgnored();
				return;
			}

			eachNotifier.fireTestStarted();
			try {
				methodBlock( tTestMethod ).evaluate();
				if( tTestMethod.getTestCase().isFail() ) {
					eachNotifier.addFailure(
						new Exception( "Test marked as fail, PASSED! " + tTestMethod.getTestCase() )
					);
				}
			} catch( AssumptionViolatedException e ) {
				eachNotifier.addFailedAssumption( e );
			} catch( Throwable ex ) {
				if( !( tTestMethod.getTestCase().isTestException() && tTestMethod.getTestCase().matchTestException( ex.getClass() ) ) ) {
					if( tTestMethod.getTestCase().isPass() ) // only add tests where pass is true and the test fails
						if( tTestMethod.getTestCase().isTestException() )
							eachNotifier.addFailure( new RuntimeException( "Exception did not match: \"" + ex.getClass().getName() + "\" and \"" + tTestMethod.getTestCase().getExceptionThrown() + "\"", ex ));						
						else
							eachNotifier.addFailure( ex );
				}
				if( !( tTestMethod.getTestCase().isTestExceptionMessage() && tTestMethod.getTestCase().matchTestExceptionMessage( ex.getMessage() ) ) ) {
					if( tTestMethod.getTestCase().isPass() ) // only add tests where pass is true and the test fails
						if( tTestMethod.getTestCase().isTestExceptionMessage() )
							eachNotifier.addFailure( new RuntimeException( "Exception message did not match: \"" + ex.getMessage() + "\" and \"" + tTestMethod.getTestCase().getExceptionMessage() + "\"", ex ));
						else
							eachNotifier.addFailure( ex );
				}
			} finally {
				eachNotifier.fireTestFinished();
			}
		}

		protected EachTestNotifier makeNotifier( FrameworkMethod method, RunNotifier notifier )
		{
			Description description = describeChild( method );
			return new EachTestNotifier( notifier, description );
		}

		/**
		 * Returns a Statement that, when executed, either returns normally if
		 * {@code method} passes, or throws an exception if {@code method} fails.
		 * <p/>
		 * Here is an outline of the default implementation:
		 * <p/>
		 * <ul>
		 * <li>Invoke {@code method} on the result of {@code createTest()}, and
		 * throw any exceptions thrown by either operation.
		 * <li>HOWEVER, if {@code method}'s {@code @Test} annotation has the {@code
		 * expecting} attribute, return normally only if the previous step threw an
		 * exception of the correct type, and throw an exception otherwise.
		 * <li>HOWEVER, if {@code method}'s {@code @Test} annotation has the {@code
		 * timeout} attribute, throw an exception if the previous step takes more
		 * than the specified number of milliseconds.
		 * <li>ALWAYS allow {@code @Rule} fields to modify the execution of the above
		 * steps.  A {@code Rule} may prevent all execution of the above steps, or
		 * add additional behavior before and after, or modify thrown exceptions.
		 * For more information, see {@link MethodRule}
		 * <li>ALWAYS run all non-overridden {@code @Before} methods on this class
		 * and superclasses before any of the previous steps; if any throws an
		 * Exception, stop execution and pass the exception on.
		 * <li>ALWAYS run all non-overridden {@code @After} methods on this class
		 * and superclasses after any of the previous steps; all After methods are
		 * always executed: exceptions thrown by previous steps are combined, if
		 * necessary, with exceptions from After methods into a
		 * MultipleFailureException.
		 * </ul>
		 * <p/>
		 * This can be overridden in subclasses, either by overriding this method,
		 * or the implementations creating each sub-statement.
		 * @param testMethod -
		 * @return Statement -
		 */
		protected Statement methodBlock( FuzzTestMethod testMethod )
		{
			Object test;
			try {
				test = new ReflectiveCallable()
				{
					@Override
					protected Object runReflectiveCall() throws Throwable
					{
						return createTest();
					}
				}.run();
			} catch( Throwable e ) {
				return new Fail( e );
			}

			Statement statement = methodInvoker( testMethod, test );
			statement = withRules( testMethod, test, statement );
			return statement;
		}

		/**
		 * Returns a {@link Statement} that invokes {@code method} on {@code test}
		 * @param testMethod -
		 * @param test -
		 * @return Statement -
		 */
		protected Statement methodInvoker( FuzzTestMethod testMethod, Object test )
		{
			return new FuzzTestInvoke( testMethod, test );
		}

		/**
		 * Adds to {@code errors} if any method in this class is annotated with
		 * {@code annotation}, but:
		 * <ul>
		 * <li>is not public, or
		 * <li>takes parameters, or
		 * <li>returns something other than void, or
		 * <li>is static (given {@code isStatic is false}), or
		 * <li>is not static (given {@code isStatic is true}).
		 */
		protected void validatePublicVoidNoArgMethods( Class<? extends Annotation> annotation, boolean isStatic, List<Throwable> errors )
		{
			List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods( annotation );

			for( FrameworkMethod eachTestMethod : methods )
				if( eachTestMethod.getAnnotation( Fuzz.class ) == null )
					eachTestMethod.validatePublicVoidNoArg( isStatic, errors );
		}

		private Statement withRules( FrameworkMethod method, Object target, Statement statement )
		{
			Statement result = statement;
			for( MethodRule each : rules( target ) )
				result = each.apply( result, method, target );
			return result;
		}

		/**
		 * @param test -
		 * @return the MethodRules that can transform the block
		 *         that runs each method in the tested class.
		 */
		protected List<MethodRule> rules( Object test )
		{
			List<MethodRule> results = new ArrayList<MethodRule>();
			for( FrameworkField each : ruleFields() )
				results.add( createRule( test, each ) );
			return results;
		}

		private List<FrameworkField> ruleFields()
		{
			return getTestClass().getAnnotatedFields( Rule.class );
		}

		private MethodRule createRule( Object test, FrameworkField each )
		{
			try {
				return (MethodRule) each.get( test );
			} catch( IllegalAccessException e ) {
				throw new RuntimeException( "How did getFields return a field we couldn't access?" );
			}
		}
	}	
}
