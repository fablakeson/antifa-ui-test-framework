package com.gotriva.testing.antifa.handler.impl;

import com.gotriva.testing.antifa.handler.PageObjectActionHandler;
import com.gotriva.testing.antifa.model.ExecutionContext;
import com.gotriva.testing.antifa.model.GenericPageObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class RollPageHandler implements PageObjectActionHandler {

  private static final int SCROLLED_PIXELS = 350;

  @Override
  public String getAction() {
    return "roll";
  }

  @Override
  public void handle(ExecutionContext context, String page, String direction) {
    GenericPageObject currentPage = context.getCurrentPage();
    WebDriver driver = currentPage.getDriver();
    switch (direction) {
      case "down":
        scrollDown(driver);
        break;
      case "up":
        scrollUp(driver);
        break;
    }
  }

  /** Moves the page scroll down. */
  private void scrollDown(WebDriver driver) {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("window.scrollBy(0,arguments[0])", SCROLLED_PIXELS);
  }

  /** Moves the page scroll up. */
  private void scrollUp(WebDriver driver) {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("window.scrollBy(0,arguments[0])", SCROLLED_PIXELS);
  }
}
