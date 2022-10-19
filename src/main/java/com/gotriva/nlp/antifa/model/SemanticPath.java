package com.gotriva.nlp.antifa.model;

import static com.gotriva.nlp.antifa.constants.DefaultConstants.DEFAULT_SEPARATOR;
import static com.gotriva.nlp.antifa.model.Command.ComponentType.ACTION;
import static com.gotriva.nlp.antifa.model.Command.ComponentType.OBJECT;
import static com.gotriva.nlp.antifa.model.Command.ComponentType.PARAMETER;
import static com.gotriva.nlp.antifa.model.Command.ComponentType.TYPE;

import com.google.common.collect.ImmutableList;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.GrammaticalRelation;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** This class represents a path traversal on a semantic graph. */
public class SemanticPath {

  private static final Logger LOGGER = LoggerFactory.getLogger(SemanticPath.class);

  /** The builder for semantic path traversal. */
  public static class Builder {

    /** Builder steps that guarantees no steps will repeat source and destination. */
    private LinkedHashSet<Step> steps;

    private Builder(LinkedHashSet<Step> steps) {
      this.steps = steps;
    }

    /** Get a new Buider for {@link SemanticPath}. */
    public static Builder newBuilder() {
      return new Builder(new LinkedHashSet<>());
    }

    /**
     * Adds a new step to this path.
     *
     * @param step the step builder
     * @return the Builder
     */
    public Builder newStep(Step.Builder step) {
      steps.add(step.build());
      return this;
    }

    /**
     * @return
     */
    public SemanticPath build() {
      return new SemanticPath(ImmutableList.copyOf(steps));
    }
  }

  /** The visitation steps triples. */
  private ImmutableList<Step> steps;

  /** Default constructor */
  private SemanticPath(ImmutableList<Step> steps) {
    this.steps = steps;
  }

  /**
   * Traverses the semantic graph on given path order and returns the interpreted action.
   *
   * @param graph the semantic graph
   * @return the decoded action if any
   */
  public Optional<Command> traverse(final SemanticGraph graph) {
    LOGGER.debug("============== Start Traversing ===========");
    LOGGER.debug("Steps: {}", steps);

    /** Get the graph root */
    IndexedWord root = graph.getFirstRoot();

    /** Sets the action root */
    Map<Command.ComponentType, Deque<IndexedWord>> knowledgeFrame = new HashMap<>();
    putOrAppend(knowledgeFrame, steps.get(0).getSource(), root);

    LOGGER.debug("My root is: {}", root);

    /** Traverses the graph from root */
    for (Step step : steps) {
      LOGGER.debug("Current step: {}", step);

      /** Get source. */
      IndexedWord source = getFirst(knowledgeFrame, step.getSource());
      LOGGER.debug("Source: {}", source);

      /** Get edge. */
      GrammaticalRelation edge = step.getEdge();
      LOGGER.debug("Edge: {}", edge);

      /** Get destination. */
      List<IndexedWord> children = new ArrayList<>(graph.getChildrenWithReln(source, edge));

      /** Check if destination was found */
      if (children.size() <= step.getOrder()) {
        LOGGER.debug("Desination not found. Ending traversal.");
        LOGGER.debug("========================================");
        return Optional.empty();
      }

      IndexedWord destination = children.get(step.getOrder());
      LOGGER.debug("Dest: {}", destination);

      /** Add destination to the graph */
      putOrAppend(knowledgeFrame, step.getDestination(), destination);

      /** Check if knowledge frame is valid */
      if (!isValid(knowledgeFrame)) {
        return Optional.empty();
      }
    }

    /** Build command from knowledge frame map with mandatory fields. */
    final Command.Builder commandBuilder =
        Command.builder().setAction(concatString(knowledgeFrame.get(ACTION)));

    /** Optional components */
    if (knowledgeFrame.containsKey(OBJECT)) {
      commandBuilder.setObject(concatString(knowledgeFrame.get(OBJECT)));
    }
    if (knowledgeFrame.containsKey(PARAMETER)) {
      commandBuilder.setParameter(concatString(knowledgeFrame.get(PARAMETER)));
    }
    if (knowledgeFrame.containsKey(TYPE)) {
      commandBuilder.setType(concatString(knowledgeFrame.get(TYPE)));
    }

    LOGGER.debug("Map: {}", knowledgeFrame);
    LOGGER.debug("========================================");

    return Optional.of(commandBuilder.build());
  }

  /**
   * @return the {@link SemanticPath} builder.
   */
  public static Builder builder() {
    return Builder.newBuilder();
  }

  /**
   * Put or appends a single value to the map key.
   *
   * @param map the map
   * @param key the key
   * @param value the value
   */
  private <T> void putOrAppend(Map<T, Deque<IndexedWord>> map, T key, IndexedWord value) {
    if (map.containsKey(key)) {
      map.get(key).addFirst(value);
    } else {
      LinkedList<IndexedWord> list = new LinkedList<>();
      list.add(value);
      map.put(key, list);
    }
  }

  /**
   * Gets the first value from map for a given key.
   *
   * @param <T>
   * @param map
   * @param key
   * @return
   */
  private <T> IndexedWord getFirst(Map<T, Deque<IndexedWord>> map, T key) {
    return map.get(key).peekFirst();
  }

  /**
   * Contatenates the original text strings from the given list.
   *
   * @param list the list to concat strings
   * @return the contatenated string
   */
  private String concatString(Deque<IndexedWord> list) {
    return list.stream()
        .map(IndexedWord::originalText)
        .collect(Collectors.joining(DEFAULT_SEPARATOR));
  }

  /**
   * Check if interpretation is valid.
   *
   * @param knowledgeFrame the knowledge frame to be checked.
   * @return true if interpretation is valid, false otherwise
   */
  private boolean isValid(Map<Command.ComponentType, Deque<IndexedWord>> knowledgeFrame) {
    /** Check if ACTION is valid. */
    if (hasPrefix(knowledgeFrame, ACTION, "#")) {
      return false;
    }
    /** Check if TYPE is valid. */
    if (hasPrefix(knowledgeFrame, TYPE, "#")) {
      return false;
    }
    /** Check if PARAMETER is valid */
    if (hasPrefix(knowledgeFrame, PARAMETER, "#object")) {
      return false;
    }
    /** Check if OBJECT is valid */
    if (hasPrefix(knowledgeFrame, OBJECT, "#param")) {
      return false;
    }
    return true;
  }

  /**
   * Check if given type is found on knowledge frame within given prefix.
   *
   * @param knowledgeFrame the knowledge frame
   * @param type the command component type
   * @param prefix the string prefix
   * @return true if it have the prefix, false otherwise.
   */
  private boolean hasPrefix(
      Map<Command.ComponentType, Deque<IndexedWord>> knowledgeFrame,
      Command.ComponentType type,
      String prefix) {
    return knowledgeFrame.containsKey(type)
        && getFirst(knowledgeFrame, type).originalText().startsWith(prefix);
  }
}
