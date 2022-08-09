package com.gotriva.testing.antifa.strategy;

import com.gotriva.testing.antifa.element.Interactable;

/** This interface represents a interactable element strategy. */
public interface InteractableActionStrategy<T extends Interactable> extends ActionStrategy {

  /**
   * Handle some interactable action with a given paramter.
   *
   * @param interactable the interactable that performs the action
   * @param string the optional action parameter
   */
  void perform(T interactable, String string);
}
