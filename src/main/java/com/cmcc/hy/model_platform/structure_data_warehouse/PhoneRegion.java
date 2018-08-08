package com.cmcc.hy.model_platform.structure_data_warehouse;

import java.io.Serializable;

import lombok.Data;

public  class PhoneRegion implements Serializable {
    private String time1;
    private String sendTime;
    private String receiveTime;
    private Integer counts;
    private String sourceId;
    private String operatorId;
    private String errorCode;
    private String phoneNumber;
    private String unName1;
    private String unName2;
    private String userName;
    private String userIP;
    private String phoneId;
    private String unName3;
    private String unName4;
    private String jsonBody;

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUnName1() {
        return unName1;
    }

    public void setUnName1(String unName1) {
        this.unName1 = unName1;
    }

    public String getUnName2() {
        return unName2;
    }

    public void setUnName2(String unName2) {
        this.unName2 = unName2;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIP() {
        return userIP;
    }

    public void setUserIP(String userIP) {
        this.userIP = userIP;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public String getUnName3() {
        return unName3;
    }

    public void setUnName3(String unName3) {
        this.unName3 = unName3;
    }

    public String getUnName4() {
        return unName4;
    }

    public void setUnName4(String unName4) {
        this.unName4 = unName4;
    }

    public String getJsonBody() {
        return jsonBody;
    }

    public void setJsonBody(String jsonBody) {
        this.jsonBody = jsonBody;
    }
}
