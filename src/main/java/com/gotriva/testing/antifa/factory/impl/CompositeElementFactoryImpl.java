package com.gotriva.testing.antifa.factory.impl;

import com.google.common.collect.ImmutableMap;
import com.gotriva.testing.antifa.factory.CompositeElementFactory;
import com.gotriva.testing.antifa.factory.ElementFactory;
import java.util.HashMap;
import java.util.Map;

/** This class implements {@link CompositeElementFactory}. */
public class CompositeElementFactoryImpl implements CompositeElementFactory {

  /** The {@link CompositeElementFactoryImpl} builder */
  public static class Builder {
    /** The builder registered factories map */
    private Map<String, ElementFactory<?>> registeredFactories;

    private Builder() {
      this.registeredFactories = new HashMap<>();
    }

    /**
     * @return a new builder instance.
     */
    public static Builder newBuilder() {
      return new Builder();
    }

    /**
     * Registers the current factory for this type.
     *
     * @param type the type
     * @param factory the element factory
     * @return this builder
     */
    public Builder registerFactory(String type, ElementFactory<?> factory) {
      registeredFactories.put(type, factory);
      return this;
    }

    public CompositeElementFactoryImpl build() {
      return new CompositeElementFactoryImpl(ImmutableMap.copyOf(registeredFactories));
    }
  }

  /** The type factories */
  private ImmutableMap<String, ElementFactory<?>> factories;

  /** Default constructor */
  CompositeElementFactoryImpl(ImmutableMap<String, ElementFactory<?>> factories) {
    this.factories = factories;
  }

  /**
   * @return a new builder instance.
   */
  public static Builder builder() {
    return Builder.newBuilder();
  }

  @Override
  public ElementFactory<?> getElementFactory(String type) {
    return factories.get(type);
  }
}
