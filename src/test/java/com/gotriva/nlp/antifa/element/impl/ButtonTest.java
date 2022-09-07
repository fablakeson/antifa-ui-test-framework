package com.gotriva.nlp.antifa.element.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

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
        "<html><body><button id=\"test1\" onclick=\"document.write('button test1"
            + " was clicked')\">Test 1</button></body></html>";
    mockPage(path, html);
    navigateTo(path);

    button = new Button(findElementById("test1"));
    button.click();

    assertTrue(() -> findElementByTag("body").getText().endsWith("button test1 was clicked"));
  }

  @Test
  public void testButtonTest2Click_thenWriteMessageButtonTest2WasClicked() {
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
}
