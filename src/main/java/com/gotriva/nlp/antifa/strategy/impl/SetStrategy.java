package com.gotriva.nlp.antifa.strategy.impl;

import com.gotriva.nlp.antifa.element.Settable;
import com.gotriva.nlp.antifa.strategy.InteractableActionStrategy;

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
