package com.github.marschall.charsequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Utility methods for dealing with {@link CharSequence} objects.
 */
public final class CharSequences {

  private static final char BOM = '\uFEFF';

  private CharSequences() {
    throw new AssertionError("not instantiable");
  }

  /**
   * Checks if the given char sequences is numeric.
   *
   * <p>A char sequence is considered to be numeric if it contains entirely
   * out of numbers between {@code 0} and {@code 9}. Empty sequences are
   * rejected as are ones with a leading {@code -}.</p>
   *
   * @param charSequence the sequence to check
   * @return if the sequence is entirely made of of number
   * @throws NullPointerException if the char sequence is {@code null}
   */
  public static boolean isNumeric(CharSequence charSequence) {
    int length = charSequence.length();
    if (length == 0) {
      return false;
    }
    for (int i = 0; i < length; ++i) {
      char c = charSequence.charAt(i);
      if ((c < '0') || (c > '9')) {
        return false;
      }
    }
    return true;
  }

  /**
   * Parses a given char sequence compatible to {@link Integer#parseInt(String)}.
   *
   * @implNote no allocation is performed
   * @implNote performance can be 35% to 50% better than Integer#parseInt(String)
   * @param charSequence the {@code CharSequence} containing the {@code int}
   *   representation to be parsed, {@code null} will cause a
   *   {@code NumberFormatException} to be thrown
   * @param beginIndex the inclusive index at which to star
   * @param endIndex the exclusive index at which to end
   * @return the integer value represented by the argument in decimal
   * @throws NumberFormatException if the charSequence does not
   *   contain a parsable int
   * @throws IndexOutOfBoundsException if beginIndex is less than endIndex,
   *         if beginIndex is negative or if endIndex is bigger than the length of charSequence
   * @see Integer#parseInt(String)
   * @see #isNumeric(CharSequence)
   */
  public static int parseInt(CharSequence charSequence, int beginIndex, int endIndex) {
    if (charSequence == null) {
      throw new NumberFormatException("null");
    }
    if ((endIndex < beginIndex) || (beginIndex < 0) || (endIndex > charSequence.length())) {
      throw new IndexOutOfBoundsException();
    }

    int length = endIndex - beginIndex;
    if (length == 0) {
      throw invalidDecimalNumber(charSequence);
    }

    char first = charSequence.charAt(beginIndex);
    int start;
    boolean negative;
    if (first == '-') {
      negative = true;
      start = beginIndex + 1;
    } else if (first == '+') {
      negative = false;
      start = beginIndex + 1;
    } else {
      negative = false;
      start = beginIndex;
    }

    if ((endIndex - start) == 0) {
      throw invalidDecimalNumber(charSequence);
    }

    int product = 0;
    // Integer.MIN_VALUE does not have a positive representation but
    // Integer.MAX_VALUE has a negative representation
    // so build negative numbers and negate those rather than build positive
    // numbers and negate them
    for (int i = start; i < endIndex; ++i) {
      char c = charSequence.charAt(i);
      if ((c < '0') || (c > '9')) {
        throw invalidDecimalNumber(charSequence);
      }
      int value = c - '0';
      try {
        // JMH microbenchmarks have shown that performance of exact methods
        // is equal or better than manual overflow checks for normal cases without overflow
        product = Math.subtractExact(Math.multiplyExact(product, 10), value);
      } catch (ArithmeticException e) {
        throw invalidDecimalNumber(charSequence);
      }
    }

    if (negative) {
      return product;
    } else {
      try {
        return Math.negateExact(product);
      } catch (ArithmeticException e) {
        throw invalidDecimalNumber(charSequence);
      }
    }

  }

  /**
   * Parses a given char sequence compatible to {@link Integer#parseInt(String)}.
   *
   * @implNote no allocation is performed
   * @implNote performance can be 35% to 50% better than Integer#parseInt(String)
   * @param charSequence the {@code CharSequence} containing the {@code int}
   *   representation to be parsed, {@code null} will cause a
   *   {@code NumberFormatException} to be thrown
   * @return the integer value represented by the argument in decimal
   * @throws NumberFormatException if the charSequence does not
   *   contain a parsable int
   * @see Integer#parseInt(String)
   * @see #isNumeric(CharSequence)
   */
  public static int parseInt(CharSequence charSequence) {
    if (charSequence == null) {
      throw new NumberFormatException("null");
    }
    return parseInt(charSequence, 0, charSequence.length());
  }



