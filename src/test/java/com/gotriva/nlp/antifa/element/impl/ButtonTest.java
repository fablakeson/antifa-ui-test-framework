package com.gotriva.nlp.antifa.element.impl;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/** Test for {@link Button} class. */
@WireMockTest(httpPort = 8001)
public class ButtonTest {

  /** Selenium webdriver (Chrome) */
  private WebDriver driver;

  /** Test subject. */
  private Button button;

  @BeforeEach
  public void init() {
    /** Initialize selenium driver */
    driver = new ChromeDriver();
  }

  @AfterEach
  public void close() {
    /** Clise selenium driver */
    driver.close();
  }

  @Test
  public void test_ClickButtonTest1_then_WriteMessageButtonTest1WasClicked() {
    /** Generate test page for button */
    String path = "/button-1-test";
    String html =
        "<html><body><button id=\"test1\" onclick=\"document.write('button test1"
            + " was clicked')\">Test 1</button></body></html>";
    mockPage(path, html);
    navigateTo(path);

    button = new Button(findElementById("test1"));
    button.click();

    assertTrue(() -> findElementByTag("body").getText().endsWith("button test1 was clicked"));
  }

  @Test
  public void test_ClickButtonTest2_then_WriteMessageButtonTest2WasClicked() {
    /** Generate test page for button */
    String path = "/button-2-test";
    String html =
        "<html><body><input id=\"test2\" onclick=\"document.write('button test2 was"
            + " clicked')\" type=\"button\" value=\"Test"
            + " 2\"/></button></body></html>";
    mockPage(path, html);
    navigateTo(path);

    button = new Button(findElementById("test2"));
    button.click();

    assertTrue(() -> findElementByTag("body").getText().endsWith("button test2 was clicked"));
  }

  private void navigateTo(String path) {
    driver.get("http://localhost:8001" + path);
  }

  private WebElement findElementById(String id) {
    return driver.findElement(By.id(id));
  }

  private WebElement findElementByTag(String tag) {
    return driver.findElement(By.tagName("body"));
  }

  private void mockPage(String path, String html) {
    stubFor(
        get(urlEqualTo(path))
            .willReturn(aResponse().withHeader("Content-Type", "text/html").withBody(html)));
  }
}
