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

import org.junit.runners.model.FrameworkMethod;

/**
 * Merge the test cases with the test method
 * @author Tim Desjardins
 * @version $Rev$
 * $Id$
 */
public class FuzzTestMethod extends FrameworkMethod
{
	public FuzzTestCase fuzzTestCase;

	/**
	 * Create the FuzzTestMethod that marries the test method with the
	 * test case.
	 *
	 * @param method FrameworkMethod (the method annotated with @Fuzz)
	 * @param fuzzTestCase the individual FuzzTestCase associated with this test method
	 */
	public FuzzTestMethod( FrameworkMethod method, FuzzTestCase fuzzTestCase )
	{
		super( method.getMethod() );
		this.fuzzTestCase = fuzzTestCase;
	}

	/**
	 * @return the FuzzTestCase
	 */
	public FuzzTestCase getTestCase()
	{
		return fuzzTestCase;
	}

	/**
	 * Return the test name for this testCase/testMethod pair
	 *
	 * @return FuzzTestCase name
	 */
	@Override
	public String getName()
	{
		return fuzzTestCase.getName();
	}
}