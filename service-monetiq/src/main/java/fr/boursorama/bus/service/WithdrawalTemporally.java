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
@XmlRootElement(name="withdrawalTemporally")
@XmlAccessorType(XmlAccessType.FIELD)
public class WithdrawalTemporally {

    @JsonIgnore
    private Long panId;
    private Long cashAmount;
    private String paymentCode;
    private Date dueDate;

    @JsonIgnore
    private Long contactId;
    @JsonIgnore
    private String source;

    public Long getPanId() {
        return panId;
    }

    public void setPanId(Long panId) {
        this.panId = panId;
    }

    public Long getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Long cashAmount) {
        this.cashAmount = cashAmount;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
