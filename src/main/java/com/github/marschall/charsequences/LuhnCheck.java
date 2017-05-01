package com.github.marschall.charsequences;

/**
 * Provides methods for running a Luhn check on a {@link CharSequence}.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Luhn_algorithm">Luhn algorithm</a>
 */
public final class LuhnCheck {

  private LuhnCheck() {
    throw new AssertionError("not instantiable");
  }

  /**
   * Checks if a {@link CharSequence} is valid according to the Luhn algorithm.
   *
   * <p>None of the following checks are performed:</p>
   * <ul>
   *  <li>No length validation is performed.</li>
   *  <li>No BIN validation is performed.</li>
   *  <li>No numeric check is performed.</li>
   * </ul>
   * <p>With the exception of the BIN validation they should all be done
   * before calling this method.</p>
   *
   * @implNote no allocation is performed
   * @implNote no modulus is performed
   * @implNote only {@code int} arithmetic is used
   * @implNote the sequence is scanned from start to end in order to help prefetching
   *
   * @param s the sequence to check, has to be numeric
   * @return if the sequence is valid according to Luhn
   * @throws NullPointerException if the sequence is {@code null}
   * @throws IllegalArgumentException if the sequence is not numeric
   * @see CharSequences#isNumeric(CharSequence)
   */
  public static boolean isValid(CharSequence s) {
    int length = s.length();
    boolean odd = (length & 1) == 1;
    int sum = odd ? oddLengthSum(s, length) : evenLengthSum(s, length);
    return sum == 0;
  }

  private static int evenLengthSum(CharSequence s, int sequenceLength) {
    int sum = 0;
    for (int i = 0; i < sequenceLength; i += 2) {
      // add the first digit, doubled
      int firstValue = toInt(s.charAt(i)) * 2;
      if (firstValue >= 10) {
        // add individual digits
        firstValue = firstValue - 9;
      }
      sum += firstValue;
      // prevent overflow
      if (sum >= 10) {
        sum -= 10;
      }

      // add the second digit
      int secondValue = toInt(s.charAt(i + 1));
      sum += secondValue;
      // prevent overflow
      if (sum >= 10) {
        sum -= 10;
      }
    }
    return sum;
  }

  private static int oddLengthSum(CharSequence s, int sequenceLength) {
    int runLength = sequenceLength - 1;
    int sum = 0;
    for (int i = 0; i < runLength; i += 2) {
      // add the first digit
      int secondValue = toInt(s.charAt(i));
      sum += secondValue;
      // prevent overflow
      if (sum >= 10) {
        sum -= 10;
      }

      // add the second digit, doubled
      int firstValue = toInt(s.charAt(i + 1)) * 2;
      if (firstValue >= 10) {
        // add individual digits
        firstValue = firstValue - 9;
      }
      sum += firstValue;
      // prevent overflow
      if (sum >= 10) {
        sum -= 10;
      }
    }

    // add the last digit, doubled
    int lastValue = toInt(s.charAt(runLength));
    sum += lastValue;
    // prevent overflow
    if (sum >= 10) {
      sum -= 10;
    }

    return sum;
  }

  static int toInt(char c) {
    if (c < '0' || c > '9') {
      throw new IllegalArgumentException();
    }
    return c - '0';
  }

}
