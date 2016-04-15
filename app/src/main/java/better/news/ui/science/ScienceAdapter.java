package better.news.ui.science;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import better.news.R;
import better.news.data.sicence.ScienceOutBean;
import better.news.support.util.ImageUtil;
import better.news.ui.base.adapter.BaseRecyclerViewAdapter;
import better.news.ui.common.DealActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Better on 2016/3/20.
 */
public class ScienceAdapter extends BaseRecyclerViewAdapter<ScienceOutBean.ScienceBean, ScienceAdapter.ViewHolder> {

    public ScienceAdapter(Activity context,Fragment fragment) {
        super(context,fragment);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sicence, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isScrollToTop) {//根据滑动方向设置动画
            holder.itemView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.item_in_top_anim));
        } else {
            holder.itemView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.item_in_bottom_anim));
        }
        final ScienceOutBean.ScienceBean bean = getItem(position);
//        Glide.with(mContext).load(bean.getSmall_image()).into(holder.imageView);
        ImageUtil.load(mContext,bean.getSmall_image(),holder.imageView);
        holder.titel.setText(bean.getTitle());
        holder.time.setText(bean.getDate_created());
        holder.content.setText(bean.getSummary());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ScienceDetailsActivity.start(mContext,bean.getResource_url());
//                DealActivity.startDealActivity(mContext,bean.getUrl());
                DealActivity.startFromScience(mFragment,bean);
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
