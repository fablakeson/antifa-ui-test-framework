package com.gotriva.nlp.antifa.element.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;

/** Test for {@link Checkbox} class. */
@WireMockTest(httpPort = 8001)
public class CheckboxTest extends AbstractElementTest {

  /** Test subject. */
  private Checkbox checkbox;

  @Test
  public void testCheck_withCheckboxUnchecked_thenIsChecked() {
    String path = "/test-checkbox-1";
    String html =
        "<html><body><input id=\"test1\" type=\"checkbox\" value=\"test\"/></body></html>";
    mockPage(path, html);
    navigateTo(path);

    checkbox = new Checkbox(findElementById("test1"));
    checkbox.check();

    assertTrue(() -> findElementById("test1").isSelected());
  }

  @Test
  public void testCheck_withCheckboxChecked_thenIsChecked() {
    String path = "/test-checkbox-2";
    String html =
        "<html><body><input id=\"test2\" type=\"checkbox\" checked=\"true\""
            + " value=\"test\"/></body></html>";
    mockPage(path, html);
    navigateTo(path);

    checkbox = new Checkbox(findElementById("test2"));
    checkbox.check();

    assertTrue(() -> findElementById("test2").isSelected());
  }

  @Test
  public void testUncheck_withCheckboxChecked_thenIsUnchecked() {
    String path = "/test-checkbox-3";
    String html =
        "<html><body><input id=\"test3\" type=\"checkbox\" checked=\"true\""
            + " value=\"test\"/></body></html>";
    mockPage(path, html);
    navigateTo(path);

    checkbox = new Checkbox(findElementById("test3"));
    checkbox.uncheck();

    assertFalse(() -> findElementById("test3").isSelected());
  }

  @Test
  public void testUncheck_withCheckboxUnchecked_thenIsUncheked() {
    String path = "/test-checkbox-4";
    String html =
        "<html><body><input id=\"test4\" type=\"checkbox\" value=\"test\"/></body></html>";
    mockPage(path, html);
    navigateTo(path);

    checkbox = new Checkbox(findElementById("test4"));
    checkbox.uncheck();

    assertFalse(() -> findElementById("test4").isSelected());
  }
}
