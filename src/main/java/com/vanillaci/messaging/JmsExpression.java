package com.vanillaci.messaging;

import java.util.ArrayList;
import java.util.List;

/**
 * User: michaelnielson
 * Date: 5/26/14
 */
public class JmsExpression {

  public static JmsExpression EMPTY = new JmsExpression(null);

  private final String expression;

  private JmsExpression andExpression;

  private JmsExpression(String expression) {
    this.expression = expression;
  }

  JmsExpression or(String expression) {
    JmsExpression jmsExpression = from(expression);

    return or(jmsExpression);
  }

  JmsExpression or(JmsExpression jmsExpression) {
    if (jmsExpression == EMPTY) {
      return this;
    }

    return new JmsExpression(toString() + " OR (" + jmsExpression.toString() + ")");
  }

  JmsExpression and(String expression) {
    JmsExpression jmsExpression = from(expression);
    if (jmsExpression == EMPTY) {
      return this;
    }
    jmsExpression.andExpression = this;
    return jmsExpression;
  }

  JmsExpression and(JmsExpression jmsExpression) {
    return jmsExpression.and(this.toString());
  }

  static JmsExpression from(String expression) {
    if (expression == null || expression.length() == 0) {
      return EMPTY;
    }
    return new JmsExpression(expression);
  }

  @Override
  public String toString() {
    JmsExpression current = this;
    List<String> subExpressions = new ArrayList<>();
    do {
      if(current.expression != null) {
        subExpressions.add(current.expression);
      }
      current = current.andExpression;
    } while (current != null);

    return String.join(" AND ", subExpressions);
  }
}
