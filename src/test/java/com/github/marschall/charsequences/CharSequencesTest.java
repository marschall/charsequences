package com.github.marschall.charsequences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class CharSequencesTest {

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

    assertInvalidInt(Long.toString((long) Integer.MAX_VALUE + 1L));
    assertInvalidInt("1" + Long.toString((long) Integer.MAX_VALUE + 1L));
    assertInvalidInt("-12" + Long.toString((long) Integer.MIN_VALUE - 1L).substring(1));
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

  private void assertParseInt(int expected, CharSequence charSequence) {
    assertEquals(expected, CharSequences.parseInt(charSequence));
    assertEquals(expected, Integer.parseInt(charSequence.toString()));
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
  public void lastIndexOf() {
    assertEquals(-1, CharSequences.lastIndexOf("aaa", 'b'));
    assertEquals(0, CharSequences.lastIndexOf("baa", 'b'));
    assertEquals(2, CharSequences.lastIndexOf("aab", 'b'));
  }

  @Test
  public void subSequenceBetween() {
    assertEquals("20.5K", CharSequences.subSequenceBetween("116.9K->96.4K(20.5K), avg 17.5%, 0.0018690 secs", '(', ')'));
    assertEquals("", CharSequences.subSequenceBetween("116.9K->96.4K(), avg 17.5%, 0.0018690 secs", '(', ')'));
    assertNull("20.5K", CharSequences.subSequenceBetween("116.9K->96.4K)(, avg 17.5%, 0.0018690 secs", '(', ')'));

    assertNull(CharSequences.subSequenceBetween("116.9K->96.4K(20.5K", '(', ')'));
    assertNull(CharSequences.subSequenceBetween("20.5K), avg 17.5%, 0.0018690 secs", '(', ')'));
  }

}
