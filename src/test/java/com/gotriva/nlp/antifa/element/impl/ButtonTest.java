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
  public void testButtonTest1Click_thenWriteMessageButtonTest1WasClicked() {
    /** Generate test page for button */
    String path = "/button-1-test";
    String html =
        body(button()
                .withId("test1")
                .attr("onclick=\"this.dataset.value='test1'\"")
                .withText("Test 1"))
            .render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test1");
    button = new Button(element);
    button.click();

    assertEquals("test1", element.getAttribute("data-value"));
  }

  @Test
  public void testButtonTest2Click_thenWriteMessageButtonTest2WasClicked() {
    /** Generate test page for button */
    String path = "/button-2-test";
    String html =
        body(input()
                .withId("test2")
                .withType("checkbox")
                .withValue("Test 2")
                .attr("onclick=\"this.dataset.value='test2'\""))
            .render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test2");
    button = new Button(element);
    button.click();

    assertEquals("test2", element.getAttribute("data-value"));
  }
}
