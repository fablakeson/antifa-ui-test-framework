package com.gotriva.nlp.antifa.parsing.impl;

import static com.gotriva.nlp.antifa.model.Command.ComponentType.COMMAND;
import static com.gotriva.nlp.antifa.model.Command.ComponentType.OBJECT;
import static com.gotriva.nlp.antifa.model.Command.ComponentType.PARAMETER;
import static com.gotriva.nlp.antifa.model.Command.ComponentType.TYPE;
import static com.gotriva.nlp.antifa.model.Step.builder;
import static edu.stanford.nlp.trees.ud.UniversalGrammaticalRelations.COMPOUND_MODIFIER;
import static edu.stanford.nlp.trees.ud.UniversalGrammaticalRelations.DIRECT_OBJECT;
import static edu.stanford.nlp.trees.ud.UniversalGrammaticalRelations.OBLIQUE_MODIFIER;

import com.google.common.collect.ImmutableList;
import com.gotriva.nlp.antifa.exception.InterpretationException;
import com.gotriva.nlp.antifa.model.Command;
import com.gotriva.nlp.antifa.model.SemanticPath;
import com.gotriva.nlp.antifa.parsing.Interpreter;
import edu.stanford.nlp.semgraph.SemanticGraph;
import java.util.List;
import java.util.Optional;

/** This class implements the {@link Interpreter}. */
public class InterpreterImpl implements Interpreter {

  // TODO: Test the semantic paths

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
              .newStep(builder().from(COMMAND).with(DIRECT_OBJECT).to(TYPE))
              /** TYPE -- compound --> OBJECT */
              .newStep(builder().from(TYPE).with(COMPOUND_MODIFIER).to(OBJECT))
              /** OBJECT -- compound --> OBJECT */
              .newStep(builder().from(OBJECT).with(COMPOUND_MODIFIER).to(OBJECT))
              /** COMMAND -- obl --> PARAMETER */
              .newStep(builder().from(COMMAND).with(OBLIQUE_MODIFIER).to(PARAMETER))
              /** Ex: open the login page at theUrl. */
              .build(),
          SemanticPath.builder()
              /** COMMAND -- obj --> PARAMETER */
              .newStep(builder().from(COMMAND).with(DIRECT_OBJECT).to(PARAMETER))
              /** COMMAND -- obl --> TYPE */
              .newStep(builder().from(COMMAND).with(OBLIQUE_MODIFIER).to(TYPE))
              /** TYPE -- compound --> OBJECT */
              .newStep(builder().from(TYPE).with(COMPOUND_MODIFIER).to(OBJECT))
              /** OBJECT -- compound --> OBJECT */
              .newStep(builder().from(OBJECT).with(COMPOUND_MODIFIER).to(OBJECT))
              /** Ex: write passwordText to the passoword input */
              .build(),

          /** Actions with parameter + type */
          SemanticPath.builder()
              /** COMMAND -- obj --> TYPE */
              .newStep(builder().from(COMMAND).with(DIRECT_OBJECT).to(TYPE))
              /** TYPE -- compound --> OBJECT */
              .newStep(builder().from(TYPE).with(COMPOUND_MODIFIER).to(OBJECT))
              /** COMMAND -- obl --> PARAMETER */
              .newStep(builder().from(COMMAND).with(OBLIQUE_MODIFIER).to(PARAMETER))
              /** Ex: open the login page at theUrl. */
              .build(),
          SemanticPath.builder()
              /** COMMAND -- obj --> PARAMETER */
              .newStep(builder().from(COMMAND).with(DIRECT_OBJECT).to(PARAMETER))
              /** COMMAND -- obl --> TYPE */
              .newStep(builder().from(COMMAND).with(OBLIQUE_MODIFIER).to(TYPE))
              /** TYPE -- compound --> OBJECT */
              .newStep(builder().from(TYPE).with(COMPOUND_MODIFIER).to(OBJECT))
              /** Ex: write passwordText to the passoword input */
              .build(),

          /** Actions with parameter and no type */
          SemanticPath.builder()
              /** COMMAND -- obj --> PARAMETER */
              .newStep(builder().from(COMMAND).with(DIRECT_OBJECT).to(PARAMETER))
              /** PARAMETER -- obj --> OBJECT */
              .newStep(builder().from(PARAMETER).with(COMPOUND_MODIFIER).to(OBJECT))
              /** Ex: write the passoword passwordText */
              .build(),

          /** Actions with type without paramter */
          SemanticPath.builder()
              /** COMMAND -- obl --> TYPE */
              .newStep(builder().from(COMMAND).with(OBLIQUE_MODIFIER).to(TYPE))
              /** TYPE -- compound --> OBJECT */
              .newStep(builder().from(TYPE).with(COMPOUND_MODIFIER).to(OBJECT))
              /** Ex: click on the login button */
              .build(),

          /** Actions without type and parameter */
          SemanticPath.builder()
              /** COMMAND -- obl --> OBJECT */
              .newStep(builder().from(COMMAND).with(OBLIQUE_MODIFIER).to(OBJECT))
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
        .map(path -> path.traverse(context))
        .filter(Optional::isPresent)
        .findFirst()
        .map(Optional::get)
        .orElseThrow(
            () ->
                new InterpretationException(
                    "No interpretations found for given context: " + context));
  }
}