  /**
   * Parses a given char sequence compatible to {@link Long#parseLong(String)}.
   *
   * @implNote no allocation is performed
   * @implNote performance can be 20% to 50% better than Long#parseLong(String)
   * @param charSequence the {@code CharSequence} containing the {@code long}
   *   representation to be parsed, {@code null} will cause a
   *   {@code NumberFormatException} to be thrown
   * @return the long value represented by the argument in decimal
   * @throws NumberFormatException if the charSequence does not
   *   contain a parsable long
   * @see Long#parseLong
   * @see #isNumeric(CharSequence)
   */
  public static long parseLong(CharSequence charSequence) {
    if (charSequence == null) {
      throw new NumberFormatException("null");
    }
    return parseLong(charSequence, 0, charSequence.length());
  }

  /**
   * Parses a given char sequence compatible to {@link Long#parseLong(String)}.
   *
   * @implNote no allocation is performed
   * @implNote performance can be 20% to 50% better than Long#parseLong(String)
   * @param charSequence the {@code CharSequence} containing the {@code long}
   *   representation to be parsed, {@code null} will cause a
   *   {@code NumberFormatException} to be thrown
   * @param beginIndex the inclusive index at which to star
   * @param endIndex the exclusive index at which to end
   * @return the long value represented by the argument in decimal
   * @throws NumberFormatException if the charSequence does not
   *   contain a parsable long
   * @throws IndexOutOfBoundsException if beginIndex is less than endIndex,
   *         if beginIndex is negative or if endIndex is bigger than the length of charSequence
   * @see Long#parseLong
   * @see #isNumeric(CharSequence)
   */
  public static long parseLong(CharSequence charSequence, int beginIndex, int endIndex) {
    if (charSequence == null) {
      throw new NumberFormatException("null");
    }
    if ((endIndex < beginIndex) || (beginIndex < 0) || (endIndex > charSequence.length())) {
      throw new IndexOutOfBoundsException();
    }
    int length = endIndex - beginIndex;
    if (length == 0) {
      throw invalidDecimalNumber(charSequence);
    }

    char first = charSequence.charAt(beginIndex);
    int start;
    boolean negative;
    if (first == '-') {
      negative = true;
      start = beginIndex + 1;
    } else if (first == '+') {
      negative = false;
      start = beginIndex + 1;
    } else {
      negative = false;
      start = beginIndex;
    }

    if ((endIndex - start) == 0) {
      throw invalidDecimalNumber(charSequence);
    }

    long product = 0;
    // Long.MIN_VALUE does not have a positive representation but
    // Long.MAX_VALUE has a negative representation
    // so build negative numbers and negate those rather than build positive
    // numbers and negate them
    for (int i = start; i < endIndex; ++i) {
      char c = charSequence.charAt(i);
      if ((c < '0') || (c > '9')) {
        throw invalidDecimalNumber(charSequence);
      }
      int value = c - '0';
      try {
        // JMH microbenchmarks have shown that performance of exact methods
        // is equal or better than manual overflow checks for normal cases without overflow
        product = Math.subtractExact(Math.multiplyExact(product, 10), value);
      } catch (ArithmeticException e) {
        throw invalidDecimalNumber(charSequence);
      }
    }

    if (negative) {
      return product;
    } else {
      try {
        return Math.negateExact(product);
      } catch (ArithmeticException e) {
        throw invalidDecimalNumber(charSequence);
      }
    }
  }

  private static NumberFormatException invalidDecimalNumber(CharSequence charSequence) {
    return new NumberFormatException("invalid decimal number " + charSequence);
  }

