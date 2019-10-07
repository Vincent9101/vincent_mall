package com.vincent.mall.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vincent.mall.constants.enumeration.EnumResponseCode;

import java.io.Serializable;

/**
 * @author: Vincent
 * @created: 2019/10/4  11:23
 * @description:服务器Response
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerResponse<Res> implements Serializable {
    private int status;
    private String msg;
    private Res data;

    private ServerResponse(int statusArg) {
        this.status = statusArg;
    }

    private ServerResponse(int statusArg, Res dataArg) {
        this.status = statusArg;
        this.data = dataArg;
    }

    private ServerResponse(int status, String msg, Res data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    @JsonIgnore
    public boolean isSuccessful() {
        return this.status == EnumResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public Res getData() {
        return data;
    }

    public static <Res> ServerResponse<Res> buildSuccessfulResponse() {
        return new ServerResponse(EnumResponseCode.SUCCESS.getCode(), EnumResponseCode.SUCCESS.getDesc());
    }

    public static <Res> ServerResponse<Res> buildSuccessfulMsgResponse(String msg) {
        return new ServerResponse(EnumResponseCode.SUCCESS.getCode(), msg);
    }

    public static <Res> ServerResponse<Res> buildSuccessfulDataResponse(Res data) {
        return new ServerResponse(EnumResponseCode.SUCCESS.getCode(), data);
    }

    public static <Res> ServerResponse<Res> buildSuccessfulDataResponse(String msg, Res data) {
        return new ServerResponse(EnumResponseCode.SUCCESS.getCode(), msg, data);
    }


    public static <Res> ServerResponse<Res> buildUnSuccessfulResponse() {
        return new ServerResponse<Res>(EnumResponseCode.ERROR.getCode(), EnumResponseCode.ERROR.getDesc());
    }

    public static <Res> ServerResponse<Res> buildUnSuccessfulMsgResponse(String errMsg) {
        return new ServerResponse(EnumResponseCode.ERROR.getCode(), errMsg);
    }

    public static <Res> ServerResponse<Res> buildUnSuccessfulCodeAndMsgResponse(int errCode, String errMsg) {
        return new ServerResponse(errCode, errMsg);
    }

}
