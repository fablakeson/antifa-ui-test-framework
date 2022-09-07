package com.gotriva.nlp.antifa.element.impl;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import com.gotriva.nlp.antifa.element.AbstractElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/** Base class for {@link AbstractElement} implementations tess. */
public class AbstractElementTest {

  /** Selenium webdriver (Chrome) */
  protected WebDriver driver;

  @BeforeEach
  public void init() {
    /** Initialize selenium headless driver */
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless");
    driver = new ChromeDriver(options);
  }

  @AfterEach
  public void close() {
    /** Clise selenium driver */
    driver.close();
  }

  /**
   * Navigates to a given test path.
   *
   * @param path
   */
  protected void navigateTo(String path) {
    driver.get("http://localhost:8001" + path);
  }

  /**
   * Find an element by its id.
   *
   * @param id
   * @return
   */
  protected WebElement findElementById(String id) {
    return driver.findElement(By.id(id));
  }

  /**
   * Find an element by its tag name.
   *
   * @param tag
   * @return
   */
  protected WebElement findElementByTag(String tag) {
    return driver.findElement(By.tagName("body"));
  }

  /**
   * Mocks some page HTML to the given test path.
   *
   * @param path
   * @param html
   */
  protected void mockPage(String path, String html) {
    stubFor(
        get(urlEqualTo(path))
            .willReturn(aResponse().withHeader("Content-Type", "text/html").withBody(html)));
  }
}
