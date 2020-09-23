package com.github.marschall.charsequences;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.util.stream.IntStream;

/**
 * Optimized empty charsequence.
 */
final class EmptyCharSequence implements CharSequence, Serializable {

  static final CharSequence INSTANCE = new EmptyCharSequence();

  private static final MethodHandle NEW_INDEX_OUT_OF_BOUNDS_EXCEPTION;

  static {
    Lookup lookup = MethodHandles.publicLookup();
    MethodHandle constructorHandle;
    try {
      try {
        Constructor<IndexOutOfBoundsException> java9Constructor = IndexOutOfBoundsException.class.getConstructor(int.class);
        constructorHandle = lookup.unreflectConstructor(java9Constructor);
      } catch (NoSuchMethodException e) {
        Constructor<IndexOutOfBoundsException> java8Constructor = IndexOutOfBoundsException.class.getConstructor();
        MethodHandle java8ConstructorHandle = lookup.unreflectConstructor(java8Constructor);
        constructorHandle = MethodHandles.dropArguments(java8ConstructorHandle, 0, int.class);
      }
    } catch (IllegalAccessException | NoSuchMethodException e) {
      throw new RuntimeException("could not find matching IndexOutOfBoundsException constructor", e);
    }
    NEW_INDEX_OUT_OF_BOUNDS_EXCEPTION = constructorHandle;
  }

  private EmptyCharSequence() {
    super();
  }

  // default method in JDK 15+
  public boolean isEmpty() {
    return true;
  }

  @Override
  public int length() {
    return 0;
  }

  @Override
  public char charAt(int index) {
    try {
      throw (IndexOutOfBoundsException) NEW_INDEX_OUT_OF_BOUNDS_EXCEPTION.invokeExact(index);
    } catch (Error | RuntimeException e) {
      throw e;
    } catch (Throwable e) {
      throw new IllegalStateException("could not call IndexOutOfBoundsException constructor", e);
    }
  }

  @Override
  public String toString() {
    return "";
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    if ((start != 0) || (end != 0)) {
      throw new IndexOutOfBoundsException();
    }
    return this;
  }

  @Override
  public IntStream chars() {
    return IntStream.empty();
  }

  @Override
  public IntStream codePoints() {
    return IntStream.empty();
  }

  private Object readResolve() throws ObjectStreamException {
    return INSTANCE;
  }

}
