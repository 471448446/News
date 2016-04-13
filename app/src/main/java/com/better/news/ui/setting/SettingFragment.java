package com.better.news.ui.setting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.better.news.R;
import com.better.news.support.DialogHelper;
import com.better.news.support.Setting;
import com.better.news.support.util.Utils;

/**
 * Created by Better on 2016/3/25.
 * thk http://stackoverflow.com/questions/14641190/android-preference-fragment-text-color
 * http://stackoverflow.com/questions/16970209/preferencefragment-background-color
 * 默认字体颜色 是根据style来确定的。
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
    private Preference mLanguage;
    private CheckBoxPreference mImageLoadMode,mExitConfirm;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        view.setBackgroundColor(getResources().getColor(R.color.colorPreferenceFragmentBg));
        view.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPreferenceFragmentBg));
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_preference);

        mLanguage=findPreference(Setting.KEY_LANGUAGE);
        mImageLoadMode = (CheckBoxPreference) findPreference(Setting.KEY_IMAGE_LOAD);
        mExitConfirm= (CheckBoxPreference) findPreference(Setting.KEY_EXIT_CONFIRM);

        mLanguage.setOnPreferenceClickListener(this);

        mImageLoadMode.setOnPreferenceChangeListener(this);
        mExitConfirm.setOnPreferenceChangeListener(this);
        //初始化
        mExitConfirm.setChecked(Setting.isExitConfirm);

    }
    //点击事件
    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        if(preference==mExitConfirm){
            Setting.isExitConfirm = Boolean.valueOf(o.toString());
            Setting.setBoolean(Setting.KEY_EXIT_CONFIRM, Boolean.valueOf(o.toString()),getActivity());
            return true;
        }else if(preference== mImageLoadMode){
            Setting.isNightMode = Boolean.valueOf(o.toString());
            Setting.setBoolean(Setting.KEY_IMAGE_LOAD,Boolean.valueOf(o.toString()),getActivity());
            return true;
        }
        return false;
    }
    //点击事件
    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(mLanguage==preference){
            DialogHelper.getInstance().getSingleChooseDialog(getActivity(), getString(R.string.str_language), getResources().getStringArray(R.array.str_array_language), Utils.getCurrentLanguage(getActivity()),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (Setting.getInt(Setting.KEY_LANGUAGE,getActivity()) != i) {
                                Setting.setInt(Setting.KEY_LANGUAGE, i, getActivity());
                                Setting.needRecreate = true;
                                dialogInterface.dismiss();
                            }
                            if (Setting.needRecreate) {
                                if (null != getActivity()) getActivity().recreate();
                            }
                        }
                    }).show();
            return true;
        }
        return false;
    }
}
