package ru.sbrf.study.service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.sbrf.study.service.dto.AccountCreate;
import ru.sbrf.study.service.entities.AccountEntity;
import ru.sbrf.study.service.entities.HistoryEntity;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Repository
@Slf4j
public class AccountService {

    private static final int ERROR = -1;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public int addAccount(AccountCreate accountCreate){
        try {
            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setClientId(accountCreate.getClientId());
            accountEntity.setSumm(accountCreate.getSumm());
            accountEntity.setCurrency(accountCreate.getCurrency());
            entityManager.persist(accountEntity);
            int newAccountId = accountEntity.getId();

            final HistoryEntity historyEntity = new HistoryEntity();
            historyEntity.setAccountId(newAccountId);
            historyEntity.setClientId(accountEntity.getClientId());
            historyEntity.setOperationId(1);
            historyEntity.setDateTime(LocalDateTime.now());
            historyEntity.setSumm(accountEntity.getSumm());
            entityManager.persist(historyEntity);

            return 1;
        }catch (Exception exc){
            exc.printStackTrace();
            return ERROR;
        }
    }
}
