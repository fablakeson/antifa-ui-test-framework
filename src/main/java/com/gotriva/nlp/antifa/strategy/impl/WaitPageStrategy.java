package com.gotriva.nlp.antifa.strategy.impl;

import com.gotriva.nlp.antifa.constants.DefaultConstants;
import com.gotriva.nlp.antifa.execution.ExecutionContext;
import com.gotriva.nlp.antifa.strategy.PageObjectActionStrategy;

public class WaitPageStrategy implements PageObjectActionStrategy {

  @Override
  public String getAction() {
    return "wait";
  }

  @Override
  public void perform(ExecutionContext context, String object, String parameter) {
    String[] parts = parameter.split(DefaultConstants.DEFAULT_SEPARATOR);
    long amount = Long.parseLong(parts[0]);
    String unit = parts[1];
    long multiplier;
    switch (unit) {
      case "second":
      case "seconds":
        multiplier = 1000;
        break;
      case "mili":
      case "milis":
        multiplier = 1;
        break;
      case "minute":
      case "minutes":
        multiplier = 1000 * 60;
        break;
      default:
        throw new IllegalArgumentException(
            "Unit '"
                + unit
                + "' is not one of [second, seconds, millisecond, milliseconds, minute, minutes].");
    }
    try {
      Thread.sleep(amount * multiplier);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
