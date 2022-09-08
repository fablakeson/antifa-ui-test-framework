package com.gotriva.nlp.antifa.element.impl;

import static j2html.TagCreator.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

/** Tests for {@link Checkbox} class. */
@WireMockTest(httpPort = 8001)
public class CheckboxTest extends AbstractElementTest {

  /** Test subject. */
  private Checkbox checkbox;

  @Test
  public void testCheck_withCheckboxUnchecked_thenIsChecked() {
    String path = "/test-checkbox-1";
    String html = body(input().withId("test1").withType("checkbox").withValue("test")).render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test1");
    checkbox = new Checkbox(element);
    checkbox.check();

    assertTrue(() -> element.isSelected());
  }

  @Test
  public void testCheck_withCheckboxChecked_thenIsChecked() {
    String path = "/test-checkbox-2";
    String html = body(input().withId("test2").withType("checkbox").withCondChecked(true)).render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test2");
    checkbox = new Checkbox(element);
    checkbox.check();

    assertTrue(() -> element.isSelected());
  }

  @Test
  public void testUncheck_withCheckboxChecked_thenIsUnchecked() {
    String path = "/test-checkbox-3";
    String html = body(input().withId("test3").withType("checkbox").withCondChecked(true)).render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test3");
    checkbox = new Checkbox(element);
    checkbox.uncheck();

    assertFalse(() -> element.isSelected());
  }

  @Test
  public void testUncheck_withCheckboxUnchecked_thenIsUncheked() {
    String path = "/test-checkbox-4";
    String html = body(input().withId("test4").withType("checkbox")).render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test4");
    checkbox = new Checkbox(element);
    checkbox.uncheck();

    assertFalse(() -> element.isSelected());
  }
}
