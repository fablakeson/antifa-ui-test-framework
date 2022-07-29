package com.gotriva.testing.antifa.parsing;

import com.gotriva.testing.antifa.model.Command;
import java.util.List;

/** This class is resposible for parse the plain text instructions into {@link Command}s. */
public interface Parser {

  /**
   * Parses the instructions into a list of commands.
   *
   * @param instructions the text plain instructions
   * @return the list of commands to execute
   */
  public List<Command> parse(List<String> instructions);
}
