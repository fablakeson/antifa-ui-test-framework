package com.gotriva.testing.antifa.model;

import java.time.LocalDateTime;

/** This class represents a step on {@link ExecutionResult}. */
public class ExecutionStep {
    
    public enum Result {
        /** Step has executed successfully. */
        SUCCESS,
        /** Step has failed on exection. */
        FAIL
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
    public ExecutionStep(
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
