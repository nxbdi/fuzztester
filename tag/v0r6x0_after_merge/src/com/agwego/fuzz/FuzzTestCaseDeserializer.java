package com.agwego.fuzz;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class FuzzTestCaseDeserializer implements JsonDeserializer<FuzzTestCase>
{
	public FuzzTestCase deserialize( JsonElement json, Type typeOfT, JsonDeserializationContext context ) throws JsonParseException
	{
		return null;
	}
}