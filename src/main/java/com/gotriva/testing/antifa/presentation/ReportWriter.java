package com.gotriva.testing.antifa.presentation;

import com.gotriva.testing.antifa.model.ExecutionResult;
import java.io.File;

/** This class gets the {@link ExecutionResult} and writes an HTML report. */
public interface ReportWriter {

  /**
   * Writes a new report file for this execution.
   *
   * @param result the execution result.
   * @param outputDirectory the output directory file.
   */
  void writeReport(ExecutionResult result, File outputDirectory);
}
