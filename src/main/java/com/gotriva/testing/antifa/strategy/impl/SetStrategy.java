package com.gotriva.testing.antifa.strategy.impl;

import com.gotriva.testing.antifa.element.Settable;
import com.gotriva.testing.antifa.strategy.InteractableActionStrategy;

/** This class implements a strategy for set action interactable. */
public class SetStrategy implements InteractableActionStrategy<Settable> {

  /** Default constructor */
  SetStrategy() {}

  @Override
  public String getAction() {
    return "set";
  }

  @Override
  public void perform(Settable interactable, String value) {
    interactable.setValue(value);
  }
}
