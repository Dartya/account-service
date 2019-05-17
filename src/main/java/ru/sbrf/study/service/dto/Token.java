package ru.sbrf.study.service.dto;

import java.util.Objects;

public class Token implements UsingToken{
    private int clientId;
    private String token;

    public Token() {
    }

    public Token(int clientId, String token) {
        this.clientId = clientId;
        this.token = token;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
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
