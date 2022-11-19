package com.gotriva.nlp.antifa.parsing.impl;

import com.gotriva.nlp.antifa.constants.DefaultConstants;
import com.gotriva.nlp.antifa.model.Command;
import com.gotriva.nlp.antifa.parsing.Interpreter;
import com.gotriva.nlp.antifa.parsing.Parser;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This class implements the {@link Parser}. */
public class ParserImpl implements Parser {

  private static final Logger LOGGER = LoggerFactory.getLogger(ParserImpl.class);

  /** Pattern to find quoted parameters. */
  private static final Pattern QUOTE_PATTERN =
      Pattern.compile("\"(.*?)\"|(@[\\w\\d]+)|(\\$[\\w\\d]+)");

  /** Pattern to find parameters replacement. */
  private static final Pattern PARAMETER_PATTERN = Pattern.compile("(#.+?[0-9]{1,})");

  /** The NLP pipeline processor. */
  private final StanfordCoreNLP pipeline;

  /** The semantic graph interpreter. */
  private final Interpreter interpreter;

  /** Default constructor. */
  ParserImpl(StanfordCoreNLP pipeline, Interpreter interpreter) {
    this.pipeline = pipeline;
    this.interpreter = interpreter;
  }

  /**
   * Parses the instructions into a list of commands.
   *
   * @param instructions
   * @return
   */
  public List<Command> parse(List<String> instructions) {
    /** Copy instructions list */
    List<String> instructionsCopy = new ArrayList<>(instructions);

    /** Commands to be returned. */
    List<Command> commands = new LinkedList<>();

    for (int i = 0; i < instructionsCopy.size(); ++i) {
      /** Replace parameters */
      // TODO: refactor replace function to work with a single string.
      Map<String, String> parameters = replace(instructionsCopy.subList(i, i + 1));
      String instruction = instructionsCopy.get(i).toLowerCase();
      /** Creates an annotation from instructions text */
      Annotation annotation = new Annotation(instruction);
      /** Execute pipeline */
      pipeline.annotate(annotation);
      /** Get the first sentence annotation */
      CoreMap sentence = annotation.get(CoreAnnotations.SentencesAnnotation.class).get(0);
      SemanticGraph dependencyParse = sentence.get(BasicDependenciesAnnotation.class);
      LOGGER.debug("dependencyParse: {}", dependencyParse);
      /** Try to interpret the graph as a command */
      Command command = interpreter.intepret(dependencyParse);
      /** Restore command parameters */
      command.setInstruction(StringUtils.capitalize(sentence.toString()));
      command = restore(command, parameters);
      /** Print action */
      LOGGER.debug("command: {}", command);
      /** add action to list */
      commands.add(command);
    }

    return commands;
  }

  // TODO: Remove dead code.
  // /**
  //  * Parses the instructions into a list of commands.
  //  *
  //  * @param instructions
  //  * @return
  //  */
  // public List<Command> parseOld(List<String> instructions) {
  //   /** Copy instructions list */
  //   List<String> instructionsCopy = new ArrayList<>(instructions);
  //   /** Replace parameters */
  //   Map<String, String> parameters = replace(instructionsCopy);
  //   /** Concatenate the instructions into one text. */
  //   String instructionsText = String.join("\n", instructionsCopy);
  //   /** Creates an annotation from instructions text */
  //   Annotation annotation = new Annotation(instructionsText);
  //   /** Execute pipeline */
  //   pipeline.annotate(annotation);
  //   /** Get sentence annotations */
  //   List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
  //   /** Commands to be returned. */
  //   List<Command> commands = new LinkedList<>();
  //   /** Parse the sentences into commands */
  //   for (CoreMap sentence : sentences) {
  //     /** Get semantic dependency graph */
  //     SemanticGraph dependencyParse = sentence.get(BasicDependenciesAnnotation.class);
  //     LOGGER.debug("dependencyParse: {}", dependencyParse);
  //     /** Try to interpret the graph as a command */
  //     Command command = interpreter.intepret(dependencyParse);
  //     /** Restore command parameters */
  //     command.setInstruction(StringUtils.capitalize(sentence.toString()));
  //     command = restore(command, parameters);
  //     /** Print action */
  //     LOGGER.debug("command: {}", command);
  //     /** add action to list */
  //     commands.add(command);
  //   }
  //   return commands;
  // }

