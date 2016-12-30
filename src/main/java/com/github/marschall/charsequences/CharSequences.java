package com.github.marschall.charsequences;

/**
 * Utility methods for dealing with {@link CharSequence} objects.
 */
public final class CharSequences {

  private CharSequences() {
    throw new AssertionError("not instantiable");
  }

  /**
   * Parses a given char sequence compatible to {@link Integer#parseInt(String)}.
   *
   * @implNote no allocation is
   * @param charSequence the {@code CharSequence} containing the {@code int}
   *   representation to be parsed, {@code null} will cause a
   *   {@code NumberFormatException} to be thrown
   * @return the integer value represented by the argument in decimal
   * @throws NumberFormatException if the charSequence does not
   *   contain a parsable int
   * @see Integer#parseInt(String)
   */
  public static int parseInt(CharSequence charSequence) {
    if (charSequence == null) {
      throw new NumberFormatException("null");
    }
    int length = charSequence.length();
    if (length == 0) {
      throw invalidDecimalNumber(charSequence);
    }
    char first = charSequence.charAt(0);
    int start;
    boolean negative;
    if (first == '-') {
      negative = true;
      start = 1;
    } else if (first == '+') {
      negative = false;
      start = 1;
    } else {
      negative = false;
      start = 0;
    }

    if (length - start == 0) {
      throw invalidDecimalNumber(charSequence);
    }

    int product = 0;
    // Integer.MIN_VALUE does not have a positive representation but
    // Integer.MAX_VALUE has a negative representation so build negative numbers
    for (int i = start; i < length; ++i) {
      char c = charSequence.charAt(i);
      if (c < '0' || c > '9') {
        throw invalidDecimalNumber(charSequence);
      }
      int value = c - '0';
      // maximum/minimum allowed values
      // -2147483648
      //  2147483647
      if (product < -214748364) {
        // will cause overflow
        throw invalidDecimalNumber(charSequence);
      } else if (product == -214748364) {
        if ((negative && value == 9) || (!negative && value > 7)) {
          // will cause overflow
          throw invalidDecimalNumber(charSequence);
        }
      }
      product = product * 10 - value;
    }

    if (negative) {
      return product;
    } else {
      return -product;
    }
  }
  static int parseIntExact(CharSequence charSequence) {
    if (charSequence == null) {
      throw new NumberFormatException("null");
    }
    int length = charSequence.length();
    if (length == 0) {
      throw invalidDecimalNumber(charSequence);
    }
    char first = charSequence.charAt(0);
    int start;
    boolean negative;
    if (first == '-') {
      negative = true;
      start = 1;
    } else if (first == '+') {
      negative = false;
      start = 1;
    } else {
      negative = false;
      start = 0;
    }

    if (length - start == 0) {
      throw invalidDecimalNumber(charSequence);
    }

    int product = 0;
    // Integer.MIN_VALUE does not have a positive representation but
    // Integer.MAX_VALUE has a negative representation so build negative numbers
    for (int i = start; i < length; ++i) {
      char c = charSequence.charAt(i);
      if (c < '0' || c > '9') {
        throw invalidDecimalNumber(charSequence);
      }
      int value = c - '0';
      try {
        product = Math.subtractExact(Math.multiplyExact(product, 10), value);
      } catch (ArithmeticException e) {
        throw invalidDecimalNumber(charSequence);
      }
    }

    if (negative) {
      return product;
    } else {
      return -product;
    }
  }

