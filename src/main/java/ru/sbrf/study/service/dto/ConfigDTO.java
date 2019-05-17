package ru.sbrf.study.service.dto;

import java.util.Objects;

public class ConfigDTO {
    private Long id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigDTO configDTO = (ConfigDTO) o;
        return getId().equals(configDTO.getId()) &&
                getKey().equals(configDTO.getKey()) &&
                Objects.equals(getValue(), configDTO.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getKey(), getValue());
    }
}
