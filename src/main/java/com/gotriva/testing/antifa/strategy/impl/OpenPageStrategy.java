package com.gotriva.testing.antifa.strategy.impl;

import com.gotriva.testing.antifa.exception.PageObjectActionException;
import com.gotriva.testing.antifa.execution.ExecutionContext;
import com.gotriva.testing.antifa.strategy.PageObjectActionStrategy;
import java.net.MalformedURLException;
import java.net.URL;

/** This class implements a page object strategy for open page action. */
public class OpenPageStrategy implements PageObjectActionStrategy {

  /** Default constructor */
  OpenPageStrategy() {}

  @Override
  public String getAction() {
    return "open";
  }

  @Override
  public void perform(ExecutionContext context, String page, String url) {
    /** Check if URL is given */
    if (url != null) {
      try {
        context.openPage(page, new URL(url));
      } catch (MalformedURLException e) {
        throw new PageObjectActionException("Error opening the page: " + page, e);
      }
    } else {
      context.openPage(page);
    }
  }
}