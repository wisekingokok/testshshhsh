package com.sherman.getwords.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sherman.getwords.R;
import com.sherman.getwords.bean.WordBean;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardHolder> {

    private List<WordBean> mCardBeanList;
    private RequestOptions mRequestOptions;
    private Typeface mFace;
    private Context context;

    public CardAdapter(List<WordBean> cardBeanList, Context mContext) {
        mCardBeanList = cardBeanList;
        context = mContext;
        mRequestOptions = new RequestOptions();
        mFace = Typeface.createFromAsset(context.getAssets(), "font/phonetic.ttf");
        mRequestOptions.placeholder(R.mipmap.card_default_film_bg).error(R.mipmap.card_default_film_bg).diskCacheStrategy(DiskCacheStrategy.NONE);
    }

    @Override
    public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_img_txt, parent, false);
        return new CardHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardHolder holder, final int position) {
        final WordBean bean = mCardBeanList.get(position);
//        Glide.with(holder.itemView).load(bean.getImgUrl()).apply(mRequestOptions).into(holder.img);
        holder.card_title.setText(bean.getWord());
        holder.card_phonetic.setTypeface(mFace);
        holder.card_phonetic.setText(bean.getPhonetic());
//        holder.card_info.setText(bean.getMeaning());
//        if (TextUtils.isEmpty(bean.getVideoUrl())){
//            holder.img.setVisibility(View.VISIBLE);
//        }else {
//            holder.img.setVisibility(View.INVISIBLE);
//        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(holder.itemView.getContext(), "click " + bean.getWord(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(holder.itemView.getContext(), "点击了 img", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCardBeanList.size();
    }

    static class CardHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView card_title;
        TextView card_phonetic;
        TextView card_info;

        public CardHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.card_img);
            card_title = itemView.findViewById(R.id.card_title);
            card_info = itemView.findViewById(R.id.card_info);
            card_phonetic = itemView.findViewById(R.id.card_soundMark);
        }
    }
}