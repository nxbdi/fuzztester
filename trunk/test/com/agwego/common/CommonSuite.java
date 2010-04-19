package com.agwego.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(value=Suite.class)
@Suite.SuiteClasses(
	value={
		StringHelperTest.class,
		NumberHelperTest.class,
		FileHelperTest.class,		
		FileFilterPrePostTest.class,
		GsonHelperTest.class
	}
)
public class CommonSuite
{	
}