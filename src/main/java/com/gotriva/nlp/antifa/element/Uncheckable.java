package com.gotriva.nlp.antifa.element;

/** An interface to represent a check/uncheck interaction capable element. */
public interface Uncheckable extends Interactable {

  /** Perform a uncheck on this element if it is checked. */
  void uncheck();
}
