package org.vadimjprokopev.token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.vadimjprokopev.token.TokenType.*;

public class Tokenizer {

    private static Map<String, TokenType> TOKEN_MAP = new HashMap<>();

    static {
        TOKEN_MAP.put("AWOO", SET);
        TOKEN_MAP.put("WOOF", ADD);
        TOKEN_MAP.put("BARK", SUBTRACT);
        TOKEN_MAP.put("ARF", MULTIPLY);

        TOKEN_MAP.put("YIP", LESS);
        TOKEN_MAP.put("YAP", MORE);

        TOKEN_MAP.put("RUF?", IF);
        TOKEN_MAP.put("VUH", THEN_IF);
        TOKEN_MAP.put("ROWH", ELSE);
        TOKEN_MAP.put("ARRUF", END_IF);

        TOKEN_MAP.put("GRRR", WHILE);
        TOKEN_MAP.put("BOW", THEN_WHILE);
        TOKEN_MAP.put("BORF", END_WHILE);

        TOKEN_MAP.put("EOF", EOF);
    }

    private Token convertStringToToken(String input) {
        TokenType tokenType = TOKEN_MAP.get(input);
        if (tokenType != null) {
            return new Token(tokenType, null, null);
        } else {
            if (input.matches("\\d+")) {
                return new Token(CONSTANT, null, Integer.parseInt(input));
            } else {
                return new Token(VARIABLE, input, null);
            }
        }
    }

    public List<Token> convertSourceCodeToTokens(List<String> sourceCode) {
        return sourceCode.stream()
                .map(this::convertStringToToken)
                .collect(Collectors.toList());
    }
}
