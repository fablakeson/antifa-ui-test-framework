package com.gotriva.testing.antifa.element;

/** An interface to represent a write interaction capable element. */
public interface Writable extends Interactable {

  /**
   * Perform the write of the given text to element content.
   *
   * @param text the text to be writed
   */
  void write(CharSequence text);
}
