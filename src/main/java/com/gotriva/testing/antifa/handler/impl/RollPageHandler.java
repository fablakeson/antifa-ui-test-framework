package com.gotriva.testing.antifa.handler.impl;

import com.gotriva.testing.antifa.handler.PageObjectHandler;
import com.gotriva.testing.antifa.model.ExecutionContext;

public class RollPageHandler implements PageObjectHandler {

  // TODO: implement roll page handler.

  @Override
  public String getAction() {
    return "roll";
  }

  @Override
  public void handle(ExecutionContext context, String page, String parameter) {}

  /** Moves the page scroll down. */
  private void scrollDown() {}

  /** Moves the page scroll up. */
  private void scrollUp() {}
}
