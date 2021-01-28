package com.ROIIM.Assignment.dto.SingleUseCustomerTokenDTOs;

import java.util.List;

public class SingleUseCustomerTokenRequestDTO {

    private String merchantRefNum;

    private List< String > paymentTypes;

    public String getMerchantRefNum() {
        return merchantRefNum;
    }

    public void setMerchantRefNum(String merchantRefNum) {
        this.merchantRefNum = merchantRefNum;
    }

    public List<String> getPaymentTypes() {
        return paymentTypes;
    }

    public void setPaymentTypes(List<String> paymentTypes) {
        this.paymentTypes = paymentTypes;
    }

    @Override
    public String toString() {
        return "SingleUseCustomerTokenRequestDTO{" +
                "merchantRefNum='" + merchantRefNum + '\'' +
                ", paymentTypes=" + paymentTypes +
                '}';
    }
}
