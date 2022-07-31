package com.gotriva.testing.antifa.presentation.impl;

import com.gotriva.testing.antifa.exception.PresentationException;
import com.gotriva.testing.antifa.model.ExecutionResult;
import com.gotriva.testing.antifa.presentation.ReportWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportWriterImpl implements ReportWriter {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReportWriterImpl.class);

  /** The template file name */
  private static final String TEMPLATE_FILE_NAME = "./src/main/resources/templates/report.html";

  /** The file name template */
  private static final String CREATED_FILE_NAME_PATTERN = "antifa_test_{0}.html";

  /** The velocity engine */
  private final VelocityEngine velocity;

  /** The velocity context */
  private final VelocityContext context;

  /** Default constructor. */
  public ReportWriterImpl(VelocityEngine velocity, VelocityContext context) {
    this.velocity = velocity;
    this.context = context;
  }

  @Override
  public void writeReport(ExecutionResult result, File outputDirectory) {
    if (!outputDirectory.isDirectory()) {
      throw new PresentationException("the output directory is not valid");
    }
    try (FileWriter writer = new FileWriter(new File(outputDirectory, getFileName()))) {
      velocity.init();
      Template template = velocity.getTemplate(TEMPLATE_FILE_NAME);
      context.put("result", result);
      template.merge(context, writer);
    } catch (IOException | RuntimeException ex) {
      LOGGER.error("Error creating report.", ex);
      throw new PresentationException(ex.getMessage(), ex);
    }
  }

  /**
   * @return The new created file name.
   */
  private String getFileName() {
    return MessageFormat.format(CREATED_FILE_NAME_PATTERN, LocalDateTime.now());
  }
}
