package com.gotriva.nlp.antifa.execution.impl;

import com.gotriva.nlp.antifa.element.CompositeElementFactory;
import com.gotriva.nlp.antifa.element.ElementMetadata;
import com.gotriva.nlp.antifa.exception.ExecutionException;
import com.gotriva.nlp.antifa.execution.ExecutionContext;
import com.gotriva.nlp.antifa.model.GenericPageObject;
import java.net.URL;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/** This class implements the {@link ExecutionContext} */
public class ExecutionContextImpl implements ExecutionContext {

  /** The stack of pages on context. */
  private Deque<GenericPageObject> pageStack;

  /** The element metadata map. */
  private Map<String, ElementMetadata> elementMetadata;

  /** The parameters map */
  private Map<String, Object> parameters;

  /** The associated context web driver */
  private WebDriver driver;

  /** The interactable factory */
  private CompositeElementFactory factory;

  /** Default constructor. */
  ExecutionContextImpl(WebDriver driver, CompositeElementFactory factory) {
    this.pageStack = new LinkedList<>();
    this.elementMetadata = new HashMap<>();
    this.parameters = new HashMap<>();
    this.driver = driver;
    this.factory = factory;
  }

  /**
   * Gets the current execution context page.
   *
   * @return
   */
  @Override
  public GenericPageObject getCurrentPage() {
    return pageStack.peekFirst();
  }

  /** Closes the current execution context page. */
  @Override
  public GenericPageObject closeCurrentPage() {
    /** Checks if context has any open page. */
    if (pageStack.isEmpty()) {
      throw new ExecutionException("No pages to close.");
    }
    pageStack.pollFirst();
    return getCurrentPage();
  }

  /** Creates a new page on context. */
  @Override
  public GenericPageObject openPage(String name) {
    /** Adds new page to the top. */
    pageStack.addFirst(
        GenericPageObject.builder().setName(name).setDriver(driver).setFactory(factory).build());
    /** Returns added page. */
    return getCurrentPage();
  }

  /** Creates a new page on context for the given URL. */
  @Override
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
  @Override
  public String getScreenshot() {
    return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
  }

  @Override
  public void close() {
    driver.close();
  }

  @Override
  public void setMetadata(String element, ElementMetadata metadata) {
    elementMetadata.put(element, metadata);
  }

  @Override
  public ElementMetadata getMetadata(String element) {
    return elementMetadata.get(element);
  }

  @Override
  public void setParameter(String parameter, Object value) {
    parameters.put(parameter, value);
  }

  @Override
  public Object getParameter(String parameter) {
    return parameters.get(parameter);
  }
}
