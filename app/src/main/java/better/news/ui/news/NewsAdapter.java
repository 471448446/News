package better.news.ui.news;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import better.news.R;
import better.news.db.Cache;
import better.news.db.table.NewsTable;
import better.news.support.sax.RssItem;
import better.news.support.util.Utils;
import better.news.ui.base.adapter.BaseRecyclerViewAdapter;
import better.news.ui.common.DealActivity;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Better on 2016/3/15.
 */
public class NewsAdapter extends BaseRecyclerViewAdapter<RssItem, NewsAdapter.ViewHolder> {

    public NewsAdapter(Activity context,Fragment fragment, Cache<RssItem> cache, boolean isFromCollect) {
        super(context,fragment, cache, isFromCollect);
    }

    public NewsAdapter(Activity context,Fragment fragment, Cache<RssItem> cache) {
//        super(context, cache);
        super(context,fragment,cache,false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (isScrollToTop) {//根据滑动方向设置动画
            holder.itemView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.item_in_top_anim));
        } else {
            holder.itemView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.item_in_bottom_anim));
        }
        final RssItem bean = mList.get(position);
        holder.tvTitle.setText(bean.getTitle());
        holder.tvDes.setText(bean.getDescription());
        holder.tvData.setText("" + bean.getData());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DealActivity.startFromNews(mFragment, bean);
            }
        });
        if (!isFromCollect) {
            Utils.setGone(holder.checkCollect);
        } else {
            holder.checkCollect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        mCache.addToCollection(bean);
                    } else {
                        mCache.execSQL(NewsTable.deleteCollectionFlag(bean.getTitle()));
                        if (isFromCollect) {
                            mCache.execSQL(NewsTable.updateCollectionFlag(bean.getTitle(), 0));
                            mCache.execSQL(NewsTable.deleteCollectionFlag(bean.getTitle()));
                            mList.remove(position);
                            notifyDataSetChanged();
                        }
                    }
                }
            });
            holder.checkCollect.setChecked(bean.getIs_collected() == 1 ? true : false);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_news_title)
        TextView tvTitle;
        @Bind(R.id.item_news_description)
        TextView tvDes;
        @Bind(R.id.item_news_date)
        TextView tvData;
        @Bind(R.id.item_news_check_collect)
        CheckBox checkCollect;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
