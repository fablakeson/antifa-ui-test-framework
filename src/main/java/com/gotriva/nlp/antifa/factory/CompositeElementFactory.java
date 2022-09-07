package com.gotriva.nlp.antifa.factory;

import com.gotriva.nlp.antifa.element.AbstractElement;
import com.gotriva.nlp.antifa.exception.InteractableTypeNotFoundException;
import org.openqa.selenium.WebElement;

/** This class creates web element wrappers from type names. */
public interface CompositeElementFactory {

  /**
   * Gets a factory for the fiven type name.
   *
   * @param type the element type
   * @return
   */
  ElementFactory<?> getElementFactory(String type);

  /**
   * Creates an wrapper for element from a type name.
   *
   * @param type the type name
   * @param element the web element for this wrapper
   * @return the created interactable
   */
  default AbstractElement create(String type, WebElement element) {
    ElementFactory<?> factory = getElementFactory(type);
    if (factory == null) {
      throw new InteractableTypeNotFoundException(type);
    }
    return factory.create(element);
  }
}
