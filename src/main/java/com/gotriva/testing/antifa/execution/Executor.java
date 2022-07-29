package com.gotriva.testing.antifa.execution;

import com.gotriva.testing.antifa.model.Command;
import com.gotriva.testing.antifa.model.ExecutionResult;
import java.util.List;

/** This class is responsible for executing a list of commands for a given test. */
public interface Executor {

  /** The execution status types. */
  enum Status {
    /** The execution has not started yed. */
    NOT_STARTED,
    /** The execution is running. */
    EXECUTING,
    /** The execution was finished. */
    FINISHED
  }

  /**
   * Executes the given commands on this executor.
   *
   * @param commands the commands to execute.
   * @return the execution result.
   */
  public ExecutionResult execute(List<Command> commands);

  /**
   * Gets the current execution status.
   *
   * @return the execution status.
   */
  public Status getStatus();

  /**
   * Gets the current execution result if finished.
   *
   * @return the execution result.
   */
  public ExecutionResult getResult();
}
