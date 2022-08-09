package com.gotriva.testing.antifa.strategy.impl;

import com.gotriva.testing.antifa.element.Hoverable;
import com.gotriva.testing.antifa.strategy.InteractableActionStrategy;

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
