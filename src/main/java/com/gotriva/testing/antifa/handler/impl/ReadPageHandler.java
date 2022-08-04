package com.gotriva.testing.antifa.handler.impl;

import com.gotriva.testing.antifa.exception.ActionHandleException;
import com.gotriva.testing.antifa.execution.ExecutionContext;
import com.gotriva.testing.antifa.handler.PageObjectActionHandler;
import com.gotriva.testing.antifa.model.GenericPageObject;
import java.text.MessageFormat;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** his class implements a page object handler for read on page action. */
public class ReadPageHandler implements PageObjectActionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReadPageHandler.class);

  private static final String FIND_MESSAGE_XPATH =
      "//*[text()[contains(.,\"{0}\")]"
          + " and not(name() = \"script\")"
          + " and not(name() = \"style\")]";

  @Override
  public String getAction() {
    return "read";
  }

  @Override
  public void handle(ExecutionContext context, String page, String message) {
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
