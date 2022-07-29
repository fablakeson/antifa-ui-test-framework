package com.gotriva.testing.antifa.element.impl;

import com.gotriva.testing.antifa.element.AbstractElement;
import com.gotriva.testing.antifa.element.Readable;
import com.gotriva.testing.antifa.element.Writable;
import org.openqa.selenium.WebElement;

public class Text extends AbstractElement implements Readable, Writable {

  public Text(WebElement element) {
    super(element);
  }

  @Override
  public void write(CharSequence text) {
    element.sendKeys(text);
  }

  @Override
  public String read() {
    return element.getText();
  }
}
