package com.sherman.getwords.bean;

/**
 * author: 李梦(<a href="mailto:limeng@danlu.com">limeng@danlu.com</a>)<br/>
 * version: $VERSION<br/>
 * since: 2018-05-21 11:29<br/>
 *
 * <p>
 * $DESCRIPTION
 * </p>
 */

public class SmsResponse {

    private String code;

    private String message;

    private Sms data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Sms getData() {
        return data;
    }

    public void setData(Sms data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;
}
