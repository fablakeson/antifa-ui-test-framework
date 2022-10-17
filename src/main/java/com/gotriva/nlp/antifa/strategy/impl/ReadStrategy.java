package com.gotriva.nlp.antifa.strategy.impl;

import com.gotriva.nlp.antifa.element.Readable;
import com.gotriva.nlp.antifa.strategy.InteractableActionStrategy;

/** This class implements a page object strategy for define element metadata action. */
public class ReadStrategy implements InteractableActionStrategy<Readable> {

  @Override
  public String getAction() {
    return "read";
  }

  @Override
  public void perform(Readable readable, String expectedText) {
    String readText = readable.read();
    assert expectedText.equals(readText)
        : String.format(
            "Text '%s' was expected but '%s' was found on element.", expectedText, readText);
  }
}
