package ru.sbrf.study.service.dto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Используется в при добавлении записей в таблицы account, history
 */
public class AccountData {

    private int accountId;
    private int clientId;
    private BigDecimal summ;
    private int operation;
    private String currency;

    public AccountData() {
    }

    public AccountData(int accountId, int clientId, BigDecimal summ, int operation, String currency) {
        this.accountId = accountId;
        this.clientId = clientId;
        this.summ = summ;
        this.operation = operation;
        this.currency = currency;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
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

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountData that = (AccountData) o;
        return accountId == that.accountId &&
                clientId == that.clientId &&
                operation == that.operation &&
                Objects.equals(summ, that.summ) &&
                Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, clientId, summ, operation, currency);
    }
}
