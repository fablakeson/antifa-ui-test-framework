package com.gotriva.nlp.antifa.element.impl;

import com.gotriva.nlp.antifa.element.AbstractElement;
import com.gotriva.nlp.antifa.element.Uploadable;
import java.nio.file.Path;
import org.openqa.selenium.WebElement;

/** This class represents a interactable file input on UI. */
public class File extends AbstractElement implements Uploadable {

  File(WebElement element) {
    super(element);
  }

  @Override
  public void upload(String filePath) {
    /** Check if it is a valid path */
    Path path = Path.of(filePath);
    element.sendKeys(path.toAbsolutePath().toString());
  }
}
