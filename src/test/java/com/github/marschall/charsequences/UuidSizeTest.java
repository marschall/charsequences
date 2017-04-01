package com.github.marschall.charsequences;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.FieldLayout;
import org.openjdk.jol.vm.VM;
import org.openjdk.jol.vm.VirtualMachine;

public class UuidSizeTest {

  private VirtualMachine vm;

  @Before
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
        assertEquals(88L, this.vm.sizeOf(fieldValue));
      }
    }
    assertEquals(24L, VM.current().sizeOf(s));
    assertEquals(112L, 24L + 88L);
  }

  @Test
  public void uuidSize() {
    UUID uuid = UUID.fromString("ba226cf7-d156-4b18-a78a-094736208cc9");
    assertEquals(32L, this.vm.sizeOf(uuid));
  }

}
