package ru.sbrf.study.service.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sbrf.study.service.entities.HistoryEntity;

import javax.ws.rs.QueryParam;
import java.util.List;

public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {

    List<HistoryEntity> findAllByAccountId(int id);

    @Query("SELECT h FROM HistoryEntity h WHERE h.id = :id")
    List<HistoryEntity> query(@QueryParam("id") int id);

    @Query("SELECT h FROM HistoryEntity h WHERE h.accountId = :accountId")
    List<HistoryEntity> queryByAccountId(@QueryParam("account_id")int accountId);

    @Query("SELECT h FROM HistoryEntity h")
    List<HistoryEntity> getAll();
}
