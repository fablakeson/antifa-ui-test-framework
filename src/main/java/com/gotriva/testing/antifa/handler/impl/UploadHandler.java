package com.gotriva.testing.antifa.handler.impl;

import com.gotriva.testing.antifa.element.Uploadable;
import com.gotriva.testing.antifa.handler.InteractableActionHandler;

/** This class implements a handler for upload action interactable. */
public class UploadHandler implements InteractableActionHandler<Uploadable> {

  @Override
  public String getAction() {
    return "upload";
  }

  @Override
  public void handle(Uploadable interactable, String filePath) {
    interactable.upload(filePath);
  }
}
