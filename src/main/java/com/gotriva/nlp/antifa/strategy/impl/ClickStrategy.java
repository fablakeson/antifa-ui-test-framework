package com.gotriva.nlp.antifa.strategy.impl;

import com.gotriva.nlp.antifa.element.Clickable;
import com.gotriva.nlp.antifa.strategy.InteractableActionStrategy;

/** This class implements a strategy for click action interactable. */
public class ClickStrategy implements InteractableActionStrategy<Clickable> {

  /** Default constructor */
  ClickStrategy() {}

  @Override
  public String getAction() {
    return "click";
  }

  @Override
  public void perform(Clickable interactable, String ignored) {
    interactable.click();
  }
}
