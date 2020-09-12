package com.github.marschall.charsequences;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class CharSequencesTest {

  @Test
  public void isNumeric() {
    assertTrue(CharSequences.isNumeric("00123456789"));
  }

  @Test
  public void isNotNumeric() {
    assertFalse(CharSequences.isNumeric("-1"));
    assertFalse(CharSequences.isNumeric(""));

    for (int i = 127; i < Character.MAX_VALUE; i++) {
      if (Character.isDigit(i)) {
        assertFalse(CharSequences.isNumeric(Character.toString((char) i)));
      }
    }
  }

  @Test
  public void validInts() {
    assertParseInt(0, "+0");
    assertParseInt(0, "-0");
    assertParseInt(0, "0");

    assertParseInt(0, "00");
    assertParseInt(1, "01");
    // more 0 than Integer.MAX_VALUE has places
    assertParseInt(1, "000000000001");
    assertParseInt(0, "+00");
    assertParseInt(1, "+01");
    assertParseInt(0, "-00");
    assertParseInt(-1, "-01");

    assertParseInt(1, "1");
    assertParseInt(-1, "-1");
    assertParseInt(Integer.MAX_VALUE, Integer.toString(Integer.MAX_VALUE));
    assertParseInt(Integer.MIN_VALUE, Integer.toString(Integer.MIN_VALUE));
    assertParseInt(Integer.MAX_VALUE, "00" + Integer.toString(Integer.MAX_VALUE));
    assertParseInt(Integer.MIN_VALUE, Integer.toString(Integer.MIN_VALUE));
  }

  @Test
  public void parseIntSubIndices() {
    assertEquals(2, CharSequences.parseInt("123", 1, 2));
    assertEquals(2, CharSequences.parseInt("1+23", 1, 3));
    assertEquals(-2, CharSequences.parseInt("1-23", 1, 3));
    assertEquals(2, CharSequences.parseInt("1-23", 2, 3));
  }

  @Test
  public void parseLongSubIndices() {
    assertEquals(2L, CharSequences.parseLong("123", 1, 2));
    assertEquals(2L, CharSequences.parseLong("1+23", 1, 3));
    assertEquals(-2L, CharSequences.parseLong("1-23", 1, 3));
    assertEquals(2L, CharSequences.parseLong("1-23", 2, 3));
  }

  @Test
  public void invalidInts() {
    assertInvalidInt(null);
    assertInvalidInt("");
    assertInvalidInt("+");
    assertInvalidInt("-");
    assertInvalidInt("++0");
    assertInvalidInt("+-0");
    assertInvalidInt("-+0");
    assertInvalidInt("--0");

    assertInvalidInt("0 ");
    assertInvalidInt(" 0");
    assertInvalidInt("0a");
    assertInvalidInt("a0");
    assertInvalidInt("0/");
    assertInvalidInt("/0");
    assertInvalidInt("0:");
    assertInvalidInt(":0");

    assertInvalidInt(Long.toString(Integer.MAX_VALUE + 1L));
    assertInvalidInt("1" + Long.toString(Integer.MAX_VALUE + 1L));
    assertInvalidInt("-12" + Long.toString(Integer.MIN_VALUE - 1L).substring(1));
  }

  @Test
  public void invalidIntRangeMessage() {
    CharSequence charSequence = "xxx12axxx";
    NumberFormatException exception = assertThrows(NumberFormatException.class,
            () -> CharSequences.parseInt(charSequence, 3, 6));
    String exceptionMessage = exception.getMessage();

    assertThat(exceptionMessage, not(containsString("x")));
  }

  @Test
  public void invalidLongRangeMessage() {
    CharSequence charSequence = "xxx12axxx";
    NumberFormatException exception = assertThrows(NumberFormatException.class,
            () -> CharSequences.parseLong(charSequence, 3, 6));
    String exceptionMessage = exception.getMessage();

    assertThat(exceptionMessage, not(containsString("x")));
  }

  @Test
  public void invalidIntArguments() {
    assertInvalidIntArgument("1234567890", -1, 1);
    assertInvalidIntArgument("1234567890", 2, 1);
    assertInvalidIntArgument("1234567890", 1, 11);
  }

  @Test
  public void validLongs() {
    assertParseLong(0, "+0");
    assertParseLong(0, "-0");
    assertParseLong(0, "0");

    assertParseLong(0, "00");
    assertParseLong(1, "01");
    // more 0 than Long.MAX_VALUE has places
    assertParseLong(1, "0000000000000000000001");
    assertParseLong(0, "+00");
    assertParseLong(1, "+01");
    assertParseLong(0, "-00");
    assertParseLong(-1, "-01");

    assertParseLong(1, "1");
    assertParseLong(-1, "-1");
    assertParseLong(Long.MAX_VALUE, Long.toString(Long.MAX_VALUE));
    assertParseLong(Long.MIN_VALUE, Long.toString(Long.MIN_VALUE));
    assertParseLong(Long.MAX_VALUE, "00" + Long.toString(Long.MAX_VALUE));
    assertParseLong(Long.MIN_VALUE, Long.toString(Long.MIN_VALUE));
  }

  @Test
  public void invalidLongs() {
    assertInvalidLong(null);
    assertInvalidLong("");
    assertInvalidLong("+");
    assertInvalidLong("-");
    assertInvalidLong("++0");
    assertInvalidLong("+-0");
    assertInvalidLong("-+0");
    assertInvalidLong("--0");

    assertInvalidLong("0 ");
    assertInvalidLong(" 0");
    assertInvalidLong("0a");
    assertInvalidLong("a0");
    assertInvalidLong("0/");
    assertInvalidLong("/0");
    assertInvalidLong("0:");
    assertInvalidLong(":0");

    assertInvalidLong("9223372036854775808");
    assertInvalidLong("19223372036854775807");
    assertInvalidLong("-9223372036854775809");
    assertInvalidLong("-19223372036854775808");
  }

  @Test
  public void invalidLongArguments() {
    assertInvalidLongArgument("1234567890", -1, 1);
    assertInvalidLongArgument("1234567890", 2, 1);
    assertInvalidLongArgument("1234567890", 1, 11);
  }

  private static void assertParseInt(int expected, CharSequence charSequence) {
    assertEquals(expected, CharSequences.parseInt(charSequence));
    assertEquals(expected, Integer.parseInt(charSequence.toString()));
  }

  private static void assertInvalidIntArgument(CharSequence charSequence, int beginIndex, int endIndex) {
    assertThrows(IndexOutOfBoundsException.class,
            () -> CharSequences.parseInt(charSequence, beginIndex, endIndex),
            "should be invalid \"" + charSequence + "\"");
  }

  private static void assertInvalidLongArgument(CharSequence charSequence, int beginIndex, int endIndex) {
    assertThrows(IndexOutOfBoundsException.class,
            () -> CharSequences.parseLong(charSequence, beginIndex, endIndex),
            "should be invalid \"" + charSequence + "\"");
  }

  private static void assertInvalidInt(CharSequence charSequence) {
    NumberFormatException exception = assertThrows(NumberFormatException.class,
            () -> CharSequences.parseInt(charSequence),
            "should be invalid \"" + charSequence + "\"");
    String exceptionMessage = exception.getMessage();
    if (charSequence != null) {
      assertThat(exceptionMessage, containsString(charSequence.toString()));
    } else {
      assertThat(exceptionMessage, containsString("null"));
    }

    exception = assertThrows(NumberFormatException.class,
            () -> Integer.parseInt(charSequence != null ? charSequence.toString() : null),
            "should be invalid \"" + charSequence + "\"");
    exceptionMessage = exception.getMessage();
    if (charSequence != null) {
      assertThat(exceptionMessage, containsString(charSequence.toString()));
    } else {
      assertThat(exceptionMessage, containsString("null"));
    }
  }

  private static void assertParseLong(long expected, CharSequence charSequence) {
    assertEquals(expected, CharSequences.parseLong(charSequence));
    assertEquals(expected, Long.parseLong(charSequence.toString()));
  }

  private static void assertInvalidLong(CharSequence charSequence) {
    NumberFormatException exception = assertThrows(NumberFormatException.class,
            () -> CharSequences.parseLong(charSequence),
            "should be invalid \"" + charSequence + "\"");
    String exceptionMessage = exception.getMessage();
    if (charSequence != null) {
      assertThat(exceptionMessage, containsString(charSequence.toString()));
    } else {
      assertThat(exceptionMessage, containsString("null"));
    }

    exception = assertThrows(NumberFormatException.class,
            () -> Long.parseLong(charSequence != null ? charSequence.toString() : null),
            "should be invalid \"" + charSequence + "\"");
    exceptionMessage = exception.getMessage();
    if (charSequence != null) {
      assertThat(exceptionMessage, containsString(charSequence.toString()));
    } else {
      assertThat(exceptionMessage, containsString("null"));
    }
  }

  @Test
  public void indexOf() {
    assertEquals(-1, CharSequences.indexOf("abcd", "abcde"));
    assertEquals(-1, CharSequences.indexOf("abcd", "bcde"));
    assertEquals(2, CharSequences.indexOf("aaab", "ab"));
  }

  @Test
  public void indexOfChar() {
    assertEquals(2, CharSequences.indexOf("aab", 'b'));
    assertEquals(0, CharSequences.indexOf("baa", 'b'));
    assertEquals(-1, CharSequences.indexOf("aaa", 'b'));
  }

  @Test
  public void indexOfCharInt() {
    assertEquals(CharSequences.indexOf("a", 'b', 0), "a".indexOf('b', 0));
    assertEquals(CharSequences.indexOf("a", 'b', 1), "a".indexOf('b', 1));
    assertEquals(CharSequences.indexOf("a", 'b', 2), "a".indexOf('b', 2));
    assertEquals(CharSequences.indexOf("a", 'b', -1), "a".indexOf('b', -1));
  }

  @Test
  public void lastIndexOf() {
    assertEquals(-1, CharSequences.lastIndexOf("aaa", 'b'));
    assertEquals(0, CharSequences.lastIndexOf("baa", 'b'));
    assertEquals(2, CharSequences.lastIndexOf("aab", 'b'));
  }

  @ParameterizedTest
  @MethodSource("emptySequences")
  public void emptyCharSequence(CharSequence s) {
    assertEquals(0, s.length());
    assertSame(s, s.subSequence(0, 0));
    assertEquals("", s.toString());
    assertArrayEquals(new int[0], s.chars().toArray());
    assertArrayEquals(new int[0], s.codePoints().toArray());
    assertThrows(IndexOutOfBoundsException.class, () -> s.subSequence(0, 1));
    assertThrows(IndexOutOfBoundsException.class, () -> s.subSequence(1, 0));
    IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class, () -> s.charAt(0));
    if (isJava9OrLater()) {
      assertThat(exception.getMessage(), endsWith("ndex out of range: 0"));
    }
  }

  private boolean isJava9OrLater() {
    try {
      Class.forName("java.lang.Runtime$Version");
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }

  public static List<CharSequence> emptySequences() {
    return Arrays.asList("", CharSequences.EMPTY);
  }

  @Test
  public void emptyCharSequenceSerialization() throws ClassNotFoundException, IOException {
    assertSame(CharSequences.EMPTY, serializationCopy(CharSequences.EMPTY));
  }

  private static Object serializationCopy(Object o) throws IOException, ClassNotFoundException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(bos)) {
      objectOutputStream.writeObject(o);
    }
    try (InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream objectInput = new ObjectInputStream(inputStream)) {
      return objectInput.readObject();
    }
  }

  // TODO
  public static CharSequence subSequenceBetween(CharSequence charSequence, char start, char end) {
    int startIndex = CharSequences.indexOf(charSequence, start);
    if (startIndex != -1) {
      int endIndex = CharSequences.lastIndexOf(charSequence, end);
      if (endIndex > startIndex) {
        return charSequence.subSequence(startIndex + 1, endIndex);
      }
    }
    return null;
  }

  @Test
  public void subSequenceBetween() {
    assertEquals("20.5K", CharSequencesTest.subSequenceBetween("116.9K->96.4K(20.5K), avg 17.5%, 0.0018690 secs", '(', ')'));
    assertEquals("", CharSequencesTest.subSequenceBetween("116.9K->96.4K(), avg 17.5%, 0.0018690 secs", '(', ')'));
    assertNull(CharSequencesTest.subSequenceBetween("116.9K->96.4K)(, avg 17.5%, 0.0018690 secs", '(', ')'));

    assertNull(CharSequencesTest.subSequenceBetween("116.9K->96.4K(20.5K", '(', ')'));
    assertNull(CharSequencesTest.subSequenceBetween("20.5K), avg 17.5%, 0.0018690 secs", '(', ')'));
  }

  @Test
  public void trim() {
    assertTrim("", "");
    assertTrim("", " ");
    assertTrim("", "  ");
    assertTrim("a", "a");

    assertTrim("a", " a");
    assertTrim("a", "a ");
    assertTrim("a", " a ");

    assertTrim("a", "  a");
    assertTrim("a", "a  ");
    assertTrim("a", "  a  ");

    assertThrows(NullPointerException.class, () -> CharSequences.trim(null), "null should not be allowed");
  }

  private static void assertTrim(String expected, CharSequence charSequence) {
    assertEquals(expected, charSequence.toString().trim());
    assertEquals(expected, CharSequences.trim(charSequence).toString());
  }

  @Test
  public void split() {
    assertEquals(Collections.singletonList("a"), split("a", ','));

    assertEquals(Arrays.asList("a", "b"), split("a,b", ','));
    assertEquals(Arrays.asList("a", "b", ""), split("a,b,", ','));
    assertEquals(Arrays.asList("", "a", "b"), split(",a,b", ','));
    assertEquals(Arrays.asList("a", "", "b"), split("a,,b", ','));
  }

  private static List<String> split(CharSequence charSequence, char delimiter) {
    List<String> result = new ArrayList<>();
    for (CharSequence each : CharSequences.split(charSequence, delimiter)) {
      result.add(each.toString());
    }
    return result;
  }

  @Test
  public void uuidFromCharSequence() {
    String s = "ba226cf7-d156-4b18-a78a-094736208cc9";
    assertEquals(UUID.fromString(s), CharSequences.uuidFromCharSequence(s));

    s = "BA226CF7-D156-4B18-A78A-094736208CC9";
    assertEquals(UUID.fromString(s), CharSequences.uuidFromCharSequence(s));
  }

  @Test
  public void startsWithBom() {
    assertFalse(CharSequences.startsWithBom(""));
    assertFalse(CharSequences.startsWithBom("a"));
    assertFalse(CharSequences.startsWithBom("\uFFFE"));
    assertTrue(CharSequences.startsWithBom("\uFEFF"));
    assertFalse(CharSequences.startsWithBom("\\uFEFF"));
  }

  @Test
  public void removeLeadingBom() {
    assertEquals("", CharSequences.removeLeadingBom(""));
    assertEquals("a", CharSequences.removeLeadingBom("a"));
    assertEquals("", CharSequences.removeLeadingBom("\uFEFF"));
    assertEquals("a", CharSequences.removeLeadingBom("\uFEFFa"));
    assertEquals("\\uFEFF", CharSequences.removeLeadingBom("\\uFEFF"));
  }

  @Test
  public void hexDigit() {
    assertEquals(9, CharSequences.hexDigit('9'));
    assertEquals(0, CharSequences.hexDigit('0'));
    assertEquals(0xa, CharSequences.hexDigit('a'));
    assertEquals(0xA, CharSequences.hexDigit('A'));
    assertEquals(0xf, CharSequences.hexDigit('f'));
    assertEquals(0xF, CharSequences.hexDigit('F'));

    assertEquals(0xAF, (CharSequences.hexDigit('A') << 4) | CharSequences.hexDigit('F'));
  }

  @Test
  public void startsWith() {
    assertTrue(CharSequences.startsWith("ab", "a"));
    assertTrue(CharSequences.startsWith("a", "a"));
    assertTrue(CharSequences.startsWith("a", ""));

    assertFalse(CharSequences.startsWith("aaa", "ab"));
    assertFalse(CharSequences.startsWith("a", "aa"));
  }

}
