package com.gotriva.nlp.antifa.strategy.impl;

import com.gotriva.nlp.antifa.execution.ExecutionContext;
import com.gotriva.nlp.antifa.model.GenericPageObject;
import com.gotriva.nlp.antifa.strategy.PageObjectActionStrategy;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/** This class implements a page object strategy for scroll page action. */
public class ScrollPageStrategy implements PageObjectActionStrategy {

  private static final int SCROLLED_PIXELS = 350;

  /** Default constructor */
  ScrollPageStrategy() {}

  @Override
  public String getAction() {
    return "scroll";
  }

  @Override
  public void perform(ExecutionContext context, String page, String direction, String type) {
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
