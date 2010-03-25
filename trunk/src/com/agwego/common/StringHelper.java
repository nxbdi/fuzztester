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

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * **************************************************************************
 * NOTE TO SELF BEFORE ADDING A NEW METHOD LOOK IN APACHE.COMMONS.STRINGUTILS
 * **************************************************************************
 *
 * @author Tim Desjardins
 * @version $Rev$
 * $Id: $
 */

public class StringHelper
{
	public static String join( AbstractCollection s, String delimiter )
	{
		StringBuffer buffer = new StringBuffer();
		Iterator iter = s.iterator();
		if( iter.hasNext() ) {
			buffer.append( iter.next() );
			while( iter.hasNext() ) {
				buffer.append( delimiter );
				buffer.append( iter.next() );
			}
		}
		return buffer.toString();
	}

	public static String join( Collection s, String delimiter )
	{
		StringBuffer buffer = new StringBuffer();
		Iterator iter = s.iterator();
		if( iter.hasNext() ) {
			buffer.append( iter.next() );
			while( iter.hasNext() ) {
				buffer.append( delimiter );
				buffer.append( iter.next() );
			}
		}
		return buffer.toString();
	}

	public static String join( String[] strs, String delimiter )
	{
		StringBuffer buffer = new StringBuffer();

		if( strs.length > 0 ) {
			buffer.append( strs[0] );
			for( int idx = 2; idx < strs.length; idx++ ) {
				buffer.append( delimiter );
				buffer.append( strs[idx] );
			}
		}
		return buffer.toString();
	}

	public static String join( String[] strs, String delimiter, String prefix )
	{
		StringBuffer buffer = new StringBuffer();

		if( strs.length > 0 ) {
			buffer.append( prefix );
			buffer.append( strs[0] );
			for( int idx = 1; idx < strs.length; idx++ ) {
				buffer.append( delimiter );
				buffer.append( prefix );
				buffer.append( strs[idx] );
			}
		}
		return buffer.toString();
	}

	/**
	 * Join pairs of Strings to create a URL query string.
	 * <p/>
	 * The key must be a String, the value can be anything. If
	 * the value is null that particular ( key, value ) is ignored.
	 *
	 * @ param kvs -
	 * @return -
	 */
/*
	public static String joinQueryPairs( List<Object> kvs )
	{
		return joinQueryPairs( kvs, false );
	}
*/
/*
	public static String joinQueryPairs( List<Object> kvs, boolean keepEmpty )
	{
		ArrayList<String> results = new ArrayList<String>();

		for( int vi = 0; vi < kvs.size(); vi += 2 ) {
			String key = (String) kvs.get( vi );
			Object value = kvs.get( vi + 1 );
			if( value == null ) {
				if( keepEmpty ) {
					value = "";
				} else {
					continue;
				}
			}

			results.add( key + "=" + StringHelper.cgihack( value.toString() ) );
		}

		return StringHelper.join( results, "&" );
	}
*/
/*
	public static String joinQueryPairs( Map<String, Object> kvs, String prefix, boolean keepEmpty )
	{
		ArrayList<String> results = new ArrayList<String>();

		for( Map.Entry<String, Object> entry : kvs.entrySet() ) {
			if( entry.getValue() == null || StringHelper.isEmpty( entry.getValue().toString() ) ) {
				if( keepEmpty ) {
					entry.setValue( "" );
				} else {
					continue;
				}
			}
			results.add( entry.getKey() + "=" + StringHelper.cgihack( entry.getValue().toString() ) );
		}

		if( results.isEmpty() )
			return "";

		return unnulled( prefix ) + join( results, "&" );
	}
*/

/*

	private static String cgihack( String value )
	{
		value = StringHelper.unnulled( value );
		value = value.replaceAll( " ", "+" );
		value = value.replaceAll( "&", "%26" );
		return value;
	}

	public enum Filter
	{
		TRIM,
		LEFT_TRIM,
		RIGHT_TRIM,
		NON_EMPTY,
		UPPER,
		LOWER, }

	public static List<String> filter( List<String> items, Filter... filter )
	{
		boolean do_trim = false;
		boolean do_left_trim = false;
		boolean do_right_trim = false;
		boolean do_non_empty = false;
		boolean do_upper = false;
		boolean do_lower = false;

		for( Filter f : filter ) {
			switch( f ) {
				case TRIM:
					do_trim = true;
					break;
				case LEFT_TRIM:
					do_left_trim = true;
					break;
				case RIGHT_TRIM:
					do_right_trim = true;
					break;
				case NON_EMPTY:
					do_non_empty = true;
					break;
				case UPPER:
					do_upper = true;
					break;
				case LOWER:
					do_lower = true;
					break;
			}
		}

		if( do_upper ) {
			for( int i = 0; i < items.size(); i++ ) {
				items.set( i, StringHelper.unnulled( items.get( i ) ).toUpperCase() );
			}
		} else if( do_lower ) {
			for( int i = 0; i < items.size(); i++ ) {
				items.set( i, StringHelper.unnulled( items.get( i ) ).toLowerCase() );
			}
		}

		if( do_trim ) {
			for( int i = 0; i < items.size(); i++ ) {
				items.set( i, StringHelper.trim( items.get( i ) ) );
			}
		} else if( do_left_trim ) {
			for( int i = 0; i < items.size(); i++ ) {
				items.set( i, StringHelper.leftTrim( items.get( i ) ) );
			}
		} else if( do_right_trim ) {
			for( int i = 0; i < items.size(); i++ ) {
				items.set( i, StringHelper.rightTrim( items.get( i ) ) );
			}
		}

		if( do_non_empty ) {
			List<String> nitems = new ArrayList<String>();

			for( int i = 0; i < items.size(); i++ ) {
				String item = items.get( i );
				if( !StringHelper.isEmpty( item ) ) {
					nitems.add( item );
				}
			}

			items = nitems;
		}

		return items;
	}
*/

