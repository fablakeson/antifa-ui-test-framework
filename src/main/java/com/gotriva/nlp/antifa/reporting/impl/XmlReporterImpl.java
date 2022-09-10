package com.gotriva.nlp.antifa.reporting.impl;

import com.gotriva.nlp.antifa.reporting.OutputFormat;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/** This class implements the {@link Reporter} for XML outputs. */
public class XmlReporterImpl extends AbstractReporter {

  XmlReporterImpl(VelocityEngine velocity, VelocityContext context) {
    super(velocity, context);
  }

  @Override
  protected String getTemplateFile() {
    return "templates/report.xml";
  }

  @Override
  protected OutputFormat getOutputFormat() {
    return OutputFormat.XML;
  }
}
