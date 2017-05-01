package com.github.marschall.charsequences;

/**
 *
 * @see <a href="https://en.wikipedia.org/wiki/Luhn_algorithm">Luhn algorithm</a>
 */
public final class LuhnCheck {

  private LuhnCheck() {
    throw new AssertionError("not instantiable");
  }

  /**
   *
   * @param s
   * @return
   * @throws NullPointerException
   * @throws IllegalArgumentException
   */
  public static boolean isValid(CharSequence s) {
    int length = s.length();
    boolean odd = (length & 1) == 1;
    int sum = odd ? oddTotalLengthSum(s, length) : evenTotalLengthSum(s, length);
    int checkDigit = toInt(s.charAt(length - 1));
    return (10- sum) == checkDigit;
  }

  private static int oddTotalLengthSum(CharSequence s, int sequenceLength) {
    // the length of the sequence we check is even

    int runLength = sequenceLength - 1;
    int sum = 0;
    for (int i = 0; i < runLength; i += 2) {
      // add the first digit
      int firstValue = toInt(s.charAt(i));
      sum += firstValue;
      // prevent overflow
      if (sum >= 10) {
        sum -= 10;
      }

      // add the second digit, doubled
      int secondValue = toInt(s.charAt(i + 1)) * 2;
      if (secondValue >= 10) {
        // add individual digits
        secondValue = secondValue - 9;
      }
      sum += secondValue;
      // prevent overflow
      if (sum >= 10) {
        sum -= 10;
      }
    }
    return sum;
  }

  private static int evenTotalLengthSum(CharSequence s, int sequenceLength) {
    // the length of the sequence we check is odd

    int runLength = sequenceLength - 2;
    int sum = 0;
    for (int i = 0; i < runLength; i += 2) {
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

    // add the last digit, doubled
    int lastValue = toInt(s.charAt(runLength)) * 2;
    if (lastValue >= 10) {
      // add individual digits
      lastValue = lastValue - 9;
    }
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