  /**
   * Parses a given char sequence compatible to {@link Long#parseLong(String)}.
   *
   * @implNote no allocation is
   * @param charSequence the {@code CharSequence} containing the {@code long}
   *   representation to be parsed, {@code null} will cause a
   *   {@code NumberFormatException} to be thrown
   * @return the long value represented by the argument in decimal
   * @throws NumberFormatException if the charSequence does not
   *   contain a parsable long
   * @see Long#parseLong
   */
  public static long parseLong(CharSequence charSequence) {
    if (charSequence == null) {
      throw new NumberFormatException("null");
    }
    int length = charSequence.length();
    if (length == 0) {
      throw invalidDecimalNumber(charSequence);
    }
    char first = charSequence.charAt(0);
    int start;
    boolean negative;
    if (first == '-') {
      negative = true;
      start = 1;
    } else if (first == '+') {
      negative = false;
      start = 1;
    } else {
      negative = false;
      start = 0;
    }

    if (length - start == 0) {
      throw invalidDecimalNumber(charSequence);
    }

    long product = 0;
    // Long.MIN_VALUE does not have a positive representation but
    // Long.MAX_VALUE has a negative representation so build negative numbers
    for (int i = start; i < length; ++i) {
      char c = charSequence.charAt(i);
      if (c < '0' || c > '9') {
        throw invalidDecimalNumber(charSequence);
      }
      int value = c - '0';
      // maximum/minimum allowed values
      // -9223372036854775808
      //  9223372036854775807
      if (product < -922337203685477580L) {
        // will cause overflow
        throw invalidDecimalNumber(charSequence);
      } else if (product == -922337203685477580L) {
        if ((negative && value == 9) || (!negative && value > 7)) {
          // will cause overflow
          throw invalidDecimalNumber(charSequence);
        }
      }
      product = product * 10 - value;
    }

    if (negative) {
      return product;
    } else {
      return -product;
    }
  }
  static long parseLongExact(CharSequence charSequence) {
    if (charSequence == null) {
      throw new NumberFormatException("null");
    }
    int length = charSequence.length();
    if (length == 0) {
      throw invalidDecimalNumber(charSequence);
    }
    char first = charSequence.charAt(0);
    int start;
    boolean negative;
    if (first == '-') {
      negative = true;
      start = 1;
    } else if (first == '+') {
      negative = false;
      start = 1;
    } else {
      negative = false;
      start = 0;
    }

    if (length - start == 0) {
      throw invalidDecimalNumber(charSequence);
    }

    long product = 0;
    // Long.MIN_VALUE does not have a positive representation but
    // Long.MAX_VALUE has a negative representation so build negative numbers
    for (int i = start; i < length; ++i) {
      char c = charSequence.charAt(i);
      if (c < '0' || c > '9') {
        throw invalidDecimalNumber(charSequence);
      }
      int value = c - '0';
      try {
        product = Math.subtractExact(Math.multiplyExact(product, 10), value);
      } catch (ArithmeticException e) {
        throw invalidDecimalNumber(charSequence);
      }
    }

    if (negative) {
      return product;
    } else {
      return -product;
    }
  }

  private static NumberFormatException invalidDecimalNumber(CharSequence charSequence) {
    return new NumberFormatException("invalid decimal number " + charSequence);
  }

  public static CharSequence betweenAnd(CharSequence charSequence, char start, char end) {
    int startIndex = indexOf(charSequence, start);
    if (startIndex != -1) {
      int endIndex = lastIndexOf(charSequence, end);
      if (endIndex > startIndex) {
        return charSequence.subSequence(startIndex + 1, endIndex);
      }
    }
    return null;
  }

  /**
   * Searches for the first occurrence of a char within a sequence
   * that's compatible with {@link String#indexOf(char)}.
   *
   * @param charSequence the sequence within to search, not {@code null}
   * @param c the {@code char} to search for
   * @return the index of the first occurrence of the specified
   *   {@code char}, or {@code -1} if there is no such occurrence
   * @see String#indexOf(char)
   */
  public static int indexOf(CharSequence charSequence, char c) {
    int length = charSequence.length();
    for (int i = 0; i < length; ++i) {
      if (charSequence.charAt(i) == c) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Searches for the first occurrence of a char within a sequence
   * staring at a given index that's compatible with
   * {@link String#indexOf(char, int)}.
   *
   * @param charSequence the sequence within to search, not {@code null}
   * @param c the {@code char} to search for
   * @param fromIndex the index at which to start the search from
   * @return the index of the first occurrence of the specified
   *   {@code char} after fromIndex, or {@code -1} if there is no such occurrence
   * @see String#indexOf(char, int)
   */
  public static int indexOf(CharSequence charSequence, char c, int fromIndex) {
    int length = charSequence.length();
    if (fromIndex >= length) {
      throw new IllegalArgumentException();
    }
    for (int i = fromIndex; i < length; ++i) {
      if (charSequence.charAt(i) == c) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Searches for the last occurrence of a char within a sequence
   * that's compatible with {@link String#indexOf(char)}.
   *
   * @param charSequence the sequence within to search, not {@code null}
   * @param c the {@code char} to search for
   * @return the index of the last occurrence of the specified
   *   {@code char}, or {@code -1} if there is no such occurrence
   * @see String#indexOf(char)
   */
  public static int lastIndexOf(CharSequence charSequence, char c) {
    int length = charSequence.length();
    for (int i = length - 1; i >= 0; --i) {
      if (charSequence.charAt(i) == c) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Searches for the first occurrence subsequence within a sequence
   * that's compatible with {@link String#indexOf(String)}:
   *
   * @param charSequence the sequence within to search, not {@code null}
   * @param subSequence the subsequence to search for, not {@code null}
   * @return the index of the first {@code char} of the first
   *   occurrence of the specified subsequence, or {@code -1} if there
   *   is no such occurrence
   * @see String#indexOf(String)
   */
  public static int indexOf(CharSequence charSequence, String subSequence) {
    int sequenceLength = charSequence.length();
    int subSequenceLength = subSequence.length();
    charLoop : for (int i = 0; i <= sequenceLength - subSequenceLength; ++i) {
      for (int j = 0; j < subSequenceLength; ++j) {
        if (charSequence.charAt(i + j) != subSequence.charAt(j)) {
          continue charLoop;
        }
      }
      return i;
    }
    return -1;
  }

}
