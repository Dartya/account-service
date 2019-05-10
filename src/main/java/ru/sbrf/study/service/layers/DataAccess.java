package ru.sbrf.study.service.layers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
}
