package com.gotriva.nlp.antifa.exception;

/** Exception that wraps errors on page object action executions. */
public class PageObjectActionException extends RuntimeException {

  public PageObjectActionException(String message, Exception ex) {
    super(message, ex);
  }
}
