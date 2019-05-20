package ru.sbrf.study.service.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sbrf.study.service.entities.AccountEntity;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
}
