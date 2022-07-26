package com.gotriva.testing.antifa.handler;

import com.gotriva.testing.antifa.model.ExecutionContext;

/** This interface represents a hadler that manipulates the page objects context. */
public interface PageObjectHandler extends Handler {

    /**
     * Handles the current execution page actions.
     * 
     * @param context the execution context
     */
    void handle(ExecutionContext context, String page, String parameter);

}
