package com.gotriva.nlp.antifa.strategy.impl;

import com.gotriva.nlp.antifa.element.Checkable;
import com.gotriva.nlp.antifa.strategy.InteractableActionStrategy;

/** This class implements a strategy for uncheck action interactable. */
public class UncheckStrategy implements InteractableActionStrategy<Checkable> {

  /** Default constructor */
  UncheckStrategy() {}

  @Override
  public String getAction() {
    return "uncheck";
  }

  @Override
  public void perform(Checkable interactable, String ignored) {
    interactable.uncheck();
  }
}
