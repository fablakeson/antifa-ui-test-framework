package com.gotriva.nlp.antifa.strategy.impl;

import java.text.MessageFormat;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gotriva.nlp.antifa.exception.ActionHandleException;
import com.gotriva.nlp.antifa.execution.ExecutionContext;
import com.gotriva.nlp.antifa.model.GenericPageObject;
import com.gotriva.nlp.antifa.strategy.PageObjectActionStrategy;

/** This class implements a page object strategy for read on page action. */
public class ReadPageStrategy implements PageObjectActionStrategy {

  private static final String FIND_MESSAGE_XPATH =
      "//*[text()[contains(.,\"{0}\")]"
          + " and not(name() = \"script\")"
          + " and not(name() = \"style\")]";

  @Override
  public String getAction() {
    return "read";
  }

  @Override
  public void perform(ExecutionContext context, String page, String message) {
    GenericPageObject pageObject = context.getCurrentPage();
    WebDriver driver = pageObject.getDriver();
    findElementWithMessage(message, driver);
  }

  /**
   * Tries to find an element with the given textual message
   *
   * @param message
   * @param driver
   * @return
   */
  private void findElementWithMessage(String message, WebDriver driver) {
    driver.findElements(By.xpath(MessageFormat.format(FIND_MESSAGE_XPATH, message))).stream()
        .filter(WebElement::isDisplayed)
        .findAny()
        .orElseThrow(
            () ->
                new ActionHandleException(
                    MessageFormat.format("Message \"{0}\" not found on page.", message)));
  }
}
