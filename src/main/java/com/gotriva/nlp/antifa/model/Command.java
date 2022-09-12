package com.gotriva.nlp.antifa.model;

import java.util.Objects;

/**
 * This class represents an interpreted command. The command consists in 5 components:
 *
 * <ol>
 *   <li>A mandatory <b>instruction</b>, the original natural language instruction for command.
 *   <li>A mandatory <b>action</b>, the action name to be performed.
 *   <li>A mandatory <b>object</b>, the object that should perform the command.
 *   <li>An <em>optional</em> <b>parameter</b>, some actions can have a parameter like a text input.
 *   <li>An <em>optional</em> <b>type</b>, the specific type of the object that should perform the
 *       action.
 * </ol>
 */
public class Command {

  public enum ComponentType {
    /** Represents the action name to call on the semantic structure. */
    ACTION,
    /** Represents the command call parameter on the semantic structure. */
    PARAMETER,
    /** Represents the target object of command call on the semantic structure. */
    OBJECT,
    /** Represents the type of the target object of command call on this semantic structure. */
    TYPE,
    /** Represents a node that is a stepway to another. */
    BYPASS,
    /** Represents a node that has no function other than disambiguate the tree. */
    NO_OP
  }

  /** The {@link Command} builder class. */
  public static class Builder {

    /** The instruction of the builder. */
    private String instruction;
    /** The action of the builder. */
    private String action;
    /** The parameter of the builder. */
    private String parameter;
    /** The object of the builder. */
    private String object;
    /** The type of the builder. */
    private String type;

    private Builder() {}

    private static Builder newBuilder() {
      return new Builder();
    }

    public Builder setInstruction(String instruction) {
      this.instruction = instruction;
      return this;
    }

    public Builder setAction(String action) {
      this.action = action;
      return this;
    }

    public Builder setParameter(String parameter) {
      this.parameter = parameter;
      return this;
    }

    public Builder setObject(String object) {
      this.object = object;
      return this;
    }

    public Builder setType(String type) {
      this.type = type;
      return this;
    }

    public Command build() {
      /** Assert that name and object are not null */
      assert action != null && object != null : "'action' and 'object' must be not null.";
      return new Command(instruction, action, parameter, object, type);
    }
  }

  /** The original instruction in natural language */
  private String instruction;

  /** The name of the command. */
  private final String action;

  /** The parameter of the command. */
  private String parameter;

  /** The object of the command. */
  private final String object;

  /** The object type. */
  private final String type;

  /** Default all args constructor for an command. */
  private Command(String instruction, String action, String parameter, String object, String type) {
    this.instruction = instruction;
    this.action = action;
    this.parameter = parameter;
    this.object = object;
    this.type = type;
  }

  /**
   * @return a new builder instance.
   */
  public static Builder builder() {
    return Builder.newBuilder();
  }

  /** Getters and Setters */
  public String getAction() {
    return action;
  }

  public String getParameter() {
    return parameter;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }

  public boolean hasParameter() {
    return parameter != null;
  }

  public String getObject() {
    return object;
  }

  public String getType() {
    return type;
  }

  public void setInstruction(String instruction) {
    this.instruction = instruction;
  }

  public String getInstruction() {
    return instruction;
  }

  @Override
  public int hashCode() {
    return Objects.hash(instruction, action, object, parameter, type);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Command) {
      Command other = (Command) obj;
      return Objects.equals(this.instruction, other.instruction)
          && Objects.equals(this.action, other.action)
          && Objects.equals(this.object, other.object)
          && Objects.equals(this.parameter, other.parameter);
    }
    return false;
  }

  @Override
  public String toString() {
    return "Command [instruction="
        + instruction
        + ", action="
        + action
        + ", object="
        + object
        + ", parameter="
        + parameter
        + ", type="
        + type
        + "]";
  }
}
