import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class TestCase {

    private int n;
    private String uri2connect;
    private String login;
    private String password;

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


    /*
    Format to call app: testcase url,login,pass,number
    @uri2connect - URI for DB connection
    @login - login to DB
    @password - password to DB
    @N - count of rows in DB

    */

    public static void main(String args[]) throws
            SQLException
            , ParserConfigurationException
            , TransformerException
            , SAXException
            , IOException {

        long timestamp = System.currentTimeMillis();
        TestCase testCase = new TestCase();
        testCase.setUri2connect(args[0]);
        testCase.setLogin(args[1]);
        testCase.setPassword(args[2]);
        testCase.setN(Integer.parseInt(args[3]));

        Connection conn = DriverManager.getConnection(testCase.getUri2connect()
                , testCase.getPassword()
                , testCase.getLogin());
        conn.setAutoCommit(false);
        Statement stmt = conn.createStatement();
        PreparedStatement psmt = conn.prepareStatement("INSERT INTO test (field) VALUES (?)");

// 2. drop table and insert N records
        stmt.execute("CREATE TABLE IF NOT EXISTS test (field INTEGER)");
        stmt.execute("TRUNCATE test");


        for (int i = 1; i <= testCase.getN(); i++) {
            psmt.setInt(1, i);
            psmt.addBatch();

        }
        psmt.executeBatch();
        conn.commit();

// 3. build xml from TEST.FIELD

        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = icFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();

        Element entries = doc.createElement("entries");
        doc.appendChild(entries);

        ResultSet r = stmt.executeQuery("SELECT field FROM test");
        while (r.next()) {
            String value = Integer.toString(r.getInt(1));
            Element entry = doc.createElement("entry");
            Element field = doc.createElement("field");
            field.setTextContent(value);
            entry.appendChild(field);
            entries.appendChild(entry);
        }

        Transformer tr = TransformerFactory.newInstance().newTransformer();
        DOMSource source = new DOMSource(doc);
        FileOutputStream fos = new FileOutputStream("1.xml");
        StreamResult result = new StreamResult(fos);
        tr.transform(source, result);

        r.close();
        stmt.close();
        conn.close();

// 4. convert elements to attributes
        Source xsl = new StreamSource(new File("convert.xsl"));
        tr = TransformerFactory.newInstance().newTransformer(xsl);
        Source xmlInput = new StreamSource(new File("1.xml"));
        Result xmlOutput = new StreamResult(new File("2.xml"));
        tr.transform(xmlInput, xmlOutput);

// 5. parse 2.xml and output sum to con

        dBuilder.reset();
        doc = dBuilder.parse(new File("2.xml"));
        NodeList fieldsList = doc.getElementsByTagName("fields");
        for (int i = 1; i <= fieldsList.getLength(); i++) {
            Node n = fieldsList.item(i).getFirstChild();
            String temp = n.getNodeValue();
            System.out.println(temp);
        }
        System.out.println("Total execution time: "+(System.currentTimeMillis()-timestamp)/1000+" sec");
    }
}

