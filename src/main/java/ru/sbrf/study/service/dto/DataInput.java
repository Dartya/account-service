package ru.sbrf.study.service.dto;

import java.util.Objects;

public class DataInput {

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataInput dataInput = (DataInput) o;
        return Objects.equals(data, dataInput.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}
