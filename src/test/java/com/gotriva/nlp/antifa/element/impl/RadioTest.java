package com.gotriva.nlp.antifa.element.impl;

import static j2html.TagCreator.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

/** Tests for {@link Radio} class. */
@WireMockTest(httpPort = 8001)
public class RadioTest extends AbstractElementTest {

  /** Test subject */
  private Radio radio;

  @Test
  public void testCheck_thenRadioIsChecked() {
    String path = "/test-radio-1";
    String html = body(input().withId("test1").withType("radio").withName("test")).render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test1");
    radio = new Radio(element);
    radio.check();

    assertTrue(element.isSelected());
  }

  @Test
  public void testCheckA_withMultipleRadios_thenOnlyRadioAIsChecked() {
    String path = "/test-radio-2";
    String html =
        body(
                input().withId("test2-A").withType("radio").withName("test").withValue("A"),
                input()
                    .withId("test2-B")
                    .withType("radio")
                    .withName("test")
                    .withValue("B")
                    .withCondChecked(true))
            .render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test2-A");
    radio = new Radio(element);
    radio.check();

    assertTrue(element.isSelected());
    assertFalse(findElementById("test2-B").isSelected());
  }
}
