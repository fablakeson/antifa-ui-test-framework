package com.gotriva.nlp.antifa.parsing.impl;

import static com.gotriva.nlp.antifa.constants.DefaultConstants.DEFAULT_SEPARATOR;
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
    "scrollInstructions",
    "setInstructions",
    "uploadInstructions",
    "writeInstructions",
    "selectInstructions",
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
            .setObject("#object1")
            .build();
    return Stream.of(
        /** Single click */
        Arguments.of("click on the #object1 button.", expectedCommand),
        Arguments.of("click the #object1 button.", expectedCommand),
        Arguments.of("click on #object1 button.", expectedCommand),
        Arguments.of("click #object1 button.", expectedCommand));
  }

  /** Check instructions for test. */
  static Stream<Arguments> checkInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setInstruction(null)
            .setAction("check")
            .setType("radio")
            .setObject("#object1")
            .build();
    return Stream.of(
        /** Single check */
        Arguments.of("check on the #object1 radio.", expectedCommand),
        Arguments.of("check the #object1 radio.", expectedCommand),
        Arguments.of("check on #object1 radio.", expectedCommand),
        Arguments.of("check #object1 radio.", expectedCommand));
  }

  /** Uncheck instructions for test. */
  static Stream<Arguments> uncheckInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setInstruction(null)
            .setAction("uncheck")
            .setType("checkbox")
            .setObject("#object1")
            .build();
    return Stream.of(
        /** Single uncheck */
        Arguments.of("uncheck on the #object1 checkbox.", expectedCommand),
        Arguments.of("uncheck the #object1 checkbox.", expectedCommand),
        Arguments.of("uncheck on #object1 checkbox.", expectedCommand),
        Arguments.of("uncheck #object1 checkbox.", expectedCommand));
  }

  /** Hover instructions for test. */
  static Stream<Arguments> hoverInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setInstruction(null)
            .setAction("hover")
            .setType("image")
            .setObject("#object1")
            .build();
    return Stream.of(
        /** Single hover */
        Arguments.of("hover on the #object1 image.", expectedCommand),
        Arguments.of("hover the #object1 image.", expectedCommand),
        Arguments.of("hover on #object1 image.", expectedCommand),
        Arguments.of("hover #object1 image.", expectedCommand));
  }

  /** Define instructions for test. */
  static Stream<Arguments> defineInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setInstruction(null)
            .setAction("define")
            .setObject("#object1")
            .setParameter(String.join(DEFAULT_SEPARATOR, "#param3", "#param2"))
            .build();
    return Stream.of(
        /** Single define */
        Arguments.of("define #object1 as #param2 located by #param3.", expectedCommand));
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
            .setObject("#object1")
            .setParameter("#param1")
            .build();
    return Stream.of(
        /** Single read */
        Arguments.of("read #param1 on #object1 input.", expectedCommand));
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

  /** Set instructions for test. */
  static Stream<Arguments> setInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setInstruction(null)
            .setAction("set")
            .setObject("#object1")
            .setParameter("#param1")
            .setType("range")
            .build();
    return Stream.of(
        /** Single set */
        Arguments.of("set #object1 range to #param1.", expectedCommand),
        Arguments.of("set the #object1 range to #param1.", expectedCommand));
  }

  /** Upload instructions for test. */
  static Stream<Arguments> uploadInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setInstruction(null)
            .setAction("upload")
            .setObject("#object1")
            .setParameter("#param1")
            .setType("file")
            .build();
    return Stream.of(
        /** Single upload */
        Arguments.of("upload #param1 to the #object1 file.", expectedCommand),
        Arguments.of("upload #param1 to #object1 file.", expectedCommand));
  }

  /** Write instructions for test. */
  static Stream<Arguments> writeInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setInstruction(null)
            .setAction("write")
            .setObject("#object1")
            .setParameter("#param1")
            .setType("input")
            .build();
    return Stream.of(
        /** Single set */
        Arguments.of("write #param1 to the #object1 input.", expectedCommand),
        Arguments.of("write #param1 to #object1 input.", expectedCommand));
  }

  static Stream<Arguments> selectInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setInstruction(null)
            .setAction("select")
            .setObject("#object1")
            .setParameter("#param1")
            .setType("list")
            .build();
    return Stream.of(
        /** Single select */
        Arguments.of("select #param1 on #object1 list.", expectedCommand),
        Arguments.of("select #param1 on the #object1 list.", expectedCommand));
  }

  /**
   * Helper function to get a semantic graph from a single instruction.
   *
   * @param instruction the instruction.
   * @return the semantic graph.
   */
  private static SemanticGraph semanticGraph(String instruction) {
    Annotation annotation = new Annotation(instruction);
    PIPELINE.annotate(annotation);
    return annotation
        .get(CoreAnnotations.SentencesAnnotation.class)
        .get(0)
        .get(BasicDependenciesAnnotation.class);
  }
}
