package com.gotriva.nlp.antifa.element.impl;

import com.gotriva.nlp.antifa.element.AbstractElement;
import com.gotriva.nlp.antifa.element.Clickable;
import com.gotriva.nlp.antifa.element.Hoverable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.interactions.Actions;

/** This class represents an interactable image on UI. */
public class Image extends AbstractElement implements Clickable, Hoverable {

  public Image(WebElement element) {
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
