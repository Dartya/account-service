package ru.sbrf.study.service.dto;

import java.io.Serializable;
import java.util.Objects;

public class ConfigDTO implements Serializable {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigDTO configDTO = (ConfigDTO) o;
        return Objects.equals(key, configDTO.key) &&
                Objects.equals(value, configDTO.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

}
