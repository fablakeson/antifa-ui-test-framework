package com.gotriva.testing.antifa.model;

import java.time.LocalDateTime;

/** This class represents a step on {@link ExecutionResult}. */
public class ExecutionStep {

  /** The result types */
  public enum Result {
    /** Step has executed successfully. */
    SUCCESS,
    /** Step has failed on exection. */
    FAIL
  }

  /** The execution step builder */
  public static class Builder {

    /** The builder command . */
    private Command command;
    /** The builder start time. */
    private LocalDateTime startTime;
    /** The builder end time. */
    private LocalDateTime endTime;
    /** The builder result status. */
    private Result result;
    /** The builder snapshot. */
    private String snapshot;

    private Builder() {}

    public static Builder newBuilder() {
      return new Builder();
    }

    public Builder setCommand(Command command) {
      this.command = command;
      return this;
    }

    public Builder setStartDate(LocalDateTime startTime) {
      this.startTime = startTime;
      return this;
    }

    public Builder startNow() {
      this.startTime = LocalDateTime.now();
      return this;
    }

    public Builder setEndTime(LocalDateTime endTime) {
      this.endTime = endTime;
      return this;
    }

    public Builder endNow() {
      this.endTime = LocalDateTime.now();
      return this;
    }

    public Builder setResult(Result result) {
      this.result = result;
      return this;
    }

    public Builder withSuccess() {
      this.result = Result.SUCCESS;
      return this;
    }

    public Builder withFail() {
      this.result = Result.FAIL;
      return this;
    }

    public Builder setSnapshot(String snapshot) {
      this.snapshot = snapshot;
      return this;
    }

    public ExecutionStep build() {
      return new ExecutionStep(command, startTime, endTime, result, snapshot);
    }
  }

  /** The interpreted command for this instruction. */
  private final Command command;

  /** The execution step start time. */
  private final LocalDateTime startTime;

  /** The execution step end time. */
  private final LocalDateTime endTime;

  /** The execution result status. */
  private final Result result;

  /** The execution step snapshot. */
  private final String snapshot;

  /** The default constructor */
  private ExecutionStep(
      Command command,
      LocalDateTime startTime,
      LocalDateTime endTime,
      Result result,
      String snapshot) {
    this.command = command;
    this.startTime = startTime;
    this.endTime = endTime;
    this.result = result;
    this.snapshot = snapshot;
  }

  /**
   * @return a builder for {@link ExecutionStep}
   */
  public static Builder builder() {
    return Builder.newBuilder();
  }

  /**
   * @return true if step was a fail.
   */
  public boolean isFail() {
    return result == Result.FAIL;
  }

  /**
   * @return true if step was a success.
   */
  public boolean isSuccess() {
    return result == Result.SUCCESS;
  }

  /** Getters */
  public Command getCommand() {
    return command;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public Result getResult() {
    return result;
  }

  public String getSnapshot() {
    return snapshot;
  }
}
