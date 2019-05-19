package org.vadimjprokopev.expression;

import org.vadimjprokopev.token.Token;
import org.vadimjprokopev.token.TokenType;

import java.util.List;
import java.util.Map;

public class PredicateExpression implements Expression<Boolean> {

    private List<Token> predicateExpression;

    public PredicateExpression(List<Token> predicateExpression) {
        this.predicateExpression = predicateExpression;
    }

    public ExpressionResult<Boolean> execute(Map<String, Integer> variables) {
        Token firstOperandToken = predicateExpression.get(0);
        Token secondOperandToken = predicateExpression.get(2);
        Token comparisonToken = predicateExpression.get(1);

        int firstOperand;
        int secondOperand;

        if (firstOperandToken.getTokenType() == TokenType.CONSTANT) {
            firstOperand = firstOperandToken.getConstantValue();
        } else {
            firstOperand = variables.get(firstOperandToken.getVariableName());
        }

        if (secondOperandToken.getTokenType() == TokenType.CONSTANT) {
            secondOperand = secondOperandToken.getConstantValue();
        } else {
            secondOperand = variables.get(secondOperandToken.getVariableName());
        }

        if (comparisonToken.getTokenType() == TokenType.MORE) {
            return new ExpressionResult<>(firstOperand > secondOperand);
        } else {
            return new ExpressionResult<>(firstOperand < secondOperand);
        }
    }
}
