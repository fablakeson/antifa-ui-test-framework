package com.gotriva.nlp.antifa.strategy.impl;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.gotriva.nlp.antifa.strategy.ActionStrategy;
import java.lang.annotation.Retention;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.inject.Qualifier;
import javax.inject.Singleton;

/** Provides strategy classes bindings. */
public class StrategiesSubModule extends AbstractModule {

  @Qualifier
  @Retention(RUNTIME)
  @interface StrategiesList {}

  @Qualifier
  @Retention(RUNTIME)
  public static @interface StrategiesMap {}

  @Provides
  @Singleton
  @StrategiesList
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
        .add(new ReadStrategy())
        .add(new SelectStrategy())
        /** Page Object strategies */
        .add(new AssertPageStrategy())
        .add(new ClosePageStrategy())
        .add(new DefinePageStrategy())
        .add(new OpenPageStrategy())
        .add(new ScrollPageStrategy())
        .add(new StorePageStrategy())
        .add(new WaitPageStrategy())
        /** return list */
        .build();
  }

  @Provides
  @Singleton
  @Inject
  @StrategiesMap
  public Map<String, ActionStrategy> providestrategiesMap(
      @StrategiesList List<ActionStrategy> strategies) {
    return strategies.stream()
        .collect(ImmutableMap.toImmutableMap(ActionStrategy::getAction, Function.identity()));
  }
}
