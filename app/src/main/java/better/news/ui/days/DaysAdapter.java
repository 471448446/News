package better.news.ui.days;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import better.news.R;
import better.news.data.days.DaysBean;
import better.news.support.util.ImageUtil;
import better.news.ui.base.adapter.BaseRecyclerViewAdapter;

/**
 * Created by Better on 2016/3/18.
 */
public class DaysAdapter extends BaseRecyclerViewAdapter<DaysBean.StoriesBean,DaysAdapter.ViewHolder> {
    public DaysAdapter(Activity context,Fragment fragment) {
        super(context,fragment);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_days,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DaysBean.StoriesBean bean=mList.get(position);
        holder.tvTitle.setText(bean.getTitle());
        if(null!=bean.getImages()&&!TextUtils.isEmpty(bean.getImages().get(0))){
//            Glide.with(mContent).load(bean.getImages().get(0)).into(holder.img);
            ImageUtil.load(mContent,bean.getImages().get(0),holder.img);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DaysDetailActivity.start(mFragment,bean);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        /*@Bind(R.id.item_days_img)*/ImageView img;
        /*@Bind(R.id.item_news_title)*/TextView tvTitle;
        View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this,itemView);
            this.itemView=itemView;
            this.img=(ImageView)itemView.findViewById(R.id.item_days_img);
            this.tvTitle=(TextView)itemView.findViewById(R.id.item_days_title);
        }
    }
}
