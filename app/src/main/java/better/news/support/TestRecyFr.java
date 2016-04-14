package better.news.support;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import better.news.R;
import better.news.http.callback.StringCallBack;
import better.news.http.CustomOkHttpClient;
;

import java.util.ArrayList;
import java.util.List;

import better.lib.utils.BaseUtils;
import better.lib.waitpolicy.emptyproxy.DefaultEmptyView;
import better.lib.waitpolicy.emptyproxy.FooterEmptyView;
import better.lib.recyclerview.BRecyclerOnScrollListener;
import better.lib.recyclerview.BRecyclerView;
import better.lib.recyclerview.HeaderViewProxyRecyclerAdapter;
import better.lib.recyclerview.HeaderViewRecyclerAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;


/**
 * Created by Better on 2016/3/10.
 */
public class TestRecyFr extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View view;
    @Bind(R.id.recy)
    BRecyclerView recyclerView;
    @Bind(R.id.swipe)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.test_ll)
    LinearLayout linearLayout;
    private List<String> mList;
    DefaultEmptyView emptyViewProxy;

    RefreshAdapter refreshAdapter;
    HeaderViewRecyclerAdapter headerAdapter;
    HeaderViewProxyRecyclerAdapter headerViewProxyRecyclerAdapter;

    String url = "http://www.xinhuanet.com/politics/news_politics.xml";
    private Handler mHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.test_fragmnet, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        emptyViewProxy=new DefaultEmptyView(getActivity());
//        linearLayout.addView(emptyViewProxy.getProxyView(),0,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));

        final TextView text = new TextView(getActivity());
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        refreshAdapter = new RefreshAdapter(mList, getActivity());
        headerViewProxyRecyclerAdapter = new HeaderViewProxyRecyclerAdapter(refreshAdapter);
        createLoadingMoreLay();

        recyclerView.setAdapter(headerViewProxyRecyclerAdapter);

        recyclerView.getEmptyViewProxy().setOnRetryClickListener(new DefaultEmptyView.onLrRetryClickListener() {
            @Override
            public void onRetryClick() {
                Log.v("MainActivity", "onRetryClick()重试()");
                recyclerView.getEmptyViewProxy().displayLoading();
                addNewList();
            }
        });
        //
        recyclerView.getEmptyViewProxy().displayLoading();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.v("MainActivity", "请求失败 ========");
                recyclerView.getEmptyViewProxy().displayRetry();
            }
        }, 1000);

//        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int currentPage) {
//                Log.v("MainActivity", "滑动到地步 加载更多");
//                addPullList();
//            }
//        });
        recyclerView.addOnScrollListener(new BRecyclerOnScrollListener() {
            @Override
            public void onBottom() {
                Log.v("MainActivity", "滑动到地步 加载更多");
                addPullList();
            }
        });
        Request request = new Request.Builder().url(url).tag(getActivity()).build();
        CustomOkHttpClient.getInstance().sendRequest(request, new StringCallBack() {

            @Override
            public void onError(Call call, Exception e) {
                BaseUtils.toastShort(getActivity(), "失败=" + e.getMessage());
            }

            @Override
            public void onResponse(String json) {
                BaseUtils.toastShort(getActivity(), "成功=" + json);
            }

        }
                /*new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                BaseUtils.toastShort(getActivity(), "失败"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                text.setText(response.body().string());
                BaseUtils.toastShort(getActivity(),"成g="+response.body().string());
            }
        }*/, null);

    }


    private void createLoadingMoreLay() {
//        View loading = View.inflate(getActivity(), R.layout.view_load_more, null);
//        View loading=LayoutInflater.from(getActivity()).inflate(R.layout.view_load_more,mRecyclerView,false);
//        headerAdapter.addFooterView(loading);
//        headerAdapter.addFooterView(viewProxy.getProxyView());
        FooterEmptyView footerEmptyView = new FooterEmptyView(getActivity());
        headerViewProxyRecyclerAdapter.addFooterViewProxy(footerEmptyView);
    }

    private void addNewList() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    list.add(new String("最新数据" + i));
                }
                mList.addAll(0, list);
                refreshAdapter.notifyDataSetChanged();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        headerViewProxyRecyclerAdapter.closeFooterView();
//                    }
//                }, 2000);
            }
        }, 1000);
    }

    private void addDownList() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(new String("最新数据" + i));
        }
        mList.addAll(0, list);
        refreshAdapter.notifyDataSetChanged();
    }

    private void addPullList() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add(new String("上拉" + i));
                }
                mList.addAll(list);
                refreshAdapter.notifyDataSetChanged();
//                headerViewProxyRecyclerAdapter.getFooterViewProxy().displayRetry();
            }
        }, 3000);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addDownList();
                refreshLayout.setRefreshing(false);
            }
        }, 3000);
    }
}
