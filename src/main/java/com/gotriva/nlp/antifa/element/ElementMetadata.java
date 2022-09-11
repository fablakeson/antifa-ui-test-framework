package com.gotriva.nlp.antifa.element;

import org.openqa.selenium.By;

/** This class holds some metadata info for elements. */
public class ElementMetadata {

  /** The label for this element name */
  private final String label;

  /** The locator for this element */
  private final By locator;

  private ElementMetadata(String label, By locator) {
    this.label = label;
    this.locator = locator;
  }

  /**
   * Creates a new element metadata instance.
   *
   * @param label the label
   * @param locator the locator
   * @return the new instance
   */
  public static ElementMetadata of(String label, By locator) {
    return new ElementMetadata(label, locator);
  }

  public String getLabel() {
    return label;
  }

  public By getLocator() {
    return locator;
  }
}
