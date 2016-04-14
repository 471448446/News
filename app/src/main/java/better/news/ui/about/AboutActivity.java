package better.news.ui.about;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import better.news.R;
import better.news.ui.base.BaseActivity;
import butterknife.Bind;

public class AboutActivity extends BaseActivity {

    @Bind(R.id.toolBar)Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setBackToolBar(toolbar).setTitle(getString(R.string.str_about));
        getFragmentManager().beginTransaction().replace(R.id.about_rl,new AboutFragment()).commit();
    }
    public static class AboutFragment extends PreferenceFragment{
        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            view.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPreferenceFragmentBg));
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.about_preference);
        }
    }
}
