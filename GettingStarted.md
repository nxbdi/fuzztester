
# Dependencies #

FuzzTester is a [jUnit](http://junit.org/) test runner, it allows your test data to separated from your test cases.

## Prerequisites ##
  * Java 1.5+
  * JUnit 4+

## Required Jars ##
  * [JUnit 4](http://github.com/KentBeck/junit/downloads) tested with 4.8.1
  * [GSON 1.4](http://code.google.com/p/google-gson/)
  * [commons-beanutils 1.7.0](http://commons.apache.org/beanutils/)
  * [commons-collections 3.2.1](http://commons.apache.org/collections/)
  * [commons-lang 2.4](http://commons.apache.org/lang/)
  * [commons-logging 1.0.4](http://commons.apache.org/logging/)

Note that included in the release is a fuzz-tester-dependencies-version.jar that rolls up the Apache Commons and GSON libraries.

# Simple Test Case (in 4 steps) #

  1. Annotate your class with the fuzztester-runner and the test parameters which is a file or files with your test data
```
1 @RunWith( FuzzTester.class )
2 @Parameters( TestDirectory = "test/com/agwego/fuzz/examples", Prefix = "GettingStarted" )
3 public class GettingStartedTest {
```
    1. uses the JUnit [@RunWith](http://kentbeck.github.com/junit/javadoc/latest/org/junit/runner/RunWith.html) annotation to say use the fuzztester runner
    1. use the Fuzz Tester [@Parameters](http://fuzztester.googlecode.com/svn/trunk/doc/api/com/agwego/fuzz/annotations/Parameters.html) annotation that provides the test file(s) location and name(s). For this example all filenames  `test/com/agwego/fuzz/examples/GettingStarted*.json` will be read and used to run the tests.
> > <br />
  1. Mark your test fixtures with the [@Fuzz](http://fuzztester.googlecode.com/svn/trunk/doc/api/com/agwego/fuzz/annotations/Fuzz.html) annotation.
```
@Fuzz
public void truncExample( final String input, final Integer endIndex, final String expected )
{
    assertEquals( expected, GettingStarted.trunc( input, endIndex ));
}
```
> > A couple of important thing to note: unlike JUnit tests which don't take arguments, Fuzz Tester test fixtures take arguments, how many and what types are up to you and are largely dictated by the methods you're testing. Test fixtures tend to be rather concise which makes the tests easier to understand since data handling/manipulation isn't interwoven with the test code.
  1. Create your test data using the [Fuzz-Tester JSON](http://code.google.com/p/fuzztester/wiki/JSONFileFormat) format, the easiest thing to do is copy one of the examples and modify it for your tests, as an example:
```
{
1    "unitTest" : [
        {
2            "ignore" : false,
3            "comment" : "Getting Started Trunc Test (input, length, expected)",
4            "method" : "truncExample",
5            "testCases" : [
6                { "args" : [ "", 6, "" ] },
7                { "args" : [ "123456789", -1, "123456789" ] },
8                { "args" : [ "123456789", 0, "" ] },
9                { "args" : [ null, 1, null ], "pass" : true }
            ]
        }
    ]
}
```
> > <br>1. "unitTest" simply indicates this is a test <br> 2. "ignore" optional item defaults to false but allows you to ignore the test method <br> 3. "comment" optional item allows you to provide commentary <br> 4. "method" the name of the method to execute the testCases on <br> 5. "testCases" is an array of testCase objects <br> 6-9. Are individual test cases with arguments for the test method - in this case tuncExample - the arguments are represented as an array, for each "args" object the test method will be executed, there is also the ability to assert the test since you may expect some tests to fail you can test for that by using "pass" : false.<br>
</li></ul><ol><li>Running tests, simply add the Fuzz Tester jars and dependencies into the same library directory as JUnit and run your tests as you normally do.</li></ol>

For a complete example project download fuzz-tester-examples-version.zip, which covers the preceding and other features.<br>
<br>
<h1>Testing Exceptions</h1>
Exception testing is completely controlled using the JSON test files and you have the option to test for the exception class and or the exception message. There are no required annotations or special actions required in your test fixture, other than declaring your method throws an exception assuming the method you're testing throws a declared exception.<br>
<br>
<pre><code>{<br>
    "unitTest" : [<br>
        {<br>
            "comment" : "Getting Started except(input, expected)",<br>
            "method" : "exceptionExample",<br>
            "testCases" : [<br>
                {<br>
                     "args" : [ "1", null ],<br>
1                    "exceptionThrown" : "java.lang.Exception"<br>
                },<br>
                {<br>
                     "args" : [ "1", null ],<br>
2                    "exceptionThrown" : "java.lang.Exception",<br>
3                    "exceptionMessage" : "This is a test exception: 1"<br>
                },<br>
                {<br>
                    "args" : [ "1", null ],<br>
4                   "exceptionMessage" : "This is a test exception: 1"<br>
                },<br>
                {<br>
                    "args" : [ null, null ]<br>
                }<br>
            ]<br>
        }<br>
    ]<br>
}<br>
</code></pre>
<ol><li>simply declare the fully qualified exception name and run your test<br>
</li><li>is an example of catching an exception class and the corresponding message<br>
</li><li>test for the exception message (in this release no wild cards are allowed, treats the exception message as a java.lang.String.startsWith for the generated exception)<br>
</li><li>an example of just testing for the exception message