package com.gotriva.nlp.antifa.model;

import com.google.common.base.Objects;
import com.gotriva.nlp.antifa.model.Command.ComponentType;
import edu.stanford.nlp.trees.GrammaticalRelation;

/** This class represents a step on the {@link SemanticPath} */
public class Step {

  /** Builder class for {@link Step} */
  public static class Builder {

    /** The builder source. */
    private Command.ComponentType source;

    /** The builder edge. */
    private GrammaticalRelation edge;

    /** The builder destination. */
    private Command.ComponentType destination;

    private Builder() {}

    /**
     * @return a new builder instance
     */
    public static Builder newBuilder() {
      return new Builder();
    }

    public Builder from(Command.ComponentType source) {
      this.source = source;
      return this;
    }

    public Builder with(GrammaticalRelation edge) {
      this.edge = edge;
      return this;
    }

    public Builder to(Command.ComponentType destination) {
      this.destination = destination;
      return this;
    }

    public Step build() {
      return new Step(source, edge, destination);
    }
  }

  /** The source of the step. */
  private final Command.ComponentType source;

  /** The edge to be traversed. */
  private final GrammaticalRelation edge;

  /** The expected destination. */
  private final Command.ComponentType destination;

  /** Private default constructor */
  private Step(ComponentType source, GrammaticalRelation edge, ComponentType destination) {
    this.source = source;
    this.edge = edge;
    this.destination = destination;
  }

  /**
   * @return the builder instance
   */
  public static Builder builder() {
    return Builder.newBuilder();
  }

  /** Getters */
  public Command.ComponentType getSource() {
    return source;
  }

  public GrammaticalRelation getEdge() {
    return edge;
  }

  public Command.ComponentType getDestination() {
    return destination;
  }

  /** Override methods */
  @Override
  public boolean equals(Object obj) {
    if (obj != null && obj instanceof Step) {
      Step other = (Step) obj;
      return Objects.equal(source, other.source) && Objects.equal(destination, other.destination);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(source, destination);
  }

  @Override
  public String toString() {
    return "Step [source=" + source + ", edge=" + edge + ", destination=" + destination + "]";
  }
}
