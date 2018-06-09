package com.sherman.getwords.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sherman.getwords.R;
import com.sherman.getwords.videomanage.ui.VideoPlayerView;

public class ActivityDialog extends Activity {

    private String url;


    private TextView bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

    }
}
