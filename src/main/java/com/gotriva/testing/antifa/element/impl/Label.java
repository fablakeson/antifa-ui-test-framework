package com.gotriva.testing.antifa.element.impl;

import com.gotriva.testing.antifa.element.AbstractElement;
import com.gotriva.testing.antifa.element.Clickable;
import com.gotriva.testing.antifa.element.Hoverable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.interactions.Actions;

/** This class represents an interactable label on UI. */
public class Label extends AbstractElement implements Clickable, Hoverable {

  public Label(WebElement element) {
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
}
