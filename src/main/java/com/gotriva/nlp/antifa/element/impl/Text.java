package com.gotriva.nlp.antifa.element.impl;

import com.gotriva.nlp.antifa.element.AbstractElement;
import com.gotriva.nlp.antifa.element.Readable;
import com.gotriva.nlp.antifa.element.Writable;
import org.openqa.selenium.WebElement;

public class Text extends AbstractElement implements Writable, Readable {

  Text(WebElement element) {
    super(element);
  }

  @Override
  public void write(CharSequence text) {
    element.sendKeys(text);
  }

  @Override
  public String read() {
    if ("input".equals(element.getTagName())) {
      return element.getAttribute("value");
    }
    return element.getText();
  }
}
