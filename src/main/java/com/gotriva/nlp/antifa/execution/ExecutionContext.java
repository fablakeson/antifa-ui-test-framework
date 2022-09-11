package com.gotriva.nlp.antifa.execution;

import com.gotriva.nlp.antifa.element.ElementMetadata;
import com.gotriva.nlp.antifa.model.GenericPageObject;
import java.io.Closeable;
import java.net.URL;

/** This class represents the execution context for the {@link Executor}. */
public interface ExecutionContext extends Closeable {

  /**
   * Sets element metadata for this execution context.
   *
   * @param element the element id.
   * @param metadata the element metadata.
   */
  public void setMetadata(String element, ElementMetadata metadata);

  /**
   * Return the metadata for this element id.
   *
   * @param element the element id.
   * @return the element stored metadata or a default value.
   */
  public ElementMetadata getMetadata(String element);

  /**
   * Gets the current execution context page.
   *
   * @return
   */
  public GenericPageObject getCurrentPage();

  /** Closes the current execution context page. */
  public GenericPageObject closeCurrentPage();

  /** Creates a new page on context. */
  public GenericPageObject openPage(String name);

  /** Creates a new page on context for the given URL. */
  public GenericPageObject openPage(String name, URL url);

  /**
   * Gets the current page screenshot.
   *
   * @return the screenshot image base-64 representation
   */
  public String getScreenshot();
}
