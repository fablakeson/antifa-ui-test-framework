package com.gotriva.nlp.antifa.execution.impl;

import java.net.URL;
import java.util.Deque;
import java.util.LinkedList;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.gotriva.nlp.antifa.exception.ExecutionException;
import com.gotriva.nlp.antifa.execution.ExecutionContext;
import com.gotriva.nlp.antifa.factory.CompositeElementFactory;
import com.gotriva.nlp.antifa.model.GenericPageObject;

/** This class implements the {@link ExecutionContext} */
public class ExecutionContextImpl implements ExecutionContext {

  /** The stack of pages on context. */
  private Deque<GenericPageObject> pageStack;

  /** The associated context web driver */
  private WebDriver driver;

  /** The interactable factory */
  private CompositeElementFactory factory;

  /** Default constructor. */
  ExecutionContextImpl(WebDriver driver, CompositeElementFactory factory) {
    this.pageStack = new LinkedList<>();
    this.driver = driver;
    this.factory = factory;
  }

  /**
   * Gets the current execution context page.
   *
   * @return
   */
  public GenericPageObject getCurrentPage() {
    return pageStack.peekFirst();
  }

  /** Closes the current execution context page. */
  public GenericPageObject closeCurrentPage() {
    /** Checks if context has any open page. */
    if (pageStack.isEmpty()) {
      throw new ExecutionException("No pages to close.");
    }
    pageStack.pollFirst();
    return getCurrentPage();
  }

  /** Creates a new page on context. */
  public GenericPageObject openPage(String name) {
    /** Adds new page to the top. */
    pageStack.addFirst(
        GenericPageObject.builder().setName(name).setDriver(driver).setFactory(factory).build());
    /** Returns added page. */
    return getCurrentPage();
  }

  /** Creates a new page on context for the given URL. */
  public GenericPageObject openPage(String name, URL url) {
    /** Adds new page to the top. */
    pageStack.addFirst(
        GenericPageObject.builder()
            .setName(name)
            .setAddress(url)
            .setDriver(driver)
            .setFactory(factory)
            .build());
    /** Returns added page. */
    return getCurrentPage();
  }

  /**
   * Gets the current page screenshot.
   *
   * @return the screenshot image base-64 representation
   */
  public String getScreenshot() {
    return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
  }

  @Override
  public void close() {
    driver.close();
  }
}
