package com.gotriva.nlp.antifa.element;

import org.openqa.selenium.WebElement;

/** Abstract class to represent an element. */
public abstract class AbstractElement implements Interactable {

  protected WebElement element;

  public AbstractElement(WebElement element) {
    this.element = element;
  }
}
