package com.gotriva.nlp.antifa.element.impl;

import static j2html.TagCreator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

/** Test for {@link Button} class. */
@WireMockTest(httpPort = 8001)
public class ButtonTest extends AbstractElementTest {

  /** Test subject. */
  private Button button;

  @Test
  public void testClick_withButtonTag_thenButtonValueIsClicked() {
    /** Generate test page for button */
    String path = "/button-1-test";
    String html =
        body(button()
                .withId("test1")
                .attr("onclick=\"this.dataset.value='clicked\'\"")
                .withText("Test 1"))
            .render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test1");
    button = new Button(element);
    button.click();

    assertEquals("clicked", element.getAttribute("data-value"));
  }

  @Test
  public void testClick_withInputButton_thenButtonValueIsClicked() {
    /** Generate test page for button */
    String path = "/button-2-test";
    String html =
        body(input()
                .withId("test2")
                .withType("checkbox")
                .withValue("Test 2")
                .attr("onclick=\"this.dataset.value='clicked'\""))
            .render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test2");
    button = new Button(element);
    button.click();

    assertEquals("clicked", element.getAttribute("data-value"));
  }
}
