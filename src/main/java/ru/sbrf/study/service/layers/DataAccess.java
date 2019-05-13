package ru.sbrf.study.service.layers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbrf.study.service.dto.AccountManagement;
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
     * Создает новую запись в таблице счетов
     * @param accountManagement dto
     */
    public void createAccount(AccountManagement accountManagement) {
        try (final Connection connection = dataSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement("insert into account (client_id, summ, currency) values (?, ?, ?)");
            statement.setInt(1, (accountManagement.getClient_id()));
            statement.setBigDecimal(2, accountManagement.getSumm());
            statement.setString(3, accountManagement.getCurrency());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Удаляет запись из таблицы счетов
     */
    public void deleteAccount(AccountManagement accountManagement){
        try (final Connection connection = dataSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement("delete from account where (client_id, currency) values (?, ?)");
            statement.setInt(1, (accountManagement.getClient_id()));
            statement.setString(2, accountManagement.getCurrency());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Вносит деньги на счет
     */
    public void pushMoney(){
        try (final Connection connection = dataSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement("UPDATE into account where 'client_id'= ");
            statement.setInt(1, (accountManagement.getClient_id()));
            statement.setString(2, accountManagement.getCurrency());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Снимает деньги со счета
     */
    public void pullMoney(){

    }
}

