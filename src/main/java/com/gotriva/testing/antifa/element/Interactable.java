package com.gotriva.testing.antifa.element;

import org.openqa.selenium.WebElement;

/** Represents a user interface interactable element. */
public interface Interactable {

  /** Returns the underlying web element. */
  WebElement getElement();
}
