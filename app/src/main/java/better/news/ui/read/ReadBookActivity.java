package better.news.ui.read;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import better.news.R;
import better.news.support.util.Utils;
import better.news.ui.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by Better on 2016/3/27.
 */
public class ReadBookActivity extends BaseActivity {
    public static void start(Activity activity,String url){
        Intent intent=new Intent(activity,ReadBookActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, url);
        activity.startActivity(intent);
        Utils.v("ReadBookActivity","传递 ="+url);
    }
    @Bind(R.id.read_web_view)WebView webView;
    @Bind(R.id.read_progress_bar)ProgressBar mProgressBar;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book);

        log("onCreate()" + url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setSupportMultipleWindows(false);
           /*
         cache web page
         */
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                //				view.loadUrl("file:///android_asset/error.html");
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
//				tvCenter.setText(title);
                super.onReceivedTitle(view, title);
            }
        });
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(url);
            }
        });
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        url=getIntent().getStringExtra(Intent.EXTRA_TEXT);
    }
}
