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

        List<Token> ifBodyTokenStream = new ArrayList<>();

        while (peekNextElement().getTokenType() != TokenType.ELSE) {
            getNextToken(ifBodyTokenStream);
        }

        ifBodyTokenStream.add(new Token(TokenType.EOF, null, null));

        ExpressionParser ifBodyParser = new ExpressionParser(ifBodyTokenStream);

        List<Expression> ifBody = new ArrayList<>();

        while(ifBodyParser.hasNext()) {
            ifBody.add(ifBodyParser.getNextExpression());
        }

        skipElement();

        List<Token> elseBodyTokenStream = new ArrayList<>();

        while (peekNextElement().getTokenType() != TokenType.END_IF) {
            getNextToken(elseBodyTokenStream);
        }

        elseBodyTokenStream.add(new Token(TokenType.EOF, null, null));

        ExpressionParser elseBodyParser = new ExpressionParser(elseBodyTokenStream);

        List<Expression> elseBody = new ArrayList<>();

        while(elseBodyParser.hasNext()) {
            elseBody.add(elseBodyParser.getNextExpression());
        }

        skipElement();

        return new IfExpression(predicateExpression, ifBody, elseBody);
    }

    private WhileExpression parseWhileExpression() {
        PredicateExpression predicateExpression = parsePredicateExpression();
        if (getNextToken().getTokenType() != TokenType.THEN_WHILE) {
            throw new IllegalArgumentException();
        }

        List<Token> bodyTokenStream = new ArrayList<>();

        while (peekNextElement().getTokenType() != TokenType.END_WHILE) {
            getNextToken(bodyTokenStream);
        }

        bodyTokenStream.add(new Token(TokenType.EOF, null, null));

        ExpressionParser bodyParser = new ExpressionParser(bodyTokenStream);

        List<Expression> body = new ArrayList<>();

        while(bodyParser.hasNext()) {
            body.add(bodyParser.getNextExpression());
        }

        skipElement();

        return new WhileExpression(predicateExpression, body);
    }

    private PredicateExpression parsePredicateExpression() {
        List<Token> predicateExpressionTokens = new ArrayList<>();

        getNextToken(predicateExpressionTokens);
        getNextToken(predicateExpressionTokens);
        getNextToken(predicateExpressionTokens);

        return new PredicateExpression(predicateExpressionTokens);
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
