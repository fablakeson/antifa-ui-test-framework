package com.gotriva.testing.antifa.factory.impl;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

import javax.inject.Qualifier;
import javax.inject.Singleton;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
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
import com.gotriva.testing.antifa.factory.AbstractElementFactory;

/** This class handles the factory bindings. */
public class FactorySubModule extends AbstractModule {

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @interface ElementFactoryMap {
    }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Factory {
    }

    @Provides
    @Singleton
    @ElementFactoryMap
    public Map<String, AbstractElementFactory<?>> provideElementFactoryMap() {
        return ImmutableMap.<String, AbstractElementFactory<?>>builder()
                /** Button input type names */
                .put(ElementConstants.BUTTON, (element) -> new Button(element))
                /** Check input type names */
                .put(ElementConstants.CHECKBOX, (element) -> new Checkbox(element))
                .put(ElementConstants.OPT_IN, (element) -> new Checkbox(element))
                /** File input type names */
                .put(ElementConstants.FILE, (element) -> new File(element))
                /** Image type names */
                .put(ElementConstants.IMAGE, (element) -> new Image(element))
                /** Label type names */
                .put(ElementConstants.LABEL, (element) -> new Label(element))
                .put(ElementConstants.HEADER, (element) -> new Label(element))
                .put(ElementConstants.CAPTION, (element) -> new Label(element))
                .put(ElementConstants.MESSAGE, (element) -> new Label(element))
                /** Option type names */
                .put(ElementConstants.RADIO, (element) -> new Radio(element))
                .put(ElementConstants.OPTION, (element) -> new Radio(element))
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
    public InteractableElementFactory provideFactory(
            @ElementFactoryMap Map<String, AbstractElementFactory<?>> typeMap) {
        return new InteractableElementFactory(typeMap);
    }
}
