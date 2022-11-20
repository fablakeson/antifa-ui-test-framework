package com.gotriva.nlp.antifa;

import com.gotriva.nlp.antifa.reporting.OutputFormat;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

public class App {

  private static final String WEBDRIVER_PATH_FLAG = "-DwebdriverPath";
  private static final String INPUT_PATH_FLAG = "-DinputPath";
  private static final String OUTPUT_PATH_FLAG = "-DoutputPath";
  private static final String OUTPUT_FORMAT_FLAG = "-DoutputFormat";

  public static void main(String[] args) {
    Map<String, String> properties = getPropertiesFromArgs(args);

    String webdriverPath = properties.getOrDefault(WEBDRIVER_PATH_FLAG, "chromedriver");
    String inputPath = properties.getOrDefault(INPUT_PATH_FLAG, "input");
    String outputPath = properties.getOrDefault(OUTPUT_PATH_FLAG, "output");
    String outputFormatValue = properties.getOrDefault(OUTPUT_FORMAT_FLAG, "HTML");

    File webdriverExecutable = new File(webdriverPath);
    validateExecutable(webdriverExecutable);

    File inputDirectory = new File(inputPath);
    validateDirectory(inputDirectory);

    File outputDirectory = new File(outputPath);
    validateDirectory(outputDirectory);

    OutputFormat outputFormat = OutputFormat.valueOf(outputFormatValue);

    Antifa antifa = Antifa.instance(outputDirectory, outputFormat);

    maybeExecuteTest(antifa, inputDirectory);
  }

  private static Map<String, String> getPropertiesFromArgs(String[] args) {
    return Stream.of(args)
        .map(arg -> arg.split("=", 2))
        .collect(Collectors.toMap(parts -> parts[0], parts -> parts[1]));
  }

  private static void validateDirectory(File directory) {
    if (!directory.exists()) {
      throw new IllegalArgumentException(
          "Directory '" + directory.getAbsolutePath() + "' does not exists.");
    }
    if (!directory.isDirectory()) {
      throw new IllegalArgumentException(
          "Directory '" + directory.getAbsolutePath() + "' is not a directory.");
    }
  }

  private static void validateExecutable(File executable) {
    if (!executable.exists()) {
      throw new IllegalArgumentException(
          "File '" + executable.getAbsolutePath() + "' does not exists.");
    }
    if (!executable.canExecute()) {
      throw new IllegalArgumentException(
          "File '" + executable.getAbsolutePath() + "' is not executable.");
    }
  }

  private static void maybeExecuteTest(Antifa antifa, File maybeTestFile) {
    if (maybeTestFile.isDirectory()) {
      for (File maybeChildrenTestFile : maybeTestFile.listFiles()) {
        maybeExecuteTest(antifa, maybeChildrenTestFile);
      }
    } else {
      executeTest(antifa, maybeTestFile);
    }
  }

  private static void executeTest(Antifa antifa, File testFile) {
    try {
      antifa.execute(getTestName(testFile.getName()), testFile);
    } catch (IOException e) {
      throw new RuntimeException("Error executing file " + testFile.getName(), e);
    }
  }

  private static String getTestName(String fileName) {
    return StringUtils.capitalize(
        String.join(" ", fileName.substring(0, fileName.lastIndexOf(".")).split("_"))
            .toLowerCase());
  }
}
