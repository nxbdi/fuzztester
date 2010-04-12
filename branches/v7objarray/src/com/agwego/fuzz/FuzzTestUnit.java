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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FuzzTestUnit
{
	private List<FuzzTestBlock> unitTest;

	public FuzzTestUnit()
	{
		unitTest = new ArrayList<FuzzTestBlock>();
	}

	public static FuzzTestUnit deserialize( JsonObject json )
	{
		final Log log = LogFactory.getLog( FuzzTestUnit.class );
		FuzzTestUnit testUnit = new FuzzTestUnit();

		Gson consumer = new GsonBuilder()
				.registerTypeAdapter( FuzzTestUnit.class, new FuzzTestUnitDeserializer() )
				.registerTypeAdapter( FuzzTestBlock.class, new FuzzTestBlockDeserializer() )
				.registerTypeAdapter( FuzzTestCase.class, new FuzzTestCaseDeserializer() )
				.setDateFormat( "yyyy-MM-dd HH:mm:ss.SSSZ" )
				.setPrettyPrinting().create();

		if( json.has( "unitTest" ) ) {
			JsonElement j = json.get( "unitTest" );
			Type fuzzTestBlockList = new TypeToken<ArrayList<FuzzTestBlock>>() {}.getType();
			List<FuzzTestBlock> ftbs = consumer.fromJson( j, fuzzTestBlockList );

			log.info( "ftbs = " + ftbs.size() );
		}

		return testUnit;
	}

	public List<FuzzTestBlock> getUnitTest()
	{
		return unitTest;
	}

	public void setUnitTest( List<FuzzTestBlock> unitTest )
	{
		this.unitTest = unitTest;
	}
}