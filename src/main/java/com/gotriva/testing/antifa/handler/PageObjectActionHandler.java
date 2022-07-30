package com.gotriva.testing.antifa.handler;

import com.gotriva.testing.antifa.execution.ExecutionContext;

/**
 * This interface represents a hadler that manipulates the page objects actions.
 */
public interface PageObjectActionHandler extends ActionHandler {

  /**
   * Handles the current execution page actions.
   *
   * @param context the execution context
   */
  void handle(ExecutionContext context, String page, String parameter);
}
