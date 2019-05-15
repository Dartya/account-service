package ru.sbrf.study.service.layers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbrf.study.service.dto.AccountData;
import ru.sbrf.study.service.dto.Record;
import ru.sbrf.study.service.dto.TokenAccManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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
    public int registerAccount(TokenAccManager tokenAccManager) throws IOException {//(int clientId, BigDecimal summ, String currency){

        String token = tokenAccManager.getToken();

        //отправляем запрос сервису авторизации - ВОЗМОЖНО НЕ ПРАВИЛЬНО
        String url = "http://e.n1ks.ru:58080/auth-service/getClientId?token="+token;

        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) obj.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response);

        //response - id сдиента или код ошибки
        int clientId = Integer.parseInt(response.toString());

        if (clientId != -1){
            final AccountData accountData = new AccountData();
            accountData.setClient_id(clientId);
            accountData.setSumm(tokenAccManager.getSumm());
            accountData.setCurrency(tokenAccManager.getCurrency());
            dataAccess.createAccount(accountData);

        } else{//else - возвращаем ошибку
            return -1;
        }

        return 1;
    }
}
