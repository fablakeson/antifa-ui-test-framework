package com.gotriva.testing.antifa.factory;

import com.gotriva.testing.antifa.element.AbstractElement;
import org.openqa.selenium.WebElement;

/** This interface represents a concrete interactable element factory */
public interface ElementFactory<T extends AbstractElement> {

  /**
   * Creates an instance of the concrete web element wrapper.
   *
   * @param element the web element
   * @return the concrete element wrapper
   */
  T create(WebElement element);
}
