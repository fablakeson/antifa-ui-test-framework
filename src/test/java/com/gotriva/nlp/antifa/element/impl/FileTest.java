package com.gotriva.nlp.antifa.element.impl;

import static j2html.TagCreator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.io.IOException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

/** Tests for {@link File} class. */
@WireMockTest(httpPort = 8001)
public class FileTest extends AbstractElementTest {

  /** The temp file */
  private static java.io.File testFile;

  /** Test subject */
  private File file;

  @BeforeAll
  public static void beforeAll() throws IOException {
    testFile = java.io.File.createTempFile("antifa_test_", ".tmp");
  }

  @AfterAll
  public static void afterAll() {
    if (testFile != null && testFile.exists()) {
      testFile.delete();
    }
  }

  @Test
  public void testUpload_withTempFile_thenTempFileIsUploaded() {
    String path = "/test-file-1";
    String html = body(input().withId("test1").withType("file")).render();
    mockPage(path, html);
    navigateTo(path);

    WebElement element = findElementById("test1");
    file = new File(element);
    file.upload(testFile.getAbsolutePath());

    assertEquals("C:\\fakepath\\" + testFile.getName(), element.getAttribute("value"));
  }
}
