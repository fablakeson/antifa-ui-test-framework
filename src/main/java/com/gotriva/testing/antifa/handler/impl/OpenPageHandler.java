package com.gotriva.testing.antifa.handler.impl;

import com.gotriva.testing.antifa.handler.PageObjectHandler;
import com.gotriva.testing.antifa.model.ExecutionContext;
import java.net.MalformedURLException;
import java.net.URL;

/** his class implements a page object handler for open page action. */
public class OpenPageHandler implements PageObjectHandler {

  @Override
  public void handle(ExecutionContext context, String page, String url) {
    /** Check if URL is given */
    if (url != null) {
      try {
        context.openPage(page, new URL(url.replaceAll("'", "")));
      } catch (MalformedURLException e) {
        // TODO: add appropriated exception type.
        throw new RuntimeException("URL is malformed: " + url);
      }
    } else {
      context.openPage(page);
    }
  }

  @Override
  public String getAction() {
    return "open";
  }
}
