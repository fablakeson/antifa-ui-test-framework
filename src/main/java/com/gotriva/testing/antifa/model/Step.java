package com.gotriva.testing.antifa.model;

import com.google.common.base.Objects;
import com.gotriva.testing.antifa.model.Command.ComponentType;
import edu.stanford.nlp.trees.GrammaticalRelation;

/** This class represents a step on the {@link SemanticPath} */
public class Step {

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

  public static Step of(ComponentType source, GrammaticalRelation edge, ComponentType destination) {
    return new Step(source, edge, destination);
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
