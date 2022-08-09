package com.gotriva.testing.antifa.strategy;

import com.gotriva.testing.antifa.execution.ExecutionContext;

/** This interface represents a hadler that manipulates the page objects actions. */
public interface PageObjectActionStrategy extends ActionStrategy {

  /**
   * Handles the current execution page actions.
   *
   * @param context the execution context
   */
  void perform(ExecutionContext context, String page, String parameter);
}
