package com.gotriva.nlp.antifa.strategy;

/** This class represents a generic strategy. */
public interface ActionStrategy {

  /** The action to be handled. */
  public String getAction();

  /** This action command instruction element should be replaced with element metadata, if any. */
  public default boolean isReplaceable() {
    return true;
  }

  /** This actions strategy should have screenshot record on result. */
  public default boolean isPrintable() {
    return true;
  }
}
