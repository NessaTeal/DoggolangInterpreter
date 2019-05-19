package org.vadimjprokopev.expression;

import java.util.List;
import java.util.Map;

public class IfExpression implements Expression<Void> {
    private PredicateExpression predicate;

    private List<Expression> ifBody;
    private List<Expression> elseBody;

    public IfExpression(PredicateExpression predicate, List<Expression> ifBody, List<Expression> elseBody) {
        this.predicate = predicate;
        this.ifBody = ifBody;
        this.elseBody = elseBody;
    }

    public ExpressionResult<Void> execute(Map<String, Integer> variables) {
        if (predicate.execute(variables).getResult()) {
            ifBody.forEach(expression -> expression.execute(variables));
        } else {
            elseBody.forEach(expression -> expression.execute(variables));
        }

        return new ExpressionResult<>(null);
    }
}
