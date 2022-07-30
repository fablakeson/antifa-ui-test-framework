package com.gotriva.testing.antifa.execution.impl;

import com.gotriva.testing.antifa.element.Interactable;
import com.gotriva.testing.antifa.exception.ExecutionException;
import com.gotriva.testing.antifa.execution.ExecutionContext;
import com.gotriva.testing.antifa.execution.Executor;
import com.gotriva.testing.antifa.handler.ActionHandler;
import com.gotriva.testing.antifa.handler.InteractableActionHandler;
import com.gotriva.testing.antifa.handler.PageObjectActionHandler;
import com.gotriva.testing.antifa.model.Command;
import com.gotriva.testing.antifa.model.ExecutionResult;
import com.gotriva.testing.antifa.model.ExecutionStep;
import com.gotriva.testing.antifa.model.GenericPageObject;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This class implements {@link Executor}. */
public class ExecutorImpl implements Executor {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorImpl.class);

  /** The handler strategies for the commands names. */
  private final Map<String, ActionHandler> handlers;

  /** The execution context for this execution. */
  private final ExecutionContext context;

  /** Default constructor. */
  ExecutorImpl(
      Map<String, ActionHandler> handlers, ExecutionContext context) {
    this.handlers = handlers;
    this.context = context;
  }

  @Override
  @SuppressWarnings("unchecked")
  public ExecutionResult execute(List<Command> commands) {
    LOGGER.info("Starting execution...");
    ExecutionResult.Builder result = ExecutionResult.builder();
    /** Within execution context */
    try (context) {
      /** Commands must be executed at the given order. */
      for (Command command : commands) {
        /** Start and end time for this command execution. */
        final ExecutionStep.Builder executionStep = ExecutionStep.builder().setCommand(command).startNow();
        /** Try to execute the command */
        try {
          /** Get handler strategy for this command. */
          ActionHandler handler = handlers.get(command.getCommand());
          /** Get appropriated handler type. */
          if (handler instanceof InteractableActionHandler) {
            /** Get the interactable object from context. */
            Interactable interactable = getInteractableFor(command);
            /** Try handle this interactable. */
            handleInteractable(
                (InteractableActionHandler<Interactable>) handler,
                interactable,
                command.getParameter());
          } else if (handler instanceof PageObjectActionHandler) {
            /** Try handle this page context action. */
            handlePageObject(
                (PageObjectActionHandler) handler, command.getObject(), command.getParameter());
          } else {
            /** Handler not reconigsed */
            throw new ExecutionException(
                "Handler for command '" + command.getCommand() + "' not found.");
          }
          /** This execution step was successful. */
          result.addStep(executionStep.endNow().withSuccess().setScreenshot(context.getScreenshot()));
          LOGGER.info("Executing command: {} ... SUCCESS", command);
        } catch (Exception e) {
          /** This execution step was failed. */
          result.addStep(executionStep.endNow().withFail());
          /** Log the fail */
          LOGGER.info("Executing command: {} ... FAIL", command);
          /** Wraps on execution exception if needed */
          if (e instanceof ExecutionException) {
            throw e;
          }
          throw new ExecutionException(e.getMessage(), e);
        }
      }
      result.withSuccess();
    } catch (ExecutionException ex) {
      LOGGER.error("Error executing command.", ex);
      result.withFail(ex.getMessage());
    } catch (IOException ioe) {
      LOGGER.error("Error closing the driver.", ioe);
    }
    /** Set final execution status */
    LOGGER.info("Finishing execution...");
    return result.build();
  }

  /**
   * Handles the page object with the given handler.
   *
   * @param <T>
   * @param handler
   * @param page
   * @param url
   */
  private <T extends PageObjectActionHandler> void handlePageObject(
      PageObjectActionHandler handler, String page, String parameter) {
    handler.handle(context, page, parameter);
  }

  /**
   * Handles the interactable with the given handler.
   *
   * @param <T>
   * @param handler      the given handler.
   * @param interactable the interactable.
   * @param parameter    the parameter
   */
  private <T extends Interactable> void handleInteractable(
      InteractableActionHandler<T> handler, T interactable, String parameter) {
    handler.handle(interactable, parameter);
  }

  /**
   * Gets the interactable on context for given command. Creates one if it does
   * not exists.
   *
   * @param command
   * @return
   * @throws InterruptedException
   */
  private Interactable getInteractableFor(Command command) {
    /** Get current page. */
    GenericPageObject pageObject = context.getCurrentPage();
    /** Check if current object not exists on page */
    if (!pageObject.hasElement(command.getObject())) {
      /** Fail if type is not present */
      if (Objects.isNull(command.getType())) {
        throw new ExecutionException(
            MessageFormat.format(
                "Command 'type' must be present when object '{0}' is referenced at first time.",
                command.getObject()));
      }
      /** Creates the object on this page */
      pageObject.addElement(command.getObject(), By.name(command.getObject()), command.getType());
    }
    /** returns the interactable element */
    return pageObject.getElement(command.getObject());
  }
}
