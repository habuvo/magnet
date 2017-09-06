package services.impl;

import model.AppProps;
import services.mainServ;
import units.Entry;
import units.OutEntries;
import units.OutEntry;

import java.util.ArrayList;

public class mainServImpl implements mainServ {

    private DBservImpl dBserv;
    private XMLservImpl xmlServ;

    public mainServImpl() {
        this.dBserv = new DBservImpl();
        this.xmlServ = new XMLservImpl();
    }

    /**
     *  transformation process
     */
    public void execute(){
        System.out.print("renew table with " + AppProps.PROPS.getFieldsCount() + " records ... ");
        dBserv.updateFieldsInDB();
        System.out.println("done");
        System.out.print("select fieids from test database ... ");
        ArrayList<Entry> entries = dBserv.selectFieldsFromDatabase();
        System.out.println("done");
        System.out.print("create first xml file ... ");
        xmlServ.createFirstXmlFile(entries);
        System.out.println("done");
        System.out.print("transform first xml file to second ... ");
        xmlServ.transformToSecondXml();
        System.out.println("done");
        System.out.print("unmarshal second xml to class ... ");
        OutEntries outEntries = xmlServ.getFromSecondXml();
        System.out.println("done");
        long summ = 0l;
        if ( outEntries != null ){
            for(OutEntry entry : outEntries.getEntries() ){
                summ += entry.getField();
            }
        }
        System.out.println("Summ is : "+summ );
    }
}
