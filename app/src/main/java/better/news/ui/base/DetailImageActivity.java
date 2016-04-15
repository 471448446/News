package better.news.ui.base;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import better.news.R;

import better.lib.waitpolicy.EmptyViewWaitPolicy;
import better.lib.waitpolicy.emptyproxy.DefaultEmptyView;
import better.lib.waitpolicy.emptyproxy.EmptyViewProxy;
import butterknife.Bind;

/**
 * image+webView++toolBar
 */
public abstract class DetailImageActivity extends BaseDetailActivity {

    @Bind(R.id.days_detail_web)public WebView webView;
    @Bind(R.id.days_detail_img)public ImageView image;
    @Bind(R.id.days_detail_continer)CoordinatorLayout layout;
    @Bind(R.id.days_detail_toolbar)Toolbar toolbar;
    protected String url;
    private DefaultEmptyView emptyView;
    protected EmptyViewWaitPolicy waitPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_details_image);
        mContainer=findViewById(R.id.days_detail_continer);
    }

    @Override
    protected void initData() {
        super.initData();

        setBackToolBar(toolbar);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
          /*
         cache web page
         */

        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);

        emptyView=new DefaultEmptyView(this);
        layout.addView(emptyView.getProxyView(),layout.getChildCount(),
                new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT,CoordinatorLayout.LayoutParams.MATCH_PARENT));
        emptyView.setOnRetryClickListener(new EmptyViewProxy.onLrRetryClickListener() {
            @Override
            public void onRetryClick() {
                asyncDetail();
            }
        });
        waitPolicy=new EmptyViewWaitPolicy(emptyView);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=webView){
            if(null!=webView.getParent()){
                ((ViewGroup)webView.getParent()).removeView(webView);
            }
            webView.stopLoading();
            webView.destroy();
        }
    }
    protected abstract void asyncDetail() ;

}
