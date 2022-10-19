package com.gotriva.nlp.antifa.model;

import com.gotriva.nlp.antifa.model.Command.ComponentType;
import edu.stanford.nlp.trees.GrammaticalRelation;
import java.util.Objects;

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

    /** The builder order. */
    private Integer order = 0;

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

    public Builder at(Integer order) {
      this.order = order;
      return this;
    }

    public Builder to(Command.ComponentType destination) {
      this.destination = destination;
      return this;
    }

    public Step build() {
      return new Step(source, edge, destination, order);
    }
  }

  /** The source of the step. */
  private final Command.ComponentType source;

  /** The edge to be traversed. */
  private final GrammaticalRelation edge;

  /** The expected destination. */
  private final Command.ComponentType destination;

  /** The order of relation on source node. */
  private final Integer order;

  /** Private default constructor */
  private Step(
      ComponentType source, GrammaticalRelation edge, ComponentType destination, Integer order) {
    this.source = source;
    this.edge = edge;
    this.destination = destination;
    this.order = order;
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

  public Integer getOrder() {
    return order;
  }

  /** Override methods */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Step) {
      Step other = (Step) obj;
      return Objects.equals(this.source, other.source)
          && Objects.equals(this.edge, other.edge)
          && Objects.equals(this.destination, other.destination);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, edge, destination);
  }

  @Override
  public String toString() {
    return "Step [source=" + source + ", edge=" + edge + ", destination=" + destination + "]";
  }
}
