package better.news.support;

import better.lib.utils.BaseSharedPref;

/**
 * Created by Better on 2016/3/25.
 */
public class Setting extends BaseSharedPref {

    public static  boolean needRecreate = false;
    public static final String NAME="NEWS_SETTING_NAME";
    public static boolean isNightMode = false;
    public static boolean isExitConfirm = true;
    //这里的key值 setting xml特在用
    public static final String KEY_LANGUAGE="key_language";
    public static final String KEY_EXIT_CONFIRM ="key_exit_confirm";
    public static final String KEY_NIGHT_MODE="key_night_mode";
    public static final String KEY_IMAGE_LOAD="key_image_load";

    public Setting(String name) {
        super(name);
    }
}
