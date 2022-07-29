package com.gotriva.testing.antifa.handler.impl;

import com.gotriva.testing.antifa.element.Hoverable;
import com.gotriva.testing.antifa.handler.InteractableActionHandler;

/** This class implements a handler for hover action interactable. */
public class HoverHandler implements InteractableActionHandler<Hoverable> {

  @Override
  public String getAction() {
    return "hover";
  }

  @Override
  public void handle(Hoverable interactable, String ignored) {
    interactable.hoverOn();
  }
}
