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

import com.agwego.common.GsonHelper;
import com.agwego.common.StringHelper;
import com.agwego.fuzz.exception.FuzzTestJsonError;
import com.google.gson.*;

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.List;

/**
 * Models the test case object, this is the Java representation of the JSON test case object.
 * <pre>
       {
            "comment" : "Test accept",
            "method" : "construtctor",
            "testCases" : [
                <b>{
                    "name" : "Test consturctor",
                    "args" : [ "prefix", "xml" ]
                },
                {
                    "pass" : false,
                    "name" : "Test consturctor",
                    "args" : [ "prefix", null ]
                },
                {
                    "skip" : true
                    "name" : "Test consturctor",
                    "args" : [ "prefix", "xml" ]
                },
                {
                    "exceptionThrown" : "java.lang.RuntimeException",
                    "name" : "Test consturctor with null prefix",
                    "args" : [ null, "postfix" ]
                },
                {
                    "exceptionMessage" : "Prefix is required",
                    "name" : "Test consturctor with null prefix",
                    "args" : [ null, "postfix" ]
                },
                {
                    "exceptionThrown" : "java.lang.RuntimeException",
                    "exceptionMessage" : "Prefix is required",
                    "name" : "Test consturctor with null prefix",
                    "args" : [ null, "postfix" ]
                }</b>
            ]
 * </pre>
 *
 * @author Tim Desjardins
 * @version $Rev$
 * 
 * $Id$
 */
public class FuzzTestCase
{
	private String methodName;
	private String message;
	private List<Object> args;
	private String exceptionThrown;
	private String exceptionMessage;
	private int number;
	private boolean pass = true;
	private boolean skip = false;

	/**
	 * Construct a FuzzTestCase
	 */
	public FuzzTestCase()
	{
		args = new ArrayList<Object>();
	}

	@SuppressWarnings( "unchecked method invocation: <T>fromJson(com.google.gson.JsonElement,java.lang.Class<T>) in com.google.gson.Gson is applied to (com.google.gson.Jon: <T>fromJson(com.google.gson.JsonElement,java.lang.Class<T>) in com.google.gson.Gson is applied to (com.google.gson.J" )
	protected static FuzzTestCase deserialize( final JsonObject jobj, final int testNumber, final String methodName, final Class testClass ) throws FuzzTestJsonError
	{
		FuzzTestCase fuzzTestCase = new FuzzTestCase();
		fuzzTestCase.setMethodName( methodName );
		fuzzTestCase.setNumber( testNumber );
		fuzzTestCase.setMessage( GsonHelper.getAsString( jobj, "comment" ));
		fuzzTestCase.setExceptionThrown( GsonHelper.getAsString( jobj, "exceptionThrown" ));
		fuzzTestCase.setExceptionMessage( GsonHelper.getAsString( jobj, "exceptionMessage" ));
		fuzzTestCase.setSkip( GsonHelper.getAsBoolean( jobj, "skip", false ));
		fuzzTestCase.setPass( GsonHelper.getAsBoolean( jobj, "pass", true ));

		JsonArray jargs;
		try {
			jargs = jobj.getAsJsonArray( "args" );
		} catch( ClassCastException ex ) {
			throw new FuzzTestJsonError( String.format( "Method '%s' argument list must be an array", methodName ), ex );
		}
		if( jargs == null || jargs.size() == 0 ) {
			throw new FuzzTestJsonError( String.format( "Method '%s' has no argument list", methodName ) );
		}
		Class params [];
		try {
			params = getMethodParams( testClass, methodName, jargs.size() );
		} catch( FuzzTestJsonError ex ) {
			throw new FuzzTestJsonError( ex.getMessage() );
		}

		int idx = 0;
		for( JsonElement jarg : jargs ) {			
			fuzzTestCase.addArg( createArg( jarg, params[idx] ));
			idx++;
		}

		return fuzzTestCase;
	}

	@SuppressWarnings( "unchecked method invocation: <T>fromJson(com.google.gson.JsonElement,java.lang.Class<T>) in com.google.gson.Gson is applied to (com.google.gson.Jon: <T>fromJson(com.google.gson.JsonElement,java.lang.Class<T>) in com.google.gson.Gson is applied to (com.google.gson.J" )
	protected static Object createArg( JsonElement jarg, Class argClass )
	{
		Gson consumer = new GsonBuilder()
			.setPrettyPrinting()
			.create();

		if( argClass == StringBuilder.class ) {
			return consumer.fromJson( jarg, String.class ) == null ? null: new StringBuilder( consumer.fromJson( jarg, String.class ) );
		} else if( argClass == Integer.class ) {
			return consumer.fromJson( jarg, Integer.class ) == null ? null: consumer.fromJson( jarg, Integer.class );
		} else {
			return consumer.fromJson( jarg, argClass == Object.class ? String.class : argClass );
		}
	}

