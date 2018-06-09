package com.sherman.getwords.bean;

import java.util.ArrayList;
import java.util.List;

public class CardMaker {
//    public static final String U1 = "http://n.sinaimg.cn/translate/20161024/WRsW-fxwztru6973377.jpg";
//    public static final String U2 = "http://img02.tooopen.com/images/20151122/tooopen_sy_149199661189.jpg";
//    public static final String U3 = "http://gmimg.geimian.com/pic/2015/04/20150419_213113_920.jpg";
//    public static final String U4 = "http://pic.qiantucdn.com/58pic/11/84/20/04s58PICiYA.jpg";
//    public static final String U5 = "http://img02.tooopen.com/images/20160122/tooopen_sy_155234647714.jpg";
//    public static final String U6 = "http://seopic.699pic.com/photo/50007/5448.jpg_wh1200.jpg";
//    public static final String U7 = "https://thumbs.dreamstime.com/b/%E6%8A%BD%E8%B1%A1%E6%B2%B9%E7%94%BB-15920804.jpg";

    public static final String U1 = "";
    public static final String U2 = "";
    public static final String U3 = "";
    public static final String U4 = "";
    public static final String U5 = "";
    public static final String U6 = "";
    public static final String U7 = "";

//    abalone
//    amaze
//            ambassador
//    auction
//            blossom
//    blush
//            chrysanthemum
//    crystal
//            damp
//    flush
//            haunt
//    malversation
//            marsupial
//    moist
//            mollusk
//    shrub
//            stool
//    sway
//            tender

//    m1.videoUrl = "https://out-28db72074e1511e8afa700163e1c9256.oss-cn-shanghai.aliyuncs.com/bf20b0ed360f47bf86a3aa8d138e0163/6f5a0895b2754bd18c0bd659c9b2d825-cefd5761074760d6b8bd5c767c0a2629-ld.mp4";
//
//    VideoModel m2 = new VideoModel();
////        m2.coverImage = "http://android-imgs.25pp.com/fs08/2017/03/27/1/4bf77f572889c1ae186ccc29e4439be4.jpg";
//    m2.videoUrl = "https://out-28db72074e1511e8afa700163e1c9256.oss-cn-shanghai.aliyuncs.com/1809c922ccfe4b5a8df5027f963953bc/7c5092abee4f49f0ad3aef6d4225e5a6-00db7bf7d96a01b51441eba6f9d86fb6-ld.mp4";
//
//    VideoModel m3 = new VideoModel();
////        m3.coverImage = "http://android-imgs.25pp.com/fs08/2017/03/05/11/f74556e9d6e776a6abb85771f3632887.jpg";
//    m3.videoUrl = "https://out-28db72074e1511e8afa700163e1c9256.oss-cn-shanghai.aliyuncs.com/a07564def31040e69bb793ad6be64b5c/a700ffeb99234ec3a436abc6c54edf2f-44d921d8ce9acab0313050b778f35a29-ld.mp4";
//
//    VideoModel m4 = new VideoModel();
////        m4.coverImage = "http://android-imgs.25pp.com/fs08/2017/04/01/2/522bf786ea8c063d31c9e2b54892f086.jpg";
//    m4.videoUrl = "https://out-28db72074e1511e8afa700163e1c9256.oss-cn-shanghai.aliyuncs.com/a45be5868cac46ae9eaa99835b2cd261/f918ccfdd1624856bef38eec9a5558c6-aead50a08b25be75205c4baa4bea7095-ld.mp4";
//
//    VideoModel m5 = new VideoModel();
////        m5.coverImage = "http://android-imgs.25pp.com/fs08/2017/04/07/9/e6cdf397bb1bf1dcd963560ae17017a4.jpg";
//    m5.videoUrl = "https://out-28db72074e1511e8afa700163e1c9256.oss-cn-shanghai.aliyuncs.com/0ec23fb0ff184bcab6d8f0de551e3873/3a33ca6e0cc5425b8c9e20ff82cc7b74-251853fc3066fb118160da150a41ebd4-ld.mp4";
//
//    VideoModel m6 = new VideoModel();
////        m6.coverImage = "http://android-imgs.25pp.com/fs08/2017/01/07/11/79f91004a25ddbbeecb562bd4256d727.jpg";
//    m6.videoUrl = "https://out-28db72074e1511e8afa700163e1c9256.oss-cn-shanghai.aliyuncs.com/07a6d49dc5764dbaa5f5a22dd2eccbe1/1abf323cfd7c46e1a8727cacde9506e7-3fcef2ca05b21c8225ba937425a07848-ld.mp4";


