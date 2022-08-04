package com.gotriva.testing.antifa.handler.impl;

import com.gotriva.testing.antifa.exception.PageObjectActionException;
import com.gotriva.testing.antifa.execution.ExecutionContext;
import com.gotriva.testing.antifa.handler.PageObjectActionHandler;
import java.net.MalformedURLException;
import java.net.URL;

/** This class implements a page object handler for open page action. */
public class OpenPageHandler implements PageObjectActionHandler {

  /** Default constructor */
  OpenPageHandler() {}

  @Override
  public String getAction() {
    return "open";
  }

  @Override
  public void handle(ExecutionContext context, String page, String url) {
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
