package com.github.marschall.charsequences;

/**
 * Utility methods for dealing with {@link CharSequence} objects.
 */
public class CharSequences {

  private CharSequences() {
    throw new AssertionError("not instantiable");
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
