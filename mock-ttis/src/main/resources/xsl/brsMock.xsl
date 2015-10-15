<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    <xsl:output method="xml" indent="yes" encoding="UTF-8" omit-xml-declaration="yes"/>


    <xsl:template match="/CSD002Aller">
        <gen:notifier xmlns:gen="http://generic.transactis.bus.boursorama.fr/">
            <messageSMPAllerXML>
                <CSD002Retour>
                    <xsl:element name="serviceMetierPublicRetour">
                        <xsl:element name="codRetour">OK</xsl:element>
                        <xsl:element name="refExtDemSMP">
                            <xsl:value-of select="//refExtEmetteur/text()" />
                        </xsl:element>
                        <xsl:element name="tempsReponse">26</xsl:element>
                    </xsl:element>
                </CSD002Retour>
            </messageSMPAllerXML>
        </gen:notifier>
    </xsl:template>

</xsl:stylesheet>