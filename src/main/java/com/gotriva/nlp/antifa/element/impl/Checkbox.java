package com.gotriva.nlp.antifa.element.impl;

import org.openqa.selenium.WebElement;

import com.gotriva.nlp.antifa.element.AbstractElement;
import com.gotriva.nlp.antifa.element.Checkable;

/** This class represents an interactable check input on UI. */
public class Checkbox extends AbstractElement implements Checkable {

  public Checkbox(WebElement element) {
    super(element);
  }

  @Override
  public void check() {
    if (!element.isSelected()) {
      element.click();
    }
  }

  @Override
  public void uncheck() {
    if (element.isSelected()) {
      element.click();
    }
  }
}
