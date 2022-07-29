package com.gotriva.testing.antifa.element;

/** An interface to represent a read interaction capable element. */
public interface Readable extends Interactable {

  /**
   * Perform a read on element and return its textual content.
   *
   * @return the readed text
   */
  String read();
}
