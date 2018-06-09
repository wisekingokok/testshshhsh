package com.sherman.getwords.bean;

import java.util.List;

/**
 * author: 李梦(<a href="mailto:limeng@danlu.com">limeng@danlu.com</a>)<br/>
 * version: $VERSION<br/>
 * since: 2018-05-22 10:05<br/>
 *
 * <p>
 * $DESCRIPTION
 * </p>
 */

public class ArticleTypeResponse {

    private String code;

    private String message;

    private List<ArticleType> data;

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

    public List<ArticleType> getData() {
        return data;
    }

    public void setData(List<ArticleType> data) {
        this.data = data;
    }
}
