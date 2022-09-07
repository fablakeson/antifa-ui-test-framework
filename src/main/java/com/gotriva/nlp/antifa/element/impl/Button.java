package com.gotriva.nlp.antifa.element.impl;

import com.gotriva.nlp.antifa.element.AbstractElement;
import com.gotriva.nlp.antifa.element.Clickable;
import org.openqa.selenium.WebElement;

/** This class represents an interactable button on UI. */
public class Button extends AbstractElement implements Clickable {

  public Button(WebElement element) {
    super(element);
  }

  @Override
  public void click() {
    element.click();
  }
}
