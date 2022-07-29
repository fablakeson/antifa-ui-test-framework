package com.gotriva.testing.antifa.exception;

import edu.stanford.nlp.semgraph.SemanticGraph;

/** This class represents exceptions ocourred during {@link SemanticGraph} interpretation. */
public class InterpretationException extends RuntimeException {

  public InterpretationException(String message) {
    super(message);
  }
}
