package com.gotriva.testing.antifa.exception;

public class InteractableTypeNotFoundException extends RuntimeException {

  private static final String MESSAGE =
      "Interactible type '%s' not match the registered types."
          + " Check the registered types on setup.";

  /**
   * @param typeName
   */
  public InteractableTypeNotFoundException(String typeName) {
    super(MESSAGE);
  }
}
