package com.sherman.getwords.bean;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class VideoBean extends RealmObject implements Serializable{

    public VideoBean(){

    }

    @PrimaryKey
    private long id;
    private String word;
    private String soundMark;
    private String meaning;
    private String level;
    private String videoUrl;
    private String videoId;
    private String phonetic;

    //记忆次数
    private long remenberNum;

    //检查次数
    private long checkNum;


    @Ignore
    public int position;
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    private long uploadTime;

    public long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getSoundMark() {
        return soundMark;
    }

    public void setSoundMark(String soundMark) {
        this.soundMark = soundMark;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public long getRemenberNum() {
        return remenberNum;
    }

    public void setRemenberNum(long remenberNum) {
        this.remenberNum = remenberNum;
    }

    public long getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(long checkNum) {
        this.checkNum = checkNum;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }
}
