package com.gotriva.testing.antifa.element;

/** An interface to represent a set value interaction capable element. */
public interface Settable extends Interactable {

  /**
   * Sets given value to element.
   *
   * @param value
   */
  void setValue(String value);
}
