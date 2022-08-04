package com.gotriva.testing.antifa.handler.impl;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.gotriva.testing.antifa.handler.ActionHandler;
import java.lang.annotation.Retention;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.inject.Qualifier;
import javax.inject.Singleton;

/** Provides handler classes bindings. */
public class HandlerSubModule extends AbstractModule {

  @Qualifier
  @Retention(RUNTIME)
  @interface HandlersList {}

  @Qualifier
  @Retention(RUNTIME)
  public static @interface HandlersMap {}

  @Provides
  @Singleton
  @HandlersList
  public List<ActionHandler> provideHandlersList() {
    /** Interactable handlers */
    return ImmutableList.<ActionHandler>builder()
        /** Interactable handlers */
        .add(new CheckHandler())
        .add(new ClickHandler())
        .add(new HoverHandler())
        .add(new SetHandler())
        .add(new UncheckHandler())
        .add(new UploadHandler())
        .add(new WriteHandler())
        /** Page Object handlers */
        .add(new OpenPageHandler())
        .add(new ClosePageHandler())
        .add(new ReadPageHandler())
        .add(new RollPageHandler())
        /** return list */
        .build();
  }

  @Provides
  @Singleton
  @Inject
  @HandlersMap
  public Map<String, ActionHandler> provideHandlersMap(@HandlersList List<ActionHandler> handlers) {
    return handlers.stream()
        .collect(ImmutableMap.toImmutableMap(ActionHandler::getAction, Function.identity()));
  }
}
