package com.gotriva.nlp.antifa.element.impl;

import static j2html.TagCreator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

@WireMockTest(httpPort = 8001)
public class SelectionTest extends AbstractElementTest {

  /** Test subject */
  private Selection selection;

  @Test
  public void testSelectItem_withTextRed_thenValueIsRed() {
    final String path = "/text-selection-1";
    final String elementId = "test1";
    final String html =
        body(select(
                    option().withText("Red").withValue("red"),
                    option().withText("Blue").withValue("blue"))
                .withId(elementId))
            .render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById(elementId);
    selection = new Selection(element);
    selection.selectItem("Red");

    assertEquals("red", element.getAttribute("value"));
  }

  @Test
  public void testSelectItem_withTextRedAndBlueSelected_thenValueIsRed() {
    final String path = "/text-selection-1";
    final String elementId = "test1";
    final String html =
        body(select(
                    option().withText("Red").withValue("red"),
                    option().withText("Blue").withValue("blue").withCondSelected(true))
                .withId(elementId))
            .render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById(elementId);
    selection = new Selection(element);
    selection.selectItem("Red");

    assertEquals("red", element.getAttribute("value"));
  }

  @Test
  public void testSelectItem_withTextRedAndHavingMultiple_thenOptionsAreRedAndBlue() {
    final String path = "/text-selection-1";
    final String elementId = "test1";
    final String html =
        body(select(
                    option().withText("Red").withValue("red"),
                    option().withText("Blue").withValue("blue").withCondSelected(true))
                .withId(elementId)
                .withCondMultiple(true)
                .attr(
                    "onclick",
                    "this.dataset.values = [...this.options].filter(opt => opt.selected).map(opt =>"
                        + " opt.value).join(',');"))
            .render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById(elementId);
    selection = new Selection(element);
    selection.selectItem("Red");

    assertEquals("red,blue", element.getAttribute("data-values"));
  }
}
