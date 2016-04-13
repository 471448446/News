package com.better.news.ui.read;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.better.news.R;
import com.better.news.data.read.ReadBean;
import com.better.news.db.Cache;
import com.better.news.db.table.ReadingTable;
import com.better.news.support.util.ImageUtil;
import com.better.news.support.util.Utils;
import com.better.news.ui.base.adapter.BaseRecyclerViewAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Better on 2016/3/23.
 */
public class ReadAdapter extends BaseRecyclerViewAdapter<ReadBean.BooksBean,ReadAdapter.ViewHolder> {

    public ReadAdapter(Activity context,Cache cache,Boolean isFromCollect) {
        super(context,cache,isFromCollect);
    }
    public ReadAdapter(Activity context,Cache cache) {
        super(context,cache);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_read,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ReadBean.BooksBean bean=getItem(position);
        if(TextUtils.isEmpty(bean.getEbook_url())){
            Utils.setGone(holder.imgEBook);
        }else{
            Utils.setVisible(holder.imgEBook);
        }
        holder.title.setText(bean.getTitle());
        if(null!=bean.getAuthor()&&!bean.getAuthor().isEmpty()){
            holder.auth.setText(mContent.getString(R.string.str_auth, bean.getAuthor().get(0)));
        }else{
            Utils.setGone(holder.auth);
        }
        holder.pages.setText(mContent.getString(R.string.str_page,bean.getPages()));
        holder.publisher.setText(mContent.getString(R.string.str_publish, bean.getPublisher()));
//        Glide.with(mContent).load(bean.getImage()).into(holder.imgBook);
        ImageUtil.load(mContent,bean.getImage(),holder.imgBook);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookDetailActivity.start(mContent, bean);
            }
        });
        holder.checkBox.setChecked(bean.getIs_collected() == 1 ? true : false);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bean.setIs_collected(b?1:0);
                if (b) {
                    mCache.addToCollection(bean);
                } else {
                    mCache.execSQL(ReadingTable.deleteCollectionFlag(bean.getTitle()));
                    if (isFromCollect){
                        mCache.execSQL(ReadingTable.updateCollectionFlag(bean.getTitle(), 0));
                        mCache.execSQL(ReadingTable.deleteCollectionFlag(bean.getTitle()));
                        mList.remove(position);
                        notifyDataSetChanged();
                    }
                }
            }
        });
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        @Bind(R.id.item_read_title)TextView title;
        @Bind(R.id.item_read_auth)TextView auth;
        @Bind(R.id.item_read_pages)TextView pages;
        @Bind(R.id.item_read_publisher)TextView publisher;
        @Bind(R.id.item_read_image)ImageView imgBook;
        @Bind(R.id.item_read_ebook)ImageView imgEBook;
        @Bind(R.id.item_read_collect)CheckBox checkBox;
        public ViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
            this.itemView=v;
        }
    }
}
