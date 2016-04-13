package com.better.news.ui.science;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.better.news.R;
import com.better.news.data.sicence.ScienceOutBean;
import com.better.news.support.util.ImageUtil;
import com.better.news.ui.base.adapter.BaseRecyclerViewAdapter;
import com.better.news.ui.common.DealActivity;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Better on 2016/3/20.
 */
public class ScienceAdapter extends BaseRecyclerViewAdapter<ScienceOutBean.ScienceBean, ScienceAdapter.ViewHolder> {

    public ScienceAdapter(Activity context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sicence, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ScienceOutBean.ScienceBean bean = getItem(position);
//        Glide.with(mContent).load(bean.getSmall_image()).into(holder.imageView);
        ImageUtil.load(mContent,bean.getSmall_image(),holder.imageView);
        holder.titel.setText(bean.getTitle());
        holder.time.setText(bean.getDate_created());
        holder.content.setText(bean.getSummary());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ScienceDetailsActivity.start(mContent,bean.getResource_url());
//                DealActivity.startDealActivity(mContent,bean.getUrl());
                DealActivity.startFromScience(mContent,bean);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        @Bind(R.id.item_science_tv_title)
        TextView titel;
        @Bind(R.id.item_science_tv_time)
        TextView time;
        @Bind(R.id.item_science_img)
        ImageView imageView;
        @Bind(R.id.item_science_tv_content)
        TextView content;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            this.itemView = v;
        }
    }
}
