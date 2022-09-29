package com.gotriva.nlp.antifa.element.impl;

import static com.gotriva.nlp.antifa.constants.ElementConstants.BUTTON;
import static com.gotriva.nlp.antifa.constants.ElementConstants.CHECKBOX;
import static com.gotriva.nlp.antifa.constants.ElementConstants.FIELD;
import static com.gotriva.nlp.antifa.constants.ElementConstants.FIGURE;
import static com.gotriva.nlp.antifa.constants.ElementConstants.FILE;
import static com.gotriva.nlp.antifa.constants.ElementConstants.HEADER;
import static com.gotriva.nlp.antifa.constants.ElementConstants.ICON;
import static com.gotriva.nlp.antifa.constants.ElementConstants.IMAGE;
import static com.gotriva.nlp.antifa.constants.ElementConstants.INPUT;
import static com.gotriva.nlp.antifa.constants.ElementConstants.LABEL;
import static com.gotriva.nlp.antifa.constants.ElementConstants.LIST;
import static com.gotriva.nlp.antifa.constants.ElementConstants.OPTION;
import static com.gotriva.nlp.antifa.constants.ElementConstants.OPTIONS;
import static com.gotriva.nlp.antifa.constants.ElementConstants.OPT_IN;
import static com.gotriva.nlp.antifa.constants.ElementConstants.RADIO;
import static com.gotriva.nlp.antifa.constants.ElementConstants.RANGE;
import static com.gotriva.nlp.antifa.constants.ElementConstants.TAG;
import static com.gotriva.nlp.antifa.constants.ElementConstants.TEXTBOX;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.gotriva.nlp.antifa.element.CompositeElementFactory;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Qualifier;
import javax.inject.Singleton;

/** This class handles the element creation bindings. */
public class ElemementSubModule extends AbstractModule {

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
        .registerFactory(ICON, (element) -> new Image(element))
        .registerFactory(FIGURE, (element) -> new Image(element))
        /** Label type names */
        .registerFactory(LABEL, (element) -> new Label(element))
        .registerFactory(HEADER, (element) -> new Label(element))
        .registerFactory(TAG, (element) -> new Label(element))
        /** Option type names */
        .registerFactory(RADIO, (element) -> new Radio(element))
        .registerFactory(OPTION, (element) -> new Radio(element))
        /** Range type names */
        .registerFactory(RANGE, (element) -> new Range(element))
        /** Select type names */
        .registerFactory(LIST, (element) -> new Selection(element))
        .registerFactory(OPTIONS, (element) -> new Selection(element))
        /** Text type names */
        .registerFactory(INPUT, (element) -> new Text(element))
        .registerFactory(FIELD, (element) -> new Text(element))
        .registerFactory(TEXTBOX, (element) -> new Text(element))
        /** return composite factory */
        .build();
  }
}
