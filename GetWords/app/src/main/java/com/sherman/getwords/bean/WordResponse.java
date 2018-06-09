package com.sherman.getwords.bean;

import java.util.List;

/**
 * author: 李梦(<a href="mailto:limeng@danlu.com">limeng@danlu.com</a>)<br/>
 * version: $VERSION<br/>
 * since: 2018-05-18 16:02<br/>
 *
 * <p>
 * $DESCRIPTION
 * </p>
 */

public class WordResponse {

    public WordResponse(){

    }

    private String code;

    private String message;

    private List<WordBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<WordBean> getData() {
        return data;
    }

    public void setData(List<WordBean> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;

    }

    public void setMessage(String message) {
        this.message = message;
    }
}
