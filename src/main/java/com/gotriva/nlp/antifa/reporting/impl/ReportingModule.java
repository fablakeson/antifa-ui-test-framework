package com.gotriva.nlp.antifa.reporting.impl;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.gotriva.nlp.antifa.reporting.Reporter;

import java.lang.annotation.Retention;
import java.util.Properties;
import javax.inject.Qualifier;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/** Provides presentation classes bindings. */
public class ReportingModule extends AbstractModule {

  @Qualifier
  @Retention(RUNTIME)
  @interface VelocityProperties {}

  @Provides
  @VelocityProperties
  public Properties proveideVelocityProperties() {
    Properties props = new Properties();
    /** Add properties here */
    props.setProperty("resource.loaders", "class,file");
    props.setProperty(
        "resource.loader.class.class",
        "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
    return props;
  }

  @Provides
  public VelocityEngine provideVelocity(@VelocityProperties Properties props) {
    return new VelocityEngine(props);
  }

  @Provides
  public VelocityContext provideVelocityContext() {
    return new VelocityContext();
  }

  @Provides
  public Reporter provideReportWriter(VelocityEngine velocity, VelocityContext context) {
    return new ReporterImpl(velocity, context);
  }
}
