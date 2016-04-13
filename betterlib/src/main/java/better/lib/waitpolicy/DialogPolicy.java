package better.lib.waitpolicy;

import android.content.Context;

import better.lib.utils.BaseUtils;
import better.lib.waitpolicy.dialog.WaitDialog;

/**
 * Created by Better on 2016/3/14.
 */
public class DialogPolicy implements WaitPolicy {
//    private WaitDialog mWaitDialog;
    private Context context;
    public DialogPolicy(Context context){
        this.context=context;
    }
    @Override
    public void displayLoading(String message) {
        WaitDialog.getInstance(context).showWaitDialog(message);
    }

    @Override
    public void displayLoading() {
        WaitDialog.getInstance(context).showWaitDialog();
    }

    @Override
    public void displayRetry(String message) {
        BaseUtils.toastShort(context,message);
        disappear();
    }

    @Override
    public void displayRetry() {

    }

    @Override
    public void disappear() {
        WaitDialog.getInstance(context).dismissWaitDialog();
    }
}
