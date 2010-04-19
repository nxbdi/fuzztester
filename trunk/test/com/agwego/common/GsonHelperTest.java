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

package com.agwego.common;

import com.agwego.fuzz.FuzzTester;
import com.agwego.fuzz.annotations.Fuzz;
import com.agwego.fuzz.annotations.Parameters;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author Tim Desjardins
 * @version $Rev: $
 * $Id: $
 */
@RunWith( FuzzTester.class )
@Parameters( TestDirectory = "test/com/agwego/common", Prefix = "GsonHelperTest" )
public class GsonHelperTest
{
	private final JsonObject testJsonObject = initJsonObject();

	public static JsonObject initJsonObject()
	{
		JsonParser jParser = new JsonParser();
		try {
			return jParser.parse( FileHelper.readFile( "test/com/agwego/common/gson_helper_test/test.json" )).getAsJsonObject();
		} catch( IOException ex ) {
			throw new RuntimeException( ex );
		}
	}

 	@Test
	public void classTest()
	{
		GsonHelper gh = new GsonHelper(); // I'm anal about 100% coverage
	}

	@Fuzz
	public void getAsString( final String key, final String expected )
	{
		assertEquals( GsonHelper.getAsString( testJsonObject, key ), expected );
	}

	@Fuzz
	public void getAsBoolean( final String key, final Boolean otherwise, final Boolean expected )
	{
		assertTrue( GsonHelper.getAsBoolean( testJsonObject, key, otherwise ) == expected );
	}

	@Fuzz
	public void getAsArray( final String key, final String [] expected )
	{
		JsonArray ja = GsonHelper.getAsArray( testJsonObject, key );
		assertTrue( ja.size() == expected.length );
	}

	@Test
	public void getAsArrayWithNull()
	{
		JsonArray ja = GsonHelper.getAsArray( null, "something" );
		assertTrue( ja.size() == 0 );
	}

	@Fuzz
	public void in( final String arrayKey, final String key, final Boolean expected )
	{
		assertTrue( GsonHelper.in( key, GsonHelper.getAsArray( testJsonObject, arrayKey ) ) == expected );
	}

	@Test
	public void inWithNull()
	{
		assertTrue( ! GsonHelper.in( "one", null ) );
	}
}