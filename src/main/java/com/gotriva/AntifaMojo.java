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
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
 * @goal touch
 * @phase process-sources
 */
public class AntifaMojo extends AbstractMojo {

  private static final Logger LOGGER = LoggerFactory.getLogger(AntifaMojo.class);

  // TODO: Add plugin properties parameterization support.
  // TODO: Add unitary tests to each class.
  // TODO: Change LOGGER.info to LOGGER.debug
  // TODO: Remove unnecessary comments (like this)

  /** The dependency injector */
  private Injector injector =
      Guice.createInjector(new ParsingModule(), new ExecutionModule(), new PresentationModule());

  /** Location of the input file. */
  private File inputDirectory = new File("./src/main/resources/test-files");

  /**
   * Location of the output files.
   *
   * @parameter expression="project.build.directory"
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
      throw new MojoExecutionException("Input directory is not valid");
    }

    if (!outputDirectory.isDirectory()) {
      throw new MojoExecutionException("Output directory is not valid");
    }

    /** For each test file */
    Stream.of(inputDirectory.listFiles())
        /** Read lines from each file */
        .map(
            file -> {
              try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                return Pair.of(file.getName(), reader.lines().collect(Collectors.toList()));
              } catch (Exception ex) {
                LOGGER.error("Error reading file.", ex);
                return Pair.of(file.getName(), Collections.<String>emptyList());
              }
            })
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
