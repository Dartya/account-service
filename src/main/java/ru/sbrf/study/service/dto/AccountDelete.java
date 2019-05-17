package ru.sbrf.study.service.dto;

import java.util.Objects;

/**
 * Используется при удалении счета
 */
public class AccountDelete implements UsingToken {
    private int accountId;
    private long token;

    public AccountDelete() {
    }

    public AccountDelete(int accountId, long token) {
        this.accountId = accountId;
        this.token = token;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
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
        AccountDelete that = (AccountDelete) o;
        return accountId == that.accountId &&
                token == that.token;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, token);
    }
}