  /**
   * Searches for the first occurrence of a char within a sequence
   * that's compatible with {@link String#indexOf(int)}.
   *
   * @param charSequence the sequence within to search, not {@code null}
   * @param c the {@code char} to search for
   * @return the index of the first occurrence of the specified
   *   {@code char}, or {@code -1} if there is no such occurrence
   * @see String#indexOf(int)
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
   * {@link String#indexOf(int, int)}.
   *
   * @param charSequence the sequence within to search, not {@code null}
   * @param c the {@code char} to search for
   * @param fromIndex the index at which to start the search from,
   *        if it's larger than the length of this string then {@code -1}
   *        is returned
   *        if it's negative then it is treated as {@code 0}
   * @return the index of the first occurrence of the specified
   *   {@code char} after fromIndex, or {@code -1} if there is no such occurrence
   * @see String#indexOf(int, int)
   */
  public static int indexOf(CharSequence charSequence, char c, int fromIndex) {
    int length = charSequence.length();
    if (fromIndex >= length) {
      return -1;
    }
    if (fromIndex < 0) {
      fromIndex = 0;
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
   * that's compatible with {@link String#indexOf(int)}.
   *
   * @param charSequence the sequence within to search, not {@code null}
   * @param c the {@code char} to search for
   * @return the index of the last occurrence of the specified
   *   {@code char}, or {@code -1} if there is no such occurrence
   * @see String#indexOf(int)
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
    charLoop : for (int i = 0; i <= (sequenceLength - subSequenceLength); ++i) {
      for (int j = 0; j < subSequenceLength; ++j) {
        if (charSequence.charAt(i + j) != subSequence.charAt(j)) {
          continue charLoop;
        }
      }
      return i;
    }
    return -1;
  }

  /**
   * Returns a sequence whose value is the given sequence, with any
   * leading and trailing spaces removed.
   *
   * @implNote allocation avoided if the sequence does not start or end with a space
   * @implNote allocation avoided if the sequence is just spaces
   * @param charSequence the sequence to trim, not {@code null}
   * @return the trimmed sequence
   * @see String#trim()
   * @see CharSequence#subSequence(int, int)
   * @throws NullPointerException if the given sequence is null
   */
  public static CharSequence trim(CharSequence charSequence) {
    int length = charSequence.length();
    int start = 0;

    // scan from start to end
    while (start < length) {
      char c = charSequence.charAt(start);
      if (c != ' ') {
        break;
      }
      start += 1;
    }


    // scan from end to start
    int end = length;
    while (end > start) {
      char c = charSequence.charAt(end - 1);
      if (c != ' ') {
        break;
      }
      end -= 1;
    }

    if (length == 0) {
      return "";
    } else if ((start == 0) && (end == length)) {
      return charSequence;
    } else {
      return charSequence.subSequence(start, end);
    }
  }

  /**
   * Splits this string around matches of the given delimiter character.
   *
   * @implNote the iterable is lazily computed, no backing collection
   *           is created
   * @implNote calling {@link Iterable#iterator()} multiple times will
   *           create a new iterator starting from the first occurrence
   *           every time
   *
   * @param charSequence the CharSequence to split
   * @param delimiter the delimiting character
   * @return iterable of CharSequence computed by splitting the given
   *         CharSequence around matches of the given delimiter character
   */
  public static Iterable<CharSequence> split(CharSequence charSequence, char delimiter) {
    return new SubSequenceIterable(delimiter, charSequence);
  }

  /**
   * Checks if a sequence starts with a
   * <a href="https://en.wikipedia.org/wiki/Byte_order_mark">byte order mark</a>.
   *
   * @param charSequence the CharSequence to check, not {@code null}
   * @return if a sequence starts with a BOM
   */
  public static boolean startsWithBom(CharSequence charSequence) {
    return (charSequence.length() > 0) && (charSequence.charAt(0) == BOM);
  }

  /**
   * Removes a leading
   * <a href="https://en.wikipedia.org/wiki/Byte_order_mark">byte order mark</a>
   * from a sequence if present.
   *
   * @param charSequence the CharSequence from which to remove the BOM, not {@code null}
   * @return a subsequence with the leading BOM removed
   *         the same sequence if the first character is not a BOM
   *         or the sequence is empty
   */
  public static CharSequence removeLeadingBom(CharSequence charSequence) {
    if (!startsWithBom(charSequence)) {
      return charSequence;
    }
    return charSequence.subSequence(1, charSequence.length());
  }

  /**
   * Creates a UUID from a {@link CharSequence} like {@link UUID#fromString(String)}
   *
   * @implNote unlike {@link UUID#fromString(String)} performs no allocation
   *           besides the {@link UUID}
   * @implNote on a 64bit JVM with oop compression a UUID is 32 and the size of
   *           a UUID string is 24 bytes for the string plus 88 for the backing
   *           character array for a total of 112 bytes (on Java 8 at least)
   *
   * @param name the char sequence that specifies a {@code UUID}, not {@code null}
   * @return a new {@code UUID} with the specified value
   * @throws IllegalArgumentException if {@code name} is not in the format specified
   * @throws NullPointerException if {@code name} is {@code null}
   * @see UUID#fromString(String)
   * @see UUID#toString()
   */
  public static UUID uuidFromCharSequence(CharSequence name) {
    if (name.length() != 36) {
      throw new IllegalArgumentException("Invalid UUID string: " + name);
    }
    if ((name.charAt(8) != '-') || (name.charAt(13) != '-') || (name.charAt(18) != '-') || (name.charAt(23) != '-')) {
      throw new IllegalArgumentException("Invalid UUID string: " + name);
    }
    long mostSigBits = 0L;
    long leastSigBits = 0;

    for (int i = 0; i < 18; ++i) {
      if ((i == 8) || (i == 13)) {
        continue;
      }
      int digit = hexDigit(name.charAt(i));
      mostSigBits = (mostSigBits << 4) | digit;
    }

    for (int i = 19; i < 36; ++i) {
      if (i == 23) {
        continue;
      }
      int digit = hexDigit(name.charAt(i));
      leastSigBits = (leastSigBits << 4) | digit;
    }

    return new UUID(mostSigBits, leastSigBits);
  }

  static int hexDigit(char c) {
    if ((c >= '0') && (c <= '9')) {
      return c - '0';
    }
    if ((c >= 'a') && (c <= 'f')) {
      return (c - 'a') + 10;
    }
    if ((c >= 'A') && (c <= 'F')) {
      return (c - 'A') + 10;
    }
    throw new IllegalArgumentException();
  }

  static final class SubSequenceIterable implements Iterable<CharSequence> {

    private final char delimiter;
    private final CharSequence charSequence;

    SubSequenceIterable(char delimeter, CharSequence charSequence) {
      this.delimiter = delimeter;
      this.charSequence = charSequence;
    }

    @Override
    public Iterator<CharSequence> iterator() {
      return new SubSequenceIterator(this.charSequence, this.delimiter);
    }

  }

  static final class SubSequenceIterator implements Iterator<CharSequence> {

    private final char delimiter;
    private final CharSequence charSequence;

    private int nextStart;
    private int nextEnd;


    SubSequenceIterator(CharSequence charSequence, char delimiter) {
      this.charSequence = charSequence;
      this.delimiter = delimiter;

      this.nextStart = 0;
      this.nextEnd = this.findEnd();
    }

    @Override
    public boolean hasNext() {
      return this.nextStart != -1;
    }

    @Override
    public CharSequence next() {
      if (!this.hasNext()) {
        throw new NoSuchElementException();
      }
      CharSequence next = this.charSequence.subSequence(this.nextStart, this.nextEnd);
      this.nextStart = this.nextEnd + 1;
      if (this.nextStart > this.charSequence.length()) {
        this.nextStart = -1;
      } else {
        this.nextEnd = this.findEnd();
      }
      return next;
    }

    private int findEnd() {
      int fromIndex = this.nextStart;
      int length = this.charSequence.length();
      if (fromIndex > length) {
        return length;
      }
      int end = indexOf(this.charSequence, this.delimiter, fromIndex);
      if (end == -1) {
        return length;
      } else {
        return end;
      }
    }

  }

}