	/*
	static public String trim( String value )
	{
		return StringHelper.unnulled( value ).replace( "^\\s+", "" ).replace( "\\s+$", "" );
	}

	static public String leftTrim( String value )
	{
		return StringHelper.unnulled( value ).replace( "^\\s+", "" );
	}

	static public String rightTrim( String value )
	{
		return StringHelper.unnulled( value ).replace( "\\s+$", "" );
	}
*/

	static public String substring( String value, int beginIndex )
	{
		value = StringHelper.unnulled( value );
		beginIndex = Math.min( beginIndex, value.length() );

		return substring( value, beginIndex, value.length() );
	}

	// no leak version of SubString without out all the god damned exceptions
	public static String substring( String s, int beginIndex, int endIndex )
	{
		if( s == null || beginIndex < 0 || endIndex > s.length() || beginIndex > endIndex || ( beginIndex == 0 && endIndex == s.length() ) )
			return s;

		char[] rtnStr = new char[endIndex - beginIndex];
		System.arraycopy( s.toCharArray(), beginIndex, rtnStr, 0, endIndex - beginIndex );

		return new String( rtnStr );
	}

	static public StringBuilder substringBuilder( String value, int beginIndex )
	{
		value = StringHelper.unnulled( value );
		beginIndex = Math.min( beginIndex, value.length() );

		return substringBuilder( value, beginIndex, value.length() );
	}

	// no leak version of SubString without out all the god damned exceptions
	public static StringBuilder substringBuilder( String s, int beginIndex, int endIndex )
	{
		return new StringBuilder( substring( s, beginIndex, endIndex ) );
	}	

	public static List<String> interleave( List<String>... lists )
	{
		ArrayList<String> results = new ArrayList<String>();

		int index = -1;
		boolean any = true;

		while( any ) {
			any = false;
			index += 1;

			for( List<String> list : lists ) {
				if( list.size() > index ) {
					results.add( list.get( index ) );
					any = true;
				}
			}
		}

		return results;
	}

	/**
	 * Extract the root page name (no extension) from the request uri
	 * if nothing relavent is found return "";
	 *
	 * @param requestUri -
	 * @return -
	 */
	public static String rootPageName( String requestUri )
	{
		try {
			String[] uriComponents = requestUri.split( "/" );
			return uriComponents[uriComponents.length - 1].split( "\\." )[0];
		} catch( Exception ex ) {
			return requestUri != null ? requestUri : "";
		}
	}

