package better.news.support.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;

import better.news.R;

public class ExitAppHelper {
	private Activity  context;
	private View view;
	//--Handler消息字段
	private static final int MSG_EXIT 					= 1; // 双击返回退出
	private static final int MSG_EXIT_WAIT 				= MSG_EXIT + 1; // 双击返回退出延时
	/**
	 * 消息处理
	 */
	private Handler mHandle = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch(msg.what) {
			case MSG_EXIT:
				if(mHandle.hasMessages(MSG_EXIT_WAIT)) {
					context.moveTaskToBack(true);
					context.finish();
				} else {
					if (null==view){
						Utils.toastShort(context, R.string.str_exit_msg);
					}else{
						Snackbar.make(view,R.string.str_exit_msg,Snackbar.LENGTH_SHORT).show();
					}
					mHandle.sendEmptyMessageDelayed(MSG_EXIT_WAIT, 2000);
				}
				break;
			case MSG_EXIT_WAIT:
				break;
			default:
				break;
			}
			return false;
		}
	});
	public ExitAppHelper(Activity activity){
		this.context=activity;
	}
	public ExitAppHelper(Activity activity,View parentView){
		this.context=activity;
		this.view=parentView;
	}
	public boolean onClickBack(int keyCode, KeyEvent event){
		if(KeyEvent.KEYCODE_BACK==keyCode){
			mHandle.sendEmptyMessage(MSG_EXIT);
			return true;
		}else{
			return false;
		}
	}
	public void exitDreictly(){
		new Handler().postAtTime(new Runnable() {
			@Override
			public void run() {
				context.finish();
			}
		},1500);
	}

}
