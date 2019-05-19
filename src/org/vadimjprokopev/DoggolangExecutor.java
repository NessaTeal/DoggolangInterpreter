package org.vadimjprokopev;

import org.vadimjprokopev.expression.ExpressionParser;
import org.vadimjprokopev.token.Token;
import org.vadimjprokopev.token.Tokenizer;

import java.util.*;

public class DoggolangExecutor {
    public static void main(String[] args) {
        List<String> sourceCode = new ArrayList<>(Arrays.asList(args[0].split("\\s+")));
        sourceCode.add("EOF");

        Tokenizer tokenizer = new Tokenizer();
        List<Token> tokens = tokenizer.convertSourceCodeToTokens(sourceCode);

        Map<String, Integer> variables = new HashMap<>();
        ExpressionParser expressionParser = new ExpressionParser(tokens);
        while (expressionParser.hasNext()) {
            expressionParser.getNextExpression().execute(variables);
        }
    }
}
