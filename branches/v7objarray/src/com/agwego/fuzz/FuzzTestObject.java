package com.agwego.fuzz;

public class FuzzTestObject
{
	private String objectType;
	private String objectClass;
	private Object object;

	public String getObjectType()
	{
		return objectType;
	}

	public void setObjectType( String objectType )
	{
		this.objectType = objectType;
	}

	public String getObjectClass()
	{
		return objectClass;
	}

	public Class getObjectsClass()
	{
		try {
			return Class.forName( objectClass );
		} catch( ClassNotFoundException ex ) {
			return null;
		}
	}

	public void setObjectClass( String objectClass )
	{
		this.objectClass = objectClass;
	}

	public Object getObject()
	{
		return object;
	}

	public void setObject( Object object )
	{
		this.object = object;
	}
}