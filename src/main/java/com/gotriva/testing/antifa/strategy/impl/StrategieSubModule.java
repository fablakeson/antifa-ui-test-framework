package com.gotriva.testing.antifa.strategy.impl;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.gotriva.testing.antifa.strategy.ActionStrategy;
import java.lang.annotation.Retention;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.inject.Qualifier;
import javax.inject.Singleton;

/** Provides strategy classes bindings. */
public class StrategieSubModule extends AbstractModule {

  @Qualifier
  @Retention(RUNTIME)
  @interface strategiesList {}

  @Qualifier
  @Retention(RUNTIME)
  public static @interface strategiesMap {}

  @Provides
  @Singleton
  @strategiesList
  public List<ActionStrategy> providestrategiesList() {
    /** Interactable strategies */
    return ImmutableList.<ActionStrategy>builder()
        /** Interactable strategies */
        .add(new CheckStrategy())
        .add(new ClickStrategy())
        .add(new HoverStrategy())
        .add(new SetStrategy())
        .add(new UncheckStrategy())
        .add(new UploadStrategy())
        .add(new WriteStrategy())
        /** Page Object strategies */
        .add(new OpenPageStrategy())
        .add(new ClosePageStrategy())
        .add(new ReadPageStrategy())
        .add(new RollPageStrategy())
        /** return list */
        .build();
  }

  @Provides
  @Singleton
  @Inject
  @strategiesMap
  public Map<String, ActionStrategy> providestrategiesMap(
      @strategiesList List<ActionStrategy> strategies) {
    return strategies.stream()
        .collect(ImmutableMap.toImmutableMap(ActionStrategy::getAction, Function.identity()));
  }
}
