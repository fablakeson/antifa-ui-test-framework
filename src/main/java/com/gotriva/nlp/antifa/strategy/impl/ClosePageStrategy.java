package com.gotriva.nlp.antifa.strategy.impl;

import com.gotriva.nlp.antifa.execution.ExecutionContext;
import com.gotriva.nlp.antifa.strategy.PageObjectActionStrategy;

/** This class implements a page object strategy for close page action. */
public class ClosePageStrategy implements PageObjectActionStrategy {

  /** Default constructor */
  ClosePageStrategy() {}

  @Override
  public String getAction() {
    return "close";
  }

  @Override
  public void perform(ExecutionContext context, String object, String parameter, String type) {
    context.closeCurrentPage();
  }
}
