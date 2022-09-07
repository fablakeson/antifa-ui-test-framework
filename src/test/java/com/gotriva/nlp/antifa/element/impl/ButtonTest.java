package com.gotriva.nlp.antifa.element.impl;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/** Test for {@link Button} class. */
@WireMockTest(httpPort = 8001)
public class ButtonTest {

  /** Selenium webdriver (Chrome) */
  private WebDriver driver;

  /** Test subject. */
  private Button button;

  @BeforeEach
  public void setup() {
    /** Initialize selenium driver */
    driver = new ChromeDriver();
  }

  @AfterEach
  public void tearDown() {
    /** Clise selenium driver */
    driver.close();
  }

  @Test
  public void test_ClickButtonTest1_then_WriteMessageButtonTest1WasClicked() {
    /** Generate test page for button */
    stubFor(
        get(urlEqualTo("/button-1-test"))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", "text/html")
                    .withBody(
                        "<html><body><button id=\"test1\" onclick=\"document.write('button test1"
                            + " was clicked')\">Test 1</button></body></html>")));
    /** Initialize button */
    driver.get("http://localhost:8001/button-1-test");
    button = new Button(driver.findElement(ById.id("test1")));

    button.click();

    assertTrue(
        () ->
            driver.findElement(By.tagName("body")).getText().endsWith("button test1 was clicked"));
  }

  @Test
  public void test_ClickButtonTest2_then_WriteMessageButtonTest2WasClicked() {
    /** Generate test page for button */
    stubFor(
        get(urlEqualTo("/button-2-test"))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", "text/html")
                    .withBody(
                        "<html><body><input id=\"test1\" onclick=\"document.write('button test2 was"
                            + " clicked')\" type=\"button\" value=\"Test"
                            + " 2\"/></button></body></html>")));
    /** Initialize button */
    driver.get("http://localhost:8001/button-2-test");
    button = new Button(driver.findElement(ById.id("test1")));

    button.click();

    assertTrue(
        () ->
            driver.findElement(By.tagName("body")).getText().endsWith("button test2 was clicked"));
  }
}
