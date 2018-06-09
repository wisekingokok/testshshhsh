package com.sherman.getwords.bean;

/**
 * author: 李梦(<a href="mailto:limeng@danlu.com">limeng@danlu.com</a>)<br/>
 * version: $VERSION<br/>
 * since: 2018-05-18 11:05<br/>
 *
 * <p>
 * $DESCRIPTION
 * </p>
 */

public class UrlBean {

    private static String urlPath = "http://119.27.173.112:8080/dict/";

    //登录
    public static String login = urlPath + "remote-login";

    //获取验证吗
    public static String getCode = urlPath + "getCode";

    //注册
    public static String regist  = urlPath + "remote-register";

    //获取所有单词词库
    public static String getAllWord = urlPath + "getAllWord";

    //获取文章分类
    public static String getAllArticleType = urlPath + "getAllArticleType";

    //根据分类获取所有文章
    public static String getAllArticle = urlPath + "getAllArticle";

    //获取视频列表
    public static String getAllVideo = urlPath + "getWordIncludeVideoUrl";

    //获取单词视频地址
    public static String getVideoUrl = urlPath + "getVideoUrl";


}
