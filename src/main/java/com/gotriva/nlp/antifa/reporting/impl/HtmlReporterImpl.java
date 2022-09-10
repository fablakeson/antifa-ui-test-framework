package com.gotriva.nlp.antifa.reporting.impl;

import com.gotriva.nlp.antifa.reporting.OutputFormat;
import com.gotriva.nlp.antifa.reporting.Reporter;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/** This class implements the {@link Reporter} for HTML outputs. */
public class HtmlReporterImpl extends AbstractReporter {

  HtmlReporterImpl(VelocityEngine velocity, VelocityContext context) {
    super(velocity, context);
  }

  @Override
  protected String getTemplateFile() {
    return "templates/report.html";
  }

  @Override
  protected OutputFormat getOutputFormat() {
    return OutputFormat.HTML;
  }
}
