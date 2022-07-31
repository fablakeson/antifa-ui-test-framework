package com.gotriva;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.gotriva.testing.antifa.execution.Executor;
import com.gotriva.testing.antifa.execution.impl.ExecutionModule;
import com.gotriva.testing.antifa.model.Command;
import com.gotriva.testing.antifa.model.ExecutionResult;
import com.gotriva.testing.antifa.parsing.Parser;
import com.gotriva.testing.antifa.parsing.impl.ParsingModule;
import com.gotriva.testing.antifa.presentation.ReportWriter;
import com.gotriva.testing.antifa.presentation.impl.PresentationModule;
import java.io.File;
import java.util.Arrays;
import java.util.List;
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

  // TODO: Add read execution from text files support.
  // TODO: Add post execution report support.
  // TODO: Add plugin properties parameterization support.
  // TODO: Add unitary tests to each class.
  // TODO: Change LOGGER.info to LOGGER.debug
  // TODO: Remove unnecessary comments (like this)

  /**
   * Location of the file.
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

    /** Dependency injection */
    Injector injector =
        Guice.createInjector(new ParsingModule(), new ExecutionModule(), new PresentationModule());

    /** Enable just basic dependency parsing. */
    List<String> instructions =
        Arrays.asList(
            "open the login page at \"http://localhost:8000/login.html\".",
            "write \"fabiojunior@gmail.com.br\" to username input.",
            "write \"password123\" to password input.",
            "write \"1234 1234 1234 1234\" to the credit card input.",
            "click on the login button.");

    Parser parser = injector.getInstance(Parser.class);

    List<Command> commands = parser.parse(instructions);

    /** Print decoded commands */
    LOGGER.info("The decoded commands: {}", commands);

    Executor executor = injector.getInstance(Executor.class);

    ExecutionResult result = executor.execute(commands);

    LOGGER.info("The result: {}", result);

    ReportWriter writer = injector.getInstance(ReportWriter.class);
    writer.writeReport(result, outputDirectory);

    LOGGER.info("Writed report to file: {}", outputDirectory.getAbsolutePath());
  }
}
