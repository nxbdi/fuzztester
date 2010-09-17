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

package com.agwego.fuzz.examples;

/**
 * SimpleObject - a simple object to demonstrate initializing an object from JSON and using simple objects
 * in yout test cases
 *
 * @author Tim Desjardins
 * @version $Rev$
 * <br/>
 * $Id$
 */

public class SimpleObject
{
	private String strObj;
	private Integer intObj;
	private Boolean boolObj;

	public SimpleObject()
	{
	}

	public SimpleObject( SimpleObject pl )
	{
		this.strObj = pl.getStrObj();
		this.intObj = pl.getIntObj();
		this.boolObj = pl.getBoolObj();
	}

	public String getStrObj()
	{
		return strObj;
	}

	public void setStrObj( String strObj )
	{
		this.strObj = strObj;
	}

	public Integer getIntObj()
	{
		return intObj;
	}

	public void setIntObj( Integer intObj )
	{
		this.intObj = intObj;
	}

	public Boolean getBoolObj()
	{
		return boolObj;
	}

	public void setBoolObj( Boolean boolObj )
	{
		this.boolObj = boolObj;
	}

	public String toString()
	{
		return String.format( " strObj = %s, intObj = %s, boolObj = %s ", strObj, intObj, boolObj );
	}
}