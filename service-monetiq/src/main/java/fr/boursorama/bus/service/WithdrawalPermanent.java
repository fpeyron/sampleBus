package fr.boursorama.bus.service;

import java.util.Date;

/**
 * Created by fpeyron on 02/10/2015.
 */
public class WithdrawalPermanent {

    private Long panId;
    private Long CashAmount;
    private String PaymentCode;

    public long getPanId() {
        return panId;
    }

    public void setPanId(long panId) {
        this.panId = panId;
    }

    public void setPanId(Long panId) {
        this.panId = panId;
    }

    public Long getCashAmount() {
        return CashAmount;
    }

    public void setCashAmount(Long cashAmount) {
        CashAmount = cashAmount;
    }

    public String getPaymentCode() {
        return PaymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        PaymentCode = paymentCode;
    }

}
