package com.gotriva.nlp.antifa.element.impl;

import com.gotriva.nlp.antifa.element.AbstractElement;
import com.gotriva.nlp.antifa.element.Checkable;
import org.openqa.selenium.WebElement;

/** This class represents a radio button on UI. */
public class Radio extends AbstractElement implements Checkable {

  Radio(WebElement element) {
    super(element);
  }

  @Override
  public void check() {
    if (!element.isSelected()) {
      element.click();
    }
  }
}
