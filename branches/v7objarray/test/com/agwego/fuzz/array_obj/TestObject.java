package com.agwego.fuzz.array_obj;

public class TestObject
{
	private Integer count;
	private String name;
	private Double value;

	public TestObject()
	{
	}

	public TestObject( Integer count, String name, Double value )
	{
		this.count = count;
		this.name = name;
		this.value = value;
	}

	public Integer getCount()
	{
		return count;
	}

	public void setCount( Integer count )
	{
		this.count = count;
	}

	public String getName()
	{
		return name;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public Double getValue()
	{
		return value;
	}

	public void setValue( Double value )
	{
		this.value = value;
	}
}