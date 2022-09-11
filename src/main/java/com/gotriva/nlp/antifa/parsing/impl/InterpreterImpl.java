package com.gotriva.nlp.antifa.parsing.impl;

import static com.gotriva.nlp.antifa.model.Command.ComponentType.ACTION;
import static com.gotriva.nlp.antifa.model.Command.ComponentType.BYPASS;
import static com.gotriva.nlp.antifa.model.Command.ComponentType.OBJECT;
import static com.gotriva.nlp.antifa.model.Command.ComponentType.PARAMETER;
import static com.gotriva.nlp.antifa.model.Command.ComponentType.TYPE;
import static com.gotriva.nlp.antifa.model.Step.builder;
import static edu.stanford.nlp.trees.ud.UniversalGrammaticalRelations.CLAUSAL_MODIFIER;
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
import java.util.function.Supplier;

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

          /** Actions with parameter + type */
          SemanticPath.builder()
              /** ACTION -- obj --> OBJECT */
              .newStep(builder().from(ACTION).with(DIRECT_OBJECT).to(OBJECT))
              /** ACTION -- obl --> PARAMETER */
              .newStep(builder().from(ACTION).with(OBLIQUE_MODIFIER).to(PARAMETER))
              /** PARAMETER -- acl --> BYPASS */
              .newStep(builder().from(PARAMETER).with(CLAUSAL_MODIFIER).to(BYPASS))
              /** BYPASS -- obl --> TYPE */
              .newStep(builder().from(BYPASS).with(OBLIQUE_MODIFIER).to(TYPE))
              /** Ex: Define @submit as "Submit" located by "input[type='submit']". */
              .build(),
          SemanticPath.builder()
              /** ACTION -- obj --> TYPE */
              .newStep(builder().from(ACTION).with(DIRECT_OBJECT).to(TYPE))
              /** TYPE -- compound --> OBJECT */
              .newStep(builder().from(TYPE).with(COMPOUND_MODIFIER).to(OBJECT))
              /** ACTION -- obl --> PARAMETER */
              .newStep(builder().from(ACTION).with(OBLIQUE_MODIFIER).to(PARAMETER))
              /** Ex: Open the login page at "http://www.google.com". */
              .build(),
          SemanticPath.builder()
              /** ACTION -- obj --> PARAMETER */
              .newStep(builder().from(ACTION).with(DIRECT_OBJECT).to(PARAMETER))
              /** ACTION -- obl --> TYPE */
              .newStep(builder().from(ACTION).with(OBLIQUE_MODIFIER).to(TYPE))
              /** TYPE -- compound --> OBJECT */
              .newStep(builder().from(TYPE).with(COMPOUND_MODIFIER).to(OBJECT))
              /** Ex: write passwordText to the passoword input */
              .build(),

          /** Actions with parameter and no type */
          SemanticPath.builder()
              /** ACTION -- obj --> PARAMETER */
              .newStep(builder().from(ACTION).with(DIRECT_OBJECT).to(PARAMETER))
              /** PARAMETER -- obj --> OBJECT */
              .newStep(builder().from(PARAMETER).with(COMPOUND_MODIFIER).to(OBJECT))
              /** Ex: write the passoword passwordText */
              .build(),

          /** Actions with type without paramter */
          SemanticPath.builder()
              /** ACTION -- obl --> TYPE */
              .newStep(builder().from(ACTION).with(OBLIQUE_MODIFIER).to(TYPE))
              /** TYPE -- compound --> OBJECT */
              .newStep(builder().from(TYPE).with(COMPOUND_MODIFIER).to(OBJECT))
              /** Ex: click on the login button */
              .build(),

          /** Actions without type and parameter */
          SemanticPath.builder()
              /** ACTION -- obl --> OBJECT */
              .newStep(builder().from(ACTION).with(OBLIQUE_MODIFIER).to(OBJECT))
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
        .orElseThrow(interpretationException(context));
  }

  private Supplier<InterpretationException> interpretationException(SemanticGraph context) {
    return () ->
        new InterpretationException("No interpretations found for given context:\n" + context);
  }
}
