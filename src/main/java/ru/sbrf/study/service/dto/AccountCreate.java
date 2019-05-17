package ru.sbrf.study.service.dto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Используется для создания нового счета
 */
public class AccountCreate implements UsingToken {
    private long token;
    private BigDecimal summ;
    private String currency;

    public AccountCreate() {
    }

    public AccountCreate(long token, BigDecimal summ, String currency) {
        this.token = token;
        this.summ = summ;
        this.currency = currency;
    }

    public long getToken() {
        return token;
    }

    public void setToken(long token) {
        this.token = token;
    }

    public BigDecimal getSumm() {
        return summ;
    }

    public void setSumm(BigDecimal summ) {
        this.summ = summ;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountCreate that = (AccountCreate) o;
        return token == that.token &&
                Objects.equals(summ, that.summ) &&
                Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, summ, currency);
    }
}
