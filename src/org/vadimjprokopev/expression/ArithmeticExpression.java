package org.vadimjprokopev.expression;

import org.vadimjprokopev.token.Token;
import org.vadimjprokopev.token.TokenType;

import java.util.List;
import java.util.Map;

public class ArithmeticExpression implements Expression<Integer> {

    private List<Token> arithmeticExpression;

    public ArithmeticExpression(List<Token> arithmeticExpression) {
        this.arithmeticExpression = arithmeticExpression;
    }

    public ExpressionResult<Integer> execute(Map<String, Integer> variables) {
        Token firstOperandToken = arithmeticExpression.get(0);
        Token secondOperandToken = arithmeticExpression.get(2);
        Token operationToken = arithmeticExpression.get(1);

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

        if (operationToken.getTokenType() == TokenType.ADD) {
            return new ExpressionResult<>(firstOperand + secondOperand);
        } else if (operationToken.getTokenType() == TokenType.SUBTRACT) {
            return new ExpressionResult<>(firstOperand - secondOperand);
        } else {
            return new ExpressionResult<>(firstOperand * secondOperand);
        }
    }
}
