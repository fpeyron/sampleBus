<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    <xsl:output method="xml" indent="yes" encoding="UTF-8" omit-xml-declaration="yes"/>

    <xsl:template match="/">
        <gen:notifierResponse xmlns:gen="http://generic.ttis.bus.boursorama.fr/">
            <messageSMPRetourXML/>
        </gen:notifierResponse>
    </xsl:template>

</xsl:stylesheet>