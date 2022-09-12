package com.gotriva.nlp.antifa.parsing.impl;

import static com.gotriva.nlp.antifa.constants.PipelinePropertiesConstants.ANNOTATORS;
import static com.gotriva.nlp.antifa.constants.PipelinePropertiesConstants.ANNOTATORS_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gotriva.nlp.antifa.model.Command;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import java.util.Properties;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** Tests for {@link InterpreterImpl} class. */
public class InterpreterImplTest {

  private static final StanfordCoreNLP PIPELINE =
      new StanfordCoreNLP(
          new Properties() {
            {
              put(ANNOTATORS, ANNOTATORS_VALUE);
            }
          });

  /** Test subject */
  private InterpreterImpl interpreter;

  @BeforeEach
  public void before() {
    interpreter = new InterpreterImpl();
  }

  @ParameterizedTest
  @MethodSource({"clickInstructions", "checkInstructions", "uncheckInstructions"})
  public void testInterpret_withInstruction_thenReturnCommand(
      String instruction, Command expecteCommand) {
    SemanticGraph context = semanticGraph(instruction);

    Command command = interpreter.intepret(context);

    assertEquals(expecteCommand, command);
  }

  /** Click instructions for test. */
  public static Stream<Arguments> clickInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setInstruction(null)
            .setAction("click")
            .setType("button")
            .setObject("#param1")
            .build();
    return Stream.of(
        /** Single click */
        Arguments.of("click on the #param1 button.", expectedCommand),
        Arguments.of("click the #param1 button.", expectedCommand),
        Arguments.of("click on #param1 button.", expectedCommand),
        Arguments.of("click #param1 button.", expectedCommand));
  }

  /** Check instructions for test. */
  public static Stream<Arguments> checkInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setInstruction(null)
            .setAction("check")
            .setType("radio")
            .setObject("#param1")
            .build();
    return Stream.of(
        /** Single check */
        Arguments.of("check on the #param1 radio.", expectedCommand),
        Arguments.of("check the #param1 radio.", expectedCommand),
        Arguments.of("check on #param1 radio.", expectedCommand),
        Arguments.of("check #param1 radio.", expectedCommand));
  }

  /** Uncheck instructions for test. */
  public static Stream<Arguments> uncheckInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setInstruction(null)
            .setAction("uncheck")
            .setType("checkbox")
            .setObject("#param1")
            .build();
    return Stream.of(
        /** Single uncheck */
        Arguments.of("uncheck on the #param1 checkbox.", expectedCommand),
        Arguments.of("uncheck the #param1 checkbox.", expectedCommand),
        Arguments.of("uncheck on #param1 checkbox.", expectedCommand),
        Arguments.of("uncheck #param1 checkbox.", expectedCommand));
  }

  private static SemanticGraph semanticGraph(String instruction) {
    Annotation annotation = new Annotation(instruction);
    PIPELINE.annotate(annotation);
    return annotation
        .get(CoreAnnotations.SentencesAnnotation.class)
        .get(0)
        .get(BasicDependenciesAnnotation.class);
  }
}
