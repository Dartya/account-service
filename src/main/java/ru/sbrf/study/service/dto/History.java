package ru.sbrf.study.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class History {
    private int id;
    private int clientId;
    private int accountId;
    private int operationId;
    private BigDecimal summ;
    private LocalDateTime dateTime;

    public History() {
    }

    public History(int id, int clientId, int accountId, int operationId, BigDecimal summ, LocalDateTime dateTime) {
        this.id = id;
        this.clientId = clientId;
        this.accountId = accountId;
        this.operationId = operationId;
        this.summ = summ;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getOperationId() {
        return operationId;
    }

    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }

    public BigDecimal getSumm() {
        return summ;
    }

    public void setSumm(BigDecimal summ) {
        this.summ = summ;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return id == history.id &&
                clientId == history.clientId &&
                accountId == history.accountId &&
                operationId == history.operationId &&
                Objects.equals(summ, history.summ) &&
                Objects.equals(dateTime, history.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, accountId, operationId, summ, dateTime);
    }
}
