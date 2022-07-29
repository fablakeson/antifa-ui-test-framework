package com.gotriva.testing.antifa.element;

import org.openqa.selenium.WebElement;

/** Abstract class to represent a element. */
public abstract class AbstractElement implements Interactable {

  protected WebElement element;

  public AbstractElement(WebElement element) {
    this.element = element;
  }

  @Override
  public WebElement getElement() {
    return element;
  }
}
