package com.gotriva.testing.antifa.factory.impl;

import static com.gotriva.testing.antifa.constants.ElementConstants.BUTTON;
import static com.gotriva.testing.antifa.constants.ElementConstants.CAPTION;
import static com.gotriva.testing.antifa.constants.ElementConstants.CHECKBOX;
import static com.gotriva.testing.antifa.constants.ElementConstants.FIELD;
import static com.gotriva.testing.antifa.constants.ElementConstants.FILE;
import static com.gotriva.testing.antifa.constants.ElementConstants.HEADER;
import static com.gotriva.testing.antifa.constants.ElementConstants.IMAGE;
import static com.gotriva.testing.antifa.constants.ElementConstants.INPUT;
import static com.gotriva.testing.antifa.constants.ElementConstants.LABEL;
import static com.gotriva.testing.antifa.constants.ElementConstants.MESSAGE;
import static com.gotriva.testing.antifa.constants.ElementConstants.OPTION;
import static com.gotriva.testing.antifa.constants.ElementConstants.OPT_IN;
import static com.gotriva.testing.antifa.constants.ElementConstants.RADIO;
import static com.gotriva.testing.antifa.constants.ElementConstants.RANGE;
import static com.gotriva.testing.antifa.constants.ElementConstants.TEXTBOX;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.gotriva.testing.antifa.element.impl.Button;
import com.gotriva.testing.antifa.element.impl.Checkbox;
import com.gotriva.testing.antifa.element.impl.File;
import com.gotriva.testing.antifa.element.impl.Image;
import com.gotriva.testing.antifa.element.impl.Label;
import com.gotriva.testing.antifa.element.impl.Radio;
import com.gotriva.testing.antifa.element.impl.Range;
import com.gotriva.testing.antifa.element.impl.Text;
import com.gotriva.testing.antifa.factory.CompositeElementFactory;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Qualifier;
import javax.inject.Singleton;

/** This class handles the factory bindings. */
public class FactorySubModule extends AbstractModule {

  @Qualifier
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Factory {}

  @Provides
  @Singleton
  @Factory
  public CompositeElementFactory provideFactory() {
    return CompositeElementFactoryImpl.builder()
        /** Button input type names */
        .registerFactory(BUTTON, (element) -> new Button(element))
        /** Check input type names */
        .registerFactory(CHECKBOX, (element) -> new Checkbox(element))
        .registerFactory(OPT_IN, (element) -> new Checkbox(element))
        /** File input type names */
        .registerFactory(FILE, (element) -> new File(element))
        /** Image type names */
        .registerFactory(IMAGE, (element) -> new Image(element))
        /** Label type names */
        .registerFactory(LABEL, (element) -> new Label(element))
        .registerFactory(HEADER, (element) -> new Label(element))
        .registerFactory(CAPTION, (element) -> new Label(element))
        .registerFactory(MESSAGE, (element) -> new Label(element))
        /** Option type names */
        .registerFactory(RADIO, (element) -> new Radio(element))
        .registerFactory(OPTION, (element) -> new Radio(element))
        /** Range type names */
        .registerFactory(RANGE, (element) -> new Range(element))
        /** Text type names */
        .registerFactory(INPUT, (element) -> new Text(element))
        .registerFactory(FIELD, (element) -> new Text(element))
        .registerFactory(TEXTBOX, (element) -> new Text(element))
        /** return composite factory */
        .build();
  }
}
