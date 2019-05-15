package ru.sbrf.study.service.dto;

import java.util.Objects;

/**
 * Содержит только id клиента, нужен для получения JSON от сервиса авторизации
 */
public class ClientId {

    private int clientId;

    public ClientId() {
    }

    public ClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientId that = (ClientId) o;
        return clientId == that.clientId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId);
    }
}
