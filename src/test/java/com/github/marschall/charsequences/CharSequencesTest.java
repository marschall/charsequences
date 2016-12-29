package com.github.marschall.charsequences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CharSequencesTest {

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
