package org.vadimjprokopev.expression;

import org.vadimjprokopev.token.Token;

import java.util.Map;

public class ConstantExpression implements Expression<Integer> {

    private Token constant;

    public ConstantExpression(Token constant) {
        this.constant = constant;
    }

    public ExpressionResult<Integer> execute(Map<String, Integer> variables) {
        return new ExpressionResult<>(constant.getConstantValue());
    }
}
