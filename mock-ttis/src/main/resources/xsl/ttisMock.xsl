<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    <xsl:output method="xml" indent="yes" encoding="UTF-8" omit-xml-declaration="yes"/>

    <xsl:template match="/CSD002Aller">
        <gen:activerResponse xmlns:gen="http://generic.monetiq.evolan.sopra.com/">
            <messageSMPRetourXML>
                <CSD002Retour>
                    <xsl:element name="serviceMetierPublicRetour">
                        <!--
                        <xsl:element name="codRetour">
                            <xsl:text>OK</xsl:text>
                        </xsl:element>
                        <xsl:element name="refExtDemSMP">
                            <xsl:value-of select="//refExtDemSMP/text()" />
                        </xsl:element>
                        <xsl:element name="tempsReponse">
                            <xsl:text>26</xsl:text>
                        </xsl:element>
                        -->
                    </xsl:element>
                </CSD002Retour>
            </messageSMPRetourXML>
        </gen:activerResponse>
    </xsl:template>

</xsl:stylesheet>