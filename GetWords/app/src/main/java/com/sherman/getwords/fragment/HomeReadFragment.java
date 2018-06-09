package com.sherman.getwords.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.sherman.getwords.R;
import com.sherman.getwords.activity.LoginActivity;
import com.sherman.getwords.activity.PageSecendActivity;
import com.sherman.getwords.adapter.HomeAdapter;
import com.sherman.getwords.bean.ArticleType;
import com.sherman.getwords.bean.ArticleTypeResponse;
import com.sherman.getwords.bean.JsonCallback;
import com.sherman.getwords.bean.UrlBean;
import com.sherman.getwords.bean.WordBean;
import com.sherman.getwords.bean.WordDBUtil;
import com.sherman.getwords.bean.WordResponse;
import com.sherman.getwords.utils.Constants;
import com.sherman.getwords.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class HomeReadFragment extends Fragment{

    private RecyclerView recyclerView;
    private List<ArticleType> mDatas;
    private HomeAdapter mAdapter;
    private SharedPreferencesHelper sharedPreferencesHelper;

    private TextView tv_remenber_text;
    private ProgressBar remenber_progressBar;

    private ProgressBar progress_center;

    public static HomeReadFragment newInstance(String s){
        HomeReadFragment homeFragment = new HomeReadFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARGS,s);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_content, container, false);

        recyclerView = view.findViewById(R.id.list);

        sharedPreferencesHelper = new SharedPreferencesHelper(getActivity().getApplication());

        tv_remenber_text = view.findViewById(R.id.tv_remenber_text);
        remenber_progressBar = view.findViewById(R.id.remenber_progressBar);

        progress_center = view.findViewById(R.id.progress_center);

        //更新记忆
        RealmResults<WordBean> wordResults = Realm.getDefaultInstance().where(WordBean.class).greaterThanOrEqualTo("remenberNum",4).findAllAsync();
        wordResults.addChangeListener(new RealmChangeListener<RealmResults<WordBean>>() {
            @Override
            public void onChange(RealmResults<WordBean> element) {
                List<WordBean> datas = Realm.getDefaultInstance().copyFromRealm(element);
                if (!datas.isEmpty()){
                    tv_remenber_text.setText(datas.size()+"");

                    remenber_progressBar.setProgress(datas.size());
                }else {
                    //获取单词库
                    OkGo.<WordResponse>post(UrlBean.getAllWord)
                            .params("token",sharedPreferencesHelper.getString("token",""))
                            .params("uploadTime",0)
                            .isMultipart(true).tag(this).execute(new JsonCallback<WordResponse>(WordResponse.class) {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<WordResponse> response) {
                            List<WordBean> list = response.body().getData();
                            WordDBUtil.getInstance().insert(list);
                        }
                    });
                }
            }
        });

        initData();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter = new HomeAdapter(getActivity(),mDatas));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        mAdapter.setOnItemClickLitener(new HomeAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position,ArticleType articleType) {

                Intent intent = new Intent(getActivity(), PageSecendActivity.class);
                intent.putExtra("typeId",articleType.getId());

                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    protected void initData()
    {
        mDatas = new ArrayList<ArticleType>();
        //获取分类
        OkGo.<ArticleTypeResponse>post(UrlBean.getAllArticleType)
                .params("token",sharedPreferencesHelper.getString("token",""))
                .isMultipart(true)
                .tag(this)
                .execute(new JsonCallback<ArticleTypeResponse>(ArticleTypeResponse.class) {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<ArticleTypeResponse> response) {
                if (response.body().getCode().equals("200")){
                    progress_center.setVisibility(View.GONE);
                    if (response.body().getData() != null){
                        mDatas.addAll(response.body().getData());
                        mAdapter.notifyDataSetChanged();
                    }
                }else {
                    progress_center.setVisibility(View.GONE);
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
    }
}
