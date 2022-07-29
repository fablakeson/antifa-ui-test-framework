package com.gotriva.testing.antifa.parsing.impl;

import static com.gotriva.testing.antifa.model.Command.ComponentType.COMMAND;
import static com.gotriva.testing.antifa.model.Command.ComponentType.OBJECT;
import static com.gotriva.testing.antifa.model.Command.ComponentType.PARAMETER;
import static com.gotriva.testing.antifa.model.Command.ComponentType.TYPE;
import static edu.stanford.nlp.trees.ud.UniversalGrammaticalRelations.COMPOUND_MODIFIER;
import static edu.stanford.nlp.trees.ud.UniversalGrammaticalRelations.DIRECT_OBJECT;
import static edu.stanford.nlp.trees.ud.UniversalGrammaticalRelations.OBLIQUE_MODIFIER;

import com.google.common.collect.ImmutableList;
import com.gotriva.testing.antifa.exception.InterpretationException;
import com.gotriva.testing.antifa.model.Command;
import com.gotriva.testing.antifa.model.SemanticPath;
import com.gotriva.testing.antifa.parsing.Interpreter;
import edu.stanford.nlp.semgraph.SemanticGraph;
import java.util.List;
import java.util.Objects;

/** This class implements the {@link Interpreter}. */
public class InterpreterImpl implements Interpreter {

  // TODO: Correct the semantic paths

  /** The semantic paths list for {@link SemanticGraph} traversals. */
  private static final List<SemanticPath> SEMANTIC_PATHS =
      ImmutableList.of(
          /**
           * List goes from the most complete actions to the most simple actions. Paths must always
           * be Depth-First traversals.
           */

          /** Actions with parameter + type and composite object names */
          SemanticPath.builder()
              /** COMMAND -- obj --> TYPE */
              .newStep(COMMAND, DIRECT_OBJECT, TYPE)
              /** TYPE -- compound --> OBJECT */
              .newStep(TYPE, COMPOUND_MODIFIER, OBJECT)
              /** OBJECT -- compound --> OBJECT */
              .newStep(OBJECT, COMPOUND_MODIFIER, OBJECT)
              /** COMMAND -- obl --> PARAMETER */
              .newStep(COMMAND, OBLIQUE_MODIFIER, PARAMETER)
              /** Ex: open the login page at theUrl. */
              .build(),
          SemanticPath.builder()
              /** COMMAND -- obj --> PARAMETER */
              .newStep(COMMAND, DIRECT_OBJECT, PARAMETER)
              /** COMMAND -- obl --> TYPE */
              .newStep(COMMAND, OBLIQUE_MODIFIER, TYPE)
              /** TYPE -- compound --> OBJECT */
              .newStep(TYPE, COMPOUND_MODIFIER, OBJECT)
              /** OBJECT -- compound --> OBJECT */
              .newStep(OBJECT, COMPOUND_MODIFIER, OBJECT)
              /** Ex: write passwordText to the passoword input */
              .build(),

          /** Actions with parameter + type */
          SemanticPath.builder()
              /** COMMAND -- obj --> TYPE */
              .newStep(COMMAND, DIRECT_OBJECT, TYPE)
              /** TYPE -- compound --> OBJECT */
              .newStep(TYPE, COMPOUND_MODIFIER, OBJECT)
              /** COMMAND -- obl --> PARAMETER */
              .newStep(COMMAND, OBLIQUE_MODIFIER, PARAMETER)
              /** Ex: open the login page at theUrl. */
              .build(),
          SemanticPath.builder()
              /** COMMAND -- obj --> PARAMETER */
              .newStep(COMMAND, DIRECT_OBJECT, PARAMETER)
              /** COMMAND -- obl --> TYPE */
              .newStep(COMMAND, OBLIQUE_MODIFIER, TYPE)
              /** TYPE -- compound --> OBJECT */
              .newStep(TYPE, COMPOUND_MODIFIER, OBJECT)
              /** Ex: write passwordText to the passoword input */
              .build(),

          /** Actions with parameter and no type */
          SemanticPath.builder()
              /** COMMAND -- obj --> PARAMETER */
              .newStep(COMMAND, DIRECT_OBJECT, PARAMETER)
              /** PARAMETER -- obj --> OBJECT */
              .newStep(PARAMETER, COMPOUND_MODIFIER, OBJECT)
              /** Ex: write the passoword passwordText */
              .build(),

          /** Actions with type without paramter */
          SemanticPath.builder()
              /** COMMAND -- obl --> TYPE */
              .newStep(COMMAND, OBLIQUE_MODIFIER, TYPE)
              /** TYPE -- compound --> OBJECT */
              .newStep(TYPE, COMPOUND_MODIFIER, OBJECT)
              /** Ex: click on the login button */
              .build(),

          /** Actions without type and parameter */
          SemanticPath.builder()
              /** COMMAND -- obl --> OBJECT */
              .newStep(COMMAND, OBLIQUE_MODIFIER, OBJECT)
              /** Ex: click on login */
              .build());

  /** The default constructor. */
  InterpreterImpl() {}

  /**
   * Interprets the given {@link SemanticGraph} and returns the interpreted {@link Command}.
   *
   * @param context the semantic dependency graph
   * @return the interpreted {@link Command}
   * @throws RuntimeException if no interpretation is found.
   */
  public Command intepret(SemanticGraph context) {
    /** Try to interpret dependency graph. */
    return SEMANTIC_PATHS.stream()
        /** Try each semantic path traversal interpretation. */
        .map(path -> tryTraverse(path, context))
        /** Filter the fails */
        .filter(Objects::nonNull)
        /** Get the first succeded interpretation. */
        .findFirst()
        /** Throw an exception if not interpretation found. */
        .orElseThrow(
            () ->
                new InterpretationException(
                    "No interpretations found for given context: " + context));
  }

  /**
   * Tries to traverse the {@link SemanticGraph} using the given {@link SemanticPath}} to interpret
   * the {@link Command}.
   *
   * @param path the interpretation path
   * @param graph the semantic dependency graph
   * @return the interpreted action if complete or null in case of {@link Exception}
   */
  private Command tryTraverse(SemanticPath path, SemanticGraph graph) {
    try {
      return path.traverse(graph);
    } catch (Exception ex) {
      return null;
    }
  }
}
