package com.gotriva.testing.antifa;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.gotriva.testing.antifa.execution.Executor;
import com.gotriva.testing.antifa.execution.impl.ExecutionModule;
import com.gotriva.testing.antifa.model.Command;
import com.gotriva.testing.antifa.model.ExecutionResult;
import com.gotriva.testing.antifa.parsing.Parser;
import com.gotriva.testing.antifa.parsing.impl.ParsingModule;
import com.gotriva.testing.antifa.reporting.Reporter;
import com.gotriva.testing.antifa.reporting.impl.ReportingModule;
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

  public Antifa(File outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  /**
   * Get a new antifa instance.
   *
   * @param outputDirectory the report output directory
   * @return the antifa instance.
   */
  public static Antifa instance(File outputDirectory) {
    return new Antifa(outputDirectory);
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
    return INJECTOR.getInstance(Reporter.class);
  }
}
