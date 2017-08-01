import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

import static java.lang.System.exit;

public class TestCase {

    private int n;
    private String uri2connect;
    private String login;
    private String password;
    private Connection dbConnection;

    public TestCase() {
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public String getUri2connect() {
        return uri2connect;
    }

    public void setUri2connect(String uri2connect) {
        this.uri2connect = uri2connect;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean startDbConnection() {
        try {
            this.dbConnection = DriverManager.getConnection(this.getUri2connect()
                    , this.getPassword()
                    , this.getLogin());
            this.dbConnection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            if (this.dbConnection != null) try {
                this.dbConnection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public void stopDBConnection() {
        try {
            this.dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*  create table and fill it
        @table - table name
        @field - field name
        @type - field type
    */

    public boolean createTable(String table, String field, String type) {
        try {
            Statement sqlStatement = dbConnection.createStatement();
            PreparedStatement sqlPrepStatement = dbConnection.prepareStatement("INSERT INTO " + table + " (" + field + ") VALUES (?)");
            sqlStatement.execute("CREATE TABLE IF NOT EXISTS " + table + " (" + field + " " + type + ")");
            sqlStatement.execute("TRUNCATE " + table);

            for (int i = 1; i <= this.getN(); i++) {
                sqlPrepStatement.setInt(1, i);
                sqlPrepStatement.addBatch();
            }
            sqlPrepStatement.executeBatch();
            dbConnection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

     /*  create XML from DB data
         @table - table name
         @field - field name
         @levelOne - top level tag name
         @levelTwo - child level tag name
         @xnlFile - XML file name
    */

    public boolean createXML(String table, String field, String levelOne, String levelTwo, String xmlFile) {

        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Statement sqlStatement = dbConnection.createStatement();

            Element entries = doc.createElement(levelOne);
            doc.appendChild(entries);

            ResultSet result = sqlStatement.executeQuery("SELECT " + field + " FROM " + table);
            while (result.next()) {
                String value = Integer.toString(result.getInt(1));
                Element entry = doc.createElement(levelTwo);
                Element xfield = doc.createElement(field);
                xfield.setTextContent(value);
                entry.appendChild(xfield);
                entries.appendChild(entry);
            }
            Transformer xmlTransform = TransformerFactory.newInstance().newTransformer();
            xmlTransform.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(xmlFile)));
            return true;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (TransformerException e) {
            e.printStackTrace();
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

    }

    /*   convert elements to attributes with XSLT script
         @inFile - file name to convert
         @outFile - result file name
         @xsl - XSLT file name

    */
    public boolean xmlConversion(String inFile, String outFile, String xsl) {
        try {
            Transformer xmlTransform = TransformerFactory.newInstance().newTransformer(new StreamSource(new File(xsl)));
            xmlTransform.transform(new StreamSource(new File(inFile)), new StreamResult(new File(outFile)));
            return true;
        } catch (TransformerException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*   summ values from XML file
         @inFile - XML file name
     */

    public long getSumm(String inFile) {

        Document doc = null;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(inFile));
        } catch (SAXException e) {
            e.printStackTrace();
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return -1;
        }

        Node root = doc.getFirstChild();
        NodeList children = root.getChildNodes();
        long summ = 0;
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            NamedNodeMap attributes = node.getAttributes();
            Node field = attributes.getNamedItem("field");
            summ += (long) Integer.parseInt(field.getNodeValue());
        }
        return summ;
    }

    /*	Format to call app: testcase url,login,pass,number
        @uri2connect - URI for DB connection
        @login - login to DB
        @password - password to DB
        @N - count of rows in DB
    */
    public static void main(String args[]) {

        long timestamp = System.currentTimeMillis();
        long summ;
        TestCase testCase = new TestCase();
        testCase.setUri2connect(args[0]);
        testCase.setLogin(args[1]);
        testCase.setPassword(args[2]);
        testCase.setN(Integer.parseInt(args[3]));

        if (!testCase.startDbConnection()) {
            System.err.println("Error on establish connection");
            exit(1);
        }
        ;
        if (!testCase.createTable("test", "field", "INTEGER")) {
            System.err.println("Error during creating table");
            exit(1);
        }
        ;
        System.out.println("Insert table execution time: "
                + (System.currentTimeMillis() - timestamp) / 1000 + " sec");

        if (!testCase.createXML("test", "field", "entries", "entry", "1.xml")) {
            System.err.println("Error during create XML");
            testCase.stopDBConnection();
            exit(1);
        }
        ;

        System.out.println("Build XML execution time: "
                + (System.currentTimeMillis() - timestamp) / 1000 + " sec");

        if (!testCase.xmlConversion("1.xml", "2.xml", "convert.xsl")) {
            System.err.println("Error during XML conversion");
            exit(1);
        }
        ;
        System.out.println("Convert to attributes time: "
                + (System.currentTimeMillis() - timestamp) / 1000 + " sec");

        summ = testCase.getSumm("2.xml");
        System.out.println(summ != -1 ?
                "Summ is " +summ
                : "Error during summ operation");
        System.out.println("Total execution time: "
                + (System.currentTimeMillis() - timestamp) / 1000 + " sec");
    }
}

