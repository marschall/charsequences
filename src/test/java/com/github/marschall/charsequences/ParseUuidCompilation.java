package com.github.marschall.charsequences;

import java.util.UUID;

public class ParseUuidCompilation {

  private static final String TEST_UUID = "ba226cf7-d156-4b18-a78a-094736208cc9";

  public ParseUuidCompilation() {

    for (int i = 0; i < 1_000_000; i++) {
      UUID u = this.parse();
      if (System.identityHashCode(u) == 1) {
        System.out.println("X");
      }
    }

  }

  UUID parse() {
    return CharSequences.uuidFromCharSequence(TEST_UUID);
  }

  public static void main(String[] args) {
    // -XX:+UnlockDiagnosticVMOptions
    // -XX:+TraceClassLoading
    // -XX:+LogCompilation
    // -XX:+PrintAssembly
    // -XX:LogFile=compilation8hotspot.log
    ParseUuidCompilation compilation = new ParseUuidCompilation();
    compilation.parse();
  }
}
