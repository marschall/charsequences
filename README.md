CharSequences [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/charsequences/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/charsequences) [![Javadocs](https://www.javadoc.io/badge/com.github.marschall/charsequences.svg)](https://www.javadoc.io/doc/com.github.marschall/charsequences) [![Build Status](https://travis-ci.org/marschall/charsequences.svg?branch=master)](https://travis-ci.org/marschall/charsequences)
=============

Utility methods for dealing with `java.lang.CharSequence`. 

```xml
<dependency>
    <groupId>com.github.marschall</groupId>
    <artifactId>charsequences</artifactId>
    <version>0.5.3</version>
</dependency>
```

When dealing with [CharSequence](https://docs.oracle.com/javase/8/docs/api/java/lang/CharSequence.html) instead of [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) many convince methods are not available. While you could simply call [toString()](https://docs.oracle.com/javase/9/docs/api/java/lang/CharSequence.html#toString--) on the CharSequence this would defeat the purpose of using a CharSequence. This is project contains implementations of some of them.

 - [indexOf(char)](https://docs.oracle.com/javase/9/docs/api/java/lang/String.html#indexOf-int-)
 - [indexOf(char, int)](https://docs.oracle.com/javase/9/docs/api/java/lang/String.html#indexOf-int-int-)
 - [contains(CharSequence)](https://docs.oracle.com/javase/9/docs/api/java/lang/String.html#contains-java.lang.CharSequence-)
 - [trim()](https://docs.oracle.com/javase/9/docs/api/java/lang/String.html#trim--)
 - [Integer.parseInt(String)](https://docs.oracle.com/javase/9/docs/api/java/lang/Integer.html#parseInt-java.lang.String-), no longer needed in Java 9 but still faster
 - [Long.parseLong(String)](https://docs.oracle.com/javase/9/docs/api/java/lang/Long.html#parseLong-java.lang.String-), no longer needed in Java 9 but still faster
 - [String.split(String)](https://docs.oracle.com/javase/9/docs/api/java/lang/String.html#split-java.lang.String-), limited, only a single character is allowed, not a full regex
 - [UUID.fromString(String)](https://docs.oracle.com/javase/9/docs/api/java/util/UUID.html#fromString-java.lang.String-), no intermediary allocation, no longer needed in Java 9 but still faster
 
They avoid allocation where possible, check out the [Javadoc](http://www.javadoc.io/doc/com.github.marschall/charsequences) for more information.

