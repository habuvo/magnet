package services;


import units.Entry;
import units.OutEntries;

import java.util.ArrayList;

public interface XMLserv {
    /**
     * Marshal Java class to XML
     *
     * @param entityList - ArrayList<>
     */
    public void createFirstXmlFile(ArrayList<Entry> entityList);

    /**
     * Transform XML to other use XLST
     */
    public void transformToSecondXml();

    /**
     * Unmarshal XML to Java class
     */
    public OutEntries getFromSecondXml();
}
