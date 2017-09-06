package services.impl;

import services.XMLserv;
import units.Entries;
import units.Entry;
import units.OutEntries;
import model.AppConst;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class XMLservImpl implements XMLserv {

    public void createFirstXmlFile(ArrayList<Entry> entityList)

    {
        try {
            JAXBContext jc = JAXBContext.newInstance(Entries.class);
            Marshaller m = jc.createMarshaller();
            Entries entries = new Entries();
            entries.setEntries(entityList);
            OutputStream os = new FileOutputStream(AppConst.FIRST_XML_FILE);
            m.marshal(entries, os);
        } catch (JAXBException | FileNotFoundException e) {
            System.err.println("Can not create first xml file, " + e.getMessage());
            System.exit(0);
        }
    }

    public void transformToSecondXml() {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            Source xslt = new StreamSource(new File(AppConst.CONVERT_XLST_FILE));
            transformer = factory.newTransformer(xslt);
            Source source = new StreamSource(new File(AppConst.FIRST_XML_FILE));
            transformer.transform(source, new StreamResult(new File(AppConst.SECOND_XML_FILE)));
        } catch (TransformerException e) {
            System.err.println("Can not transform to second xml file, " + e.getMessage());
            System.exit(0);
        }
    }


    public OutEntries getFromSecondXml() {
        OutEntries entries = null;
        try {
            JAXBContext jc = JAXBContext.newInstance(OutEntries.class);
            Unmarshaller u = jc.createUnmarshaller();
            entries = (OutEntries) u.unmarshal(new File(AppConst.SECOND_XML_FILE));
        } catch (JAXBException e) {
            System.err.println("Can not unmarshal second xml file, " + e.getMessage());
            System.exit(0);
        }
        return entries;
    }
}