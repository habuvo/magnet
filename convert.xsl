<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:result-document href="2.xml" />
  <xsl:template match="entries">
    <entries>
      <xsl:apply-templates/>
    </entries>
  </xsl:template>

  <xsl:template match="entry">
    <entry>
        <xsl:attribute name="{name()}">
          <xsl:value-of select="text()"/>
        </xsl:attribute>
  </xsl:template>
</xsl:stylesheet>