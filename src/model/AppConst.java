package model;

import java.io.File;

public class AppConst {

    public static final String WORK_DIR = System.getProperty("user.home")+ File.separator+"magnet";

    public static final String COLUMN_FIELD = "field";
    public static final String FIRST_XML_FILE = WORK_DIR + File.separator + "1.xml";
    public static final String SECOND_XML_FILE = WORK_DIR + File.separator + "2.xml";
    public static final String TEMPLATE_XLST =
            "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">"+
                    "<xsl:template match=\"entries\">"+
                        "<xsl:element name=\"entries\">"+
                            "<xsl:apply-templates select=\"./entry\"/>"+
                        "</xsl:element>"+
                    "</xsl:template>"+
                    "<xsl:template match=\"entry\">"+
                        "<xsl:element name=\"entry\">"+
                            "<xsl:attribute name=\"field\">"+
                            "<xsl:value-of select=\"field\"/>"+
                        "</xsl:attribute>"+
                    "</xsl:element>"+
                    "</xsl:template>"+
            "</xsl:stylesheet>";
    public static final String CONVERT_XLST_FILE = WORK_DIR + File.separator + "convert.xlst";

}
