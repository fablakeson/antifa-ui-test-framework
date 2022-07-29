package com.gotriva.testing.antifa.handler.impl;

import com.gotriva.testing.antifa.element.Checkable;
import com.gotriva.testing.antifa.handler.InteractableActionHandler;

/** This class implements a handler for check action interactable. */
public class CheckHandler implements InteractableActionHandler<Checkable> {

  @Override
  public String getAction() {
    return "check";
  }

  @Override
  public void handle(Checkable interactable, String ignored) {
    interactable.check();
  }
}
