package com.gotriva.testing.antifa.reporting;

import com.gotriva.testing.antifa.model.ExecutionResult;
import java.io.File;

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
