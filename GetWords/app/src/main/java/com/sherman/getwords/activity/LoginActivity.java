package com.sherman.getwords.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.sherman.getwords.R;
import com.sherman.getwords.bean.JsonCallback;
import com.sherman.getwords.bean.UrlBean;
import com.sherman.getwords.bean.UserResponse;
import com.sherman.getwords.utils.SharedPreferencesHelper;

/**
 * author: 李梦(<a href="mailto:limeng@danlu.com">limeng@danlu.com</a>)<br/>
 * version: $VERSION<br/>
 * since: 2018-05-21 9:58<br/>
 *
 * <p>
 * $DESCRIPTION
 * </p>
 */

public class LoginActivity extends AppCompatActivity {

    private SharedPreferencesHelper sharedPreferencesHelper;

    private EditText et_username;
    private EditText et_password;
    private TextView tv_regist;

    private Button btn_login;

    private ProgressBar progress_center;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        sharedPreferencesHelper = new SharedPreferencesHelper(getApplication());
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        tv_regist = findViewById(R.id.tv_regist);

        progress_center = findViewById(R.id.progress_center);

        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userName = et_username.getText().toString();
                String userPwd = et_password.getText().toString();

                if (TextUtils.isEmpty(userName)){
                    Toast.makeText(LoginActivity.this,"用户名为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(userPwd)){
                    Toast.makeText(LoginActivity.this,"密码为空",Toast.LENGTH_SHORT).show();
                    return;
                }

                btn_login.setEnabled(false);
                progress_center.setVisibility(View.VISIBLE);

                //登录
                OkGo.<UserResponse>post(UrlBean.login)
                        .params("username",userName)
                        .params("password",userPwd)
                        .isMultipart(true)
                        .tag(this).execute(new JsonCallback<UserResponse>(UserResponse.class) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<UserResponse> response) {
                        if (response.body().getCode().equals("200")){
                            btn_login.setEnabled(true);
                            progress_center.setVisibility(View.GONE);

                            sharedPreferencesHelper.save("userName",userName);
                            sharedPreferencesHelper.save("token",response.body().getToken());

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            btn_login.setEnabled(true);
                            progress_center.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        tv_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
