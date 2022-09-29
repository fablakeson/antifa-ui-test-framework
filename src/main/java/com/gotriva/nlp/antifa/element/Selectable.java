package com.gotriva.nlp.antifa.element;

public interface Selectable extends Interactable {

  /**
   * Select a some item with the given text.
   *
   * @param text the text.
   */
  void selectItem(String text);
}
