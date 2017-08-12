package com.github.marschall.charsequences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

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
    this.assertParseInt(0, "+0");
    this.assertParseInt(0, "-0");
    this.assertParseInt(0, "0");

    this.assertParseInt(0, "00");
    this.assertParseInt(1, "01");
    // more 0 than Integer.MAX_VALUE has places
    this.assertParseInt(1, "000000000001");
    this.assertParseInt(0, "+00");
    this.assertParseInt(1, "+01");
    this.assertParseInt(0, "-00");
    this.assertParseInt(-1, "-01");

    this.assertParseInt(1, "1");
    this.assertParseInt(-1, "-1");
    this.assertParseInt(Integer.MAX_VALUE, Integer.toString(Integer.MAX_VALUE));
    this.assertParseInt(Integer.MIN_VALUE, Integer.toString(Integer.MIN_VALUE));
    this.assertParseInt(Integer.MAX_VALUE, "00" + Integer.toString(Integer.MAX_VALUE));
    this.assertParseInt(Integer.MIN_VALUE, Integer.toString(Integer.MIN_VALUE));
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
    this.assertInvalidInt(null);
    this.assertInvalidInt("");
    this.assertInvalidInt("+");
    this.assertInvalidInt("-");
    this.assertInvalidInt("++0");
    this.assertInvalidInt("+-0");
    this.assertInvalidInt("-+0");
    this.assertInvalidInt("--0");

    this.assertInvalidInt("0 ");
    this.assertInvalidInt(" 0");
    this.assertInvalidInt("0a");
    this.assertInvalidInt("a0");
    this.assertInvalidInt("0/");
    this.assertInvalidInt("/0");
    this.assertInvalidInt("0:");
    this.assertInvalidInt(":0");

    this.assertInvalidInt(Long.toString(Integer.MAX_VALUE + 1L));
    this.assertInvalidInt("1" + Long.toString(Integer.MAX_VALUE + 1L));
    this.assertInvalidInt("-12" + Long.toString(Integer.MIN_VALUE - 1L).substring(1));
  }

  @Test
  public void invalidIntArguments() {
    this.assertInvalidIntArgument("1234567890", -1, 1);
    this.assertInvalidIntArgument("1234567890", 2, 1);
    this.assertInvalidIntArgument("1234567890", 1, 11);
  }

  @Test
  public void validLongs() {
    this.assertParseLong(0, "+0");
    this.assertParseLong(0, "-0");
    this.assertParseLong(0, "0");

    this.assertParseLong(0, "00");
    this.assertParseLong(1, "01");
    // more 0 than Long.MAX_VALUE has places
    this.assertParseLong(1, "0000000000000000000001");
    this.assertParseLong(0, "+00");
    this.assertParseLong(1, "+01");
    this.assertParseLong(0, "-00");
    this.assertParseLong(-1, "-01");

    this.assertParseLong(1, "1");
    this.assertParseLong(-1, "-1");
    this.assertParseLong(Long.MAX_VALUE, Long.toString(Long.MAX_VALUE));
    this.assertParseLong(Long.MIN_VALUE, Long.toString(Long.MIN_VALUE));
    this.assertParseLong(Long.MAX_VALUE, "00" + Long.toString(Long.MAX_VALUE));
    this.assertParseLong(Long.MIN_VALUE, Long.toString(Long.MIN_VALUE));
  }

  @Test
  public void invalidLongs() {
    this.assertInvalidLong(null);
    this.assertInvalidLong("");
    this.assertInvalidLong("+");
    this.assertInvalidLong("-");
    this.assertInvalidLong("++0");
    this.assertInvalidLong("+-0");
    this.assertInvalidLong("-+0");
    this.assertInvalidLong("--0");

    this.assertInvalidLong("0 ");
    this.assertInvalidLong(" 0");
    this.assertInvalidLong("0a");
    this.assertInvalidLong("a0");
    this.assertInvalidLong("0/");
    this.assertInvalidLong("/0");
    this.assertInvalidLong("0:");
    this.assertInvalidLong(":0");

    this.assertInvalidLong("9223372036854775808");
    this.assertInvalidLong("19223372036854775807");
    this.assertInvalidLong("-9223372036854775809");
    this.assertInvalidLong("-19223372036854775808");
  }

  @Test
  public void invalidLongArguments() {
    this.assertInvalidLongArgument("1234567890", -1, 1);
    this.assertInvalidLongArgument("1234567890", 2, 1);
    this.assertInvalidLongArgument("1234567890", 1, 11);
  }

  private void assertParseInt(int expected, CharSequence charSequence) {
    assertEquals(expected, CharSequences.parseInt(charSequence));
    assertEquals(expected, Integer.parseInt(charSequence.toString()));
  }

  private void assertInvalidIntArgument(CharSequence charSequence, int beginIndex, int endIndex) {
    try {
      CharSequences.parseInt(charSequence, beginIndex, endIndex);
      fail("should be invalid \"" + charSequence + "\"");
    } catch (IndexOutOfBoundsException e) {
      // should reach here
    }
  }

  private void assertInvalidLongArgument(CharSequence charSequence, int beginIndex, int endIndex) {
    try {
      CharSequences.parseLong(charSequence, beginIndex, endIndex);
      fail("should be invalid \"" + charSequence + "\"");
    } catch (IndexOutOfBoundsException e) {
      // should reach here
    }
  }

  private void assertInvalidInt(CharSequence charSequence) {
    try {
      CharSequences.parseInt(charSequence);
      fail("should be invalid \"" + charSequence + "\"");
    } catch (NumberFormatException e) {
      if (charSequence != null) {
        assertTrue(e.getMessage().contains(charSequence));
      } else {
        assertTrue(e.getMessage().contains("null"));
      }
    }
    try {
      if (charSequence != null) {
        Integer.parseInt(charSequence.toString());
      } else {
        Integer.parseInt(null);
      }
      fail("should be invalid \"" + charSequence + "\"");
    } catch (NumberFormatException e) {
      if (charSequence != null) {
        assertTrue(e.getMessage().contains(charSequence));
      } else {
        assertTrue(e.getMessage().contains("null"));
      }
    }
  }

  private void assertParseLong(long expected, CharSequence charSequence) {
    assertEquals(expected, CharSequences.parseLong(charSequence));
    assertEquals(expected, Long.parseLong(charSequence.toString()));
  }

  private void assertInvalidLong(CharSequence charSequence) {
    try {
      CharSequences.parseLong(charSequence);
      fail("should be invalid \"" + charSequence + "\"");
    } catch (NumberFormatException e) {
      if (charSequence != null) {
        assertTrue(e.getMessage().contains(charSequence));
      } else {
        assertTrue(e.getMessage().contains("null"));
      }
    }
    try {
      if (charSequence != null) {
        Long.parseLong(charSequence.toString());
      } else {
        Long.parseLong(null);
      }
      fail("should be invalid \"" + charSequence + "\"");
    } catch (NumberFormatException e) {
      if (charSequence != null) {
        assertTrue(e.getMessage().contains(charSequence));
      } else {
        assertTrue(e.getMessage().contains("null"));
      }
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
    assertNull("20.5K", CharSequencesTest.subSequenceBetween("116.9K->96.4K)(, avg 17.5%, 0.0018690 secs", '(', ')'));

    assertNull(CharSequencesTest.subSequenceBetween("116.9K->96.4K(20.5K", '(', ')'));
    assertNull(CharSequencesTest.subSequenceBetween("20.5K), avg 17.5%, 0.0018690 secs", '(', ')'));
  }

  @Test
  public void trim() {
    this.assertTrim("", "");
    this.assertTrim("", " ");
    this.assertTrim("", "  ");
    this.assertTrim("a", "a");

    this.assertTrim("a", " a");
    this.assertTrim("a", "a ");
    this.assertTrim("a", " a ");

    this.assertTrim("a", "  a");
    this.assertTrim("a", "a  ");
    this.assertTrim("a", "  a  ");

    try {
      CharSequences.trim(null);
      fail("null should not be allowed");
    } catch (NullPointerException e) {
      // should reach here
    }
  }

  private void assertTrim(String expected, CharSequence charSequence) {
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

}