	protected static Class[] getMethodParams( final Class testClass, final String methodName, final int argsNum ) throws FuzzTestJsonError
	{
		Method methods[] = testClass.getMethods();
		Class params [] = null;
		Method testMethod = null;
		for( Method method : methods ) {
			if( method.getName().equals( methodName) ) {
				params = method.getParameterTypes();
				if( params.length > argsNum )
					throw new FuzzTestJsonError( String.format( "Method '%s' has a mismatched parameters signature (too few)", methodName ));
				else if( params.length < argsNum )
					throw new FuzzTestJsonError( String.format( "Method '%s' has a mismatched parameters signature (too many)", methodName ));
				if( testMethod == null )
					testMethod = method;
			}
		}

		if( params == null )
			throw new FuzzTestJsonError( String.format( "No test method '%s' with matching parameters signature", methodName ));

		return params;
	}

	/**
	 * Does the test pass
	 *
	 * @return boolean
	 */
	public boolean isPass()
	{
		return pass;
	}

	/**
	 * Does the test fail
	 *
	 * @return boolean
	 */
	public boolean isFail()
	{
		return ! pass;
	}

	/**
	 * Does the test pass
	 *
	 * @return boolean
	 */
	public boolean getPass()
	{
		return isPass();
	}

	/**
	 * set whether the test passes or fails
	 *
	 * @param pass does the test pass
	 */
	public void setPass( boolean pass )
	{
		this.pass = pass;
	}

	/**
	 * Get the test's message
	 *
	 * @return comment
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * Set the test's message
	 *
	 * @param message -
	 */
	public void setMessage( String message )
	{
		this.message = message;
	}

	/**
	 * Get the args to call the test method with (via reflection)
	 *
	 * @return Object [] of method arguments
	 */
	public Object [] getArgs()
	{
		return args.toArray();
	}

	/**
	 * @param arg add the command line argument
	 */
	public void addArg( Object arg )
	{
		args.add( arg );
	}

	/**
	 * Get the test number, set by FuzzTester
	 *
	 * @return int test number
	 */
	public int getNumber()
	{
		return number;
	}

	/**
	 * Set the test number, with a fluent interface
	 *
	 * @param number the test number (set by FuzzTester)
	 * @return this (fluent interface)
	 */
	public FuzzTestCase setNumber( int number )
	{
		this.number = number;
		return this;
	}

	/**
	 * Get the test method name
	 * @return String
	 */
	public String getMethodName()
	{
		return methodName;
	}

	/**
	 * Set the test method name, with a fluent interface
	 * @param methodName the method name for this testcase
	 * @return this (fluent interface)
	 */
	public FuzzTestCase setMethodName( String methodName )
	{
		this.methodName = methodName;
		return this;
	}

	/**
	 * Return the thrown exception for this test case (if there is one)
	 * @return String representation of the Exception e.g. "java.lang.Exception"
	 */
	public String getExceptionThrown()
	{
		return exceptionThrown;
	}

	/**
	 * Set the exception expected to be thrown by this test case
	 *
	 * @param exceptionThrown String representation of the exception e.g. "java.lang.Exception"
	 */
	public void setExceptionThrown( String exceptionThrown )
	{
		this.exceptionThrown = exceptionThrown;
	}

	/**
	 * Has the Test Exception been set
	 *
	 * @return boolean is an exception expected
	 */
	public boolean isTestException()
	{
		return exceptionThrown != null;
	}

	/**
	 *  Test if the exception thrown matches that of the test case
	 *
	 * @param testException throwable
	 * @return do the exceptions match
	 */
	public boolean matchTestException( Throwable testException )
	{
		return StringHelper.unnulled( exceptionThrown ).equals( testException.getClass().getName() );
	}

	/**
	 * Get the excepted exception message
	 *
	 * @return String the exception message
	 */
	public String getExceptionMessage()
	{
		return exceptionMessage;
	}

	/**
	 * Match the exception message thrown for the test with the expected exception message in the test case
	 *
	 * @param exception the throwable
	 * @return does the exected exception message match the thrown message
	 */
	public boolean matchTestExceptionMessage( Throwable exception )
	{
		return StringHelper.unnulled( exceptionMessage ).compareToIgnoreCase( exception.getMessage() ) == 0;
	}

	/**
	 * Has the test exception message been set (i.e. should we check for the message
	 * in FuzzTester)
	 * @return is it set
	 */
	public boolean isTestExceptionMessage()
	{
		return !StringHelper.isEmpty( exceptionMessage );
	}

	/**
	 * Set the exception message
	 *
	 * @param exceptionMessage the exception message expected
	 */
	public void setExceptionMessage( String exceptionMessage )
	{
		this.exceptionMessage = exceptionMessage;
	}

	/**
	 * @return skip this test?
	 */
	public boolean isSkip()
	{
		return skip;
	}

	/**
	 * @param skip skip the test?
	 */
	public void setSkip( boolean skip )
	{
		this.skip = skip;
	}

	/**
	 * The string version, used by the JUnit test runner
	 *
	 * @return the test case identity
	 */
	public String toString()
	{
		return String.format( "%s, #%d, Pass: %b %s", methodName, number, pass, ( ! StringHelper.isEmpty( exceptionThrown ) ? exceptionThrown : "" ) );
	}
}
