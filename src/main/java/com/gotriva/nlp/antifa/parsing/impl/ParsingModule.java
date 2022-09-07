package com.gotriva.nlp.antifa.parsing.impl;

import static com.gotriva.nlp.antifa.constants.PipelinePropertiesConstants.ANNOTATORS;
import static com.gotriva.nlp.antifa.constants.PipelinePropertiesConstants.ANNOTATORS_VALUE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.gotriva.nlp.antifa.parsing.Interpreter;
import com.gotriva.nlp.antifa.parsing.Parser;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import java.lang.annotation.Retention;
import java.util.Properties;
import javax.inject.Qualifier;
import javax.inject.Singleton;

/** Provides parsing classes bindings. */
public class ParsingModule extends AbstractModule {

  @Qualifier
  @Retention(RUNTIME)
  @interface PipelineProperties {}

  @Provides
  @Singleton
  @PipelineProperties
  public Properties provideProps() {
    Properties props = new Properties();
    props.setProperty(ANNOTATORS, ANNOTATORS_VALUE);
    return props;
  }

  @Provides
  @Singleton
  public StanfordCoreNLP providePipeline(@PipelineProperties Properties props) {
    return new StanfordCoreNLP(props);
  }

  @Provides
  @Singleton
  public Interpreter provideInterpreter() {
    return new InterpreterImpl();
  }

  @Provides
  @Singleton
  public Parser provideParser(StanfordCoreNLP pipeline, Interpreter interpreter) {
    return new ParserImpl(pipeline, interpreter);
  }
}
