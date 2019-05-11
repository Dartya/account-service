package ru.sbrf.study.service.dto;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * DTO, содержащий поля для передачи его из сервиса в бизнес-логику
 */
public class TokenAccManager {
    private long token;
    private BigDecimal summ;
    private String currency;

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
        TokenAccManager that = (TokenAccManager) o;
        return token == that.token &&
                Objects.equals(summ, that.summ) &&
                Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, summ, currency);
    }
}
