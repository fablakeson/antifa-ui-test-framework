package com.gotriva.testing.antifa.reporting.impl;

import com.gotriva.testing.antifa.exception.PresentationException;
import com.gotriva.testing.antifa.model.ExecutionResult;
import com.gotriva.testing.antifa.reporting.Reporter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This class implements the {@link Reporter}. */
public class ReporterImpl implements Reporter {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReporterImpl.class);

  /** The file name split tokens */
  private static final String SPLIT_TOKENS = "( |_)";

  /** The template file name */
  private static final String TEMPLATE_FILE_NAME = "templates/report.html";

  /** The file name template */
  private static final String CREATED_FILE_NAME_PATTERN = "antifa_test_{0}_{1,number,#}.html";

  /** The velocity engine */
  private final VelocityEngine velocity;

  /** The velocity context */
  private final VelocityContext context;

  /** Default constructor. */
  public ReporterImpl(VelocityEngine velocity, VelocityContext context) {
    this.velocity = velocity;
    this.context = context;
  }

  @Override
  public File writeReport(ExecutionResult result, String testName, File outputDirectory) {
    if (!outputDirectory.isDirectory()) {
      throw new PresentationException("the output directory is not valid");
    }
    File file = new File(outputDirectory, getFileName(testName));
    try (FileWriter writer = new FileWriter(file)) {
      velocity.init();
      Template template = velocity.getTemplate(TEMPLATE_FILE_NAME);
      context.put("testName", formatTestName(testName));
      context.put("result", result);
      template.merge(context, writer);
    } catch (IOException | RuntimeException ex) {
      LOGGER.error("Error creating report.", ex);
      throw new PresentationException(ex.getMessage(), ex);
    }
    return file;
  }

  /**
   * @return The new created file name.
   */
  private String getFileName(String testName) {
    return MessageFormat.format(
        CREATED_FILE_NAME_PATTERN, formatFileName(testName), System.currentTimeMillis());
  }

  /**
   * Format the file name from test name.
   *
   * @param testName the test name
   * @return
   */
  private String formatFileName(String testName) {
    return String.join("_", testName.split(SPLIT_TOKENS)).toLowerCase();
  }

  /**
   * Format the test printable name.
   *
   * @param testName
   * @return
   */
  private String formatTestName(String testName) {
    return StringUtils.capitalize(String.join(" ", testName.split(SPLIT_TOKENS)));
  }
}
