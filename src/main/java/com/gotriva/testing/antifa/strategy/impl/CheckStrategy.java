package com.gotriva.testing.antifa.strategy.impl;

import com.gotriva.testing.antifa.element.Checkable;
import com.gotriva.testing.antifa.strategy.InteractableActionStrategy;

/** This class implements a strategy for check action interactable. */
public class CheckStrategy implements InteractableActionStrategy<Checkable> {

  /** Default constructor */
  CheckStrategy() {}

  @Override
  public String getAction() {
    return "check";
  }

  @Override
  public void perform(Checkable interactable, String ignored) {
    interactable.check();
  }
}
