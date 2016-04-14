package better.news.ui.setting;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import better.news.R;
import better.news.ui.base.BaseActivity;

import butterknife.Bind;

public class SettingActivity extends BaseActivity {

    @Bind(R.id.toolBar)Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(R.style.style_setting);
//        changeLanguage();
        setContentView(R.layout.activity_setting);

        setBackToolBar(toolbar).setTitle(getString(R.string.str_setting));
        getFragmentManager().beginTransaction().replace(R.id.setting_content,new SettingFragment()).commit();
    }
}
