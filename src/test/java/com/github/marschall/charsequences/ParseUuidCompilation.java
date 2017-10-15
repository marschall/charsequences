package com.github.marschall.charsequences;

import java.util.UUID;

public class ParseUuidCompilation {

  private static final String[] TEST_UUIDS = {
      "ba226cf7-d156-4b18-a78a-094736208cc9",
      "123e4567-e89b-12d3-a456-426655440000",
      "00112233-4455-6677-8899-aabbccddeeff",
      "e2a578f6-8fce-47b4-9a66-c7be263fe0b1",
      };

  ParseUuidCompilation() {
    super();
  }

  void parse(int count) {
    for (int i = 0; i < count; i++) {
      UUID u = CharSequences.uuidFromCharSequence(TEST_UUIDS[count % (TEST_UUIDS.length - 1)]);
      if (System.identityHashCode(u) == 1) {
        System.out.println(u);
      }
    }
  }

  public static void main(String[] args) {
    // -XX:+UnlockDiagnosticVMOptions
    // -XX:+TraceClassLoading
    // -XX:+LogCompilation
    // -XX:+PrintAssembly
    // -XX:LogFile=compilation8hotspot.log
    int count;
    if (args.length == 1) {
      count = Integer.parseInt(args[0]);
    } else {
      count = 100_000_000;
    }
    ParseUuidCompilation compilation = new ParseUuidCompilation();
    compilation.parse(count);
  }
}
