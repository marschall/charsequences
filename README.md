CharSequences [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/charsequences/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/charsequences) [![Javadocs](http://www.javadoc.io/badge/com.github.marschall/charsequences.svg)](http://www.javadoc.io/doc/com.github.marschall/charsequences)
=============

```xml
<dependency>
    <groupId>com.github.marschall</groupId>
    <artifactId>charsequences</artifactId>
    <version>0.2.0</version>
</dependency>
```

Utility methods for dealing with `java.lang.CharSequence`. When dealing with [CharSequence](https://docs.oracle.com/javase/8/docs/api/java/lang/CharSequence.html) instead of [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) many convince methods are not available. While you could simply call [toString()](https://docs.oracle.com/javase/8/docs/api/java/lang/CharSequence.html#toString--) on the CharSequence this would defeat the purpose of using a CharSequence. This is project contains implementations of some of them.

 - [indexOf(char)](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html#indexOf-int-)
 - [indexOf(char, int)](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html#indexOf-int-int-)
 - [contains(CharSequence)](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html#contains-java.lang.CharSequence-)
 - [trim()](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html#trim--)
 - [Integer.parseInt(String)](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html#parseInt-java.lang.String-)
 - [Long.parseLong(String)](https://docs.oracle.com/javase/8/docs/api/java/lang/Long.html#parseLong-java.lang.String-)
 
They avoid allocation where possible, check out the [Javadoc](http://www.javadoc.io/doc/com.github.marschall/charsequences) for more information.

