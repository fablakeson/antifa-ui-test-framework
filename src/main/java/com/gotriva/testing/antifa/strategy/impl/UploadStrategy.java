package com.gotriva.testing.antifa.strategy.impl;

import com.gotriva.testing.antifa.element.Uploadable;
import com.gotriva.testing.antifa.strategy.InteractableActionStrategy;

/** This class implements a strategy for upload action interactable. */
public class UploadStrategy implements InteractableActionStrategy<Uploadable> {

  /** Default constructor */
  UploadStrategy() {}

  @Override
  public String getAction() {
    return "upload";
  }

  @Override
  public void perform(Uploadable interactable, String filePath) {
    interactable.upload(filePath);
  }
}
