package com.gotriva.nlp.antifa.parsing.impl;

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
  private static final Pattern QUOTE_PATTERN = Pattern.compile("\"(.*?)\"|(@[\\w\\d]+)");

  /** Pattern to find parameters replacement. */
  private static final Pattern PARAMETER_PATTERN = Pattern.compile("(#\\.+\\d{1,})");

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

    /** Replace parameters */
    Map<String, String> parameters = replaceParameters(instructionsCopy);

    /** Concatenate the instructions into one text. */
    String instructionsText = String.join("\n", instructionsCopy);

    /** Creates an annotation from instructions text */
    Annotation annotation = new Annotation(instructionsText);

    /** Execute pipeline */
    pipeline.annotate(annotation);

    /** Get sentence annotations */
    List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

    /** Commands to be returned. */
    List<Command> commands = new LinkedList<>();

    /** Parse the sentences into commands */
    for (CoreMap sentence : sentences) {
      /** Get semantic dependency graph */
      SemanticGraph dependencyParse = sentence.get(BasicDependenciesAnnotation.class);
      LOGGER.debug("dependencyParse: {}", dependencyParse);
      /** Try to interpret the graph as a command */
      Command command = interpreter.intepret(dependencyParse);
      /** Restore command parameters */
      command.setInstruction(StringUtils.capitalize(sentence.toString()));
      command = restoreParameters(command, parameters);
      /** Print action */
      LOGGER.debug("command: {}", command);
      /** add action to list */
      commands.add(command);
    }

    return commands;
  }

  /**
   * Restore quoted parameters original values on command.
   *
   * @param command the command with values to be restored.
   * @param parameters the parameters replacement map.
   * @return the replaced command.
   */
  private Command restoreParameters(Command command, Map<String, String> parameters) {
    /** Check if command fields where replaced by parameters. */
    LOGGER.debug("Restoring: {}", command);
    Matcher matcher = PARAMETER_PATTERN.matcher(command.getInstruction());
    if (!matcher.find()) {
      return command;
    }
    return Command.builder()
        .setAction(command.getAction())
        .setParameter(parameters.getOrDefault(command.getParameter(), command.getParameter()))
        .setType(parameters.getOrDefault(command.getType(), command.getType()))
        .setObject(parameters.getOrDefault(command.getObject(), command.getObject()))
        .setInstruction(
            matcher.replaceAll(
                (result) -> {
                  String originalValue = parameters.get(result.group());
                  return originalValue.startsWith("@")
                      ? originalValue
                      : "\"" + originalValue + "\"";
                }))
        .build();
  }

  /**
   * Replace instructions quoted parameters inplace.
   *
   * @param instructions the instructions to be replaced.
   * @return the replacement map.
   */
  private Map<String, String> replaceParameters(List<String> instructions) {
    Map<String, String> parameters = new HashMap<>();
    AtomicInteger counter = new AtomicInteger(0);
    ListIterator<String> iterator = instructions.listIterator();
    while (iterator.hasNext()) {
      String currentInstruction = iterator.next();
      Matcher matcher = QUOTE_PATTERN.matcher(currentInstruction);
      String newInstruction =
          matcher
              .replaceAll(
                  (result) -> {
                    final String parameter;
                    final String value;
                    if (result.group(1) != null) {
                      parameter = "#param" + counter.getAndIncrement();
                      value = result.group(1);
                    } else {
                      parameter = "#object" + counter.getAndIncrement();
                      value = result.group(2);
                    }
                    parameters.put(parameter, value);
                    return parameter;
                  })
              .toLowerCase();
      iterator.set(newInstruction);
      LOGGER.debug("Replaced: {} -> {}", currentInstruction, newInstruction);
    }

    return parameters;
  }
}
