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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;
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
public class ArrayObjTest
{
	protected final Log log = LogFactory.getLog( getClass() );

	@Test
	public void testAssumptionsFailed() throws Exception
	{
		FuzzTester ft = new FuzzTester( com.agwego.fuzz.fuzz_tester_test.TestAssumptionsFail.class );
		List<Runner> children = ft.getChildren();
		assertEquals( 1, children.size() );
		FuzzTestRunner ftRunner = (FuzzTestRunner) children.get( 0 );
		TestNotifier rn = new TestNotifier();
		ftRunner.run( rn );
		assertEquals( 0, rn.getFailureCount() );
		assertEquals( 1, rn.getAssumptionsFailedCount() );
		assertEquals( 1, rn.getFinishedCount() );
	}
}
