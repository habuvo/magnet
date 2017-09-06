package services.impl;

import DBhandler.PostgreSQLhandler;
import model.AppProps;
import services.DBserv;
import units.Entry;

import java.sql.SQLException;
import java.util.ArrayList;

public class DBservImpl implements DBserv {

    private PostgreSQLhandler dbHandler;

    public DBservImpl() {
        this.dbHandler = new PostgreSQLhandler();
    }

    /*
   Select fields from DB
    */
    public ArrayList<Entry> selectFieldsFromDatabase() {
        ArrayList<Entry> entries = null;
        try {
            entries = dbHandler.selectFieldsFromDB();
        } catch (SQLException e) {
            System.err.println("Can not select fields from database, " + e.getMessage());
            System.exit(0);
        }
        return entries;
    }

   /*
   Renew table
    */

    public void updateFieldsInDB() {
        int fieldCount = AppProps.PROPS.getFieldsCount();

        try {
            dbHandler.truncateDB();
        } catch (SQLException e) {
            System.err.println("Can't truncate table. Program terminated" + e.getMessage());
            System.exit(0);
        }
        try {
            dbHandler.insertFieldsToDB(1, fieldCount);
        } catch (SQLException e) {
            System.err.println("Can't fill table. Program terminated" + e.getMessage());
            System.exit(0);
        }
    }
}