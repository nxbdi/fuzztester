package com.agwego;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(value=Suite.class)
@Suite.SuiteClasses(value={ com.agwego.common.CommonSuite.class, com.agwego.fuzz.FuzzSuite.class })
public class AllTests
{
}