package com.gotriva.testing.antifa.execution.impl;

import static com.gotriva.testing.antifa.constants.ElementConstants.BUTTON;
import static com.gotriva.testing.antifa.constants.ElementConstants.CAPTION;
import static com.gotriva.testing.antifa.constants.ElementConstants.CHECKBOX;
import static com.gotriva.testing.antifa.constants.ElementConstants.FILE;
import static com.gotriva.testing.antifa.constants.ElementConstants.HEADER;
import static com.gotriva.testing.antifa.constants.ElementConstants.IMAGE;
import static com.gotriva.testing.antifa.constants.ElementConstants.LABEL;
import static com.gotriva.testing.antifa.constants.ElementConstants.MESSAGE;
import static com.gotriva.testing.antifa.constants.ElementConstants.OPTION;
import static com.gotriva.testing.antifa.constants.ElementConstants.OPT_IN;
import static com.gotriva.testing.antifa.constants.ElementConstants.RADIO;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.gotriva.testing.antifa.constants.ElementConstants;
import com.gotriva.testing.antifa.element.impl.Button;
import com.gotriva.testing.antifa.element.impl.Checkbox;
import com.gotriva.testing.antifa.element.impl.File;
import com.gotriva.testing.antifa.element.impl.Image;
import com.gotriva.testing.antifa.element.impl.Label;
import com.gotriva.testing.antifa.element.impl.Password;
import com.gotriva.testing.antifa.element.impl.Radio;
import com.gotriva.testing.antifa.element.impl.Range;
import com.gotriva.testing.antifa.element.impl.Text;
import com.gotriva.testing.antifa.execution.Executor;
import com.gotriva.testing.antifa.factory.AbstractElementFactory;
import com.gotriva.testing.antifa.factory.InteractableFactory;
import com.gotriva.testing.antifa.handler.ActionHandler;
import com.gotriva.testing.antifa.handler.impl.HandlerSubModule;
import com.gotriva.testing.antifa.handler.impl.HandlerSubModule.HandlersMap;
import com.gotriva.testing.antifa.model.ExecutionContext;
import java.lang.annotation.Retention;
import java.util.Map;
import java.util.Properties;
import javax.inject.Qualifier;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/** Provides execution classes bindings. */
public class ExecutionModule extends AbstractModule {

  @Qualifier
  @Retention(RUNTIME)
  @interface WebDriverProperties {}

  @Qualifier
  @Retention(RUNTIME)
  @interface Driver {}

  @Qualifier
  @Retention(RUNTIME)
  @interface ElementFactoryMap {}

  @Qualifier
  @Retention(RUNTIME)
  @interface Factory {}

  @Qualifier
  @Retention(RUNTIME)
  @interface Context {}

  @Override
  protected void configure() {
    install(new HandlerSubModule());
  }

  @Provides
  @Singleton
  @WebDriverProperties
  public Properties provideProps() {
    Properties props = new Properties();
    return props;
  }

  @Provides
  @Driver
  public WebDriver provideWebDriver(@WebDriverProperties Properties props) {
    // TODO: get driver by properties
    return new ChromeDriver();
  }

  @Provides
  @Singleton
  @ElementFactoryMap
  public Map<String, AbstractElementFactory<?>> provideElementFactoryMap() {
    return ImmutableMap.<String, AbstractElementFactory<?>>builder()
        /** Button input type names */
        .put(BUTTON, (element) -> new Button(element))
        /** Check input type names */
        .put(CHECKBOX, (element) -> new Checkbox(element))
        .put(OPT_IN, (element) -> new Checkbox(element))
        /** File input type names */
        .put(FILE, (element) -> new File(element))
        /** Image type names */
        .put(IMAGE, (element) -> new Image(element))
        /** Label type names */
        .put(LABEL, (element) -> new Label(element))
        .put(HEADER, (element) -> new Label(element))
        .put(CAPTION, (element) -> new Label(element))
        .put(MESSAGE, (element) -> new Label(element))
        /** Option type names */
        .put(RADIO, (element) -> new Radio(element))
        .put(OPTION, (element) -> new Radio(element))
        /** Range type names */
        .put(ElementConstants.RANGE, (element) -> new Range(element))
        /** Text type names */
        .put(ElementConstants.INPUT, (element) -> new Text(element))
        .put(ElementConstants.FIELD, (element) -> new Text(element))
        .put(ElementConstants.TEXTBOX, (element) -> new Text(element))
        /** Password type names */
        .put(ElementConstants.PASSWORD, (element) -> new Password(element))
        /** returns map */
        .build();
  }

  @Provides
  @Singleton
  @Factory
  public InteractableFactory provideFactory(
      @ElementFactoryMap Map<String, AbstractElementFactory<?>> typeMap) {
    return new InteractableFactory(typeMap);
  }

  @Provides
  @Context
  public ExecutionContext provideContext(
      @Driver WebDriver driver, @Factory InteractableFactory factory) {
    return new ExecutionContext(driver, factory);
  }

  @Provides
  public Executor providExecutor(
      @HandlersMap Map<String, ActionHandler> handlers,
      @Context ExecutionContext context) {
    return new ExecutorImpl(handlers, context);
  }
}
