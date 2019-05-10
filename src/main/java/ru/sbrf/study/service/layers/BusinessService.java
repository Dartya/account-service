package ru.sbrf.study.service.layers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbrf.study.service.dto.Record;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BusinessService {

    @Autowired
    private DataAccess dataAccess;

    public void registerCall(String data) {
        final Record record = new Record();
        record.setDateTime(LocalDateTime.now());
        record.setData(data);
        dataAccess.createRecord(record);
    }

    public List<Record> getRecords() {
        return dataAccess.getRecords();
    }
}
