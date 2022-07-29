package com.gotriva.testing.antifa.element.impl;

import com.gotriva.testing.antifa.element.AbstractElement;
import com.gotriva.testing.antifa.element.Checkable;
import org.openqa.selenium.WebElement;

/** This class represents a radio button on UI. */
public class Radio extends AbstractElement implements Checkable {

  public Radio(WebElement element) {
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
