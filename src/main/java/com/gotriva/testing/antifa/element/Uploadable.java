package com.gotriva.testing.antifa.element;

/** An interace to represent a upload interaction capable element. */
public interface Uploadable {

  /**
   * Uploads a file from given file path.
   *
   * @param filePath the file path.
   */
  void upload(String filePath);
}
