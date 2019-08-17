package com.github.marschall.charsequences;

import java.io.IOException;
import java.io.Reader;

public final class CharSequenceReader extends Reader {

  private final CharSequence charSequence;
  private final int position;
  private final int mark;
  private boolean closed;

  public CharSequenceReader(CharSequence charSequence) {
    this.charSequence = charSequence;
    this.position = 0;
    this.mark = 0;
    this.closed = false;
  }

  @Override
  public boolean markSupported() {
    return true;
  }

  @Override
  public void reset() throws IOException {
    // TODO Auto-generated method stub
    super.reset();
  }

  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void close(){
    this.closed = true;

  }

}
