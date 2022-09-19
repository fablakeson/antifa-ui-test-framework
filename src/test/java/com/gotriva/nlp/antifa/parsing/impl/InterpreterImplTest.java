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
  public void beforeEach() {
    interpreter = new InterpreterImpl();
  }

  @ParameterizedTest
  @MethodSource({
    "clickInstructions",
    "checkInstructions",
    "uncheckInstructions",
    "hoverInstructions",
    "defineInstructions",
    "closeInstructions",
    "openInstructions",
    "readInstructions",
    "scrollInstructions"
  })
  public void testInterpret_withInstruction_thenReturnCommand(
      String instruction, Command expecteCommand) {
    SemanticGraph context = semanticGraph(instruction);

    Command command = interpreter.intepret(context);

    assertEquals(expecteCommand, command);
  }

  /** Click instructions for test. */
  static Stream<Arguments> clickInstructions() {
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
  static Stream<Arguments> checkInstructions() {
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
  static Stream<Arguments> uncheckInstructions() {
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

  /** Hover instructions for test. */
  static Stream<Arguments> hoverInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setInstruction(null)
            .setAction("hover")
            .setType("image")
            .setObject("#param1")
            .build();
    return Stream.of(
        /** Single hover */
        Arguments.of("hover on the #param1 image.", expectedCommand),
        Arguments.of("hover the #param1 image.", expectedCommand),
        Arguments.of("hover on #param1 image.", expectedCommand),
        Arguments.of("hover #param1 image.", expectedCommand));
  }

  /** Define instructions for test. */
  static Stream<Arguments> defineInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setInstruction(null)
            .setAction("define")
            .setType("#param3")
            .setObject("#param1")
            .setParameter("#param2")
            .build();
    return Stream.of(
        /** Single define */
        Arguments.of("define #param1 as #param2 located by #param3.", expectedCommand));
  }

  /** Close instructions for test. */
  static Stream<Arguments> closeInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setInstruction(null)
            .setAction("close")
            .setObject("login")
            .setType("page")
            .build();
    return Stream.of(
        /** Single close */
        Arguments.of("close the login page.", expectedCommand),
        Arguments.of("close login page.", expectedCommand));
  }

  /** Open instructions for test. */
  static Stream<Arguments> openInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setInstruction(null)
            .setAction("open")
            .setObject("home")
            .setParameter("#param1")
            .setType("page")
            .build();
    return Stream.of(
        /** Single open */
        Arguments.of("open the home page at #param1.", expectedCommand),
        Arguments.of("open home page at #param1.", expectedCommand));
  }

  /** Read instructions for test. */
  static Stream<Arguments> readInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setInstruction(null)
            .setAction("read")
            .setObject("page")
            .setParameter("#param1")
            .build();
    return Stream.of(
        /** Single read */
        Arguments.of("read #param1 on the page.", expectedCommand),
        Arguments.of("read #param1 on page.", expectedCommand));
  }

  /** Scroll instructions for test. */
  static Stream<Arguments> scrollInstructions() {
    final Command expectedScrollDownCommand =
        Command.builder()
            .setInstruction(null)
            .setAction("scroll")
            .setObject("page")
            .setParameter("down")
            .build();
    final Command expectedScrollUpCommand =
        Command.builder()
            .setInstruction(null)
            .setAction("scroll")
            .setObject("page")
            .setParameter("up")
            .build();
    return Stream.of(
        /** Single scroll */
        Arguments.of("scroll down the page.", expectedScrollDownCommand),
        Arguments.of("scroll up the page.", expectedScrollUpCommand));
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
