package fr.boursorama.bus.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by fpeyron on 02/10/2015.
 */
@XmlRootElement(name = "bankCard")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BankCard {

    @JsonIgnore
    private Long panId;
    @JsonIgnore
    private Long contactId;

    private String visualCode;

    private Boolean activeSupport;

    private Boolean activeSupportNfc;

    private String PermanentCeilingPaymentCode;

    private String PermanentCeilingCashCode;

    private String TemporaryCeilingPaymentCode;

    private String TemporaryCeilingCashCode;

    private Date TemporaryCeilingDate;

    public Long getPanId() {
        return panId;
    }

    public void setPanId(Long panId) {
        this.panId = panId;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getVisualCode() {
        return visualCode;
    }

    public void setVisualCode(String visualCode) {
        this.visualCode = visualCode;
    }

    public boolean isActiveSupport() {
        return activeSupport;
    }

    public void setActiveSupport(boolean activeSupport) {
        this.activeSupport = activeSupport;
    }

    public boolean getActiveSupportNfc() {
        return activeSupportNfc;
    }

    public void setActiveSupportNfc(boolean activeSupportNfc) {
        this.activeSupportNfc = activeSupportNfc;
    }

    public String getPermanentCeilingPaymentCode() {
        return PermanentCeilingPaymentCode;
    }

    public void setPermanentCeilingPaymentCode(String permanentCeilingPaymentCode) {
        PermanentCeilingPaymentCode = permanentCeilingPaymentCode;
    }

    public String getPermanentCeilingCashCode() {
        return PermanentCeilingCashCode;
    }

    public void setPermanentCeilingCashCode(String permanentCeilingCashCode) {
        PermanentCeilingCashCode = permanentCeilingCashCode;
    }

    public String getTemporaryCeilingPaymentCode() {
        return TemporaryCeilingPaymentCode;
    }

    public void setTemporaryCeilingPaymentCode(String temporaryCeilingPaymentCode) {
        TemporaryCeilingPaymentCode = temporaryCeilingPaymentCode;
    }

    public String getTemporaryCeilingCashCode() {
        return TemporaryCeilingCashCode;
    }

    public void setTemporaryCeilingCashCode(String temporaryCeilingCashCode) {
        TemporaryCeilingCashCode = temporaryCeilingCashCode;
    }

    public Date getTemporaryCeilingDate() {
        return TemporaryCeilingDate;
    }

    public void setTemporaryCeilingDate(Date temporaryCeilingDate) {
        TemporaryCeilingDate = temporaryCeilingDate;
    }
}
