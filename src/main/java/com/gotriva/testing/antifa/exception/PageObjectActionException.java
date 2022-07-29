package com.gotriva.testing.antifa.exception;

/** Exception that wraps errors on page object action executions. */
public class PageObjectActionException extends RuntimeException {

  public PageObjectActionException(String message, Throwable ex) {
    super(message, ex);
  }
}
