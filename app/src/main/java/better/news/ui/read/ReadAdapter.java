package better.news.ui.read;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import better.news.R;
import better.news.data.read.ReadBean;
import better.news.db.Cache;
import better.news.db.table.ReadingTable;
import better.news.support.util.ImageUtil;
import better.news.support.util.Utils;
import better.news.ui.base.adapter.BaseRecyclerViewAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Better on 2016/3/23.
 */
public class ReadAdapter extends BaseRecyclerViewAdapter<ReadBean.BooksBean, ReadAdapter.ViewHolder> {

    public ReadAdapter(Activity context, Fragment fragment, Cache cache, Boolean isFromCollect) {
        super(context, fragment, cache, isFromCollect);
    }

    public ReadAdapter(Activity context, Fragment fragment, Cache cache) {
        super(context, fragment, cache, false);
    }

    public ReadAdapter(Activity context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_read, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        if (isScrollToTop) {//根据滑动方向设置动画
//            holder.itemView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_in_down_right));
//        } else {
//            holder.itemView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_in_up_right));
//        }
        final ReadBean.BooksBean bean = getItem(position);
        if (TextUtils.isEmpty(bean.getEbook_url())) {
            Utils.setGone(holder.imgEBook);
        } else {
            Utils.setVisible(holder.imgEBook);
        }
        holder.title.setText(bean.getTitle());
        if (null != bean.getAuthor() && !bean.getAuthor().isEmpty()) {
            holder.auth.setText(mContext.getString(R.string.str_auth, bean.getAuthor().get(0)));
        } else {
            Utils.setGone(holder.auth);
        }
        holder.pages.setText(mContext.getString(R.string.str_page, bean.getPages()));
        holder.publisher.setText(mContext.getString(R.string.str_publish, bean.getPublisher()));
//        Glide.with(mContext).load(bean.getImage()).into(holder.imgBook);
        ImageUtil.load(mContext, bean.getImage(), holder.imgBook);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mFragment)
                    BookDetailActivity.start(mFragment, bean);
                else
                    BookDetailActivity.start(mContext, bean);
            }
        });
        holder.checkBox.setChecked(bean.getIs_collected() == 1 ? true : false);
//        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    mCache.addToCollection(bean);
//                } else {
//                    mCache.execSQL(ReadingTable.deleteCollectionFlag(bean.getTitle()));
//                    if (isFromCollect){
//                        mCache.execSQL(ReadingTable.updateCollectionFlag(bean.getTitle(), 0));
//                        mCache.execSQL(ReadingTable.deleteCollectionFlag(bean.getTitle()));
//                        mList.remove(position);
//                        notifyDataSetChanged();
//                    }
//                }
//            }
//        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bean.getIs_collected() == 1) {
                    mCache.execSQL(ReadingTable.deleteCollectionFlag(bean.getTitle()));
                    if (isFromCollect) {
                        mCache.execSQL(ReadingTable.updateCollectionFlag(bean.getTitle(), 0));
                        mCache.execSQL(ReadingTable.deleteCollectionFlag(bean.getTitle()));
                        mList.remove(position);
                        notifyDataSetChanged();
                    }
                    holder.checkBox.setChecked(false);
                    bean.setIs_collected(0);
                } else {
                    mCache.addToCollection(bean);
                    holder.checkBox.setChecked(true);
                    bean.setIs_collected(1);
                }
            }
        });
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        @Bind(R.id.item_read_title)
        TextView title;
        @Bind(R.id.item_read_auth)
        TextView auth;
        @Bind(R.id.item_read_pages)
        TextView pages;
        @Bind(R.id.item_read_publisher)
        TextView publisher;
        @Bind(R.id.item_read_image)
        ImageView imgBook;
        @Bind(R.id.item_read_ebook)
        ImageView imgEBook;
        @Bind(R.id.item_read_collect)
        CheckBox checkBox;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            this.itemView = v;
        }
    }
}
