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
    "storeInstructions",
    "assertInstructions",
    "waitInstructions"
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
        Command.builder().setAction("click").setType("button").setObject("#objectA").build();
    return Stream.of(
        /** Single click */
        Arguments.of("click on the #objectA button.", expectedCommand),
        Arguments.of("click the #objectA button.", expectedCommand),
        Arguments.of("click on #objectA button.", expectedCommand),
        Arguments.of("click #objectA button.", expectedCommand));
  }

  /** Check instructions for test. */
  static Stream<Arguments> checkInstructions() {
    final Command expectedCommand =
        Command.builder().setAction("check").setType("radio").setObject("#objectA").build();
    return Stream.of(
        /** Single check */
        Arguments.of("check on the #objectA radio.", expectedCommand),
        Arguments.of("check the #objectA radio.", expectedCommand),
        Arguments.of("check on #objectA radio.", expectedCommand),
        Arguments.of("check #objectA radio.", expectedCommand));
  }

  /** Uncheck instructions for test. */
  static Stream<Arguments> uncheckInstructions() {
    final Command expectedCommand =
        Command.builder().setAction("uncheck").setType("checkbox").setObject("#objectA").build();
    return Stream.of(
        /** Single uncheck */
        Arguments.of("uncheck on the #objectA checkbox.", expectedCommand),
        Arguments.of("uncheck the #objectA checkbox.", expectedCommand),
        Arguments.of("uncheck on #objectA checkbox.", expectedCommand),
        Arguments.of("uncheck #objectA checkbox.", expectedCommand));
  }

  /** Hover instructions for test. */
  static Stream<Arguments> hoverInstructions() {
    final Command expectedCommand =
        Command.builder().setAction("hover").setType("image").setObject("#objectA").build();
    return Stream.of(
        /** Single hover */
        Arguments.of("hover on the #objectA image.", expectedCommand),
        Arguments.of("hover the #objectA image.", expectedCommand),
        Arguments.of("hover on #objectA image.", expectedCommand),
        Arguments.of("hover #objectA image.", expectedCommand));
  }

  /** Define instructions for test. */
  static Stream<Arguments> defineInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setAction("define")
            .setObject("#objectA")
            .setParameter(String.join(DEFAULT_SEPARATOR, "#param3", "#param2"))
            .build();
    return Stream.of(
        /** Single define */
        Arguments.of("define #objectA as #param2 located by #param3.", expectedCommand));
  }

  /** Close instructions for test. */
  static Stream<Arguments> closeInstructions() {
    final Command expectedCommand =
        Command.builder().setAction("close").setObject("login").setType("page").build();
    return Stream.of(
        /** Single close */
        Arguments.of("close the login page.", expectedCommand),
        Arguments.of("close login page.", expectedCommand));
  }

  /** Open instructions for test. */
  static Stream<Arguments> openInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setAction("open")
            .setObject("home")
            .setParameter("#paramA")
            .setType("page")
            .build();
    return Stream.of(
        /** Single open */
        Arguments.of("open the home page at #paramA.", expectedCommand),
        Arguments.of("open home page at #paramA.", expectedCommand));
  }

  /** Read instructions for test. */
  static Stream<Arguments> readInstructions() {
    final Command expectedCommand =
        Command.builder().setAction("read").setObject("#objectA").setParameter("#paramA").build();
    return Stream.of(
        /** Single read */
        Arguments.of("read #paramA on #objectA input.", expectedCommand));
  }

  /** Scroll instructions for test. */
  static Stream<Arguments> scrollInstructions() {
    final Command expectedScrollDownCommand =
        Command.builder().setAction("scroll").setObject("page").setParameter("down").build();
    final Command expectedScrollUpCommand =
        Command.builder().setAction("scroll").setObject("page").setParameter("up").build();
    return Stream.of(
        /** Single scroll */
        Arguments.of("scroll down the page.", expectedScrollDownCommand),
        Arguments.of("scroll up the page.", expectedScrollUpCommand));
  }

  /** Set instructions for test. */
  static Stream<Arguments> setInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setAction("set")
            .setObject("#objectA")
            .setParameter("#paramA")
            .setType("range")
            .build();
    return Stream.of(
        /** Single set */
        Arguments.of("set #objectA range to #paramA.", expectedCommand),
        Arguments.of("set the #objectA range to #paramA.", expectedCommand));
  }

  /** Upload instructions for test. */
  static Stream<Arguments> uploadInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setAction("upload")
            .setObject("#objectA")
            .setParameter("#paramA")
            .setType("file")
            .build();
    return Stream.of(
        /** Single upload */
        Arguments.of("upload #paramA to the #objectA file.", expectedCommand),
        Arguments.of("upload #paramA to #objectA file.", expectedCommand));
  }

  /** Write instructions for test. */
  static Stream<Arguments> writeInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setAction("write")
            .setObject("#objectB")
            .setParameter("#paramA")
            .setType("input")
            .build();
    return Stream.of(
        /** Single set */
        Arguments.of("write #paramA to #objectB input.", expectedCommand),
        Arguments.of("write #paramA to the #objectB input.", expectedCommand));
  }

  static Stream<Arguments> selectInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setAction("select")
            .setObject("#objectA")
            .setParameter("#paramA")
            .setType("list")
            .build();
    return Stream.of(
        /** Single select */
        Arguments.of("select #paramA on #objectA list.", expectedCommand),
        Arguments.of("select #paramA on the #objectA list.", expectedCommand));
  }

  static Stream<Arguments> storeInstructions() {
    final Command expectedCommand =
        Command.builder().setAction("store").setObject("#objectA").setParameter("#paramA").build();
    return Stream.of(
        /** Single store */
        Arguments.of("store #objectA input value on #paramA.", expectedCommand),
        Arguments.of("store #objectA display value on #paramA.", expectedCommand),
        Arguments.of("store #objectA textarea value on #paramA.", expectedCommand),
        Arguments.of("store #objectA label value on #paramA.", expectedCommand),
        Arguments.of("store #objectA message value on #paramA.", expectedCommand));
  }

  static Stream<Arguments> assertInstructions() {
    final Command expectedCommand =
        Command.builder()
            .setAction("assert")
            .setParameter(String.join(DEFAULT_SEPARATOR, "#param2", "#paramA", "equals"))
            .build();
    return Stream.of(
        /** Single assert */
        Arguments.of("assert #paramA equals to #param2.", expectedCommand));
  }

  static Stream<Arguments> waitInstructions() {
    final Command.Builder baseCommand = Command.builder().setAction("wait");
    return Stream.of(
        /** Single assert */
        Arguments.of(
            "wait 1 millisecond.",
            baseCommand.setParameter(String.join(DEFAULT_SEPARATOR, "1", "millisecond")).build()),
        Arguments.of(
            "wait 300 milliseconds.",
            baseCommand
                .setParameter(String.join(DEFAULT_SEPARATOR, "300", "milliseconds"))
                .build()),
        Arguments.of(
            "wait 1 second.",
            baseCommand.setParameter(String.join(DEFAULT_SEPARATOR, "1", "second")).build()),
        Arguments.of(
            "wait 15 seconds.",
            baseCommand.setParameter(String.join(DEFAULT_SEPARATOR, "15", "seconds")).build()),
        Arguments.of(
            "wait 1 minute.",
            baseCommand.setParameter(String.join(DEFAULT_SEPARATOR, "1", "minute")).build()),
        Arguments.of(
            "wait 2 minutes.",
            baseCommand.setParameter(String.join(DEFAULT_SEPARATOR, "2", "minutes")).build()));
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
