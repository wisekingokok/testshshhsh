package com.sherman.getwords.activity;

import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import com.sherman.getwords.R;
import com.sherman.getwords.adapter.ScanViewAdapter;
import com.sherman.getwords.adapter.TextPageAdapter;
import com.sherman.getwords.reader.utils.ReaderSettingManager;
import com.sherman.getwords.utils.ScreenUtils;
import com.sherman.getwords.videoplayer.utils.Utils;
import com.sherman.getwords.view.ScanView;
import java.util.ArrayList;
import java.util.List;

public class PageReadActivity extends AppCompatActivity {

    ScanView scanview;
    ScanViewAdapter adapter;
    private Button btn_tips;

    private String articleText;

    private TextPaint mTextPaint;

    List<String> mPageTextList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_read);

        articleText = getIntent().getStringExtra("ArticleText");

        ReaderSettingManager.init(this);
        scanview = (ScanView) findViewById(R.id.scanview);

        init();
        if(!TextUtils.isEmpty(articleText)){

            scanview.post(new Runnable() {
                @Override
                public void run() {
                    StaticLayout staticLayout = new StaticLayout(articleText , mTextPaint , scanview.getWidth() , Layout.Alignment.ALIGN_NORMAL , 1f , 0.5f , true);

                    int lineHeight = staticLayout.getLineBottom(0) - staticLayout.getLineTop(0);
                    int pageMaxlineCount = scanview.getHeight() / lineHeight;
                    Log.i("======",staticLayout.getLineCount()+"="+pageMaxlineCount+"="+scanview.getHeight());
                    if(staticLayout.getLineCount() > pageMaxlineCount){
                        // 需要有多页
                        int maxPageTextNum = staticLayout.getLineEnd(pageMaxlineCount);
                        int beginIndex = 0;
                        while(beginIndex < articleText.length()){
                            int endIndex = beginIndex + maxPageTextNum;
                            if(endIndex > articleText.length()){
                                endIndex = articleText.length();
                            }
                            String pageText = articleText.substring(beginIndex , endIndex);
                            mPageTextList.add(pageText);
                            beginIndex = endIndex;
                        }
                    }else{
                        // 只有一页
                        mPageTextList.add(articleText);
                    }


                    adapter = new ScanViewAdapter(PageReadActivity.this, articleText,mPageTextList);
                    scanview.setAdapter(adapter);
                }
            });


        }


        btn_tips = findViewById(R.id.btn_tips);

        btn_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DictionaryDialog(PageReadActivity.this, new DictionaryDialog.ShareOnClickListener() {
                    @Override
                    public void onClickPosition(int position) {

                    }
                }).show();
            }
        });


    }

    private void init(){
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(Utils.dp2px(this , 30));
        mTextPaint.setColor(0xff000000);

        mPageTextList = new ArrayList<>();
    }

}
