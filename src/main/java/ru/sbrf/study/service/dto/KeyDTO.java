package ru.sbrf.study.service.dto;

import java.util.Objects;

public class KeyDTO {
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyDTO keyDTO = (KeyDTO) o;
        return Objects.equals(key, keyDTO.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
