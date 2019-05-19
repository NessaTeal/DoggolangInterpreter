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

    private Token getNextElement(List<Token> body) {
        Token element = tokens.get(currentPosition);
        body.add(element);
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

    public Expression getNextExpression() {
        List<Token> tokenStream = new ArrayList<>();
        Token nextElement = getNextElement(tokenStream);

        if (nextElement.getTokenType() == TokenType.VARIABLE) {
            if (peekNextElement().getTokenType() != TokenType.SET) {
                return new PrintExpression(tokenStream.get(0));
            } else {
                getNextElement(tokenStream);
                getNextElement(tokenStream);

                if (peekNextElement().getTokenType() == TokenType.ADD
                        || peekNextElement().getTokenType() == TokenType.SUBTRACT
                        || peekNextElement().getTokenType() == TokenType.MULTIPLY) {
                    getNextElement(tokenStream);
                    getNextElement(tokenStream);

                    ArithmeticExpression arithmeticExpression = new ArithmeticExpression(tokenStream.subList(2, 5));
                    return new SetExpression(tokenStream.get(0), arithmeticExpression);
                } else {
                    ConstantExpression constantExpression = new ConstantExpression(tokenStream.get(2));
                    return new SetExpression(tokenStream.get(0), constantExpression);
                }
            }
        } else if (nextElement.getTokenType() == TokenType.IF) {
            List<Token> predicateTokenStream = new ArrayList<>();

            getNextElement(predicateTokenStream);
            getNextElement(predicateTokenStream);
            getNextElement(predicateTokenStream);

            if (getNextElement(tokenStream).getTokenType() != TokenType.THEN_IF) {
                throw new IllegalArgumentException();
            }

            PredicateExpression predicateExpression = new PredicateExpression(predicateTokenStream);

            List<Token> ifBodyTokenStream = new ArrayList<>();

            while (peekNextElement().getTokenType() != TokenType.ELSE) {
                getNextElement(ifBodyTokenStream);
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
                getNextElement(elseBodyTokenStream);
            }

            elseBodyTokenStream.add(new Token(TokenType.EOF, null, null));

            ExpressionParser elseBodyParser = new ExpressionParser(elseBodyTokenStream);

            List<Expression> elseBody = new ArrayList<>();

            while(elseBodyParser.hasNext()) {
                elseBody.add(elseBodyParser.getNextExpression());
            }

            skipElement();

            return new IfExpression(predicateExpression, ifBody, elseBody);
        } else if (nextElement.getTokenType() == TokenType.WHILE) {
            List<Token> predicateTokenStream = new ArrayList<>();

            getNextElement(predicateTokenStream);
            getNextElement(predicateTokenStream);
            getNextElement(predicateTokenStream);

            if (getNextElement(tokenStream).getTokenType() != TokenType.THEN_WHILE) {
                throw new IllegalArgumentException();
            }

            PredicateExpression predicateExpression = new PredicateExpression(predicateTokenStream);

            List<Token> bodyTokenStream = new ArrayList<>();

            while (peekNextElement().getTokenType() != TokenType.END_WHILE) {
                getNextElement(bodyTokenStream);
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

        throw new IllegalArgumentException();
    }
}
