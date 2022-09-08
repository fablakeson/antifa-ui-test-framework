package com.gotriva.nlp.antifa.element.impl;

import com.gotriva.nlp.antifa.element.AbstractElement;
import com.gotriva.nlp.antifa.element.Settable;
import java.util.Optional;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

/** This class represents a range input on UI. */
public class Range extends AbstractElement implements Settable {

  Range(WebElement element) {
    super(element);
  }

  @Override
  public void setValue(String value) {
    /** Put focus on element. */
    focus();
    /** Check value type */
    if (value.endsWith("%")) {
      /** Value is relative */
      setRelativeValue(Double.parseDouble(value.replace("%", "")) / 100.0);
    } else {
      /** Value is absolute */
      setAbsuluteValue(Double.parseDouble(value));
    }
  }

  /**
   * Sets the rage to approximated percent value.
   *
   * @param percent the range percent to set
   */
  private void setRelativeValue(Double percent) {
    /** Get the absolute value */
    Double range = getMax() - getMin();
    Double value = range * percent;
    setAbsuluteValue(value);
  }

  /**
   * Sets an absolute value to the range. Get the closest lowest value if it is not present.
   *
   * @param value the value to set.
   */
  private void setAbsuluteValue(Double value) {
    value = sanitize(value);
    /** Get the closest <= value */
    for (Double currentValue = getValue();
        Double.compare(currentValue, value) < 0;
        currentValue = increment())
      ;
    for (Double currentValue = getValue();
        Double.compare(currentValue, value) > 0;
        currentValue = decrement())
      ;
  }

  /** Put focus on current element. */
  private void focus() {
    element.click();
  }

  /** Gets element value property. */
  private Double getValue() {
    return Double.parseDouble(element.getAttribute("value"));
  }

  /** Grants that value will be bounded to Min and Max */
  private Double sanitize(Double value) {
    return Math.max(Math.min(value, getMax()), getMin());
  }

  /** Gets element max property. */
  private Double getMax() {
    return Optional.ofNullable(element.getAttribute("max"))
        .map(Double::parseDouble)
        .orElse(Double.MAX_VALUE);
  }

  /** Gets element min property. */
  private Double getMin() {
    return Optional.ofNullable(element.getAttribute("min"))
        .map(Double::parseDouble)
        .orElse(Double.MIN_VALUE);
  }

  /** Increment current range step. */
  private Double increment() {
    element.sendKeys(Keys.RIGHT);
    return getValue();
  }

  /** Decrement current range step. */
  private Double decrement() {
    element.sendKeys(Keys.LEFT);
    return getValue();
  }
}
