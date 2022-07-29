package com.gotriva.testing.antifa.model;

import com.google.common.collect.ImmutableList;
import java.time.LocalDateTime;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/** This class represents the result of an execution call on {@link ExecutorImpl}. */
public class ExecutionResult {

  public enum Status {
    /** This execution has not executed yet. */
    NOT_EXECUTED,
    /** This execution has not a final status. */
    UNKNOWN,
    /** This execution was a success. */
    SUCCESS,
    /** This excection failed. */
    FAIL
  }

  /** The execution result steps. */
  private final Deque<ExecutionStep> steps;

  /** The execution result status. */
  private Status status;

  /** All args constructor */
  public ExecutionResult(Deque<ExecutionStep> steps, Status status) {
    this.steps = steps;
    this.status = status;
  }

  /** Default constructor */
  public ExecutionResult() {
    this(new LinkedList<>(), Status.NOT_EXECUTED);
  }

  /**
   * Adds a new step to execution result. The execution fails if the step is a FAIL. Otherwise, the
   * execution result remains unknown until the finish method call.
   *
   * @param step the execution step to be added.
   * @return
   */
  public Status addStep(ExecutionStep step) {
    /** Check execution result state. */
    if (isFinished()) {
      throw new IllegalStateException("Execution is already finished.");
    }
    /** Add step to execution result. */
    this.steps.add(step);

    /** Check if step is a fail, then mark execution as fail. */
    if (step.isFail()) {
      fail();
    } else if (status != Status.UNKNOWN) {
      /** Execution result status is unknown now. */
      status = Status.UNKNOWN;
    }
    return status;
  }

  /** Marks current execution result as FAIL. */
  private void fail() {
    this.status = Status.FAIL;
  }

  /** Marks current execution result as SUCCESS; */
  private void success() {
    this.status = Status.SUCCESS;
  }

  /**
   * Marks current execution result as finished with SUCCESS. It will not accept new steps after.
   */
  public void finish() {
    /** If this execution has not executed, then it fails. */
    if (isFinished()) {
      return;
    } else if (isStarted()) {
      success();
    } else {
      fail();
    }
  }

  /**
   * Check if current execution is finished.
   *
   * @return true if this execution is finished.
   */
  public boolean isFinished() {
    return status == Status.SUCCESS || status == Status.FAIL;
  }

  /**
   * Check if this execution is started.
   *
   * @return true if this execution have at last one step.
   */
  public boolean isStarted() {
    return !steps.isEmpty();
  }

  /** Get the execution start time, if started. */
  public LocalDateTime getStartTime() {
    if (!isStarted()) {
      throw new IllegalStateException("Execution has not started.");
    }
    return steps.getFirst().getStartTime();
  }

  /** Get the execution end time, if finished. */
  public LocalDateTime getEndTime() {
    if (!isFinished()) {
      throw new IllegalStateException("Execution has not ended.");
    }
    return steps.getLast().getEndTime();
  }

  /** Get the current execution steps list copy. */
  public List<ExecutionStep> getSteps() {
    return ImmutableList.copyOf(steps);
  }
}
