package com.gotriva.nlp.antifa.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

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
    /** The builder screenshot. */
    private String screenshot;

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

    public Builder setScreenshot(String screenshot) {
      this.screenshot = screenshot;
      return this;
    }

    public ExecutionStep build() {
      return new ExecutionStep(command, startTime, endTime, result, screenshot);
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

  /** The execution step screenshot. */
  private final String screenshot;

  /** The default constructor */
  private ExecutionStep(
      Command command,
      LocalDateTime startTime,
      LocalDateTime endTime,
      Result result,
      String screenshot) {
    this.command = command;
    this.startTime = startTime;
    this.endTime = endTime;
    this.result = result;
    this.screenshot = screenshot;
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

  public String getScreenshot() {
    return screenshot;
  }

  public Long getElapsedTime() {
    return ChronoUnit.MILLIS.between(getStartTime(), getEndTime());
  }

  @Override
  public int hashCode() {
    return Objects.hash(command, endTime, result, screenshot, startTime);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ExecutionStep) {
      ExecutionStep other = (ExecutionStep) obj;
      return Objects.equals(this.command, other.command)
          && Objects.equals(this.endTime, other.endTime)
          && Objects.equals(this.result, other.result)
          && Objects.equals(this.screenshot, other.screenshot)
          && Objects.equals(this.startTime, other.startTime);
    }
    return false;
  }

  @Override
  public String toString() {
    return "ExecutionStep [command="
        + command
        + ", endTime="
        + endTime
        + ", result="
        + result
        + ", screenshot="
        + (screenshot != null ? "[...]" : null)
        + ", startTime="
        + startTime
        + "]";
  }
}
