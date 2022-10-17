package com.gotriva.nlp.antifa.element;

/** An interace to represent a read value interaction capable element. */
public interface Selectable extends Interactable {

  /**
   * Select a some item with the given text.
   *
   * @param text the text.
   */
  void selectItem(String text);
}
