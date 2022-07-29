package com.gotriva.testing.antifa.handler.impl;

import com.gotriva.testing.antifa.element.Settable;
import com.gotriva.testing.antifa.handler.InteractableActionHandler;

public class SetHandler implements InteractableActionHandler<Settable> {

  @Override
  public String getAction() {
    return "set";
  }

  @Override
  public void handle(Settable interactable, String value) {
    interactable.setValue(value);
  }
}
