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

import java.util.Properties;

/**
 * Version: dump the version info
 *
 * @author Tim Desjardins
 * @version $Rev$
 * <br/>
 * $Id$
 */
public class Version
{
	/**
	 * Echo the version info
	 * 
	 * @param args - defaults
	 * @throws java.io.IOException - in case the build properties are missing
	 */
	public static void main( String[] args ) throws java.io.IOException
	{
		Properties buildTags = new Properties();

		buildTags.load( ClassLoader.getSystemResourceAsStream( "build_tag.properties" ) );

		System.out.println( "Version: " + buildTags.getProperty( "fuzz.version", "not set" ));
		System.out.println( "Built By: " + buildTags.getProperty( "build.user.name", "not set" ));
		System.out.println( "Built On: " + buildTags.getProperty( "build.computer", "not set" ));
		System.out.println( "Build Date: " + buildTags.getProperty( "build.date", "not set" ));
		System.out.println( "Build Time Stamp: " + buildTags.getProperty( "build.timestamp", "not set" ));
		System.out.println( "Build revision number (SVN): " + buildTags.getProperty( "build.revision_number", "not set" ));
	}
}