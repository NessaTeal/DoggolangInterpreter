package org.vadimjprokopev.expression;

import java.util.List;
import java.util.Map;

public class WhileExpression implements Expression<Void> {
    private PredicateExpression predicate;

    private List<Expression> body;

    public WhileExpression(PredicateExpression predicate, List<Expression> body) {
        this.predicate = predicate;
        this.body = body;
    }

    public ExpressionResult<Void> execute(Map<String, Integer> variables) {
        while (predicate.execute(variables).getResult()) {
            body.forEach(expression -> expression.execute(variables));
        }

        return new ExpressionResult<>(null);
    }
}
