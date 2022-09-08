package com.gotriva.nlp.antifa.element.impl;

import static j2html.TagCreator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

/** Tests for {@link Image} class. */
@WireMockTest(httpPort = 8001)
public class ImageTest extends AbstractElementTest {

  /** 11px square image */
  private static final String IMAGE_SRC =
      "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEAAQABAAD/2wBDAAUDBAQEAwUEBAQFBQUGBwwIBwcHBw8LCwkM"
          + "EQ8SEhEPERETFhwXExQaFRERGCEYGh0dHx8fExciJCIeJBweHx7/2wBDAQUFBQcGBw4ICA4eFBEUHh4eHh4eHh4eHh4"
          + "eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh4eHh7/wAARCAALAAsDASIAAhEBAxEB/8QAFwAAAwEAAA"
          + "AAAAAAAAAAAAAABAYHCP/EACEQAAICAgIDAQEBAAAAAAAAAAECAwUEEQYSAAchIggT/8QAFQEBAQAAAAAAAAAAAAAAA"
          + "AAAAgP/xAAdEQACAQQDAAAAAAAAAAAAAAABIREAAgNhEkFx/9oADAMBAAIRAxEAPwBR5Dyn23eXthaycCo+SCxU5OJP"
          + "Y4jzSYsQH2LHJkURhDsaUduwJ2WPlAsPeHsrgWU3EMimxOQT1irFNnalm7P1DNH33t/8yTH2P09Nkk7PhP8ATVrZcM9"
          + "mVWBxfPyavEtuuZmQQyHo07SntIinYjZiAxKddt+j9++aZ4/T1tHT49VV4iY+JAv4TZY7YlmZmbbMzMSxYkkkkkknw2"
          + "i7uqZDjMcAQnJl6QWn7X//2Q==";

  /** Test subject */
  private Image image;

  @Test
  public void testClick_thenImageTagIsClicked() {
    String path = "/test-image-1";
    String html =
        body(img().withId("test1").withSrc(IMAGE_SRC).attr("onclick=\"this.alt='clicked'\""))
            .render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test1");
    image = new Image(element);
    image.click();

    assertEquals("clicked", element.getAttribute("alt"));
  }

  @Test
  public void testHoverOn_thenImageTagIsHovered() {
    String path = "/test-image-2";
    String html =
        body(img().withId("test2").withSrc(IMAGE_SRC).attr("onmouseover=\"this.alt='hovered'\""))
            .render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test2");
    image = new Image(element);
    image.hoverOn();

    assertEquals("hovered", element.getAttribute("alt"));
  }
}
