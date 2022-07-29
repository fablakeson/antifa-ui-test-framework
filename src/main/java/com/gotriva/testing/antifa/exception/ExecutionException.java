package com.gotriva.testing.antifa.exception;

/** This class represents an exception ocourred during {@link Executor} execution. */
public class ExecutionException extends RuntimeException {

  public ExecutionException(String message, Exception ex) {
    super(message, ex);
  }

  public ExecutionException(String message) {
    super(message);
  }
}
