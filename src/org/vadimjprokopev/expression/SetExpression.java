package org.vadimjprokopev.expression;

import org.vadimjprokopev.token.Token;

import java.util.Map;

public class SetExpression implements Expression<Void> {

    private Token variable;
    private Expression<Integer> value;

    public SetExpression(Token variable, Expression<Integer> value) {
        this.variable = variable;
        this.value = value;
    }

    public ExpressionResult<Void> execute(Map<String, Integer> variables) {
        variables.put(variable.getVariableName(), value.execute(variables).getResult());
        return new ExpressionResult<>(null);
    }
}
