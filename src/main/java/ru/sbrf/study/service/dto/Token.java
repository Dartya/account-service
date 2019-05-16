package ru.sbrf.study.service.dto;

import java.util.Objects;

public class Token implements UsingToken{
    private long token;

    public Token() {
    }

    public Token(long token) {
        this.token = token;
    }

    @Override
    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token1 = (Token) o;
        return token == token1.token;
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
