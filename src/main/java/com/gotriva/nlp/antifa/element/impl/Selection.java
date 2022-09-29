package com.gotriva.nlp.antifa.element.impl;

import com.gotriva.nlp.antifa.element.AbstractElement;
import com.gotriva.nlp.antifa.element.Selectable;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class Selection extends AbstractElement implements Selectable {

  private Select select;

  public Selection(WebElement element) {
    super(element);
    this.select = new Select(element);
  }

  @Override
  public void selectItem(String text) {
    select.selectByVisibleText(text);
  }
}
