package com.github.marschall.charsequences;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.FieldLayout;
import org.openjdk.jol.vm.VM;
import org.openjdk.jol.vm.VirtualMachine;

public class UuidSizeTest {

  private VirtualMachine vm;

  @BeforeEach
  public void setUp() {
    this.vm = VM.current();
  }

  @Test
  public void stringSize() {
    String s = "ba226cf7-d156-4b18-a78a-094736208cc9";
    ClassLayout classLayout = ClassLayout.parseInstance(s);
    for (FieldLayout field : classLayout.fields()) {
      if (field.typeClass().endsWith("[]")) {
        Object fieldValue = this.vm.getObject(s, field.offset());
        if (isJava9OrLater()) {
          assertEquals(56L, this.vm.sizeOf(fieldValue), "size of Stirng.value");
        } else {
          assertEquals(88L, this.vm.sizeOf(fieldValue), "size of Stirng.value");
        }
      }
    }
    assertEquals(24L, this.vm.sizeOf(s));
    assertEquals(112L, 24L + 88L);
  }

  @Test
  public void uuidSize() {
    UUID uuid = UUID.fromString("ba226cf7-d156-4b18-a78a-094736208cc9");
    assertEquals(32L, this.vm.sizeOf(uuid));
  }

  private static boolean isJava9OrLater() {
    try {
      Class.forName("java.lang.Runtime$Version");
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }

}
