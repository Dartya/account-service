package ru.sbrf.study.service.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "history")
public class HistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "client_id")
    private int clientId;
    @Column(name = "account_id")
    private int accountId;
    @Column(name = "operation_id")
    private int operationId;
    private BigDecimal summ;
    @Column(name = "datetime")
    private LocalDateTime dateTime;

    public HistoryEntity() {
    }

    public HistoryEntity(int id, int clientId, int accountId, int operationId, BigDecimal summ, LocalDateTime dateTime) {
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
        HistoryEntity historyEntity = (HistoryEntity) o;
        return id == historyEntity.id &&
                clientId == historyEntity.clientId &&
                accountId == historyEntity.accountId &&
                operationId == historyEntity.operationId &&
                Objects.equals(summ, historyEntity.summ) &&
                Objects.equals(dateTime, historyEntity.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, accountId, operationId, summ, dateTime);
    }
}
