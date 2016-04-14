package better.news.support.util;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import better.news.R;
import better.news.support.Setting;
import com.bumptech.glide.Glide;

/**
 * Created by Better on 2016/4/3.
 */
public class ImageUtil {
    public static void load( final Context context, final String url, final ImageView imageView){
        if(Setting.getBoolean(Setting.KEY_IMAGE_LOAD,context)){
            imageView.setImageResource(R.drawable.img_click_load);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Glide.with(context).load(url).into(imageView);
                }
            });
        }else{
            Glide.with(context).load(url).into(imageView);
        }
    }
}
