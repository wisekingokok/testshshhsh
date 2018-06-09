package com.sherman.getwords.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sherman.getwords.R;
import com.sherman.getwords.lib.SeekBarRelativeLayout;

public class DictionaryDialog extends Dialog implements View.OnClickListener {

    private ShareOnClickListener mShareOnClickListener;

    public DictionaryDialog(Context context, ShareOnClickListener shareOnClickListener) {
        super(context, R.style.ShareDialog);
        initView();
        mShareOnClickListener = shareOnClickListener;
    }

    private void initView() {
        View view = View.inflate(getContext().getApplicationContext(), R.layout.activity_dialog, null);
        SeekBarRelativeLayout seekBarRelativeLayout = (SeekBarRelativeLayout) view.findViewById(R.id.seek_bar);
        final TextView seek_bar_relative_layout_text_view = view.findViewById(R.id.seek_bar_relative_layout_text_view);
        seekBarRelativeLayout.initSeekBar();

        seekBarRelativeLayout.setProgress(1000);
        seek_bar_relative_layout_text_view.setText(1000+"");
        seekBarRelativeLayout.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                seek_bar_relative_layout_text_view.setText(progress+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                seek_bar_relative_layout_text_view.setText(seekBar.getProgress()+"");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                seek_bar_relative_layout_text_view.setText(seekBar.getProgress()+"");
            }
        });
        seek_bar_relative_layout_text_view.setText(seekBarRelativeLayout.getProcess()+"");

        // 加载布局
        setContentView(view);
        // 设置Dialog参数
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    public interface ShareOnClickListener {
        public void onClickPosition(int position);
    }
}
