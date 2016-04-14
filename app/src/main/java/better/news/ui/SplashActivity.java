package better.news.ui;

import android.os.Bundle;
import android.os.Handler;

import better.news.R;
import better.news.ui.base.BaseActivity;
/**
 * Des 欢迎页面 <br>
 * Created by Better on 2016/4/14 14:09.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                forward(MainActivity.class);
                finish();
            }
        },1500);
    }
}
