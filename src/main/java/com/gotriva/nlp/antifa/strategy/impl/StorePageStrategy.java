package com.gotriva.nlp.antifa.strategy.impl;

import com.gotriva.nlp.antifa.element.Interactable;
import com.gotriva.nlp.antifa.element.Readable;
import com.gotriva.nlp.antifa.execution.ExecutionContext;
import com.gotriva.nlp.antifa.strategy.PageObjectActionStrategy;

/** This class implements a page object strategy for store page action. */
public class StorePageStrategy implements PageObjectActionStrategy {

  @Override
  public String getAction() {
    return "store";
  }

  /** Store object value on parameter at execution context. */
  @Override
  public void perform(ExecutionContext context, String object, String parameter) {
    Interactable interactable = context.getCurrentPage().getElement(object);
    assert interactable instanceof Readable
        : String.format("Element '%s' is not 'Readable'.", object);
    Readable readable = (Readable) interactable;
    context.setParameter(parameter, readable.read());
  }

  @Override
  public boolean isPrintable() {
    return false;
  }
}
