package better.lib.waitpolicy;

import better.lib.utils.BaseUtils;
import better.lib.waitpolicy.emptyproxy.EmptyViewProxy;

/**
 * Created by Better on 2016/3/14.
 */
public class EmptyViewWaitPolicy implements WaitPolicy {
    private EmptyViewProxy emptyViewProxy;

    public EmptyViewWaitPolicy(EmptyViewProxy emptyViewProxy) {
        this.emptyViewProxy = emptyViewProxy;
    }
    @Override
    public void displayLoading(String message) {
        emptyViewProxy.displayLoading(message);
    }

    @Override
    public void displayLoading() {
        emptyViewProxy.displayLoading();
    }

    @Override
    public void displayRetry(String message) {
        emptyViewProxy.displayRetry();
    }

    @Override
    public void displayRetry() {
        emptyViewProxy.displayRetry();
    }

    @Override
    public void disappear() {
        BaseUtils.setGone(emptyViewProxy.getProxyView());
    }
}
