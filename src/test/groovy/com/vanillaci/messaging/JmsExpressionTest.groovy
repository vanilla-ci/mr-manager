package com.vanillaci.messaging;

import org.junit.Test;

public class JmsExpressionTest {

  @Test
  public void testEmpty() throws Exception {
    assert JmsExpression.EMPTY.toString() == '';

    assert JmsExpression.EMPTY.and(JmsExpression.EMPTY) == JmsExpression.EMPTY
    assert JmsExpression.EMPTY.or(JmsExpression.EMPTY) == JmsExpression.EMPTY
    assert JmsExpression.EMPTY.or('') == JmsExpression.EMPTY
    assert JmsExpression.EMPTY.and('') == JmsExpression.EMPTY
    JmsExpression expression = JmsExpression.from("weight = 'test'")
    assert expression.and(JmsExpression.EMPTY).toString() == expression.toString()
    assert expression.and('').toString() == expression.toString()
    assert expression.or(JmsExpression.EMPTY).toString() == expression.toString()
    assert expression.or('').toString() == expression.toString()
  }

  @Test
  public void testBasicExpression() throws Exception {
    assert JmsExpression.from("weight = 'test'").toString() == "weight = 'test'"
  }

  @Test
  public void testBasicAnd() throws Exception {
    JmsExpression weight = JmsExpression.from("weight = 'test'")
    JmsExpression os = JmsExpression.from("os in ('linux', 'unix')")
    assert weight.and(os).toString() == "weight = 'test' AND os in ('linux', 'unix')"
  }

  @Test
  public void testBasicOr() throws Exception {
    JmsExpression weight = JmsExpression.from("weight = 1")
    JmsExpression weightIsNull = JmsExpression.from("weight IS NULL")

    assert weight.or(weightIsNull).toString() == "weight = 1 OR (weight IS NULL)"

    assert weight.or("weight IS NULL").toString() == "weight = 1 OR (weight IS NULL)"
  }
}
