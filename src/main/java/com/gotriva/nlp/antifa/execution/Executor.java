package com.gotriva.nlp.antifa.execution;

import java.util.List;

import com.gotriva.nlp.antifa.model.Command;
import com.gotriva.nlp.antifa.model.ExecutionResult;

/** This class is responsible for executing a list of commands for a given test. */
public interface Executor {

  /**
   * Executes the given commands on this executor.
   *
   * @param commands the commands to execute.
   * @return the execution result.
   */
  public ExecutionResult execute(List<Command> commands);
}
