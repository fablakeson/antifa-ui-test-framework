package com.gotriva.testing.antifa.handler.impl;

import com.gotriva.testing.antifa.exception.PageObjectActionException;
import com.gotriva.testing.antifa.handler.PageObjectActionHandler;
import com.gotriva.testing.antifa.model.ExecutionContext;
import java.net.MalformedURLException;
import java.net.URL;

/** his class implements a page object handler for open page action. */
public class OpenPageHandler implements PageObjectActionHandler {

  @Override
  public String getAction() {
    return "open";
  }

  @Override
  public void handle(ExecutionContext context, String page, String url) {
    /** Check if URL is given */
    if (url != null) {
      try {
        context.openPage(page, new URL(url.replaceAll("'", "")));
      } catch (MalformedURLException e) {
        throw new PageObjectActionException("Error opening the page: " + page, e);
      }
    } else {
      context.openPage(page);
    }
  }
}
