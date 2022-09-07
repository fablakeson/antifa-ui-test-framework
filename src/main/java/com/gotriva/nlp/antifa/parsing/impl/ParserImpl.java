package com.gotriva.nlp.antifa.parsing.impl;

import com.gotriva.nlp.antifa.exception.InstructionParsingException;
import com.gotriva.nlp.antifa.model.Command;
import com.gotriva.nlp.antifa.parsing.Interpreter;
import com.gotriva.nlp.antifa.parsing.Parser;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This class implements the {@link Parser}. */
public class ParserImpl implements Parser {

  private static final Logger LOGGER = LoggerFactory.getLogger(ParserImpl.class);

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
    Map<String, String> parameters = prepareInstructions(instructionsCopy);

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
      /** Replace parameter value (if any) */
      if (command.hasParameter()) {
        String parameterValue = parameters.get(command.getParameter());
        String originalInstruction =
            StringUtils.capitalize(
                sentence
                    .toString()
                    .replace(
                        command.getParameter(), MessageFormat.format("\"{0}\"", parameterValue)));
        command.setParameter(parameterValue);
        command.setInstruction(originalInstruction);
      } else {
        command.setInstruction(StringUtils.capitalize(sentence.toString()));
      }
      // Print action
      LOGGER.debug("command: {}", command);
      // add action to list
      commands.add(command);
    }

    return commands;
  }

  /**
   * Replaces the instructions parameters for variable names and lower case instruction text.
   *
   * @param instructions the instructions to be replaced.
   * @return the map of replaced parameters
   */
  private Map<String, String> prepareInstructions(List<String> instructions) {
    Map<String, String> parameters = new HashMap<>();
    for (int i = 0; i < instructions.size(); ++i) {
      final String instruction = instructions.get(i);
      final int parameterStart = instruction.indexOf("\"");
      if (parameterStart >= 0) {
        final int parameterEnd = instruction.indexOf("\"", parameterStart + 1);
        if (parameterEnd <= 0) {
          throw new InstructionParsingException(
              "Parameter must have a closing quote: " + instruction);
        }
        /** Register parameter */
        String parameterName = "param" + i;
        String parameterValue = instruction.substring(parameterStart + 1, parameterEnd);
        parameters.put(parameterName, parameterValue);
        /** Replace parameters */
        final String newInstruction =
            instruction.substring(0, parameterStart)
                + parameterName
                + instruction.substring(parameterEnd + 1);
        instructions.set(i, newInstruction.toLowerCase());
        LOGGER.debug("Instruction: {} --> {}", instruction, instructions.get(i));
      } else {
        LOGGER.debug("Instruction: {}", instruction);
        instructions.set(i, instruction.toLowerCase());
      }
    }
    return parameters;
  }
}
