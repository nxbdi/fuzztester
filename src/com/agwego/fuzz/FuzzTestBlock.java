package com.agwego.fuzz;

import com.google.gson.JsonObject;

import java.util.List;

public class FuzzTestBlock
{
	private String method;
	private List<FuzzTestCase> testCases;

	public FuzzTestBlock()
	{
	}

	public static FuzzTestBlock deserialize( JsonObject json )
	{
		FuzzTestBlock testBlock = new FuzzTestBlock();


		return testBlock;
	}

	public String getMethod()
	{
		return method;
	}

	public void setMethod( String method )
	{
		this.method = method;
	}

	public List<FuzzTestCase> getTestCases()
	{
		return testCases;
	}

	public void setTestCases( List<FuzzTestCase> testCases )
	{
		this.testCases = testCases;
	}
}