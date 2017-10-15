package com.github.marschall.charsequences;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Produces a stream of UTF-8 bytes from a {@link CharSequence}. The
 * opposite of {@link InputStreamReader}.
 *
 * <p>This class is not thread-safe.</p>
 */
public final class CharSequenceUtf8InputStream extends InputStream {

  private final CharSequence charSequence;

  private final int length;

  private boolean closed;

  // index of the next char read
  private int charIndex;

  // index of the byte in the next char read
  private int byteIndex;

  public CharSequenceUtf8InputStream(CharSequence charSequence) {
    this.charSequence = charSequence;
    this.length = charSequence.length();
    this.closed = false;
    if (this.length > 0) {
      if (Character.isLowSurrogate(this.charSequence.charAt(0))) {
        // maybe throw EOFException
        throw new IllegalArgumentException("truncated input");
      }
    }
  }

  private static int getLength(char c) {
    if (c < 128) {
      return 1;
    }
    if (c < 2048) {
      return 2;
    }
    if (!Character.isLowSurrogate(c)) {
      return 3;
    }
    return 4;
  }

  public static void main(String[] args) {
    char[] chars = Character.toChars(0x1F4A9);
    System.out.println(0b11111111111);
    System.out.println(Integer.toHexString(0b11111111111));
    System.out.println(Integer.toHexString(0b11111111111 + 1));
    System.out.println(new String(chars));
    System.out.println(chars.length);
    System.out.println(Character.isHighSurrogate(chars[0]));
    System.out.println(Character.isLowSurrogate(chars[1]));

    System.out.println((int) Character.MIN_HIGH_SURROGATE);
    System.out.println((int) Character.MAX_HIGH_SURROGATE);
    System.out.println((int) Character.MIN_LOW_SURROGATE);
    System.out.println((int) Character.MAX_LOW_SURROGATE);

    System.out.println(Integer.toBinaryString((int) Character.MIN_HIGH_SURROGATE));
    System.out.println(Integer.toBinaryString((int) Character.MAX_HIGH_SURROGATE));
    System.out.println(Integer.toBinaryString((int) Character.MIN_LOW_SURROGATE));
    System.out.println(Integer.toBinaryString((int) Character.MAX_LOW_SURROGATE));
  }

  private void closedCheck() throws IOException {
    if (this.closed) {
      throw new IOException("closed");
    }
  }

  @Override
  public int read() throws IOException {
    this.closedCheck();
    if (this.charIndex == this.length) {
      return -1;
    }
    char c = this.charSequence.charAt(this.charIndex);
    int byteLength = getLength(c);
    // TODO Auto-generated method stub
    return 0;
  }


  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    this.closedCheck();
    if (this.charIndex == this.length) {
      return -1;
    }
    if (b == null) {
      throw new NullPointerException();
    } else if (off < 0 || len < 0 || len > b.length - off) {
      throw new IndexOutOfBoundsException();
    } else if (len == 0) {
      return 0;
    }

    int left = len;
    if (this.byteIndex == 0) {
      int index = this.charIndex;
      while (index < this.length) {
        char c = this.charSequence.charAt(index);
        int byteLenght = getLength(c);
        if (byteLenght < 4) {


          index += 1;
        } else {
          if (index == this.length - 1) {
            throw new EOFException("truncated input");
          }
          int codePoint = Character.toCodePoint(c, this.charSequence.charAt(index + 1));

          index += 2;
        }
        left -= byteLenght;
      }
    }

    // TODO Auto-generated method stub
    return super.read(b, off, len);
  }

  @Override
  public long skip(long n) throws IOException {
    this.closedCheck();
    // TODO Auto-generated method stub
    return super.skip(n);
  }

  @Override
  public int available() throws IOException {
    this.closedCheck();
    // TODO Auto-generated method stub
    return super.available();
  }

  @Override
  public void close() {
    this.closed = true;
  }

  @Override
  public void mark(int readlimit) {
    // TODO Auto-generated method stub
    super.mark(readlimit);
  }

  @Override
  public void reset() throws IOException {
    // TODO Auto-generated method stub
    super.reset();
  }

  @Override
  public boolean markSupported() {
    // TODO Auto-generated method stub
    return super.markSupported();
  }

  public long transferTo​(OutputStream out) throws IOException {
    this.closedCheck();
    // TODO Auto-generated method stub
    return 0;
  }

}
