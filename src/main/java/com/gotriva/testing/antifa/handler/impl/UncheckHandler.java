package com.gotriva.testing.antifa.handler.impl;

import com.gotriva.testing.antifa.element.Checkable;
import com.gotriva.testing.antifa.handler.InteractableActionHandler;

public class UncheckHandler implements InteractableActionHandler<Checkable> {

  /** Default constructor */
  UncheckHandler() {}

  @Override
  public String getAction() {
    return "uncheck";
  }

  @Override
  public void handle(Checkable interactable, String ignored) {
    interactable.uncheck();
  }
}
