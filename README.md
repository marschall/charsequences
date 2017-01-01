CharSequences [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/charsequences/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/charsequences) [![Javadocs](http://www.javadoc.io/badge/com.github.marschall/charsequences.svg)](http://www.javadoc.io/doc/com.github.marschall/charsequences)
=============

```xml
<dependency>
    <groupId>com.github.marschall</groupId>
    <artifactId>charsequences</artifactId>
    <version>0.1.0</version>
</dependency>
```

Utility methods for dealing with `java.lang.CharSequence`. When dealing with [CharSequence](https://docs.oracle.com/javase/8/docs/api/java/lang/CharSequence.html) instead of [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) many convince methods such as [indexOf](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html#indexOf-int-) and [contains](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html#contains-java.lang.CharSequence-) are not available. While you could simply call [toString](https://docs.oracle.com/javase/8/docs/api/java/lang/CharSequence.html#toString--) on the CharSequence this would defeat the purpose of using a CharSequence. This is project contains implementations of some of them.

In addition this project contains implementations of [Integer.parseInt(String)](https://docs.oracle.com/javase/8/docs/api/java/lang/Integer.html#parseInt-java.lang.String-) and [Long.parseLong(String)](https://docs.oracle.com/javase/8/docs/api/java/lang/Long.html#parseLong-java.lang.String-) that work with a CharSequence without allocating a String.
