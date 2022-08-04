package com.gotriva;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.gotriva.testing.antifa.execution.Executor;
import com.gotriva.testing.antifa.execution.impl.ExecutionModule;
import com.gotriva.testing.antifa.parsing.Parser;
import com.gotriva.testing.antifa.parsing.impl.ParsingModule;
import com.gotriva.testing.antifa.presentation.ReportWriter;
import com.gotriva.testing.antifa.presentation.impl.PresentationModule;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

/**
 * Goal which touches a timestamp file.
 *
 * @goal ui-test
 * @phase test
 */
public class AntifaMojo extends AbstractMojo {

  private static final Logger LOGGER = LoggerFactory.getLogger(AntifaMojo.class);

  // TODO: Add unitary tests to each class.
  // TODO: Remove unnecessary comments (like this)

  /** The dependency injector */
  private Injector injector =
      Guice.createInjector(new ParsingModule(), new ExecutionModule(), new PresentationModule());

  /**
   * Location of the input files.
   *
   * @parameter default-value="${project.basedir}/src/test/resources/antifa"
   * @required
   */
  private File inputDirectory;

  /**
   * Location of the output files.
   *
   * @parameter default-value="${project.build.directory}"
   * @required
   */
  private File outputDirectory;

  /*
   * (non-Javadoc)
   *
   * @see org.apache.maven.plugin.Mojo#execute()
   */
  public void execute() throws MojoExecutionException {

    /** Check input and output directories */
    if (!inputDirectory.isDirectory()) {
      throw new MojoExecutionException(
          "Input directory is not valid: " + inputDirectory.getAbsolutePath());
    }
    LOGGER.info("Reading files on directory: {}", inputDirectory.getAbsolutePath());

    if (!outputDirectory.isDirectory()) {
      throw new MojoExecutionException(
          "Output directory is not valid: " + outputDirectory.getAbsolutePath());
    }
    LOGGER.info("Output files on directory: {}", outputDirectory.getAbsolutePath());

    /** For each test file */
    Stream.of(inputDirectory.listFiles())
        /** Read lines from each file */
        .map(file -> Pair.of(file.getName(), getLines(file)))
        /** Log file if debug */
        .peek(pair -> LOGGER.info("Running tests on file: {}", pair.getKey()))
        /** Parse instruction lines into commands */
        .map(pair -> Pair.of(pair.getKey(), getParser().parse(pair.getValue())))
        /** Execute commands and get result */
        .map(pair -> Pair.of(pair.getKey(), getExecutor().execute(pair.getValue())))
        /** Write the result report */
        .forEach(
            pair ->
                getReportWriter()
                    .writeReport(
                        pair.getValue(), removeFileExtension(pair.getKey()), outputDirectory));
    LOGGER.info("All done!");
  }

  private List<String> getLines(File file) {
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      return reader
          .lines()
          /** Remove comment lines */
          .filter(line -> !line.startsWith("#"))
          /** Remove blank lines */
          .filter(StringUtils::isNotBlank)
          .collect(Collectors.toList());
    } catch (Exception ex) {
      LOGGER.error("Error reading file.", ex);
      return Collections.emptyList();
    }
  }

  private String removeFileExtension(String fileName) {
    return fileName.substring(0, fileName.lastIndexOf("."));
  }

  private Parser getParser() {
    return injector.getInstance(Parser.class);
  }

  private Executor getExecutor() {
    return injector.getInstance(Executor.class);
  }

  private ReportWriter getReportWriter() {
    return injector.getInstance(ReportWriter.class);
  }
}
