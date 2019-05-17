package ru.sbrf.study.service.dto;

import java.util.Objects;

/**
 * Используется при удалении счета
 */
public class AccountDelete implements UsingToken {
    private int clientId;
    private int accountId;
    private String token;

    public AccountDelete() {
    }

    public AccountDelete(int clientId, int accountId, String token) {
        this.clientId = clientId;
        this.accountId = accountId;
        this.token = token;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
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
        AccountDelete that = (AccountDelete) o;
        return accountId == that.accountId &&
                token == that.token;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, token);
    }
}
