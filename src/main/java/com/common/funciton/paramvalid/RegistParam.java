package com.common.funciton.paramvalid;

import com.common.funciton.ItvJsonUtil;

/**
 * Created by wangqiang on 2019/8/6.
 */
public class RegistParam extends RequestMsg{
    @ParamNotNull(message = "注册账号不可为空")
    private String account;
    @ParamNotNull(message = "注册密码不可为空")
    private String password;

    public RegistParam() {
    }

    public RegistParam(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public RegistParam(String communicationId, String requstIp, String equipmentId, String swapType, Long requstTime, String sign, String account, String password) {
        super(communicationId, requstIp, equipmentId, swapType, requstTime, sign);
        this.account = account;
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return ItvJsonUtil.toJson(this);
    }

    /*public static void main(String[] args) {
        RegistParam param = new RegistParam();
        param.setRequstIp("woerj");
        param.setAccount(IdUtil.getUId());
        param.setPassword("ouisrow");
        param.setSwapType("sjdf");
        System.out.println(param.toString());
    }*/
}
