package com.common.funciton.paramvalid;


import java.io.Serializable;

/**
 * @Description:
 * @Author: wangqiang
 * @Date:2019/3/4 17:00
 */
public class RequestMsg implements Serializable{
    @ParamNotNull(message = "通讯流水号为空")
    private String communicationId;
    @ParamNotNull(message = "请求IP为空")
    private String requstIp;
    //@ParamNotNull(message = "设备id为空")
    private String equipmentId;
    @ParamNotNull(message = "交易类型为空")
    private String swapType;//一个接口一个交易类型
    @ParamNotNull(message = "请求时间为空")
    private Long requstTime;//时间戳
    //@ParamNotNull(message = "签名为空")
    private String sign;

    public RequestMsg() {
    }

    public RequestMsg(String communicationId, String requstIp, String equipmentId, String swapType, Long requstTime, String sign) {
        this.communicationId = communicationId;
        this.requstIp = requstIp;
        this.equipmentId = equipmentId;
        this.swapType = swapType;
        this.requstTime = requstTime;
        this.sign = sign;
    }

    public String getCommunicationId() {
        return communicationId;
    }

    public void setCommunicationId(String communicationId) {
        this.communicationId = communicationId;
    }

    public String getRequstIp() {
        return requstIp;
    }

    public void setRequstIp(String requstIp) {
        this.requstIp = requstIp;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getSwapType() {
        return swapType;
    }

    public void setSwapType(String swapType) {
        this.swapType = swapType;
    }

    public Long getRequstTime() {
        return requstTime;
    }

    public void setRequstTime(Long requstTime) {
        this.requstTime = requstTime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "RequestMsg{" +
                "communicationId='" + communicationId + '\'' +
                ", requstIp='" + requstIp + '\'' +
                ", equipmentId='" + equipmentId + '\'' +
                ", swapType='" + swapType + '\'' +
                ", requstTime='" + requstTime + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
