package ru.sbrf.study.service.dto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Используется при создании нового аккаунта, (удалении старого?), внесении и снятии денег со счета
 */
public class AccountData {

    private int client_id;
    private BigDecimal summ;
    private String currency;

    public AccountData() {
    }

    public AccountData(int client_id, BigDecimal summ, String currency) {
        this.client_id = client_id;
        this.summ = summ;
        this.currency = currency;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
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
        AccountData that = (AccountData) o;
        return client_id == that.client_id &&
                Objects.equals(summ, that.summ) &&
                Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client_id, summ, currency);
    }
}
