package ru.sbrf.study.service.layers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbrf.study.service.dto.AccountData;
import ru.sbrf.study.service.dto.Record;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataAccess implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(DataAccess.class);

    @Autowired
    private DataSource dataSource;

    @Override
    public void afterPropertiesSet() {
        connectionCheck();
    }

    public void createRecord(Record record) {
        try (final Connection connection = dataSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement("insert into data (timestamp, data) values (?, ?)");
            statement.setTimestamp(1, Timestamp.valueOf(record.getDateTime()));
            statement.setString(2, record.getData());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Record> getRecords() {
        try (final Connection connection = dataSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement("select id, timestamp, data from data");
            final ResultSet resultSet = statement.executeQuery();
            final List<Record> records = new ArrayList<>();
            while (resultSet.next()) {
                final Record record = new Record();
                record.setId(resultSet.getLong(1));
                record.setDateTime(resultSet.getTimestamp(2).toLocalDateTime());
                record.setData(resultSet.getString(3));
                records.add(record);
            }
            return records;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void connectionCheck() {
        try (final Connection connection = dataSource.getConnection()) {
            if (connection.prepareStatement("select null").executeQuery().next())
                logger.info("db connection checked successfully");
            else
                throw new RuntimeException("db connection error");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Создает новую запись в таблице счетов (account)
     * Для создания записи необходимы: client_id, summ, currency - поля AccountData
     * <p>
     * Создает новую запись в таблице истории операций (history)
     * <p>
     * Нужно получить номер счета account_id, чтобы использовать AccountData
     * для внесения записи в таблицу истории операций
     *
     * @param accountData dto
     */
    public void createAccount(AccountData accountData) {
        try (final Connection connection = dataSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement("insert into account (client_id, summ, currency) values (?, ?, ?)");
            statement.setInt(1, (accountData.getClientId()));
            statement.setBigDecimal(2, accountData.getSumm());
            statement.setString(3, accountData.getCurrency());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Удаляет запись из таблицы счетов (account)
     * <p>
     * Для удаления нужен id счета - берем в AccountData
     * <p>
     * Создает новую запись в таблице истории операций (history)
     */
    public void deleteAccount(AccountData accountData) {
        try (final Connection connection = dataSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement("delete from account where account=?");
            statement.setInt(1, (accountData.getAccountId()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Вносит деньги на счет, использует AccountData - нужен id счета
     * <p>
     * Создает новую запись в таблице истории операций (history)
     */
    public void pushMoney(AccountData accountData) {
        try (final Connection connection = dataSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement("UPDATE account SET summ=? WHERE id=?");
            statement.setBigDecimal(1, accountData.getSumm());
            statement.setInt(2, accountData.getAccountId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Снимает деньги со счета, использует AccountData - нужен id счета
     * <p>
     * Создает новую запись в таблице истории операций (history)
     */
    public void pullMoney(AccountData accountData) {
        try (final Connection connection = dataSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement("UPDATE account SET summ = ? WHERE id=?");
            statement.setBigDecimal(1, accountData.getSumm());
            statement.setInt(2, accountData.getAccountId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Создает новую запись в таблице
     */
    public void writeHistoryRow(AccountData accountData){
        try (final Connection connection = dataSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement("INSERT into history (client_id, account_id, operation_id, summ) values (?, ?, ?, ?)");
            statement.setInt(1, (accountData.getClientId()));
            statement.setInt(2, accountData.getAccountId());
            statement.setInt(3, accountData.getOperation());
            statement.setBigDecimal(4, accountData.getSumm());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

