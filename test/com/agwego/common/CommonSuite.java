package com.agwego.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(value=Suite.class)
@Suite.SuiteClasses(
	value={
		FileFilterPrePostTest.class,
		StringHelperTest.class,
		NumberHelperTest.class,
		FileHelperTest.class
	}
)
public class CommonSuite
{	
}