package com.gotriva.testing.antifa.strategy.impl;

import com.gotriva.testing.antifa.execution.ExecutionContext;
import com.gotriva.testing.antifa.strategy.PageObjectActionStrategy;

/** This class implements a page object strategy for close page action. */
public class ClosePageStrategy implements PageObjectActionStrategy {

  /** Default constructor */
  ClosePageStrategy() {}

  @Override
  public String getAction() {
    return "close";
  }

  @Override
  public void perform(ExecutionContext context, String page, String parameter) {
    context.closeCurrentPage();
  }
}
