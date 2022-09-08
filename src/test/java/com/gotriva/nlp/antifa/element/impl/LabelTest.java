package com.gotriva.nlp.antifa.element.impl;

import static j2html.TagCreator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

/** Tests for {@link Label} class. */
@WireMockTest(httpPort = 8001)
public class LabelTest extends AbstractElementTest {

  /** Test subject */
  private Label label;

  @Test
  public void testClick_withLabelTag_thenLabelTextIsClicked() {
    String path = "/test-label-1";
    String html =
        body(label()
                .withId("test1")
                .withText("Not clicked")
                .attr("onclick=\"this.innerHTML='clicked'\""))
            .render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test1");
    label = new Label(element);
    label.click();

    assertEquals("clicked", element.getText());
  }

  @Test
  public void testClick_withHeaderTag_thenHeaderTextIsClicked() {
    String path = "/test-label-2";
    String html =
        body(h1().withId("test2")
                .withText("Not clicked")
                .attr("onclick=\"this.innerHTML='clicked'\""))
            .render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test2");
    label = new Label(element);
    label.click();

    assertEquals("clicked", element.getText());
  }

  @Test
  public void testHoverOn_withLabelTag_thenLabelTextIsHovered() {
    String path = "/test-label-3";
    String html =
        body(label()
                .withId("test3")
                .withText("Not clicked")
                .attr("onmouseover=\"this.innerHTML='hovered'\""))
            .render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test3");
    label = new Label(element);
    label.click();

    assertEquals("hovered", element.getText());
  }

  @Test
  public void testHoverOn_withHeaderTag_thenHeaderTextIsHovered() {
    String path = "/test-label-4";
    String html =
        body(h1().withId("test4")
                .withText("Not clicked")
                .attr("onmouseover=\"this.innerHTML='hovered'\""))
            .render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test4");
    label = new Label(element);
    label.click();

    assertEquals("hovered", element.getText());
  }
}
