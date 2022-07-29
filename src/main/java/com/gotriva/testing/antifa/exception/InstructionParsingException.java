package com.gotriva.testing.antifa.exception;

/** Exception that wraps errors on instructions parsing execution. */
public class InstructionParsingException extends RuntimeException {

  public InstructionParsingException(String message) {
    super(message);
  }

  public InstructionParsingException(String message, Exception ex) {
    super(message, ex);
  }
}
