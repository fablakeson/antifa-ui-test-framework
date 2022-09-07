package com.gotriva.nlp.antifa.execution.impl;

import static com.gotriva.nlp.antifa.constants.WebDriverPropertiesConstants.WEB_DRIVER;
import static com.gotriva.nlp.antifa.constants.WebDriverPropertiesConstants.WEB_DRIVER_CHROME_VALUE;
import static com.gotriva.nlp.antifa.constants.WebDriverPropertiesConstants.WEB_DRIVER_EDGE_VALUE;
import static com.gotriva.nlp.antifa.constants.WebDriverPropertiesConstants.WEB_DRIVER_FIREFOX_VALUE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.gotriva.nlp.antifa.execution.ExecutionContext;
import com.gotriva.nlp.antifa.execution.Executor;
import com.gotriva.nlp.antifa.factory.CompositeElementFactory;
import com.gotriva.nlp.antifa.factory.impl.FactorySubModule;
import com.gotriva.nlp.antifa.factory.impl.FactorySubModule.Factory;
import com.gotriva.nlp.antifa.strategy.ActionStrategy;
import com.gotriva.nlp.antifa.strategy.impl.StrategieSubModule;
import com.gotriva.nlp.antifa.strategy.impl.StrategieSubModule.strategiesMap;
import java.lang.annotation.Retention;
import java.util.Map;
import java.util.Properties;
import javax.inject.Qualifier;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

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
  @interface Context {}

  @Override
  protected void configure() {
    install(new StrategieSubModule());
    install(new FactorySubModule());
  }

  @Provides
  @Singleton
  @WebDriverProperties
  public Properties provideProps() {
    Properties props = new Properties();
    props.put(WEB_DRIVER, WEB_DRIVER_CHROME_VALUE);
    return props;
  }

  @Provides
  @Driver
  public WebDriver provideWebDriver(@WebDriverProperties Properties props) {
    switch (props.getProperty(WEB_DRIVER)) {
      case WEB_DRIVER_CHROME_VALUE:
        return new ChromeDriver();
      case WEB_DRIVER_FIREFOX_VALUE:
        return new FirefoxDriver();
      case WEB_DRIVER_EDGE_VALUE:
        return new EdgeDriver();
      default:
        return new ChromeDriver();
    }
  }

  @Provides
  @Context
  public ExecutionContext provideContext(
      @Driver WebDriver driver, @Factory CompositeElementFactory factory) {
    return new ExecutionContextImpl(driver, factory);
  }

  @Provides
  public Executor providExecutor(
      @strategiesMap Map<String, ActionStrategy> strategies, @Context ExecutionContext context) {
    return new ExecutorImpl(strategies, context);
  }
}
