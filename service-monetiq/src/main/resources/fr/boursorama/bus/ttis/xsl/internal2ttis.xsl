<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    <xsl:output method="xml" indent="yes" encoding="UTF-8" omit-xml-declaration="yes"/>

    <xsl:param name="correlationId" />

    <xsl:template match="/">
        <xsl:element name="CSD002Aller">
            <xsl:element name="serviceMetierPublicAller">
                <xsl:element name="refExtEmetteur"><xsl:value-of select="$correlationId" /></xsl:element>
                <xsl:element name="idService">232</xsl:element>
                <xsl:element name="version">1.0</xsl:element>
                <xsl:element name="userid">012121</xsl:element>
                <xsl:element name="codLang">23232</xsl:element>
                <xsl:element name="refExtDemSMP">2121</xsl:element>
            </xsl:element>
            <numPan>1212323453535</numPan>
            <datFinValidite>201602</datFinValidite>
            <codDemande>A232</codDemande>
            <codCanalAcquisition>21DSD22</codCanalAcquisition>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>