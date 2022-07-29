package com.gotriva.testing.antifa.handler.impl;

import com.gotriva.testing.antifa.element.Writable;
import com.gotriva.testing.antifa.handler.InteractableHandler;

/** This class implements a interactable handler for write action. */
public class WriteHandler implements InteractableHandler<Writable> {

  @Override
  public void handle(Writable interactable, String string) {
    interactable.write(string);
  }

  @Override
  public String getAction() {
    return "write";
  }
}
