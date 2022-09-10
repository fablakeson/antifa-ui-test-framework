package com.gotriva.nlp.antifa.reporting.impl;

import com.gotriva.nlp.antifa.exception.PresentationException;
import com.gotriva.nlp.antifa.model.ExecutionResult;
import com.gotriva.nlp.antifa.reporting.OutputFormat;
import com.gotriva.nlp.antifa.reporting.Reporter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public abstract class AbstractReporter implements Reporter {

  /** The file name split tokens */
  private static final String SPLIT_TOKENS = "( |_)";

  /** The file name template */
  private static final String CREATED_FILE_NAME_PATTERN = "antifa_test_{0}_{1,number,#}.{2}";

  /** The velocity engine */
  private final VelocityEngine velocity;

  /** The velocity context */
  private final VelocityContext context;

  /** Default constructor. */
  AbstractReporter(VelocityEngine velocity, VelocityContext context) {
    this.velocity = velocity;
    this.context = context;
  }

  /**
   * @return The template file name.
   */
  protected abstract String getTemplateFile();

  /**
   * @return The output file format.
   */
  protected abstract OutputFormat getOutputFormat();

  /* (non-Javadoc)
   * @see com.gotriva.nlp.antifa.reporting.Reporter#writeReport(com.gotriva.nlp.antifa.model.ExecutionResult, java.lang.String, java.io.File)
   */
  @Override
  public File writeReport(ExecutionResult result, String testName, File outputDirectory) {
    File file = new File(outputDirectory, getFileName(testName));
    try (FileWriter writer = new FileWriter(file)) {
      velocity.init();
      Template template = velocity.getTemplate(getTemplateFile());
      context.put("testName", formatTestName(testName));
      context.put("result", result);
      template.merge(context, writer);
      return file;
    } catch (IOException | RuntimeException ex) {
      throw new PresentationException("Error creating HTML report.", ex);
    }
  }

  /**
   * @return The new created file name.
   */
  private String getFileName(String testName) {
    return MessageFormat.format(
        CREATED_FILE_NAME_PATTERN,
        formatFileName(testName),
        System.currentTimeMillis(),
        getOutputFormat().name().toLowerCase());
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
