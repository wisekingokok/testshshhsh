package com.sherman.getwords.bean;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class WordDBUtil {

    private static volatile WordDBUtil instance=null;

    private WordDBUtil (){

    }

    public static WordDBUtil getInstance(){
        if(instance == null){
            synchronized(WordDBUtil .class){
                if(instance == null){
                    instance = new WordDBUtil ();
                }
            }
        }
        return instance;
    }

    //保存单词库
    public boolean insert(final List<WordBean> object) {
        try {
            Realm.getDefaultInstance().beginTransaction();
            Realm.getDefaultInstance().copyToRealmOrUpdate(object);
            Realm.getDefaultInstance().commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Realm.getDefaultInstance().cancelTransaction();
            return false;
        }
    }

    //保存单词库视频
    public boolean insertVideo(final List<VideoBean> object) {
        try {
            Realm.getDefaultInstance().beginTransaction();
            Realm.getDefaultInstance().copyToRealmOrUpdate(object);
            Realm.getDefaultInstance().commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Realm.getDefaultInstance().cancelTransaction();
            return false;
        }
    }

    //获取单词库
    public final List<WordBean> getAllWord(){

        return Realm.getDefaultInstance().where(WordBean.class).findAll();
    }

    //分页获取单词库
    public final List<WordBean> getPageWord(int page){
        RealmResults<WordBean> wordBeans = Realm.getDefaultInstance().where(WordBean.class).findAllAsync();
        if (!wordBeans.isEmpty()){
            return wordBeans.subList((page-1)*20,page*20);
        }
        return null;
    }



}
