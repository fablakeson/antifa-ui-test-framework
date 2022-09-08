package com.gotriva.nlp.antifa.strategy.impl;

import com.gotriva.nlp.antifa.element.Uncheckable;
import com.gotriva.nlp.antifa.strategy.InteractableActionStrategy;

/** This class implements a strategy for uncheck action interactable. */
public class UncheckStrategy implements InteractableActionStrategy<Uncheckable> {

  /** Default constructor */
  UncheckStrategy() {}

  @Override
  public String getAction() {
    return "uncheck";
  }

  @Override
  public void perform(Uncheckable interactable, String ignored) {
    interactable.uncheck();
  }
}
