package DBhandler;

import model.AppConst;
import model.AppProps;
import units.Entry;


import java.sql.*;
import java.util.ArrayList;

public class PostgreSQLhandler {

    public static final String INSERT_STATEMENT = "INSERT INTO test ( field ) VALUES ( ? )";
    public static final String TRUNCATE_STATEMENT = "TRUNCATE test";
    public static final String SELECT_STATEMENT = "SELECT field FROM test";

    /**
     *  Establish connection to database
     *  @return Connection
     *  @throws SQLException
     */
     public Connection getDBConnection() {
        Connection dbConnection = null;
        try {
             dbConnection = DriverManager.getConnection(AppProps.PROPS.getDBUrl()
                    , AppProps.PROPS.getUserName()
                    , AppProps.PROPS.getUserPassword());

        } catch (SQLException e) {
            e.printStackTrace();
            if (dbConnection != null) try {
                dbConnection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
                return null;
            }
        }
        return dbConnection;
    }

    /**
     *  Delete all data from database
     *  @throws SQLException
     */
    public void truncateDB() throws SQLException {
        try (Statement stmt = getDBConnection().createStatement()) {
            stmt.executeUpdate(TRUNCATE_STATEMENT);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Select all data from database
     *  @return ArrayList<Entry>
     *  @throws SQLException
     */
    public ArrayList<Entry> selectFieldsFromDB() throws SQLException {
        ArrayList<Entry> entries = new ArrayList<>();
        try (Statement stmt = getDBConnection().createStatement()) {
            ResultSet resultSet = stmt.executeQuery(SELECT_STATEMENT);
            while (resultSet.next()) {
                Entry entry = new Entry();
                entry.setField(resultSet.getInt(AppConst.COLUMN_FIELD));
                entries.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }

    /**
     *  Insert data to database
     *  @throws SQLException
     */
    public void insertFieldsToDB(int from, int to) throws SQLException {
        try (Connection conn = getDBConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement prepStmt = conn.prepareStatement(INSERT_STATEMENT)) {
                for (int i = from; i <= to; i++) {
                    prepStmt.setInt(1, i);
                    prepStmt.addBatch();
                }
                prepStmt.executeBatch();
                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
