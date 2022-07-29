package com.gotriva.testing.antifa.handler.impl;

import com.gotriva.testing.antifa.handler.PageObjectHandler;
import com.gotriva.testing.antifa.model.ExecutionContext;

/** This class implements a page object handler for close page action. */
public class ClosePageHandler implements PageObjectHandler {

  @Override
  public void handle(ExecutionContext context, String page, String parameter) {
    context.closeCurrentPage();
  }

  @Override
  public String getAction() {
    return "close";
  }
}
