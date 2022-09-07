package com.gotriva.nlp.antifa.exception;

/** This class wraps exceptions on action handle. */
public class ActionHandleException extends RuntimeException {

  public ActionHandleException(String message) {
    super(message);
  }

  public ActionHandleException(String message, Exception ex) {
    super(message, ex);
  }
}
