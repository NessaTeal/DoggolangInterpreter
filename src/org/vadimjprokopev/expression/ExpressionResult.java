package org.vadimjprokopev.expression;

public class ExpressionResult<T> {
    private T result;

    public ExpressionResult(T result) {
        this.result = result;
    }

    public T getResult() {
        return result;
    }
}
