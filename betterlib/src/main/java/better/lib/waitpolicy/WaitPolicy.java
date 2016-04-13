package better.lib.waitpolicy;

/**
 * Created by Better on 2016/3/14.
 */
public interface WaitPolicy {
    /**
     * 显示请求等待。
     * @param message 等待提示消息。
     */
    public void displayLoading(String message);
    public void displayLoading();

    /**
     * 显示请求重试。
     * @param message 重试提示消息：失败、无数据。
     */
    public void displayRetry(String message);
    public void displayRetry();

    /**
     * 请求页面取消显示
     */
    public void disappear();
}
