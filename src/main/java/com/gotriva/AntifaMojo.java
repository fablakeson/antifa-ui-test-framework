package com.gotriva;

import com.gotriva.nlp.antifa.Antifa;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
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

    Antifa antifa = Antifa.instance(outputDirectory);

    /** For each test file */
    Stream.of(inputDirectory.listFiles())
        /** execute tests for files */
        .forEach(
            file -> {
              try {
                antifa.execute(getTestName(file.getName()), file);
              } catch (IOException e) {
                LOGGER.error("Error executing file " + file.getName(), e);
                throw new RuntimeException(e);
              }
            });

    LOGGER.info("All done!");
  }

  private String getTestName(String fileName) {
    return StringUtils.capitalize(
        String.join(" ", fileName.substring(0, fileName.lastIndexOf(".")).split("_"))
            .toLowerCase());
  }
}
