package com.gotriva.nlp.antifa.strategy.impl;

import com.gotriva.nlp.antifa.constants.DefaultConstants;
import com.gotriva.nlp.antifa.element.Readable;
import com.gotriva.nlp.antifa.execution.ExecutionContext;
import com.gotriva.nlp.antifa.strategy.PageObjectActionStrategy;

/** This class implements a page object strategy for assert page action. */
public class AssertPageStrategy implements PageObjectActionStrategy {

  @Override
  public String getAction() {
    return "assert";
  }

  @Override
  public void perform(ExecutionContext context, String object, String parameter) {
    final String[] parameters = parameter.split(DefaultConstants.DEFAULT_SEPARATOR);
    final String probableValue = parameters[0];
    final String expectedValue = parameters[1];
    final String operation = parameters[2];
    final String value;

    switch (probableValue.charAt(0)) {
      case '$': // Parameter
        value = context.getParameter(probableValue).toString();
        break;
      case '#': // Element
        value = ((Readable) context.getCurrentPage().getElement(probableValue)).read();
        break;
      default: // Literal
        value = probableValue;
    }

    switch (operation) {
      case "equals":
        if (!expectedValue.equals(value)) {
          throw new RuntimeException(
              String.format("Expected value '%s' but '%s' was found.", expectedValue, value));
        }
        break;
      default:
        throw new IllegalArgumentException(
            String.format("Unsupported assert operation '%s'", operation));
    }
  }

  @Override
  public boolean isPrintable() {
    return false;
  }
}