  // TODO: Add methods below to a testable helper class.

  /**
   * Restore replaced tokens to original values on command.
   *
   * @param command the command with values to be restored.
   * @param parameters the parameters replacement map.
   * @return the replaced command.
   */
  private Command restore(Command command, Map<String, String> parameters) {
    /** Check if command fields where replaced by parameters. */
    LOGGER.debug("Restoring: {}", command);
    Matcher matcher = PARAMETER_PATTERN.matcher(command.getInstruction());
    if (!matcher.find()) {
      return command;
    }
    return Command.builder()
        .setAction(command.getAction())
        .setParameter(restore(command.getParameter(), parameters))
        .setObject(restore(command.getObject(), parameters))
        .setType(parameters.getOrDefault(command.getType(), command.getType()))
        .setInstruction(restore(matcher, parameters))
        .build();
  }

  /**
   * Restore replaced tokens to original values on parameter.
   *
   * @param parameter the possible replaced tokens parameter.
   * @param parameters the replacements map.
   * @return the original parameter.
   */
  private String restore(String parameter, Map<String, String> parameters) {
    if (parameter == null) {
      return null;
    }
    String[] parts = parameter.split(DefaultConstants.DEFAULT_SEPARATOR);
    for (int i = 0; i < parts.length; ++i) {
      String originalPart = parameters.get(parts[i]);
      if (originalPart != null) {
        parts[i] = originalPart;
      }
    }
    return String.join(DefaultConstants.DEFAULT_SEPARATOR, parts);
  }

  /**
   * Restore replaced tokens on a matched regex string.
   *
   * @param matcher the regex string matcher.
   * @param parameters the replacements map.
   * @return the original string.
   */
  private String restore(Matcher matcher, Map<String, String> parameters) {
    LOGGER.debug(">>>> parameters: {}", parameters);
    return matcher.replaceAll(
        (result) -> {
          String originalValue = parameters.get(result.group());
          if (originalValue.startsWith("@")) {
            return originalValue;
          }
          if (originalValue.startsWith("$")) {
            return "\\" + originalValue;
          }
          return "\"" + originalValue + "\"";
        });
  }

  /**
   * Replace instructions quoted parameters inplace.
   *
   * @param instructions the instructions to be replaced.
   * @return the replacement map.
   */
  private Map<String, String> replace(List<String> instructions) {
    Map<String, String> parameters = new HashMap<>();
    AtomicInteger counter = new AtomicInteger(0);
    ListIterator<String> iterator = instructions.listIterator();
    while (iterator.hasNext()) {
      String currentInstruction = iterator.next();
      Matcher matcher = QUOTE_PATTERN.matcher(currentInstruction);
      String newInstruction = replace(matcher, counter, parameters);
      iterator.set(newInstruction);
      LOGGER.debug("Replaced: {} -> {}", currentInstruction, newInstruction);
    }

    return parameters;
  }

  /**
   * Replace found tokens on matcher.
   *
   * @param matcher the matching tokens.
   * @param counter the replacement counter.
   * @param parameters the parameters map.
   * @return the replaced string.
   */
  private String replace(Matcher matcher, AtomicInteger counter, Map<String, String> parameters) {
    return matcher.replaceAll(
        (result) -> {
          final String parameter;
          final String value;
          if (result.group(1) != null) {
            parameter = "#param" + counter.getAndIncrement();
            value = result.group(1);
          } else if (result.group(2) != null) {
            parameter = "#object" + counter.getAndIncrement();
            value = result.group(2);
          } else {
            parameter = "#param" + counter.getAndIncrement();
            value = result.group(3);
          }
          parameters.put(parameter, value);
          return parameter;
        });
  }
}
