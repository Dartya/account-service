package ru.sbrf.study.service.layers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.sbrf.study.service.dto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Service
public class BusinessService {

    private static final String SERVICE_NAME = "auth";
    private static final int ERROR = -1;

    @Autowired
    private DataAccess dataAccess;

    @Autowired
    private Function<String, String> serviceLocator;

    @Autowired
    private RestTemplate restTemplate;

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
     * @param accountCreate DTO, содержащий токен, сумму пополнения, тикер валюты счета
     * @return код подтверждения операции "1" или код ошибки "-1"
     */
    public int registerAccount(AccountCreate accountCreate){

        int clientId = validateToken(accountCreate);
        if (clientId == -1) return ERROR;

        final AccountData accountData = new AccountData();
        accountData.setClientId(clientId);
        accountData.setSumm(accountCreate.getSumm());
        accountData.setCurrency(accountCreate.getCurrency());
        dataAccess.createAccount(accountData);
        return 1;
    }

    /**
     * Удаляет ранее созданный счет
     * @param accountDelete DTO, содержащий id счета и токен
     * @return код подтверждения операции "1" или код ошибки "-1"
     */
    public int deleteAccount(AccountDelete accountDelete){

        int clientId = validateToken(accountDelete);
        if (clientId == -1) return ERROR;

        final AccountData accountData = new AccountData();
        accountData.setAccountId(clientId);
        accountData.setAccountId(accountDelete.getAccountId());
        dataAccess.deleteAccount(accountData);
        return 1;
    }

    /**
     * Снимает деньги со счета
     * @param pullPushMoney DTO, содержаний сумму операции и токен
     * @return код подтверждения операции "1" или код ошибки "-1"
     */
    public int pullMoney(PullPushMoney pullPushMoney){

        int clientId = validateToken(pullPushMoney);
        if (clientId == -1) return ERROR;

        final AccountData accountData = prepareData(pullPushMoney, clientId);
        dataAccess.pullMoney(accountData);
        return 1;
    }

    /**
     * Вносит деньги на счет
     * @param pullPushMoney DTO, содержаний сумму операции и токен
     * @return код подтверждения операции "1" или код ошибки "-1"
     */
    public int pushMoney(PullPushMoney pullPushMoney){

        int clientId = validateToken(pullPushMoney);
        if (clientId == -1) return ERROR;

        final AccountData accountData = prepareData(pullPushMoney, clientId);
        dataAccess.pushMoney(accountData);
        return 1;
    }

    /**
     * Подготавливает AccountData, содержит повторяющийся код
     * @param pullPushMoney DTO, содержаний сумму операции и токен
     * @param clientId id клиента
     * @return заполненный AccountData
     */
    private AccountData prepareData(PullPushMoney pullPushMoney, int clientId){
        final AccountData accountData = new AccountData();
        accountData.setClientId(clientId);
        accountData.setSumm(pullPushMoney.getSumm());
        accountData.setAccountId(pullPushMoney.getAccountId());
        return accountData;
    }

    /**
     * Передает Token на проверку сервису авторизации, получает ClientId, содержащий либо id клиента, либо ошибку - значение "-1"
     * @param object объект, реализующий интерфейс UsingToken
     */
    private int validateToken(UsingToken object){
        Token token = new Token(object.getToken());
        ClientId clientId =  restTemplate.exchange(serviceLocator.apply(SERVICE_NAME) + "auth/getClientId", HttpMethod.GET, new HttpEntity<>(token), new ParameterizedTypeReference<ClientId>() {}).getBody();
        if (clientId.getClientId() == -1 || clientId == null || clientId.equals(null)) return ERROR;
        return clientId.getClientId();
    }
}
