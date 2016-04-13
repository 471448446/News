package better.lib.waitpolicy.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import better.lib.R;

public class WaitDialog extends Dialog {
	
	private WaitDialog(Context context, int theme) {
		super(context, theme);
	}

	private static WaitDialog waitDialog = null;
	private static Context mContext;

	public static WaitDialog getInstance(Context context) {
		//		Utils.logw("WaitDialog", (null == waitDialog ? "null " : "") + (context == mContext)
		//				+ " new " + context + " old " + mContext);
//		if(waitDialog == null || context != mContext) {
//			mContext = context;
//			View view = View.inflate(mContext, R.layout.loading, null);
//			waitDialog = (WaitDialog) new WaitDialog(mContext, R.style.alert_dialog_style);
//			waitDialog.setContentView(view);
//			waitDialog.setCancelable(false);
//		}
//		return waitDialog;
		return getInstance(context, false);
	}
	/**
	 * 因需要可以取消的waitdialog 所以改范围private为public  151215
	 */
	public static WaitDialog getInstance(Context context,boolean isCancelable) {
		//		Utils.logw("WaitDialog", (null == waitDialog ? "null " : "") + (context == mContext)
		//				+ " new " + context + " old " + mContext);
		if(waitDialog == null || context != mContext) {
			mContext = context;
			View view = View.inflate(mContext, R.layout.loading, null);
			waitDialog = (WaitDialog) new WaitDialog(mContext, R.style.alert_dialog_style);
			waitDialog.setContentView(view);
			waitDialog.setCancelable(isCancelable);
		}
		return waitDialog;
	}
	public void showWaitDialog(int msgId) {
		showWaitDialog(mContext.getString(msgId));
	}

	public void showWaitDialog() {
		showWaitDialog(mContext.getString(R.string.str_loading_wait));
	}

	public void showWaitDialog(String msg) {
		TextView msgTv = (TextView) waitDialog.findViewById(R.id.loading_msg);
		msgTv.setText(msg);
		if(!waitDialog.isShowing()) {
			waitDialog.show();
		}
	}

	public void dismissWaitDialog() {
		if(null != waitDialog && waitDialog.isShowing()) {
			waitDialog.dismiss();
			//			waitDialog = null;
		}
	}
}
