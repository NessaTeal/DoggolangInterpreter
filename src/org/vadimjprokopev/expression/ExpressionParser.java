package org.vadimjprokopev.expression;

import org.vadimjprokopev.token.Token;
import org.vadimjprokopev.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class ExpressionParser {
    private List<Token> tokens;

    private int currentPosition = 0;

    public ExpressionParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token getNextToken(List<Token> body) {
        Token element = tokens.get(currentPosition);
        body.add(element);
        currentPosition++;
        return element;
    }

    private Token getNextToken() {
        Token element = tokens.get(currentPosition);
        currentPosition++;
        return element;
    }

    private void skipElement() {
        currentPosition++;
    }

    private Token peekNextElement() {
        return tokens.get(currentPosition);
    }

    public boolean hasNext() {
        return tokens.get(currentPosition).getTokenType() != TokenType.EOF;
    }

    private SetExpression parseSetExpression(Token settedVariable) {
        List<Token> setExpressionTokens = new ArrayList<>();
        setExpressionTokens.add(settedVariable);

        getNextToken(setExpressionTokens);
        getNextToken(setExpressionTokens);

        if (peekNextElement().getTokenType() == TokenType.ADD
                || peekNextElement().getTokenType() == TokenType.SUBTRACT
                || peekNextElement().getTokenType() == TokenType.MULTIPLY) {
            getNextToken(setExpressionTokens);
            getNextToken(setExpressionTokens);

            ArithmeticExpression arithmeticExpression = new ArithmeticExpression(setExpressionTokens.subList(2, 5));
            return new SetExpression(setExpressionTokens.get(0), arithmeticExpression);
        } else {
            ConstantExpression constantExpression = new ConstantExpression(setExpressionTokens.get(2));
            return new SetExpression(setExpressionTokens.get(0), constantExpression);
        }
    }

    private IfExpression parseIfExpression() {
        PredicateExpression predicateExpression = parsePredicateExpression();
        if (getNextToken().getTokenType() != TokenType.THEN_IF) {
            throw new IllegalArgumentException();
        }

        List<Expression> ifBody = parseExpressionsUntil(TokenType.ELSE);

        skipElement();

        List<Expression> elseBody = parseExpressionsUntil(TokenType.END_IF);

        skipElement();

        return new IfExpression(predicateExpression, ifBody, elseBody);
    }

    private WhileExpression parseWhileExpression() {
        PredicateExpression predicateExpression = parsePredicateExpression();
        if (getNextToken().getTokenType() != TokenType.THEN_WHILE) {
            throw new IllegalArgumentException();
        }

        List<Expression> expressions = parseExpressionsUntil(TokenType.END_WHILE);

        skipElement();

        return new WhileExpression(predicateExpression, expressions);
    }

    private PredicateExpression parsePredicateExpression() {
        List<Token> predicateExpressionTokens = new ArrayList<>();

        getNextToken(predicateExpressionTokens);
        getNextToken(predicateExpressionTokens);
        getNextToken(predicateExpressionTokens);

        return new PredicateExpression(predicateExpressionTokens);
    }

    private List<Expression> parseExpressionsUntil(TokenType tokenType) {
        List<Token> expressionsTokens = new ArrayList<>();
        while (peekNextElement().getTokenType() != tokenType) {
            getNextToken(expressionsTokens);
        }
        expressionsTokens.add(new Token(TokenType.EOF, null, null));

        ExpressionParser expressionsParser = new ExpressionParser(expressionsTokens);
        List<Expression> expressions = new ArrayList<>();
        while(expressionsParser.hasNext()) {
            expressions.add(expressionsParser.getNextExpression());
        }
        return expressions;
    }

    public Expression getNextExpression() {
        Token firstToken = getNextToken();

        if (firstToken.getTokenType() == TokenType.VARIABLE) {
            if (peekNextElement().getTokenType() != TokenType.SET) {
                return new PrintExpression(firstToken);
            } else {
                return parseSetExpression(firstToken);
            }
        } else if (firstToken.getTokenType() == TokenType.IF) {
            return parseIfExpression();
        } else if (firstToken.getTokenType() == TokenType.WHILE) {
            return parseWhileExpression();
        }

        throw new IllegalArgumentException();
    }
}
