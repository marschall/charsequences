package com.github.marschall.charsequences;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class LuhnCheckTest {

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

  @Test
  public void exception() {
    assertThrows(IllegalArgumentException.class, () -> LuhnCheck.isValid("5116 3711 4643 4084"));
  }

}
