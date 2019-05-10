package ru.sbrf.study.service.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class Record {

    private Long id;
    private LocalDateTime dateTime;
    private String data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

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
        Record record = (Record) o;
        return Objects.equals(id, record.id) &&
                Objects.equals(dateTime, record.dateTime) &&
                Objects.equals(data, record.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, data);
    }
}
