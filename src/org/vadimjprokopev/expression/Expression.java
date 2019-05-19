package org.vadimjprokopev.expression;

import java.util.Map;

public interface Expression<T> {
    ExpressionResult<T> execute(Map<String, Integer> variables);
}
