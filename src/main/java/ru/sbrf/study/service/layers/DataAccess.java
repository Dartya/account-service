package ru.sbrf.study.service.layers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sbrf.study.service.dto.AccountData;
import ru.sbrf.study.service.dto.History;
import ru.sbrf.study.service.dto.Record;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    /**
     * Обращется к таблице history, делает выборку всех строк
     * @return возвращает все строки таблицы в виде листа объектов History
     */
    public List<History> getHistory(){
        try (final Connection connection = dataSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM history");
            final ResultSet resultSet = statement.executeQuery();

            return makeHistoryList(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Обращется к таблице history, делает выборку строк, относящихся к clientId
     * @param clientId id обратившегося и подтвердившего валидность клиента
     * @return возвращает строки таблицы в виде листа объектов History
     */
    public List<History> getHistoryByClientId(int clientId) {
        try (final Connection connection = dataSource.getConnection()) {
            final PreparedStatement statement = connection.prepareStatement("SELECT * FROM history WHERE client_id=?");
            statement.setInt(1, clientId);
            final ResultSet resultSet = statement.executeQuery();

            return makeHistoryList(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Генерирует List<History>, содержит повторяющийся код методов getHistory() и getHistoryByClientId()
     *
     * @param resultSet множество строк-ответа СУБД
     * @return возвращет лист со строками таблицы, относящихся к обратившемуся клиенту
     * @throws SQLException
     */
    private List<History> makeHistoryList(ResultSet resultSet) throws SQLException{
        final List<History> records = new ArrayList<>();
        while (resultSet.next()) {
            final History history = new History();
            history.setId(resultSet.getInt(1));
            history.setClientId(resultSet.getInt(2));
            history.setAccountId(resultSet.getInt(3));
            history.setOperationId(resultSet.getInt(4));
            history.setSumm(resultSet.getBigDecimal(5));
            history.setDateTime(resultSet.getTimestamp(6).toLocalDateTime());
            records.add(history);
        }
        return records;
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
     * Создает новую запись в таблице истории операций (history)
     * Нужно получить номер счета account_id, чтобы использовать AccountData
     * для внесения записи в таблицу истории операций
     *
     * @param accountData DTO
     * @return номер счета (id), или код ошибки "-1"
     */
    public int createAccount(AccountData accountData) {

        Integer num;
        Integer result = -1;
        final String query = "INSERT INTO account (client_id, summ, currency) VALUES (?, ?, ?)";
        try (final Connection connection = dataSource.getConnection()) {
            //начало транзакции
            connection.setAutoCommit(false);

            //новая запись в account
            final PreparedStatement statementInsertAccount = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statementInsertAccount.setInt(1, accountData.getClientId());
            statementInsertAccount.setBigDecimal(2, accountData.getSumm());
            statementInsertAccount.setString(3, accountData.getCurrency());
            num = statementInsertAccount.executeUpdate();

            //получаем id вставленной строки, если id валидный (> 0), делаем запись в history
            writeHistoryRow(accountData, statementInsertAccount, connection, 1, num);

            //завершение транзакции
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Удаляет запись из таблицы счетов (account)
     * Для удаления нужен id счета - берем в AccountData
     * Создает новую запись в таблице истории операций (history)
     *
     * @param accountData DTO
     * @return результат операции: "1" - успешно, "-1" - ошибка
     */
    public int deleteAccount(AccountData accountData) {

        Integer num;
        Integer result = -1;
        final String query = "DELETE FROM account WHERE id=?";
        try (final Connection connection = dataSource.getConnection()) {
            //начало транзакции
            connection.setAutoCommit(false);

            //удаление записи в account
            final PreparedStatement statementDeleteAccount = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statementDeleteAccount.setInt(1, (accountData.getAccountId()));
            num = statementDeleteAccount.executeUpdate();

            //получаем id вставленной строки, если id валидный (> 0), делаем запись в history
            result = writeHistoryRow(accountData, statementDeleteAccount, connection, 2, num);

            //завершение транзакции
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Вносит деньги на счет, использует AccountData - нужен id счета
     * Создает новую запись в таблице истории операций (history)
     *
     * @param accountData DTO
     * @return результат операции: "1" - успешно, "-1" - ошибка
     */
    public int pushMoney(AccountData accountData) {

        Integer num;
        Integer result = -1;
        BigDecimal accSumm;
        final String query = "UPDATE account SET summ=? WHERE id=?";
        try (final Connection connection = dataSource.getConnection()) {
            //начало транзакции
            connection.setAutoCommit(false);

            //производим выборку из БД по указанному номеру счета, достаем сумму
            final PreparedStatement statementSelectRow = connection.prepareStatement("SELECT summ FROM account WHERE id=?");
            statementSelectRow.setInt(1, accountData.getAccountId());
            final ResultSet resultSet = statementSelectRow.executeQuery();
            System.out.println("account ID = "+accountData.getAccountId());
            if (resultSet.next()) {
                accSumm = resultSet.getBigDecimal(1);
                System.out.println("Summ before increase = "+accSumm);
                accSumm = accSumm.add(accountData.getSumm());
                System.out.println("Summ after increase = "+accSumm);
                //обновление записи в account - внесение денег на счет
                final PreparedStatement statementUpdateAccount = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statementUpdateAccount.setBigDecimal(1, accSumm);
                statementUpdateAccount.setInt(2, accountData.getAccountId());
                num = statementUpdateAccount.executeUpdate();

                //получаем id вставленной строки, если id валидный (> 0), делаем запись в history
                result = writeHistoryRow(accountData, statementUpdateAccount, connection, 3, num);
            } else {
                result = -2;
            }

            //завершение транзакции
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Снимает деньги со счета, использует AccountData - нужен id счета
     * Создает новую запись в таблице истории операций (history)
     *
     * @param accountData DTO
     * @return результат операции: "1" - успешно, "-1" - ошибка
     */
    public int pullMoney(AccountData accountData) {
        Integer num;
        Integer result = -1;
        BigDecimal accSumm;
        final String query = "UPDATE account SET summ=? WHERE id=?";
        try (final Connection connection = dataSource.getConnection()) {
            //начало транзакции
            connection.setAutoCommit(false);

            //производим выборку из БД по указанному номеру счета, достаем сумму
            final PreparedStatement statementSelectRow = connection.prepareStatement("SELECT summ FROM account WHERE id=?");
            statementSelectRow.setInt(1, accountData.getAccountId());

            final ResultSet resultSet = statementSelectRow.executeQuery();
            if (resultSet.next()) {
                accSumm = resultSet.getBigDecimal(1);
                if (accSumm.subtract(accountData.getSumm()).doubleValue() > 0.00) {

                    //обновление записи в account - внесение денег на счет
                    final PreparedStatement statementUpdateAccount = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                    statementUpdateAccount.setBigDecimal(1, accSumm);
                    statementUpdateAccount.setInt(2, accountData.getAccountId());
                    num = statementUpdateAccount.executeUpdate();

                    //получаем id вставленной строки, если id валидный (> 0), делаем запись в history
                    result = writeHistoryRow(accountData, statementUpdateAccount, connection, 4, num);
                }
            }

            //завершение транзакции
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Создает новую запись в таблице history
     *
     * @param accountData
     * @param accountId
     * @param connection
     * @param operation
     * @param preparedStatement
     * @return результат операции: "1" - успешно, "-1" - ошибка
     */
    private int writeHistoryRow(AccountData accountData, PreparedStatement preparedStatement, Connection connection, int operation, int accountId){
        int result = -1;
        try {
            //получаем id вставленной строки
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                result = rs.getInt(1);
                System.out.println("result of prev insert = "+result);
            }

            //новая запись в history
            final PreparedStatement statementInsertHistory = connection.prepareStatement("insert into history (client_id, account_id, operation_id, summ, datetime) values (?, ?, ?, ?, ?)");
            statementInsertHistory.setInt(1, accountData.getClientId());
            statementInsertHistory.setInt(2, accountId);
            statementInsertHistory.setInt(3, operation);
            statementInsertHistory.setBigDecimal(4, accountData.getSumm());
            statementInsertHistory.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            result = statementInsertHistory.executeUpdate();
            System.out.println(result+" row(s) inserted in history");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
