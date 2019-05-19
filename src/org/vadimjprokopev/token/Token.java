package org.vadimjprokopev.token;

public class Token {
    private TokenType tokenType;
    private String variableName;
    private Integer constantValue;

    public Token(TokenType tokenType, String variableName, Integer constantValue) {
        this.tokenType = tokenType;
        this.variableName = variableName;
        this.constantValue = constantValue;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getVariableName() {
        return variableName;
    }

    public Integer getConstantValue() {
        return constantValue;
    }
}
