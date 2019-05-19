package org.vadimjprokopev.expression;

import org.vadimjprokopev.token.Token;

import java.util.Map;

public class PrintExpression implements Expression<Void> {
    private Token variable;

    public PrintExpression(Token variable) {
        this.variable = variable;
    }

    public ExpressionResult<Void> execute(Map<String, Integer> variables) {
        System.out.println(variables.get(variable.getVariableName()));
        return new ExpressionResult<>(null);
    }
}
