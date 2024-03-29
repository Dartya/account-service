package ru.sbrf.study.service.layers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.sbrf.study.service.dto.*;
import ru.sbrf.study.service.entities.HistoryEntity;
import ru.sbrf.study.service.mocks.Mock;
import ru.sbrf.study.service.service.AccountService;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Service
public class BusinessService {

    private static final String SERVICE_NAME = "auth-service";
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

        boolean isTokenValid = validateToken(accountCreate);
        if (!isTokenValid) return ERROR;

        final AccountData accountData = new AccountData();
        accountData.setClientId(accountCreate.getClientId());
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

        boolean isTokenValid = validateToken(accountDelete);
        if (!isTokenValid) return ERROR;

        final AccountData accountData = new AccountData();
        accountData.setClientId(accountDelete.getClientId());
        accountData.setAccountId(accountDelete.getAccountId());
        return dataAccess.deleteAccount(accountData);
    }

    /**
     * Снимает деньги со счета, предварительно запрашивает сервис конфига параметр максимальной суммы расходной операции
     * @param pullPushMoney DTO, содержаний сумму операции и токен
     * @return код подтверждения операции "1" или код ошибки "-1"
     */
    public int pullMoney(PullPushMoney pullPushMoney){

        boolean isTokenValid = validateToken(pullPushMoney);
        if (!isTokenValid) return ERROR;

        ConfigDTO configDTO = getMaxSummParam(); //maxSumm = 10000000
        if (pullPushMoney.getSumm().doubleValue() > Double.parseDouble(configDTO.getValue())) {
            System.out.println("Pull summ > maxSumm");
            return ERROR;
        }

        final AccountData accountData = prepareData(pullPushMoney);
        return dataAccess.pullMoney(accountData);
    }

    /**
     * Вносит деньги на счет
     * @param pullPushMoney DTO, содержаний сумму операции и токен
     * @return код подтверждения операции "1" или код ошибки "-1"
     */
    public int pushMoney(PullPushMoney pullPushMoney){

        boolean isTokenValid = validateToken(pullPushMoney);
        if (!isTokenValid) return ERROR;

        final AccountData accountData = prepareData(pullPushMoney);
        return dataAccess.pushMoney(accountData);
    }

    /**
     * Обращается к таблице history, делает выборку всех строк - истории операций
     * @return заполеннный List
     */
    public List<HistoryEntity> getHistory(){
        return dataAccess.getHistory();
    }


    /**
     * Обращается к таблице history, делает выборку строк, относящихся к отдельному клиенту
     * @param clientToken
     * @return
     */
    public List<HistoryEntity> getMyHistory(ClientToken clientToken){

        boolean isTokenValid = validateToken(clientToken);
        if (!isTokenValid) return null;

        return dataAccess.getHistoryByClientId(clientToken.getClientId());
    }

    /**
     * Подготавливает AccountData, содержит повторяющийся код
     * @param pullPushMoney DTO, содержаний сумму операции и токен
     * @return заполненный AccountData
     */
    private AccountData prepareData(PullPushMoney pullPushMoney){
        final AccountData accountData = new AccountData();
        accountData.setClientId(pullPushMoney.getClientid());
        accountData.setSumm(pullPushMoney.getSumm());
        accountData.setAccountId(pullPushMoney.getAccountId());
        return accountData;
    }

    /**
     * Передает ClientToken на проверку сервису авторизации, получает ClientId, содержащий либо id клиента, либо ошибку - значение "-1"
     * @param object объект, реализующий интерфейс UsingToken
     */
    public boolean validateToken(UsingToken object){
        Token token = new Token(object.getToken());
        return restTemplate.exchange("http://e.n1ks.ru:32811/auth-service/validateToken", HttpMethod.POST, new HttpEntity<>(token), new ParameterizedTypeReference<Boolean>() {}).getBody();
        //return Mock.isTokenValid();
    }

    /**
     * Передает GET запрос сервису конфигурации, запрашивает параметр максимальной суммы расходной операции
     * @return ConfigDTO с параметром - максимальной суммой расходной операции
     */
    private ConfigDTO getMaxSummParam(){
        KeyDTO keyDTO = new KeyDTO();
        keyDTO.setKey("maxSum");

        ConfigDTO configDTO = restTemplate.exchange("http://e.n1ks.ru:32808/config/get", HttpMethod.POST, new HttpEntity<>(keyDTO), new ParameterizedTypeReference<ConfigDTO>() {}).getBody();
        return configDTO;
        //return new MaxSumm(Mock.getMaxSumm()); //serviceLocator.apply(CONFIG_SERVICE_NAME) + "/get
    }
}
