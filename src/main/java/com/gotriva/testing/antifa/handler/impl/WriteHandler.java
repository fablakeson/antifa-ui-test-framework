package com.gotriva.testing.antifa.handler.impl;

import com.gotriva.testing.antifa.element.Writable;
import com.gotriva.testing.antifa.handler.InteractableActionHandler;

/** This class implements a handler for write action interactable. */
public class WriteHandler implements InteractableActionHandler<Writable> {

  @Override
  public String getAction() {
    return "write";
  }

  @Override
  public void handle(Writable interactable, String text) {
    interactable.write(text);
  }
}
