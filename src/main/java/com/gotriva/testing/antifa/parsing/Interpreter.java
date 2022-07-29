package com.gotriva.testing.antifa.parsing;

import com.gotriva.testing.antifa.model.Command;
import edu.stanford.nlp.semgraph.SemanticGraph;

/**
 * This class is responsible for identify the semantic actions structures and return the {@link
 * Command} accordingly.
 */
public interface Interpreter {

  /**
   * Interprets the given {@link SemanticGraph} and returns the interpreted {@link Command}.
   *
   * @param context the semantic dependency graph
   * @return the interpreted {@link Command}
   */
  Command intepret(SemanticGraph context);
}
