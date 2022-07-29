package com.gotriva.testing.antifa.model;

import com.gotriva.testing.antifa.exception.ExecutionException;
import com.gotriva.testing.antifa.factory.InteractableFactory;
import java.io.Closeable;
import java.net.URL;
import java.util.Deque;
import java.util.LinkedList;
import org.openqa.selenium.WebDriver;

/** This class represents the execution context for the {@link ExecutorImpl}. */
public class ExecutionContext implements Closeable {

  /** The stack of pages on context. */
  private Deque<GenericPageObject> pageStack;

  /** The associated context web driver */
  private WebDriver driver;

  /** The interactable factory */
  private InteractableFactory factory;

  /** Default constructor. */
  public ExecutionContext(WebDriver driver, InteractableFactory factory) {
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

  @Override
  public void close() {
    driver.close();
  }
}
