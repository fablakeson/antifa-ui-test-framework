package com.gotriva.testing.antifa.strategy.impl;

import com.gotriva.testing.antifa.element.Writable;
import com.gotriva.testing.antifa.strategy.InteractableActionStrategy;

/** This class implements a strategy for write action interactable. */
public class WriteStrategy implements InteractableActionStrategy<Writable> {

  /** Default constructor */
  WriteStrategy() {}

  @Override
  public String getAction() {
    return "write";
  }

  @Override
  public void perform(Writable interactable, String text) {
    interactable.write(text);
  }
}
