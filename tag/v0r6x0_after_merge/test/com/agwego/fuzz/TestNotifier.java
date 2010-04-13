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

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tim Desjardins
 * @version $Rev: 8 $
 *
 * $Id: FileFilterPrePostTest.java 8 2010-03-26 03:45:50Z agwego $
 */
public class TestNotifier extends RunNotifier
{
	private List<Failure> failures = new ArrayList<Failure>();
	private List<Description> ignored = new ArrayList<Description>();
	private List<Description> finished = new ArrayList<Description>();
	private List<Failure> assumptionsFailed = new ArrayList<Failure>();

	@Override
	public void fireTestFailure( final Failure failure )
	{
		failures.add( failure );
	}

	@Override
	public void fireTestIgnored( Description description )
	{
		ignored.add( description );
	}

	@Override
	public void fireTestFinished( Description description )
	{
		finished.add( description );
	}

	@Override
	public void fireTestAssumptionFailed( Failure failure )
	{
		assumptionsFailed.add( failure );
	}

	public int getFailureCount()
	{
		return failures.size();
	}

	public List<Failure> getFailures()
	{
		return failures;
	}

	public int getIgnoredCount()
	{
		return ignored.size();
	}

	public List<Description> getIgnored()
	{
		return ignored;
	}

	public int getFinishedCount()
	{
		return finished.size();
	}

	public List<Description> getFinished()
	{
		return finished;
	}

	public int getAssumptionsFailedCount()
	{
		return assumptionsFailed.size();
	}

	public List<Failure> getAssumptionsFailed()
	{
		return assumptionsFailed;
	}
}
