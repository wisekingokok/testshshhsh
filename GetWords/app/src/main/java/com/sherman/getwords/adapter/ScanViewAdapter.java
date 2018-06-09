package com.sherman.getwords.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sherman.getwords.R;
import com.sherman.getwords.bean.WordBean;
import com.sherman.getwords.lib.GetWordTextView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class ScanViewAdapter extends PageAdapter
{
	Context context;
	String mEnglishContent;
	List<String> items;
	AssetManager am;

	public ScanViewAdapter(Context context, String mContent,List<String> items)
	{
		this.context = context;
		this.mEnglishContent = mContent;
		this.items = items;
		am = context.getAssets();
	}

	public void addContent(View view, int position)
	{
		GetWordTextView content = (GetWordTextView) view.findViewById(R.id.content);
		if ((position - 1) < 0 || (position - 1) >= getCount())
			return;
		content.setText(items.get(position-1).toString());
//		tv.setText(position+"");
		content.setOnWordClickListener(new GetWordTextView.OnWordClickListener() {
			@Override
			public void onClick(final String word) {
			    //加入生词库
                RealmResults<WordBean> wordResults = Realm.getDefaultInstance().where(WordBean.class).equalTo("word",word).findAllAsync();
                wordResults.addChangeListener(new RealmChangeListener<RealmResults<WordBean>>() {
                    @Override
                    public void onChange(RealmResults<WordBean> element) {
                        List<WordBean> datas = Realm.getDefaultInstance().copyFromRealm(element);
                        if (datas.size() == 0){
                            Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    WordBean wordBean = realm.createObject(WordBean.class);
                                    wordBean.setWord(word);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(context,"增加生词成功" ,Toast.LENGTH_SHORT).show();
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    Toast.makeText(context,"增加生词成功" ,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

//				Toast.makeText(context, word, Toast.LENGTH_SHORT);
			}
		});
	}

	public int getCount()
	{
		return items.size();
	}

	public View getView()
	{
		View view = LayoutInflater.from(context).inflate(R.layout.page_layout,
				null);
		return view;
	}
}
