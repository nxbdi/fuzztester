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
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.*;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * The FuzzTester: The fuzz tester (also supports regular JUnit @Test annotations)
 *
 * @author Tim Desjardins
 * @version $Rev: 8 $
 * 
 * $Id: FuzzTester.java 8 2010-03-26 03:45:50Z agwego $
 */
class FuzzTestRunner extends ParentRunner<FrameworkMethod>
{
	protected final Log log = LogFactory.getLog( getClass() );

	private List<FuzzTestCase> testCases;
	private final String testCaseName;

	/**
	 * Create the test runner
	 *
	 * @param aClass The generic class of this test
	 * @param testCaseName the name of the tests case (method name)
	 * @param testCases the Map of test cases
	 * @throws InitializationError via super class
	 */
	public FuzzTestRunner( Class<?> aClass, String testCaseName, List<FuzzTestCase> testCases ) throws InitializationError
	{
		super( aClass );
		this.testCases = testCases;
		this.testCaseName = testCaseName;
	}

	/**
	 * The name for this test
	 *
	 * @return the test case name
	 */
	@Override
	public String getName()
	{
		return testCaseName;
	}

	/**
	 * Create the test
	 * @return the test class
	 * @throws Exception per getTestClass
	 */
	public Object createTest() throws Exception
	{
		TestClass tc = getTestClass();
		tc.getOnlyConstructor();

		return tc.getOnlyConstructor().newInstance(  );
	}

	/**
	 * @see org.junit.runners.ParentRunner
	 *
	 * @param notifier the RunNotifier for this test
	 * @return Statement
	 */
	@Override
	protected Statement classBlock( RunNotifier notifier )
	{
		return childrenInvoker( notifier );
	}

	/**
	 * @see org.junit.runners.ParentRunner
	 *
	 * @return return the list of children to run
	 */
	@Override
	protected List<FrameworkMethod> getChildren()
	{
		List<FrameworkMethod> methods = new ArrayList<FrameworkMethod>();

		FrameworkMethod fm = getFrameworkMethod( testCaseName );
		if( fm != null )
			for( FuzzTestCase tc : testCases )
				methods.add( new FuzzTestMethod( fm, tc ) );

		return methods;
	}

	/**
	 * Make sure the test method has been annotated with @code @Fuzz
	 *
	 * @param methodName the tests method name
	 * @return The appropriate FrameworkMethod for methodName
	 */
	protected FrameworkMethod getFrameworkMethod( String methodName )
	{
		for( FrameworkMethod fm : getTestClass().getAnnotatedMethods( Fuzz.class ) )
			if( fm.getName().equals( methodName ) )
				return fm;

		//throw new RuntimeException( "No test method: " + methodName + " annotated with @Fuzz" );
		return null;
	}

	/**
	 * Uniquely identify the tests
	 *
	 * @param method The test method
	 * @return a brief description of the tests
	 */
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

	/**
	 * Run a test (child), test for expected exceptions.
	 *
	 * @param method - the method (test) to run
	 * @param notifier - notification of test results
	 */
	@Override
	protected void runChild( FrameworkMethod method, RunNotifier notifier )
	{
		EachTestNotifier eachNotifier = makeNotifier( method, notifier );
		FuzzTestMethod tTestMethod = (FuzzTestMethod) method;

		eachNotifier.fireTestStarted();
		if( method.getAnnotation( Ignore.class ) != null || tTestMethod.getTestCase().isSkip() ) {
			eachNotifier.fireTestIgnored();
			return;
		}

		try {
			methodBlock( tTestMethod ).evaluate();
			if( tTestMethod.getTestCase().isFail() ) {
				eachNotifier.addFailure(
					new Exception( "Test marked as fail, PASSED! " + tTestMethod.getTestCase() )
				);
			}
		} catch( AssumptionViolatedException ex ) {
			eachNotifier.addFailedAssumption( ex );
		} catch( Throwable ex ) {
			if( tTestMethod.getTestCase().isPass() ) { // only add tests where pass is true and the test fails
				boolean addNotification = true;
				if( tTestMethod.getTestCase().isTestException() && ! tTestMethod.getTestCase().matchTestException( ex ) ) {
					eachNotifier.addFailure(
						new RuntimeException( "Exception did not match: \"" + ex.getClass().getName() + "\" expected \"" + tTestMethod.getTestCase().getExceptionThrown() + "\"", ex )
					);
					addNotification = false;
				}

				if( tTestMethod.getTestCase().isTestExceptionMessage() && ! tTestMethod.getTestCase().matchTestExceptionMessage( ex ) ) {
					eachNotifier.addFailure(
						new RuntimeException( "Exception message did not match: \"" + ex.getMessage() + "\" expected \"" + tTestMethod.getTestCase().getExceptionMessage() + "\"", ex )
					);
					addNotification = false;
				}

				if( addNotification && ! ( tTestMethod.getTestCase().isTestException() || tTestMethod.getTestCase().isTestExceptionMessage() ) )
					eachNotifier.addFailure( ex );
			}
		} finally {
			eachNotifier.fireTestFinished();
		}
	}

	/**
	 * @param method the test
	 * @param notifier a test run notifier
	 * @return the married test and notifier
	 */
	protected EachTestNotifier makeNotifier( FrameworkMethod method, RunNotifier notifier )
	{
		return new EachTestNotifier( notifier, describeChild( method ) );
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
	 *
	 * @param testMethod the method and a test case
	 * @param test the actual test
	 * @return Statement
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
	 * @param test the test class
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
