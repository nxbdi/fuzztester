package com.agwego.fuzz;

import java.util.Properties;

public class Version
{
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