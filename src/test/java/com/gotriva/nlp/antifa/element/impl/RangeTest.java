package com.gotriva.nlp.antifa.element.impl;

import static j2html.TagCreator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.openqa.selenium.WebElement;

/** Tests for {@link Range} class. */
@WireMockTest(httpPort = 8001)
@Timeout(5)
public class RangeTest extends AbstractElementTest {

  /** Test subject */
  private Range range;

  @Test
  public void testSetValue_with50Percent_thenRangeValueIs10() {
    String path = "/test-range-1";
    String html =
        body(input().withId("test1").withType("range").attr("min=\"0\"").attr("max=\"20\""))
            .render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test1");
    range = new Range(element);
    range.setValue("50%");

    assertEquals("10", element.getAttribute("value"));
  }

  @Test
  public void testSetValue_with42_thenRangeValueIs42() {
    String path = "/test-range-2";
    String html =
        body(input().withId("test2").withType("range").attr("min=\"0\"").attr("max=\"100\""))
            .render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test2");
    range = new Range(element);
    range.setValue("42");

    assertEquals("42", element.getAttribute("value"));
  }

  @Test
  public void testSetValue_with11AndMax10_thenRangeValueIs10() {
    String path = "/test-range-3";
    String html =
        body(input().withId("test3").withType("range").attr("min=\"0\"").attr("max=\"10\""))
            .render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test3");
    range = new Range(element);
    range.setValue("11");

    assertEquals("10", element.getAttribute("value"));
  }

  @Test
  public void testSetValue_with0AndMin1_thenRangeValueIs1() {
    String path = "/test-range-4";
    String html =
        body(input().withId("test4").withType("range").attr("min=\"1\"").attr("max=\"10\""))
            .render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test4");
    range = new Range(element);
    range.setValue("0");

    assertEquals("1", element.getAttribute("value"));
  }

  @Test
  public void testSetValue_with3AndStep2_thenRangeValueIs2() {
    String path = "/test-range-5";
    String html =
        body(input()
                .withId("test5")
                .withType("range")
                .attr("min=\"0\"")
                .attr("max=\"10\"")
                .attr("step=\"2\""))
            .render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test5");
    range = new Range(element);
    range.setValue("3");

    assertEquals("2", element.getAttribute("value"));
  }
}
