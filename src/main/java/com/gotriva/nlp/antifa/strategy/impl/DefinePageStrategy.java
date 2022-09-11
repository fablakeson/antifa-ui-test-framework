package com.gotriva.nlp.antifa.strategy.impl;

import com.gotriva.nlp.antifa.element.ElementMetadata;
import com.gotriva.nlp.antifa.execution.ExecutionContext;
import com.gotriva.nlp.antifa.strategy.PageObjectActionStrategy;
import org.openqa.selenium.By;

/** This class implements a page object strategy for define element metadata action. */
public class DefinePageStrategy implements PageObjectActionStrategy {

  @Override
  public String getAction() {
    return "define";
  }

  @Override
  public void perform(ExecutionContext context, String element, String label, String locator) {
    context.setMetadata(element, ElementMetadata.of(label, By.cssSelector(locator)));
  }

  @Override
  public boolean isPrintable() {
    /** Should not print define as it not changes the page. */
    return false;
  }

  @Override
  public boolean isReplaceable() {
    /** Should not replace this action strategy instruction. */
    return false;
  }
}
