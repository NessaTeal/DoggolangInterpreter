package org.vadimjprokopev;

import org.vadimjprokopev.expression.ExpressionParser;
import org.vadimjprokopev.token.Token;
import org.vadimjprokopev.token.Tokenizer;

import java.util.*;

public class DoggolangExecutor {

    public static void main(String[] args) {
        Map<String, Integer> variables = new HashMap<>();
        Tokenizer tokenizer = new Tokenizer();
        List<String> sourceCode = new ArrayList<>(Arrays.asList(args));
        sourceCode.add("EOF");
        List<Token> tokens = tokenizer.convertSourceCodeToTokens(sourceCode);
        ExpressionParser expressionParser = new ExpressionParser(tokens);

        while (expressionParser.hasNext()) {
            expressionParser.getNextExpression().execute(variables);
        }
    }
}
