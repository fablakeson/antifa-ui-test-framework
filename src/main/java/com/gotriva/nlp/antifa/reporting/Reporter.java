package com.gotriva.nlp.antifa.reporting;

import java.io.File;

import com.gotriva.nlp.antifa.model.ExecutionResult;

/** This class gets the {@link ExecutionResult} and writes an HTML report. */
public interface Reporter {

  /**
   * Writes a new report file for this execution.
   *
   * @param result the execution result.
   * @param testName the test file name
   * @param outputDirectory the output directory file.
   * @return the writed file.
   */
  File writeReport(ExecutionResult result, String testName, File outputDirectory);
}
