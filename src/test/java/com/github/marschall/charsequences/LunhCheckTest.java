package com.github.marschall.charsequences;

import static org.junit.Assert.*;

import org.junit.Test;

public class LunhCheckTest {

  @Test
  public void valid() {
    assertTrue(LuhnCheck.isValid("79927398713"));
    assertTrue(LuhnCheck.isValid("5116371146434084"));
    assertTrue(LuhnCheck.isValid("374648856525765"));
  }

  @Test
  public void invalid() {
    assertFalse(LuhnCheck.isValid("5116371146434085"));
    assertFalse(LuhnCheck.isValid("374648856525766"));
  }

}
