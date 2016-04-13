package com.better.news.ui.read;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.better.news.R;
import com.better.news.support.C;
import com.better.news.ui.base.BaseFragment;

import butterknife.Bind;

/**
 * Created by Better on 2016/3/24.
 */
public class BookDetailFragment extends BaseFragment {
    private String mMsg;
    @Bind(R.id.read_detail_tv_msg)
    TextView tvMsg;
    public static Fragment getInstance(String msg){
        BookDetailFragment fragmnet=new BookDetailFragment();
        Bundle bund=new Bundle();
        bund.putString(C.EXTRA_KEY, msg);
        fragmnet.setArguments(bund);
        return fragmnet;
    }
    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmnet_readdetail,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(TextUtils.isEmpty(mMsg)){
            tvMsg.setText(R.string.str_none_information);
        }else{
            tvMsg.setText(mMsg);
        }
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        mMsg=getArguments().getString(C.EXTRA_KEY);
    }
}
