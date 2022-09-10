package com.gotriva.nlp.antifa;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.gotriva.nlp.antifa.execution.Executor;
import com.gotriva.nlp.antifa.execution.impl.ExecutionModule;
import com.gotriva.nlp.antifa.model.Command;
import com.gotriva.nlp.antifa.model.ExecutionResult;
import com.gotriva.nlp.antifa.parsing.Parser;
import com.gotriva.nlp.antifa.parsing.impl.ParsingModule;
import com.gotriva.nlp.antifa.reporting.OutputFormat;
import com.gotriva.nlp.antifa.reporting.Reporter;
import com.gotriva.nlp.antifa.reporting.impl.ReportingModule;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This class represents the main API of this library. */
public final class Antifa {

  private static final Logger LOGGER = LoggerFactory.getLogger(Antifa.class);

  private static final Injector INJECTOR;

  /** Use Guice for dependency injection */
  static {
    INJECTOR =
        Guice.createInjector(new ParsingModule(), new ExecutionModule(), new ReportingModule());
  }
  /** The output directory. */
  private final File outputDirectory;

  private final OutputFormat outputFormat;

  public Antifa(File outputDirectory, OutputFormat outputFormat) {
    this.outputDirectory = outputDirectory;
    this.outputFormat = outputFormat;
  }

  /**
   * Get a new antifa instance.
   *
   * @param outputDirectory the report output directory
   * @return the antifa instance.
   */
  public static Antifa instance(File outputDirectory, OutputFormat outputFormat) {
    return new Antifa(outputDirectory, outputFormat);
  }

  public void execute(String testName, String instructions) {
    execute(testName, Arrays.asList(instructions.split("\n")));
  }

  public void execute(String testName, File instructions) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(instructions))) {
      execute(
          testName,
          reader
              .lines()
              /** Remove comment lines */
              .filter(line -> !line.startsWith("#"))
              /** Remove blank lines */
              .filter(StringUtils::isNotBlank)
              .collect(Collectors.toList()));
    }
  }

  public void execute(String testName, List<String> instructions) {
    sanityCheck(instructions);
    LOGGER.info("Executing test: {}", testName);
    List<Command> commands = parseInstructions(instructions);
    ExecutionResult result = executeCommands(commands);
    File report = writeResultReport(testName, result);
    LOGGER.info("Finished test: {}", testName);
    LOGGER.info("Result added to: {}", report.getAbsolutePath());
  }

  /**
   * Calls the instructions parser.
   *
   * @param instructions
   * @return
   */
  private List<Command> parseInstructions(List<String> instructions) {
    return getParser().parse(instructions);
  }

  /**
   * Calls the executor.
   *
   * @param commands
   * @return
   */
  private ExecutionResult executeCommands(List<Command> commands) {
    return getExecutor().execute(commands);
  }

  /**
   * Writes the report.
   *
   * @param testName
   * @param result
   * @return the report file.
   */
  private File writeResultReport(String testName, ExecutionResult result) {
    return getReportWriter().writeReport(result, testName, outputDirectory);
  }

  /**
   * Assert the instructions list.
   *
   * @param instructions
   */
  private void sanityCheck(List<String> instructions) {
    assert instructions.stream().allMatch(instruction -> instruction.endsWith("."));
  }

  /** Getters */
  private Parser getParser() {
    return INJECTOR.getInstance(Parser.class);
  }

  private Executor getExecutor() {
    return INJECTOR.getInstance(Executor.class);
  }

  private Reporter getReportWriter() {
    return INJECTOR.getInstance(Key.get(Reporter.class, Names.named(outputFormat.name())));
  }
}
