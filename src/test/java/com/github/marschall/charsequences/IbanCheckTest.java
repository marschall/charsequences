package com.github.marschall.charsequences;

import static org.junit.Assert.*;

import org.junit.Test;

public class IbanCheckTest {

  @Test
  public void isValid() {
    assertTrue(IbanCheck.isValid("GB82WEST12345698765432"));
    assertTrue(IbanCheck.isValid("DE44500105175407324931"));
    assertTrue(IbanCheck.isValid("GR1601101250000000012300695"));
    assertTrue(IbanCheck.isValid("GB29NWBK60161331926819"));
    assertTrue(IbanCheck.isValid("SA0380000000608010167519"));
    assertTrue(IbanCheck.isValid("CH9300762011623852957"));
    assertTrue(IbanCheck.isValid("TR330006100519786457841326"));
  }

  @Test
  public void isNotValid() {
    assertFalse(IbanCheck.isValid("GB82WEST12345698765433"));
    assertFalse(IbanCheck.isValid("DE44500105175407324932"));
    assertFalse(IbanCheck.isValid("GR1601101250000000012300696"));
    assertFalse(IbanCheck.isValid("GB29NWBK60161331926818"));
    assertFalse(IbanCheck.isValid("SA0380000000608010167518"));
    assertFalse(IbanCheck.isValid("CH9300762011623852958"));
    assertFalse(IbanCheck.isValid("TR330006100519786457841327"));
  }

}