	/**
	 * Does a string contain a number, dear java does it really have to be this hard
	 *
	 * @param str the string in question
	 * @return does it have a number
	 */
	public static boolean hasNumber( String str )
	{
		for( Character c : str.toCharArray() ) {
			if( Character.isDigit( c ) )
				return true;
		}

		return false;
	}

	/**
	 * Does a string contain an alpha, dear java does it really have to be this hard
	 *
	 * @param str the string in question
	 * @return well, does it have a number
	 */
	public static boolean hasAlpha( String str )
	{
		for( Character c : str.toCharArray() ) {
			if( Character.isLetter( c ) )
				return true;
		}

		return false;
	}

	/**
	 * Does a string contain a lower alpha char
	 *
	 * @param str the string in question
	 * @return well, does it have a lower alpha char
	 */
	public static boolean hasAlphaLower( String str )
	{
		for( Character c : str.toCharArray() ) {
			if( Character.isLetter( c ) && Character.isLowerCase( c ) )
				return true;
		}

		return false;
	}

	/**
	 * Does a string contain a upper alpha char
	 *
	 * @param str the string in question
	 * @return well, does it have a lower alpha char
	 */
	public static boolean hasAlphaUpper( String str )
	{
		for( Character c : str.toCharArray() ) {
			if( Character.isLetter( c ) && Character.isUpperCase( c ) )
				return true;
		}

		return false;
	}

	/**
	 * Why is java so fucking brain dead
	 *
	 * @param str      -
	 * @param lastChar -
	 * @return our trunc'd string or the original string, whatever
	 */
	public static String trunc( String str, int lastChar )
	{
		try {
			return str.substring( 0, lastChar );
		} catch( Exception ex ) {
			// we don't care
		}
		return str;
	}

	public static boolean isEmpty( byte[] s )
	{
		return s == null || s.length == 0;
	}

	public static boolean isEmpty( String s )
	{
		return ( s == null ) || ( s.length() == 0 );
	}

	public static String nulled( String s )
	{
		if( ( s == null ) || ( s.length() == 0 ) ) {
			return null;
		} else {
			return s;
		}
	}

	public static String nulled( Object s )
	{
		if( s == null ) {
			return null;
		}

		return nulled( s.toString() );
	}

	public static String unnulled( String s )
	{
		return unnulled( s, "" );
	}

	public static String unnulled( String s, String otherwise )
	{
		if( s == null ) {
			return otherwise;
		} else {
			return s;
		}
	}

	public static String unnulled( Object s )
	{
		if( s == null ) {
			return "";
		}

		return unnulled( s.toString() );
	}

	public static String unnulled( Object s, String otherwise )
	{
		if( s == null ) {
			return otherwise;
		}

		return unnulled( s.toString(), otherwise );
	}

	public static String unempty( String s, String otherwise )
	{
		if( isEmpty( s ) ) {
			return otherwise;
		} else {
			return s;
		}
	}

	public static boolean isSame( String a, String b )
	{
		return unnulled( a ).equals( unnulled( b ) );
	}

	public static boolean isSameIgnoreCase( String a, String b )
	{
		return unnulled( a ).toLowerCase().equals( unnulled( b ).toLowerCase() );
	}

	/**
	 * If 'value' is not in the list of values, return the first value.
	 * I.e. make sure a value is in an enumeration
	 *
	 * @param value  -
	 * @param values -
	 * @return -
	 */
	public static String constrainToList( String value, String... values )
	{
		for( String v : values ) {
			if( StringHelper.isSame( value, v ) ) {
				return value;
			}
		}

		return values[0];
	}

	public static String convertChar( char ch )
	{
		String result;
		int ich = (int) ch;
		if( charMap.containsKey( ich ) ) {
			result = charMap.get( ich );
		} else {
			if( ich >= 0x80 ) {
				result = "&#x" + Integer.toHexString( ich ) + ";";
			} else {
				result = Character.toString( ch );
			}
			charMap.put( ich, result );
		}
		return result;
	}

	public static String u2h( String inText )
	{
		if( inText == null )
			return inText;

		StringBuilder buf = new StringBuilder();
		int textLen = inText.length();
		for( int ix = 0; ix < textLen; ix++ ) {
			buf.append( convertChar( inText.charAt( ix ) ) );
		}
		return buf.toString();
	}

