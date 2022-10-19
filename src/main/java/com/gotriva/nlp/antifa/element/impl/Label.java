package com.gotriva.nlp.antifa.element.impl;

import com.gotriva.nlp.antifa.element.AbstractElement;
import com.gotriva.nlp.antifa.element.Clickable;
import com.gotriva.nlp.antifa.element.Hoverable;
import com.gotriva.nlp.antifa.element.Readable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.interactions.Actions;

/** This class represents an interactable label on UI. */
public class Label extends AbstractElement implements Clickable, Hoverable, Readable {

  Label(WebElement element) {
    super(element);
  }

  @Override
  public void hoverOn() {
    WebDriver driver = ((WrapsDriver) element).getWrappedDriver();
    Actions action = new Actions(driver);
    action.moveToElement(element).perform();
  }

  @Override
  public void click() {
    element.click();
  }

  @Override
  public String read() {
    return element.getText();
  }
}
