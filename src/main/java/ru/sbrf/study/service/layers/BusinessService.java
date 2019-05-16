package ru.sbrf.study.service.layers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbrf.study.service.dto.AccountData;
import ru.sbrf.study.service.dto.AccountDelete;
import ru.sbrf.study.service.dto.Record;
import ru.sbrf.study.service.dto.AccountCreate;

import java.io.IOException;
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

    /**
     * Создает новый счет
     */
    public int registerAccount(AccountCreate accountCreate) throws IOException {//(int clientId, BigDecimal summ, String currency){

        //response - id сдиента или код ошибки
        int clientId = 1;

        if (clientId != -1){
            final AccountData accountData = new AccountData();
            accountData.setClientId(clientId);
            accountData.setSumm(accountCreate.getSumm());
            accountData.setCurrency(accountCreate.getCurrency());
            dataAccess.createAccount(accountData);

        } else{//else - возвращаем ошибку
            return -1;
        }

        return 1;
    }

    public int deleteAccount(AccountDelete accountDelete){


        return 1;
    }
}