	public static String h2u( String inText )
	{
		if( inText == null )
			return inText;

		/*
		 *	Numbered entities
		 */
		Pattern n_pattern = Pattern.compile( "&#([0-9]{1,4});" );
		Matcher n_matcher = n_pattern.matcher( inText );

		HashSet<String> n_found = new HashSet<String>();
		while( n_matcher.find() ) {
			n_found.add( inText.substring( n_matcher.start(), n_matcher.end() ) );
		}

		for( String f : n_found ) {
			int codepoint = NumberHelper.parseInt( f.replaceAll( "[^0-9]", "" ) );
			if( codepoint != 0 ) {
				inText = inText.replaceAll( f, new String( Character.toChars( codepoint ) ) );
			}
		}

		/*
		 *	Named entities
		 */
		Pattern pattern = Pattern.compile( "&([0-9a-zA-Z ]{3,6});" );

		Matcher matcher = pattern.matcher( inText );

		Stack<String> found = new Stack<String>();
		while( matcher.find() ) {
			found.push( inText.substring( matcher.start(), matcher.end() ) );
		}

		for( String f : found ) {
			if( escMap.get( f ) != null )
				inText = inText.replaceAll( f, escMap.get( f ) );
		}

		return inText;
	}

	// A map of Unicode values to HTML/XML char "entities" (named escape seqs).
	private static HashMap<Integer, String> charMap;
	private static HashMap<String, String> escMap;

	// Key/value pairs to populate the charMap.
	private static final String[] charMapData = { "\"", "&quot;", "&", "&amp;", "\'", "&apos;", "<", "&lt;", ">", "&gt;", "\u00A0", "&nbsp;", "\u00A1", "&iexcl;", "\u00A2", "&cent;", "\u00A3", "&pound;", "\u00A4", "&curren;", "\u00A5", "&yen;", "\u00A6", "&brvbar;", "\u00A7", "&sect;", "\u00A8", "&uml;", "\u00A9", "&copy;", "\u00AA", "&ordf;", "\u00AB", "&laquo;", "\u00AC", "&not;", "\u00AD", "&shy;", "\u00AE", "&reg;", "\u00AF", "&hibar;", "\u00B0", "&deg;", "\u00B1", "&plusmn;", "\u00B2", "&sup2;", "\u00B3", "&sup3;", "\u00B4", "&acute;", "\u00B5", "&micro;", "\u00B6", "&para;", "\u00B7", "&middot;", "\u00B8", "&cedil;", "\u00B9", "&sup1;", "\u00BA", "&ordm;", "\u00BB", "&raquo;", "\u00BC", "&frac14;", "\u00BD", "&frac12;", "\u00BE", "&frac34;", "\u00BF", "&iquest;", "\u00C0", "&Agrave;", "\u00C1", "&Aacute;", "\u00C2", "&Acirc;", "\u00C3", "&Atilde;", "\u00C4", "&Auml;", "\u00C5", "&Aring;", "\u00C6", "&AElig;", "\u00C7", "&Ccedil;", "\u00C8", "&Egrave;", "\u00C9", "&Eacute;", "\u00CA", "&Ecirc;", "\u00CB", "&Euml;", "\u00CC", "&Igrave;", "\u00CD", "&Iacute;", "\u00CE", "&Icirc;", "\u00CF", "&Iuml;", "\u00D0", "&ETH;", "\u00D1", "&Ntilde;", "\u00D2", "&Ograve;", "\u00D3", "&Oacute;", "\u00D4", "&Ocirc;", "\u00D5", "&Otilde;", "\u00D6", "&Ouml;", "\u00D7", "&times;", "\u00D8", "&Oslash;", "\u00D9", "&Ugrave;", "\u00DA", "&Uacute;", "\u00DB", "&Ucirc;", "\u00DC", "&Uuml;", "\u00DD", "&Yacute;", "\u00DE", "&THORN;", "\u00DF", "&szlig;", "\u00E0", "&agrave;", "\u00E1", "&aacute;", "\u00E2", "&acirc;", "\u00E3", "&atilde;", "\u00E4", "&auml;", "\u00E5", "&aring;", "\u00E6", "&aelig;", "\u00E7", "&ccedil;", "\u00E8", "&egrave;", "\u00E9", "&eacute;", "\u00EA", "&ecirc;", "\u00EB", "&euml;", "\u00EC", "&igrave;", "\u00ED", "&iacute;", "\u00EE", "&icirc;", "\u00EF", "&iuml;", "\u00F0", "&eth;", "\u00F1", "&ntilde;", "\u00F2", "&ograve;", "\u00F3", "&oacute;", "\u00F4", "&ocirc;", "\u00F5", "&otilde;", "\u00F6", "&ouml;", "\u00F7", "&divide;", "\u00F8", "&oslash;", "\u00F9", "&ugrave;", "\u00FA", "&uacute;", "\u00FB", "&ucirc;", "\u00FC", "&uuml;", "\u00FD", "&yacute;", "\u00FE", "&thorn;", "\u00FF", "&yuml;", "\u0152", "&OElig;", "\u0153", "&oelig;", "\u0160", "&Scaron;", "\u0161", "&scaron;", "\u0178", "&Yuml;", "\u017D", "&Zcaron;", "\u017E", "&zcaron;", "\u0192", "&fnof;", "\u02C6", "&circ;", "\u02DC", "&tilde;", "\u03A9", "&Omega;", "\u03C0", "&pi;", "\u2013", "&ndash;", "\u2014", "&mdash;", "\u2018", "&lsquo;", "\u2019", "&rsquo;", "\u201A", "&sbquo;", "\u201C", "&ldquo;", "\u201D", "&rdquo;", "\u201E", "&bdquote;", "\u2020", "&dagger;", "\u2021", "&Dagger;", "\u2022", "&bull;", "\u2026", "&hellip;", "\u2030", "&permil;", "\u2039", "&lsaquo;", "\u203A", "&rsaquo;", "\u2044", "&frasl;", "\u20AC", "&euro;", "\u2122", "&trade;", "\u2202", "&part;", "\u220F", "&prod;", "\u2211", "&sum;", "\u221A", "&radic;", "\u221E", "&infin;", "\u222B", "&int;", "\u2248", "&asymp;", "\u2260", "&ne;", "\u2264", "&le;", "\u2265", "&ge;", "\u25CA", "&loz;" };

