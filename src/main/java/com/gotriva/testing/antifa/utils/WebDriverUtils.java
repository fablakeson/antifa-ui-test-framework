package com.gotriva.testing.antifa.utils;

import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/** Utility methods to interact with {@link WebDriver} */
public class WebDriverUtils {

  public static WebDriver createWebDriver(Properties properties) {
    /** Get chromedriver as default */
    // TODO: get driver by properties
    return new ChromeDriver();
  }
}
