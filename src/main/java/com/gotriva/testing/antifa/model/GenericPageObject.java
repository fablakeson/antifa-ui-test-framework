package com.gotriva.testing.antifa.model;

import com.gotriva.testing.antifa.element.AbstractElement;
import com.gotriva.testing.antifa.element.Interactable;
import com.gotriva.testing.antifa.factory.CompositeElementFactory;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/** A page object model generic implementation. */
public class GenericPageObject {

  /** A builder class for {@link GenericPageObject} */
  public static class Builder {

    /** The builder name. */
    private String name;
    /** The builder address. */
    private URL address;
    /** The buidler driver. */
    private WebDriver driver;
    /** The builder factory. */
    private CompositeElementFactory factory;

    /** The page object web elements. */
    private Builder() {}

    /**
     * @return a new builder instance.
     */
    private static Builder newBuilder() {
      return new Builder();
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setAddress(URL address) {
      this.address = address;
      return this;
    }

    public Builder setDriver(WebDriver driver) {
      this.driver = driver;
      return this;
    }

    public Builder setFactory(CompositeElementFactory factory) {
      this.factory = factory;
      return this;
    }

    public GenericPageObject build() {
      if (address != null) {
        return new GenericPageObject(name, address, driver, factory);
      }
      return new GenericPageObject(name, driver, factory);
    }
  }

  /** The name of this page object. */
  private String name;

  /** The web address of this page. */
  private URL address;

  /** The web driver instance. */
  private WebDriver driver;

  /** The element wrapper instance. */
  private CompositeElementFactory factory;

  /** The page object web elements. */
  Map<String, AbstractElement> elements;

  /** Constructor for pages that not opens a new URL. */
  private GenericPageObject(String name, WebDriver driver, CompositeElementFactory factory) {
    this.name = name;
    this.driver = driver;
    this.factory = factory;
    this.elements = new HashMap<>();
  }

  /**
   * Constructor for pages that opens a new URL.
   *
   * @param name the page name
   * @param address the page address
   * @param driver the session webdriver
   * @param factory the element wrapper
   */
  private GenericPageObject(
      String name, URL address, WebDriver driver, CompositeElementFactory factory) {
    this(name, driver, factory);
    this.address = address;
    navigateToAddress();
  }

  /**
   * @return the builder isntance.
   */
  public static Builder builder() {
    return Builder.newBuilder();
  }

  /**
   * Create and attaches a new interactable element to this page.
   *
   * @param <T> the type of created interactable
   * @param name the name of the element
   * @param locator the location strategy to find the webelement
   * @param type the interactable type
   */
  public <T extends Interactable> void addElement(String name, By locator, String type) {
    WebElement element = driver.findElement(locator);
    if (type != null) {}
    AbstractElement elementWrapper = factory.create(type, element);
    elements.put(name, elementWrapper);
  }

  /**
   * Get an {@link Interactable} element of this page.
   *
   * @param name the element name.
   * @return the interactable element.
   */
  public Interactable getElement(String name) {
    return elements.get(name);
  }

  /**
   * Checks if current page contains an element with this name.
   *
   * @param name the name to be checked
   * @return true if it contains
   */
  public boolean hasElement(String name) {
    return elements.containsKey(name);
  }

  /** Navigate to the current page address. */
  private void navigateToAddress() {
    driver.get(address.toString());
  }

  /** Getters and Setters. */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public URL getAddress() {
    return address;
  }

  public void setAddress(URL address) {
    this.address = address;
  }

  public WebDriver getDriver() {
    return this.driver;
  }
}
