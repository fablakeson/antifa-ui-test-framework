package com.gotriva.testing.antifa.handler;

import com.gotriva.testing.antifa.element.Interactable;

/** This interface represents a interactable element action handler. */
public interface InteractableActionHandler<T extends Interactable> extends ActionHandler {

  /**
   * Handle some interactable action with a given paramter.
   *
   * @param interactable the interactable that performs the action
   * @param string the optional action parameter
   */
  void handle(T interactable, String string);
}
