package com.github.marschall.charsequences;

/**
 * Utility methods for dealing with {@link CharSequence} objects.
 */
public final class CharSequences {

  private CharSequences() {
    throw new AssertionError("not instantiable");
  }

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
      product = product * 10 - (c - '0');
    }

    if (negative) {
      return product;
    } else {
      return -product;
    }
  }

  public static long parseLong(CharSequence charSequence) {
    if (charSequence == null) {
      throw new NumberFormatException("null");
    }
    return 0;
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

  public static int indexOf(CharSequence charSequence, char c) {
    int length = charSequence.length();
    for (int i = 0; i < length; ++i) {
      if (charSequence.charAt(i) == c) {
        return i;
      }
    }
    return -1;
  }

  public static int indexOf(CharSequence charSequence, int start, char c) {
    int length = charSequence.length();
    if (start >= length) {
      throw new IllegalArgumentException();
    }
    for (int i = start; i < length; ++i) {
      if (charSequence.charAt(i) == c) {
        return i;
      }
    }
    return -1;
  }

  public static int lastIndexOf(CharSequence charSequence, char c) {
    int length = charSequence.length();
    for (int i = length - 1; i >= 0; --i) {
      if (charSequence.charAt(i) == c) {
        return i;
      }
    }
    return -1;
  }

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
