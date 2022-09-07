package com.gotriva.nlp.antifa.element.impl;

import org.openqa.selenium.WebElement;

import com.gotriva.nlp.antifa.element.AbstractElement;
import com.gotriva.nlp.antifa.element.Writable;

public class Text extends AbstractElement implements Writable {

  public Text(WebElement element) {
    super(element);
  }

  @Override
  public void write(CharSequence text) {
    element.sendKeys(text);
  }
}
