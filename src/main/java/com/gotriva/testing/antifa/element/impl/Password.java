package com.gotriva.testing.antifa.element.impl;

import com.gotriva.testing.antifa.element.AbstractElement;
import com.gotriva.testing.antifa.element.Writable;
import org.openqa.selenium.WebElement;

/** This class represents an interactable password input on UI. */
public class Password extends AbstractElement implements Writable {

  public Password(WebElement element) {
    super(element);
  }

  @Override
  public void write(CharSequence password) {
    element.sendKeys(password);
  }
}
