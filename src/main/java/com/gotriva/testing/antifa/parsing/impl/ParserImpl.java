package com.gotriva.testing.antifa.parsing.impl;

import com.gotriva.testing.antifa.exception.InstructionParsingException;
import com.gotriva.testing.antifa.model.Command;
import com.gotriva.testing.antifa.parsing.Interpreter;
import com.gotriva.testing.antifa.parsing.Parser;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
    Map<String, String> parameters = replaceParameter(instructionsCopy);

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
      LOGGER.info("dependencyParse: {}", dependencyParse);
      /** Try to interpret the graph as a command */
      Command command = interpreter.intepret(dependencyParse);
      /** Replace parameter value (if any) */
      if (command.hasParameter()) {
        command.setParameter(parameters.get(command.getParameter()));
      }
      // Print action
      LOGGER.info("command: {}", command);
      // add action to list
      commands.add(command);
    }

    return commands;
  }

  /**
   * Replaces the instructions parameters for variable names.
   *
   * @param instructions the instructions to be replaced.
   * @return the map of replaced parameters
   */
  private Map<String, String> replaceParameter(List<String> instructions) {
    Map<String, String> parameters = new HashMap<>();
    for (int i = 0; i < instructions.size(); ++i) {
      String instruction = instructions.get(i);
      int parameterStart = instruction.indexOf("\"");
      if (parameterStart >= 0) {
        int parameterEnd = instruction.indexOf("\"", parameterStart + 1);
        if (parameterEnd <= 0) {
          throw new InstructionParsingException(
              "Parameter must have a closing quote: " + instruction);
        }
        /** Register parameter */
        String parameterName = "param" + i;
        String parameterValue = instruction.substring(parameterStart + 1, parameterEnd);
        parameters.put(parameterName, parameterValue);
        /** Replace parameters */
        String newInstruction =
            instruction.substring(0, parameterStart)
                + parameterName
                + instruction.substring(parameterEnd + 1);
        instructions.set(i, newInstruction);
        LOGGER.info("Instruction: {} --> {}", instruction, instructions.get(i));
      } else {
        LOGGER.info("Instruction: {}", instruction);
      }
    }
    return parameters;
  }
}
