package ru.sbrf.study.service.layers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.sbrf.study.service.dto.*;
import ru.sbrf.study.service.mocks.Mock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Service
public class BusinessService {

    private static final String SERVICE_NAME = "auth";
    private static final String CONFIG_SERVICE_NAME = "config";
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
        accountData.setCurrency(accountCreate.getCurrency());
        accountData.setSumm(accountCreate.getSumm());
        return dataAccess.createAccount(accountData);
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
        accountData.setClientId(clientId);
        accountData.setAccountId(accountDelete.getAccountId());
        return dataAccess.deleteAccount(accountData);
    }

    /**
     * Снимает деньги со счета
     * @param pullPushMoney DTO, содержаний сумму операции и токен
     * @return код подтверждения операции "1" или код ошибки "-1"
     */
    public int pullMoney(PullPushMoney pullPushMoney){

        int clientId = validateToken(pullPushMoney);
        if (clientId == -1) return ERROR;

        MaxSumm maxSumm = getMaxSummParam();
        if (maxSumm.getMaxSumm().doubleValue() < pullPushMoney.getSumm().doubleValue()) return ERROR;

        final AccountData accountData = prepareData(pullPushMoney, clientId);
        return dataAccess.pullMoney(accountData);
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
        return dataAccess.pushMoney(accountData);
    }

    /**
     * Обращается к таблице history, делает выборку всех строк - истории операций
     * @return заполеннный List
     */
    public List<History> getHistory(){
        return dataAccess.getHistory();
    }


    /**
     * Обращается к таблице history, делает выборку строк, относящихся к отдельному клиенту
     * @param token
     * @return
     */
    public List<History> getMyHistory(Token token){

        int clientId = validateToken(token);
        if (clientId == -1) return null;

        return dataAccess.getHistoryByClientId(clientId);
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
        //Token token = new Token(object.getToken());
        //ClientId clientId =  restTemplate.exchange(serviceLocator.apply(SERVICE_NAME) + "auth/getClientId", HttpMethod.GET, new HttpEntity<>(token), new ParameterizedTypeReference<ClientId>() {}).getBody();
        //if (clientId.getClientId() == -1 || clientId == null || clientId.equals(null)) return ERROR;
        //return clientId.getClientId();
        return Mock.getClientId();
    }

    /**
     * Передает GET запрос сервису конфигурации, запрашивает параметр максимальной суммы расходной операции
     * @return MaxSumm - максимальаня сумма расходной операции
     */
    private MaxSumm getMaxSummParam(){
        //return restTemplate.exchange(serviceLocator.apply(CONFIG_SERVICE_NAME) + "config/getMaxSumm", HttpMethod.GET, null, new ParameterizedTypeReference<MaxSumm>() {}).getBody();
        return new MaxSumm(Mock.getMaxSumm());
    }
}
