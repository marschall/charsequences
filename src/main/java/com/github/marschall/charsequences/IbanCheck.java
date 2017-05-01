package com.github.marschall.charsequences;

public final class IbanCheck {

  private IbanCheck() {
    throw new AssertionError("not instantiable");
  }

  public static boolean isValid(CharSequence s) {
    int length = s.length();
    int sum = 0;
    for (int i = 4; i < length; i++) {
      char c = s.charAt(i);
      sum = accumulate(sum, c);
      if (sum > 97) {
        System.out.println("x");
      }
    }
    for (int i = 0; i < 4; i++) {
      char c = s.charAt(i);
      sum = accumulate(sum, c);
      if (sum > 97) {
        System.out.println("x");
      }
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
      while (result >= 97) {
        result -= 97;
      }
      return result;
    } else {
      throw new IllegalArgumentException();
    }
  }

}
