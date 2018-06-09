package com.sherman.getwords.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.lzy.okgo.OkGo;
import com.sherman.getwords.R;
import com.sherman.getwords.adapter.SecendPageAdapter;
import com.sherman.getwords.bean.AllArticleResponse;
import com.sherman.getwords.bean.Article;
import com.sherman.getwords.bean.JsonCallback;
import com.sherman.getwords.bean.UrlBean;
import com.sherman.getwords.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

public class PageSecendActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Article> mDatas;
    private SecendPageAdapter mAdapter;
    private int typeId;
    private ProgressBar progress_center;
    private SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secend_page);

        sharedPreferencesHelper = new SharedPreferencesHelper(getApplication());
        recyclerView = findViewById(R.id.list);
        progress_center = findViewById(R.id.progress_center);
        initData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new SecendPageAdapter(this,mDatas));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        mAdapter.setOnItemClickLitener(new SecendPageAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(String article) {
                Intent intent = new Intent(PageSecendActivity.this,PageReadActivity.class);
                intent.putExtra("ArticleText",article);
                Log.i("11=========",article);
                startActivity(intent);
            }
        });

    }

    protected void initData()
    {
        mDatas = new ArrayList<Article>();
        typeId = getIntent().getIntExtra("typeId",0);
        //根据分类id获取分类文章
        OkGo.<AllArticleResponse>post(UrlBean.getAllArticle)
                .params("token",sharedPreferencesHelper.getString("token",""))
                .params("typeId",typeId)
                .isMultipart(true)
                .tag(this)
                .execute(new JsonCallback<AllArticleResponse>(AllArticleResponse.class) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<AllArticleResponse> response) {
                        if (response.body().getCode().equals("200")){
                            if (response.body().getData() != null){
                                progress_center.setVisibility(View.GONE);
                                mDatas.addAll(response.body().getData());
                                mAdapter.notifyDataSetChanged();
                            }
                        }else {
                            Intent intent = new Intent(getApplication(), LoginActivity.class);
                            startActivity(intent);
                        }

                    }
                });
    }

}
