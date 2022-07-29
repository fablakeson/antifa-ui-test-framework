package com.gotriva.testing.antifa.handler.impl;

import com.gotriva.testing.antifa.element.Clickable;
import com.gotriva.testing.antifa.handler.InteractableHandler;

/** This class implements a interactable handler for click action. */
public class ClickableHandler implements InteractableHandler<Clickable> {

  @Override
  public void handle(Clickable interactable, String string) {
    interactable.click();
  }

  @Override
  public String getAction() {
    return "click";
  }
}