	static {    // Static block to init the char map from the data array.
		charMap = new HashMap<Integer, String>();
		escMap = new HashMap<String, String>();

		for( int ix = charMapData.length; ix > 0; ) {
			String val = charMapData[--ix];
			String sKey = charMapData[--ix];
			charMap.put( sKey.codePointAt( 0 ), val );
			escMap.put( val, sKey );
		}
	}

	/*
	static public List<String> makeWordList( String value )
	{
		value = StringHelper.unnulled( value ).toLowerCase();
		value = value.replaceAll( "\\s", " " );
		value = value.replaceAll( "\\p{Cntrl}", " " );
		value = value.replaceAll( "[\u0100-\uFFFF]", " " );
		value = value.replaceAll( "\\p{Punct}", " " );

		return StringHelper.filter( Arrays.asList( value.split( " +" ) ), StringHelper.Filter.NON_EMPTY );
	}
	*/

	public static String padRight( String value, int len, String ch )
	{
		return padWithChar( value, len, ch, true );
	}

	public static String padLeft( String value, int len, String ch )
	{
		return padWithChar( value, len, ch, false );
	}

	public static String padRight( String value, int len )
	{
		return padRight( value, len, " " );
	}

	public static String padLeft( String value, int len )
	{
		return padLeft( value, len, " " );
	}

	public static String padWithChar( String value, int len, String ch, boolean _right )
	{
		String tempStr = StringHelper.unnulled( value );

		// make sure the value to be padded doesn't exceed the required size
		if( tempStr.length() > len ) {
			tempStr = tempStr.substring( 0, len );
		}

		// pad the result with spaces
		StringBuffer result = new StringBuffer( tempStr );
		int lsize = result.length();
		for( int i = lsize; i < len; i++ ) {
			if( _right ) {
				result.append( ch );
			} else {
				result.insert( 0, ch );
			}
		}
		return result.toString();
	}
}