package com.gotriva.nlp.antifa.exception;

/** This class wraps an exception during the presentation phase. */
public class PresentationException extends RuntimeException {

  public PresentationException(String message, Exception ex) {
    super(message, ex);
  }

  public PresentationException(String message) {
    super(message);
  }
}
