package com.gotriva.nlp.antifa.strategy.impl;

import com.gotriva.nlp.antifa.element.Hoverable;
import com.gotriva.nlp.antifa.strategy.InteractableActionStrategy;

/** This class implements a strategy for hover action interactable. */
public class HoverStrategy implements InteractableActionStrategy<Hoverable> {

  /** Default constructor */
  HoverStrategy() {}

  @Override
  public String getAction() {
    return "hover";
  }

  @Override
  public void perform(Hoverable interactable, String ignored) {
    interactable.hoverOn();
  }
}
