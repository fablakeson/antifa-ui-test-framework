package com.gotriva.testing.antifa.execution.impl;

import com.gotriva.testing.antifa.element.Interactable;
import com.gotriva.testing.antifa.exception.ExecutionException;
import com.gotriva.testing.antifa.execution.Executor;
import com.gotriva.testing.antifa.handler.ActionHandler;
import com.gotriva.testing.antifa.handler.InteractableActionHandler;
import com.gotriva.testing.antifa.handler.PageObjectActionHandler;
import com.gotriva.testing.antifa.model.Command;
import com.gotriva.testing.antifa.model.ExecutionContext;
import com.gotriva.testing.antifa.model.ExecutionResult;
import com.gotriva.testing.antifa.model.ExecutionStep;
import com.gotriva.testing.antifa.model.GenericPageObject;
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

  /** The result of this execution. */
  private final ExecutionResult result;

  /** The execution status. */
  private Status status = Status.NOT_STARTED;

  /** Default constructor. */
  ExecutorImpl(
      Map<String, ActionHandler> handlers, ExecutionContext context, ExecutionResult result) {
    this.handlers = handlers;
    this.context = context;
    this.result = result;
  }

  @Override
  @SuppressWarnings("unchecked")
  public ExecutionResult execute(List<Command> commands) {
    LOGGER.info("Starting execution...");
    /** Validate execution state. */
    if (status != Status.NOT_STARTED) {
      throw new IllegalStateException("Executor has already started execution.");
    }
    /** Set execution status. */
    status = Status.EXECUTING;

    /** Within execution context */
    try (context) {
      /** Commands must be executed at the given order. */
      for (Command command : commands) {
        /** Start and end time for this command execution. */
        final ExecutionStep.Builder executionStep =
            ExecutionStep.builder().setCommand(command).startNow();
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
            // TODO: Add correct exception
            /** Handler not reconigsed */
            throw new ExecutionException(
                "Handler for command '" + command.getCommand() + "' not found.");
          }
          // TODO: add screenshot capability
          // ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
          String snapshot = null;
          /** This execution step was successful. */
          result.addStep(executionStep.endNow().withSuccess().setSnapshot(snapshot).build());
          LOGGER.info("Executing command: {} ... SUCCESS", command);
        } catch (RuntimeException re) {
          /** This execution step was failed. */
          result.addStep(executionStep.endNow().withFail().build());
          LOGGER.info("Executing command: {} ... FAIL", command);
          LOGGER.error("Error executing command.", re);
          /** Break the command execution loop */
          break;
        } finally {
          result.finish();
        }
      }
    }
    /** Set final execution status */
    status = Status.FINISHED;
    LOGGER.info("Finishing execution...");

    return result;
  }

  /** Getters */
  @Override
  public Status getStatus() {
    return status;
  }

  @Override
  public ExecutionResult getResult() {
    return result;
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
   * @param handler the given handler.
   * @param interactable the interactable.
   * @param parameter the parameter
   */
  private <T extends Interactable> void handleInteractable(
      InteractableActionHandler<T> handler, T interactable, String parameter) {
    handler.handle(interactable, parameter);
  }

  /**
   * Gets the interactable on context for given command. Creates one if it does not exists.
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
