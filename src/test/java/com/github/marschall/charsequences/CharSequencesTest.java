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
  public void betweenAnd() {
    assertEquals("20.5K", CharSequences.betweenAnd("116.9K->96.4K(20.5K), avg 17.5%, 0.0018690 secs", '(', ')'));
    assertEquals("", CharSequences.betweenAnd("116.9K->96.4K(), avg 17.5%, 0.0018690 secs", '(', ')'));
    assertNull("20.5K", CharSequences.betweenAnd("116.9K->96.4K)(, avg 17.5%, 0.0018690 secs", '(', ')'));

    assertNull(CharSequences.betweenAnd("116.9K->96.4K(20.5K", '(', ')'));
    assertNull(CharSequences.betweenAnd("20.5K), avg 17.5%, 0.0018690 secs", '(', ')'));
  }

}
