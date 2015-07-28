
## JSON Object format ##

Fuzz Testers test cases are codified with Javascript Object Notation (JSON) format see [JSON.org](http://json.org/) for details concerning escaping special characters, types, etc. Since Google GSON is the underlying JSON library refer to the [GSON documentation](http://sites.google.com/site/gson/gson-user-guide) for a detailed description of type handling.

## Simple Types ##
  * int, long, short, byte -> [java.lang.Integer](http://download-llnw.oracle.com/javase/6/docs/api/java/lang/Integer.html) (if possible)
  * float, double -> [java.lang.Double](http://download-llnw.oracle.com/javase/6/docs/api/java/lang/Double.html)
  * boolean -> [java.lang.Boolean](http://download-llnw.oracle.com/javase/6/docs/api/java/lang/Boolean.html)
  * string -> [java.lang.String](http://download-llnw.oracle.com/javase/6/docs/api/java/lang/String.html)
  * null
  * arrays of simple types or simple json objects
  * simple objects(beans) (i.e. only acyclic object graphs, no collections of objects are supported at this time, no self referential objects)

## Test Format by Example ##

Here's an example JSON test file,

```
{
    "only" : [ "truncExample", "anotherExample" ], # optional, a list of method names to run
    "unitTest" : [ # required
        {
            "skip" : true,  # optional, default false, skip this method in its entirety 
            "comment" : "Getting Started Trunc Test (input, length, expected)",  # optional
            "method" : "truncExample",  # required the method name to test
            "testCases" : [  # required, list of test cases to run
                { "args" : [ "12", 6, "12" ] },
                { "args" : [ "123456", 6, "123456" ] },
                { "args" : [ "123456789", -1, "123456789" ] },
                {
                  "args" : [ null, 1, null ],
                  "pass" : false, # optional, assert the result of the test, default true
                }
            ]
        }
    ]
}
```
> ### Notes on args ###
  * type errors will look like the following: `The JsonDeserializer IntegerTypeAdapter failed to deserialized json object XYZ given the type class java.lang.Integer`
  * using built-in types in your method will generate errors like the following: `java.lang.ClassCastException`

## Testing Exceptions ##
```
{
    "unitTest" : [ # required
        {
            "comment" : "Getting Started except(input, expected)",
            "method" : "exceptionExample",
            "testCases" : [
                {
                    "name" : "some descriptive text", # optional
                    "args" : [ "1", null ],
                    "exceptionThrown" : "java.lang.Exception" # optinal, check that the fully qualified exception is thrown
                },
                {
                    "args" : [ "1", null ],
                    "exceptionThrown" : "java.lang.Exception",
                    "exceptionMessage" : "This is a test exception: 1" # optional check that the exception message matches
                },
                {
                    "args" : [ "1", null ],
                    "exceptionMessage" : "This is a test exception: 1"
                },
                {
                    "skip" : true, # optional, skip this test only, default false
                    "args" : [ null, null ]
                }
            ]
        }
    ]
}
```
> ### Notes on exceptions ###
  * you have to supply the fully qualified exception name for "exceptionThrown"
  * "exceptionMessage" only matches the beginning of the thrown exception message with the expected message, no wild cards or macros supported (at this time), depending on the requirement more sophisticated matching may be implemented like "exceptionMessageContains"

## Complex method arguments ##
An example of passing beans and arrays

```
{
    "unitTest" : [ # required
        {
            "method" : "truncExample2", 
            "testCases" : [
                { 
                  "args" : [ 
1                       { "memberData1" : "12", "someVariable" : 6, "anotherVariable" : 4.5 },
                       true,
2                       [ 1, 2, 9 ]
                  ],
3                 "name" : "My Trunc Example #1"
                }
            ]
        }
    ]
}
```
  1. denotes a simple object or bean with three member variables "memberData1", "someVariable" and "anotherVariable"
  1. is an array of ints
  1. you can name your tests, the name which will be used when printing out test results by the test runner (all tests are also numbered)
> ### Notes on objects ###
  * objects must have a default constructor defined (i.e. a constructor that takes no arguments) the error looks like this `No-args constructor for class x.y.z does not exist. Register an InstanceCreator with Gson for this type to fix this problem.`
  * by definition objects have no collections of other objects, but can reference other objects,m self references are not allowed
  * The JSON keys must match the member data names otherwise the value will not be set

## Tips ##
  * if you're having parsing issues with your JSON, try using a JSON lint resource like http://www.jsonlint.com/, the GSON parser doesn't offer the best error messages unfortunately
  * you can add meta-data to the JSON files, for instance key-value pairs that aren't recognized by FuzzTestRunner, for instance:
```
{
    "id" : "$Id: $",   # meta-data adds a subversion Id
    "unitTest" : [
        {
            "description" : "Some more meta-data",   # meta-data ignored by FuzzTestRunner
            "method" : "truncExample2", 
            "testCases" : [
                { "args" : [ true, "xyz" ] }
            ]
        }
    ]
}
```

## Element Descriptions ##
Elements in quotes (") are literals, _italicized_ elements are anonymous JSON objects e.g. an unnamed object denoted by braces {}
  * **"UnitTest"** : required, (only one) contains a collection of _test_ objects
  * "only" : optional, specifies a list of tests to run only
    * **_test_** object : required (many)
      * **"method"** : required the method name to be tested
      * "skip" : optional, default false if set to true the test method will not be run
      * "comment: optional, provide a comment for this test method
      * **"testCases"** : required, (only one) contains a collection of _testCase_
        * **_testCase_** object : required (many)
          * **"args"** : required, contains an array of parameters that will be passed to the test method
          * "pass" : optional, default true, if you expect the inputs to fail set pass to false
          * "skip" : optional, default false if set to true only the marked test will be skipped
          * "name" : optional, name this test case the name value is used in test runner output, this name will be echoed by the test runner