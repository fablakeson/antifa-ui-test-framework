package com.gotriva.nlp.antifa.strategy;

import com.gotriva.nlp.antifa.execution.ExecutionContext;

/** This interface represents a hadler that manipulates the page objects actions. */
public interface PageObjectActionStrategy extends ActionStrategy {

  /**
   * Handles the current execution page actions.
   *
   * @param context the execution context.
   * @param object the command object.
   * @param parameter the command parameter.
   * @param type the command type.
   */
  void perform(ExecutionContext context, String object, String parameter, String type);

  @Override
  public default boolean isReplaceable() {
    /** page action objects doesn't need to be replaced. */
    return false;
  }
}
