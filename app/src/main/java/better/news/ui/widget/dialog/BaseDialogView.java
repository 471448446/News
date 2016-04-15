package better.news.ui.widget.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import better.news.R;
import better.news.support.util.ScreenUtils;

/**
 * 方便统一对对话框样式
 *
 */
public abstract class BaseDialogView extends BaseDialogFragmentView {
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_base_layout, null);
		if(null!=getContentView())((FrameLayout)view.findViewById(R.id.dialog_base_frame_content)).addView(getContentView());
		return view;
	}
	/**
	 * 获取ContentView
	 */
	protected abstract View getContentView();
	@Override
	protected int[] getWH() {
		int[] wh={(int) (ScreenUtils.getInstance().getScreenWidth(getActivity())*0.8),getDialog().getWindow().getAttributes().height};
		return wh;
	}
	@Override
	protected boolean isNoTitle() {
		return true;
	}
}
