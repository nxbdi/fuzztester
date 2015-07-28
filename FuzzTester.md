# Overview #

FuzzTester is a JUnit4 test class Runner. FuzzTester's test cases are stored in structured JSON files that allow for easy editing and running of test cases without having to write additional code to run or manage test data. Fuzztester supports rapid testing and development and eases the burden of testing boundary or edge cases.

Current features are basic but the idea is to get it out and tested, basic features include mixing JUnit and FuzzTests in the same test class, testing for exceptions and exception messages without any special code.

Future enhancements will include support for template driven inputs, macros for testing relative values (like todayPlusDays( 2 ), nowMinusMinuntes( 60 ) ), and generator based inputs, custom setup and tear down methods per fuzztest case.

# Easy to use #
See [Getting Started](GettingStarted.md)
  1. write your test
  1. annotate your tests
  1. create your test data in JSON format
  1. run your tests

# Test Restrictions #

  * Unit tests must have unique method names and argument signatures (otherwise an exception will be thrown)
  * Method parameters must subclass object (no primitives, you can use primitives in the JSON files which will be cast to their Object representation )
> #### The following are limitations imposed by GSON ####
    * You cannot initialize objects via JSON that include collections of objects or self-referential objects, acyclic object trees are supported.
    * Every object that is to instantiated from a JSON file needs a default constructor defined.

# Benefits #
  * Add test cases without recompiling (just run) allows you to rapidly add tests without coding (until you break you code of course)
  * less test code since test data is external to the tests you end up writing smaller simpler tests
  * Mix @fuzz and @test in the same test class
  * One or many JSON formatted test files per unit test
  * Test cases show up properly in JUnit test runner
  * Easy exception handling, test for the exception class and or message
  * Properly supports 'null'
  * few restrictions on how you wish to manage your JSON test files
  * support for arrays [.md](.md) in test method arguments
  * skip individual testcases, skip entire methods, or only run certain tests

# Dependencies #
  * [GSON 1.4](http://code.google.com/p/google-gson/)
  * [JUnit 4.8.1](http://junit.org/)
  * [commons-beanutils 1.7.0](http://commons.apache.org/beanutils/)
  * [commons-collections 3.2.1](http://commons.apache.org/collections/)
  * [commons-lang 2.4](http://commons.apache.org/lang/)
  * [commons-logging 1.0.4](http://commons.apache.org/logging/)

# Version #
To determine the current version of the jar file you have you can use the following:
```
java -jar fuzz-tester.jar com.agwego.fuzz.Version
```