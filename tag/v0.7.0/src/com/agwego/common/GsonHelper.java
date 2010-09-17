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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Gson Helpers
 * 
 * @author Tim Desjardins
 * @version $Rev$
 * <br/>
 * $Id$
 */
public class GsonHelper
{
	/**
	 * Short hand, encapsulate the check if the object has the key and return the appropriate String otherwise null
	 *
	 * @param jObject the JsonObject in question
	 * @param key the key to lookup
	 * @return the value for key as a String
	 */
    public static String getAsString( final JsonObject jObject, final String key )
    {
        return getAsString( jObject, key, null);
    }

	/**
	 * Short hand, encapsulate the check if the object has the key and return the appropriate String otherwise null
	 *
	 * @param jObject the JsonObject in question
	 * @param key the key to lookup
	 * @param otherwise value to return if no key is found
	 * @return the value for key as a String
	 */
    public static String getAsString( final JsonObject jObject, final String key, final String otherwise )
    {
        return jObject.has( key ) ? jObject.get( key ).getAsString() : otherwise ;
    }

	/**
	 * Short hand, encapsulate the check if the object has the key and return the appropriate Boolean, otherwise the
	 * default value
	 *
	 * @param jObject the JsonObject in question
	 * @param key the key to lookup
	 * @param otherwise - if the key doesn't exist
	 * @return the value for key as a Boolean
	 */
    public static Boolean getAsBoolean( final JsonObject jObject, final String key, final Boolean otherwise )
    {
        return jObject.has( key ) ? jObject.get( key ).getAsBoolean() : otherwise;
    }

	/**
	 * Short hand, check if the object is not null and the key is presence return the result as a JsonArray
	 *
	 * @param jObject the JsonObject in question
	 * @param key the key to lookup
	 * @return the JsonArray for this key or a new empty JsonArray
	 */
	public static JsonArray getAsArray( final JsonObject jObject, final String key )
	{
		if( jObject != null && jObject.has( key ) ) {
			try {
				return jObject.getAsJsonArray( key );
			} catch( ClassCastException ex ) {
				// fall through
			}
		}

		return new JsonArray();
	}

	/**
	 * Determine if the key is present in the given JsonArray
	 * 
	 * @param key the key to lookup
	 * @param array the JsonArray in question
	 * @return is the key present in the JsonArray (a null array returns false)
	 */
	public static boolean in( final String key, final JsonArray array )
	{
		if( array == null )
			return false;
	
		for( JsonElement jElement : array )
			if( jElement.getAsString().equals( key ) )
				return true;

		return false;
	}
}