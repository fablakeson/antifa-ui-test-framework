package com.gotriva.testing.antifa.model;

import com.google.common.collect.ImmutableList;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/** This class represents the result of an execution call on {@link ExecutorImpl}. */
public class ExecutionResult {

  public enum Status {
    /** This execution has not executed yet. */
    NOT_EXECUTED,
    /** This execution was a success. */
    SUCCESS,
    /** This excection failed. */
    FAIL
  }

  /** The {@link ExecutionResult} builder. */
  public static class Builder {

    /** The builder steps. */
    private Deque<ExecutionStep> steps;

    /** The builder status. */
    private Status status;

    /** The builder fail reason. */
    private String failReason;

    private Builder() {
      this.steps = new LinkedList<>();
      this.status = Status.NOT_EXECUTED;
    }

    /**
     * @return a new builder instance
     */
    private static Builder newBuilder() {
      return new Builder();
    }

    /**
     * Adds a new step to execution result.
     *
     * @param step the step builder
     * @return this builder
     */
    public Builder addStep(ExecutionStep.Builder step) {
      steps.add(step.build());
      return this;
    }

    /**
     * Adds FAIL status to execution result.
     *
     * @return this builder
     */
    public Builder withFail(String reason) {
      failReason = reason;
      status = Status.FAIL;
      return this;
    }

    /**
     * Adds SUCCESS status to execution result.
     *
     * @return
     */
    public Builder withSuccess() {
      status = Status.SUCCESS;
      return this;
    }

    /**
     * Builds a new instance of execution result.
     *
     * @return the execution result instance
     */
    public ExecutionResult build() {
      /** If not executed, then should not have steps. */
      assert status == Status.NOT_EXECUTED ^ !steps.isEmpty()
          : "Executed results must have termination state SUCCESS or FAIL.";
      /** If fail must have a reason */
      assert status == Status.FAIL ^ failReason == null : "Failed results must have 'failReason'.";
      /** If success must have */
      return new ExecutionResult(ImmutableList.copyOf(steps), status, failReason);
    }
  }

  /** The execution result steps. */
  private final ImmutableList<ExecutionStep> steps;

  /** The execution result status. */
  private final Status status;

  /** The excecutio result fail reason message. */
  private final String failReason;

  /** Default constructor */
  private ExecutionResult(ImmutableList<ExecutionStep> steps, Status status, String failReason) {
    this.steps = steps;
    this.status = status;
    this.failReason = failReason;
  }

  /**
   * @return a new builder instance
   */
  public static Builder builder() {
    return Builder.newBuilder();
  }

  /**
   * Check if this execution is done.
   *
   * @return true if this execution have at last one step.
   */
  public boolean isExecuted() {
    return !steps.isEmpty();
  }

  /** Get the execution start time, if executed. */
  public LocalDateTime getStartTime() {
    if (!isExecuted()) {
      return null;
    }
    return steps.get(0).getStartTime();
  }

  /** Get the execution end time, if executed. */
  public LocalDateTime getEndTime() {
    if (!isExecuted()) {
      return null;
    }
    return steps.get(steps.size() - 1).getEndTime();
  }

  /** Get the execution elapsad time in milisseconds, if executed */
  public Long getElapsedTime() {
    if (!isExecuted()) {
      return 0L;
    }
    return ChronoUnit.MILLIS.between(getStartTime(), getEndTime());
  }

  /**
   * @return the execution result status.
   */
  public Status getStatus() {
    return status;
  }

  /**
   * @return the fail reason, if any
   */
  public String getFailReason() {
    return failReason;
  }

  /**
   * @return the execution result steps list.
   */
  public List<ExecutionStep> getSteps() {
    return steps;
  }
}
