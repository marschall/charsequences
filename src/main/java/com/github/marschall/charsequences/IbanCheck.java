package com.github.marschall.charsequences;

/**
 * Provides methods for running a IBAN validation on a {@link CharSequence}.
 *
 * @see <a href="https://en.wikipedia.org/wiki/International_Bank_Account_Number">International Bank Account Number</a>
 */
public final class IbanCheck {

  private IbanCheck() {
    throw new AssertionError("not instantiable");
  }

  /**
   * Checks if a {@link CharSequence} is valid according to IBAN check digits.
   *
   * <p>Performs no country validation, this has to be done separately beforehand.</p>
   * <p>Performs no length validation (country dependent),
   * this has to be done separately beforehand.</p>
   * <p>Assumes the IBAN has has valid format (no spaces),
   * this has to be done separately beforehand.</p>
   *
   * @implNote no allocation is performed
   * @implNote no modulus is performed
   * @implNote only {@code int} arithmetic is used
   *
   * @param s the sequence to check, has to be numeric
   * @return if the sequence is valid according to IBAN check digits
   * @throws IllegalArgumentException if the IBAN format is not valid.
   */
  public static boolean isValid(CharSequence s) {
    int length = s.length();
    int sum = 0;
    for (int i = 4; i < length; i++) {
      char c = s.charAt(i);
      sum = accumulate(sum, c);
    }
    for (int i = 0; i < 4; i++) {
      char c = s.charAt(i);
      sum = accumulate(sum, c);
    }
    return sum == 1;
  }

  static int accumulate(int sum, char c) {
    if (c >= '0' && c <= '9') {
      int result = sum * 10 + (c - '0');
      // prevent overflow
      while (result >= 97) {
        result -= 97;
      }
      return result;
    } else if (c >= 'A' && c <= 'Z') {
      int value = c - 'A' + 10;
      int result = sum * 100 + value;
      // prevent overflow
      while (result >= 97) {
        result -= 97;
      }
      return result;
    } else {
      throw new IllegalArgumentException();
    }
  }

}
