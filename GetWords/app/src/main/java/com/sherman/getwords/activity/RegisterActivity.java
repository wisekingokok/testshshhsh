package com.sherman.getwords.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.sherman.getwords.R;
import com.sherman.getwords.bean.JsonCallback;
import com.sherman.getwords.bean.SmsResponse;
import com.sherman.getwords.bean.UrlBean;
import com.sherman.getwords.bean.UserResponse;
import com.sherman.getwords.utils.SharedPreferencesHelper;

/**
 * author: 李梦(<a href="mailto:limeng@danlu.com">limeng@danlu.com</a>)<br/>
 * version: $VERSION<br/>
 * since: 2018-05-21 10:11<br/>
 *
 * <p>
 * $DESCRIPTION
 * </p>
 */

public class RegisterActivity extends AppCompatActivity {

    private SharedPreferencesHelper sharedPreferencesHelper;
    private EditText et_username;
    private EditText et_password;
    private EditText et_userauth_code;

    private Button btn_get_auth;
    private Button btn_Regist;

    private ProgressBar progress_center;

    private MyCountDownTimer myCountDownTimer;
    private boolean CountDownTimerIsStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        sharedPreferencesHelper = new SharedPreferencesHelper(getApplication());

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_userauth_code = findViewById(R.id.et_userauth_code);

        progress_center = findViewById(R.id.progress_center);

        btn_get_auth = findViewById(R.id.btn_get_auth);
        btn_Regist = findViewById(R.id.btn_Regist);

        btn_get_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et_username.getText().length() == 11){
                    btn_get_auth.setBackgroundResource(R.drawable.btn_bg_selector);

                    myCountDownTimer = new MyCountDownTimer(60000,1000);
                    myCountDownTimer.start();

                    OkGo.<SmsResponse>post(UrlBean.getCode)
                            .params("phoneNum",et_username.getText().toString())
                            .tag(this).execute(new JsonCallback<SmsResponse>(SmsResponse.class) {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<SmsResponse> response) {
                            if (response.body().getCode().equals("200")){
                                Toast.makeText(RegisterActivity.this,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(RegisterActivity.this,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    return;
                }
            }
        });

        btn_Regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_username.getText().toString())){
                    Toast.makeText(RegisterActivity.this,"用户名为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(et_password.getText().toString())){
                    Toast.makeText(RegisterActivity.this,"密码为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(et_userauth_code.getText().toString())){
                    Toast.makeText(RegisterActivity.this,"短信验证码为空",Toast.LENGTH_SHORT).show();
                    return;
                }

                btn_Regist.setEnabled(false);

                OkGo.<UserResponse>post(UrlBean.regist)
                        .params("username",et_username.getText().toString())
                        .params("password",et_password.getText().toString())
                        .params("code",et_userauth_code.getText().toString())
                        .isMultipart(true)
                        .tag(this).execute(new JsonCallback<UserResponse>(UserResponse.class) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<UserResponse> response) {
                        if (response.body().getCode().equals("200")){
                            sharedPreferencesHelper.save("userName",et_username.getText().toString());
                            sharedPreferencesHelper.save("token",response.body().getToken());

                            btn_Regist.setEnabled(true);
                            progress_center.setVisibility(View.GONE);

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(RegisterActivity.this,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    /**
     * 倒计时
     */
    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @SuppressLint("StringFormatMatches")
        @Override
        public void onTick(long millisUntilFinished) {
            CountDownTimerIsStart = true;
            btn_get_auth.setText(String.format(getString(R.string.code_second), millisUntilFinished/1000));
            btn_get_auth.setBackgroundResource(R.drawable.bg_confirm);
            btn_get_auth.setClickable(false);
        }

        @Override
        public void onFinish() {
            CountDownTimerIsStart = false;
            btn_get_auth.setText(getString(R.string.code_second_repeat));
            btn_get_auth.setClickable(true);
            btn_get_auth.setBackgroundResource(R.drawable.btn_bg_selector);
        }
    }
}
