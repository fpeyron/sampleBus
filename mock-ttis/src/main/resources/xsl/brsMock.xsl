<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    <xsl:output method="xml" indent="yes" encoding="UTF-8" omit-xml-declaration="yes"/>

    <xsl:template match="/CSD002Aller">
        <gen:notifier xmlns:gen="http://generic.ttis.bus.boursorama.fr/">
            <messageSMPRetourXML>
                <CSA002Retour>
                    <xsl:element name="serviceMetierPublicRetour">
                        <xsl:element name="codRetour">
                            <xsl:text>OK</xsl:text>
                        </xsl:element>
                        <xsl:element name="refExtDemSMP">
                            <xsl:value-of select="//refExtDemSMP/text()" />
                        </xsl:element>
                        <xsl:element name="tempsReponse">
                            <xsl:text>26</xsl:text>
                        </xsl:element>
                    </xsl:element>
                </CSA002Retour>
            </messageSMPRetourXML>
        </gen:notifier>
    </xsl:template>

</xsl:stylesheet>