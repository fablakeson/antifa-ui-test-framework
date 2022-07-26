package com.gotriva.testing.antifa.model;

import com.gotriva.testing.antifa.element.Interactable;
import com.gotriva.testing.antifa.factory.InteractableFactory;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


/** A page object model generic implementation. */
public class GenericPageObject {

    /** The name of this page object. */
    private String name;

    /** The web address of this page. */
    private URL address;

    /** The web driver instance. */
    private WebDriver driver;

    /** The interactable factory instance. */
    private InteractableFactory factory;

    /** The page object web elements. */
    Map<String, Interactable> elements;

    /** Constructor for pages that not opens a new URL. */
    private GenericPageObject(String name, WebDriver driver, InteractableFactory factory) {
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
     * @param factory the interactable factory
     */
    private GenericPageObject(String name, URL address, WebDriver driver, InteractableFactory factory) {
        this(name, driver, factory);
        this.address = address;
        navigateToAddress();
    }

    /**
     * Generates a new page object model.
     * 
     * @param name the page name
     * @param driver the session webdriver
     * @param factory the interactable factory
     * @return
     */
    public static GenericPageObject newPage(String name, WebDriver driver, InteractableFactory factory) {
        return new GenericPageObject(name, driver, factory);
    }

    /**
     * Generates a new page object model opening the given URL address. 
     * 
     * @param name the page name
     * @param address the URL address
     * @param driver the session webdriver
     * @param factory the interactable factory
     * @return
     */
    public static GenericPageObject newPage(String name, URL address, WebDriver driver, InteractableFactory factory) {
        return new GenericPageObject(name, address, driver, factory);
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
        Interactable interactable = factory.createInteractableFromType(element, type);
        elements.put(name, interactable);
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
}
