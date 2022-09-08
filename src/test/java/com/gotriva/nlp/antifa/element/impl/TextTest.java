package com.gotriva.nlp.antifa.element.impl;

import static j2html.TagCreator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

@WireMockTest(httpPort = 8001)
public class TextTest extends AbstractElementTest {

  /** Test subject */
  private Text text;

  @Test
  public void testWrite_withInputText_thenInputValueIsWrited() {
    String path = "/test-text-1";
    String html = body(input().withId("test1").withType("text")).render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test1");
    text = new Text(element);
    text.write("writed");

    assertEquals("writed", element.getAttribute("value"));
  }

  @Test
  public void testWrite_withTextarea_thenTextareaValueIsWrited() {
    String path = "/test-text-2";
    String html = body(textarea().withId("test2")).render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test2");
    text = new Text(element);
    text.write("writed");

    assertEquals("writed", element.getAttribute("value"));
  }
}
