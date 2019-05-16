package ru.sbrf.study.service.dto;

import java.math.BigDecimal;
import java.util.Objects;


/**
 * Используется при пополнении счета и снятии денег
 */
public class PullPushMoney implements UsingToken {

    private int accountId;
    private long token;
    private BigDecimal summ;

    public PullPushMoney() {
    }

    public PullPushMoney(int accountId, long token, BigDecimal summ) {
        this.accountId = accountId;
        this.token = token;
        this.summ = summ;
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

    public BigDecimal getSumm() {
        return summ;
    }

    public void setSumm(BigDecimal summ) {
        this.summ = summ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PullPushMoney that = (PullPushMoney) o;
        return accountId == that.accountId &&
                token == that.token &&
                Objects.equals(summ, that.summ);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, token, summ);
    }
}
