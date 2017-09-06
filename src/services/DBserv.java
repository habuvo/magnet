package services;

import units.Entry;

import java.util.ArrayList;

public interface DBserv<E> {

    /* Select fields from DB
    @return ArrayList<E>
     */
    public ArrayList<E>  selectFieldsFromDatabase();

    /**
     *  Renew table
     */
    public void updateFieldsInDB();

}