    public static List<WordBean> initCards() {
        List<WordBean> list = new ArrayList<>();
        WordBean cardBean = new WordBean();
//        cardBean.setImgUrl(U1);
        cardBean.setWord("abalone");
        cardBean.setMeaning("vt.鲍鱼");
        cardBean.setVideoUrl("https://out-28db72074e1511e8afa700163e1c9256.oss-cn-shanghai.aliyuncs.com/bf20b0ed360f47bf86a3aa8d138e0163/6f5a0895b2754bd18c0bd659c9b2d825-cefd5761074760d6b8bd5c767c0a2629-ld.mp4");


        WordBean cardBean1 = new WordBean();
//        cardBean1.setImgUrl(U2);
        cardBean1.setWord("amaze");
        cardBean1.setMeaning("vt.吃惊，好奇");
        cardBean1.setVideoUrl("http://android-imgs.25pp.com/fs08/2017/03/27/1/4bf77f572889c1ae186ccc29e4439be4.jpg");

        WordBean cardBean2 = new WordBean();
//        cardBean2.setImgUrl(U3);
        cardBean2.setWord("ambassador");
        cardBean2.setMeaning("vt.大使，使节");
        cardBean2.setVideoUrl("http://android-imgs.25pp.com/fs08/2017/03/05/11/f74556e9d6e776a6abb85771f3632887.jpg");

        WordBean cardBean3 = new WordBean();
//        cardBean3.setImgUrl(U4);
        cardBean3.setWord("auction");
        cardBean3.setMeaning("vt.拍卖;竞卖");
        cardBean3.setVideoUrl("http://android-imgs.25pp.com/fs08/2017/04/01/2/522bf786ea8c063d31c9e2b54892f086.jpg");

        WordBean cardBean4 = new WordBean();
//        cardBean4.setImgUrl(U5);
        cardBean4.setWord("blossom");
        cardBean4.setMeaning("vt.花，群花");
        cardBean4.setVideoUrl("https://out-28db72074e1511e8afa700163e1c9256.oss-cn-shanghai.aliyuncs.com/0ec23fb0ff184bcab6d8f0de551e3873/3a33ca6e0cc5425b8c9e20ff82cc7b74-251853fc3066fb118160da150a41ebd4-ld.mp4");


        WordBean cardBean5 = new WordBean();
//        cardBean5.setImgUrl(U6);
        cardBean5.setWord("blush");
        cardBean5.setMeaning("vt.脸红，惭愧");
        cardBean5.setVideoUrl("https://out-28db72074e1511e8afa700163e1c9256.oss-cn-shanghai.aliyuncs.com/07a6d49dc5764dbaa5f5a22dd2eccbe1/1abf323cfd7c46e1a8727cacde9506e7-3fcef2ca05b21c8225ba937425a07848-ld.mp4");


        WordBean cardBean6 = new WordBean();
//        cardBean6.setImgUrl(U7);
        cardBean6.setWord("flush");
        cardBean6.setMeaning("vt.冲刷");
        cardBean6.setVideoUrl("");

        list.add(cardBean);
        list.add(cardBean1);
        list.add(cardBean2);
        list.add(cardBean3);
        list.add(cardBean4);
        list.add(cardBean5);
        list.add(cardBean6);


        return list;
    }
}
