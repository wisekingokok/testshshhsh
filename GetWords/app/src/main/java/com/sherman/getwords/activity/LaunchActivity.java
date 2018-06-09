package com.sherman.getwords.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.sherman.getwords.R;
import com.sherman.getwords.utils.SharedPreferencesHelper;

/**
 * author: 李梦(<a href="mailto:limeng@danlu.com">limeng@danlu.com</a>)<br/>
 * version: $VERSION<br/>
 * since: 2018-05-21 10:25<br/>
 *
 * <p>
 * $DESCRIPTION
 * </p>
 */

public class LaunchActivity extends AppCompatActivity {

    SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);

        sharedPreferencesHelper = new SharedPreferencesHelper(getApplication());
        if (!TextUtils.isEmpty(sharedPreferencesHelper.getString("userName",""))){
            forwordMain();
            finish();
        }else {
            forwordLogin();
            finish();
        }
    }

    public void forwordMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void forwordLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
