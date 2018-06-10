package com.sherman.getwords.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sherman.getwords.R;
import com.sherman.getwords.utils.Constants;
import com.sherman.getwords.utils.SharedPreferencesHelper;


public class HomePersonFragment extends Fragment{

    private SharedPreferencesHelper sharedPreferencesHelper;
    private RelativeLayout rl_exit;
    private TextView tv_name;

    public static HomePersonFragment newInstance(String s){
        HomePersonFragment homeFragment = new HomePersonFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARGS,s);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me_content, container, false);

        sharedPreferencesHelper = new SharedPreferencesHelper(getActivity().getApplication());
        String username = sharedPreferencesHelper.getString("userName","");

        rl_exit = view.findViewById(R.id.rl_exit);
        tv_name = view.findViewById(R.id.tv_name);
        rl_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(Settings.ACTION_SETTINGS));
                getActivity().finish();
            }
        });
        tv_name.setText(username);
        return view;
    }
}
