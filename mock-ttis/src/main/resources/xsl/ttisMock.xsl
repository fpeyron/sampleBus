<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    <xsl:output method="xml" indent="yes" encoding="UTF-8" omit-xml-declaration="yes"/>

    <xsl:template match="/CSD001Aller|/CSD002Aller">
        <gen:activerResponse xmlns:gen="http://generic.monetiq.evolan.sopra.com/">
            <messageSMPRetourXML>
                <CSD002Retour>
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
                </CSD002Retour>
            </messageSMPRetourXML>
        </gen:activerResponse>
    </xsl:template>


    <xsl:template match="/CSQ004Aller">
        <gen:activerResponse xmlns:gen="http://generic.monetiq.evolan.sopra.com/">
            <messageSMPRetourXML>
                <CSQ004Retour>
                    <xsl:element name="serviceMetierPublicRetour">
                        <xsl:element name="codRetour">
                            <xsl:text>OK</xsl:text>
                        </xsl:element>
                        <xsl:element name="refExtDemSMP">
                            <xsl:value-of select="//refExtDemSMP/text()"/>
                        </xsl:element>
                        <xsl:element name="tempsReponse">
                            <xsl:text>26</xsl:text>
                        </xsl:element>
                    </xsl:element>
                    <datConsultation>20151015</datConsultation>
                    <heureConsultation>101410</heureConsultation>
                    <listOfEncours>
                        <encours>
                            <codPlafond>PAYMENT</codPlafond>
                            <mntPlafond>2000</mntPlafond>
                            <encoursPlafond>525</encoursPlafond>
                            <periodElementaire>3</periodElementaire>
                            <nbPeriodes>1</nbPeriodes>
                        </encours>
                        <encours>
                            <codPlafond>PAYMENT</codPlafond>
                            <mntPlafond>2000</mntPlafond>
                            <encoursPlafond>1850</encoursPlafond>
                            <periodElementaire>1</periodElementaire>
                            <nbPeriodes>2</nbPeriodes>
                            <datDebutEffet>20151014</datDebutEffet>
                            <datFinEffet>20151015</datFinEffet>
                        </encours>
                        <encours>
                            <codPlafond>CASH</codPlafond>
                            <mntPlafond>500</mntPlafond>
                            <encoursPlafond>200</encoursPlafond>
                            <periodElementaire>2</periodElementaire>
                            <nbPeriodes>1</nbPeriodes>
                        </encours>
                        <encours>
                            <codPlafond>CASH</codPlafond>
                            <mntPlafond>2000</mntPlafond>
                            <encoursPlafond>1500</encoursPlafond>
                            <periodElementaire>1</periodElementaire>
                            <nbPeriodes>1</nbPeriodes>
                            <datDebutEffet>20151014</datDebutEffet>
                            <datFinEffet>20151015</datFinEffet>
                        </encours>
                    </listOfEncours>
                </CSQ004Retour>
            </messageSMPRetourXML>
        </gen:activerResponse>
    </xsl:template>


</xsl:stylesheet>