package com.gotriva.nlp.antifa.factory;

import org.openqa.selenium.WebElement;

import com.gotriva.nlp.antifa.element.AbstractElement;

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
