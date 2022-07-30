package com.gotriva.testing.antifa.execution;

import java.io.Closeable;
import java.net.URL;

import com.gotriva.testing.antifa.model.GenericPageObject;

/** This class represents the execution context for the {@link Executor}. */
public interface ExecutionContext extends Closeable {

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
