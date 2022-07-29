package com.gotriva.testing.antifa.handler.impl;

import com.gotriva.testing.antifa.element.Clickable;
import com.gotriva.testing.antifa.handler.InteractableActionHandler;

/** This class implements a handler for click action interactable. */
public class ClickHandler implements InteractableActionHandler<Clickable> {

  /** Default constructor */
  ClickHandler() {}

  @Override
  public String getAction() {
    return "click";
  }

  @Override
  public void handle(Clickable interactable, String ignored) {
    interactable.click();
  }
}
