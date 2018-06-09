package com.sherman.getwords.bean;

/**
 * author: 李梦(<a href="mailto:limeng@danlu.com">limeng@danlu.com</a>)<br/>
 * version: $VERSION<br/>
 * since: 2018-05-23 10:25<br/>
 *
 * <p>
 * $DESCRIPTION
 * </p>
 */

public class Article {

    private int id;

    private int typeId;

    private String typeName;

    private String articleFileName;

    private String articleText;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getArticleText() {
        return articleText;
    }

    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }

    public String getArticleFileName() {
        return articleFileName;
    }

    public void setArticleFileName(String articleFileName) {
        this.articleFileName = articleFileName;
    }
}
