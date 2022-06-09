package com.laola.apa.common;

/**
 * 状态码
 *
 * @Author: fan
 * @DateTime: 2021-07-09 16:23
 **/
public enum StateCodeEnum {

    /**
     * 请求成功
     */
    SUCCESS(200, "请求成功"),

    /**
     * 请求失败
     */
    FAIL(500, "请求失败");

    /**
     * 状态码
     */
    private int code;

    /**
     * 提示信息
     */
    private String msg;

    StateCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
