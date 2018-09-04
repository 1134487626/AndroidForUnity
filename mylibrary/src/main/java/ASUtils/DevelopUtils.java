package ASUtils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.location.LocationManager;

import com.unity3d.player.UnityPlayer;

import UnityBasic.Extends;

/**
 * Created by Administrator on 2018/8/10.
 * 通用开发工具类
 */

public final class DevelopUtils {

    private static LocationManager location;
    private static ClipboardManager clipboard;
    private static MyAlertDialog dialog;

    public static MyAlertDialog getDialog() {
        if (dialog == null) dialog = new MyAlertDialog();
        return dialog;
    }

    public static ClipboardManager getClipboard() {
        if (clipboard == null) {
            clipboard = (ClipboardManager) UnityPlayer.currentActivity.getSystemService(Activity.CLIPBOARD_SERVICE);
        }
        return clipboard;
    }

    public static LocationManager getLocation() {
        if (location == null) {
            location = (LocationManager) UnityPlayer.currentActivity.getSystemService(Context.LOCATION_SERVICE);
        }
        return location;
    }

    /**
     * 是否打开定位权限
     */
    public static boolean isOpenGPS() {
        return getLocation().isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }

    /**
     * 拷贝需要的文本
     */
    public static void setCopyText(String str) {

        if (getClipboard() != null) {
            ClipData data = ClipData.newPlainText("Label", str);
            getClipboard().setPrimaryClip(data);
        }
    }

    /**
     * 返回要粘贴的文本
     */
    public static String getCopyText() {

        if (getClipboard() != null && getClipboard().hasPrimaryClip()) {
            if (getClipboard().getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                ClipData data = getClipboard().getPrimaryClip();
                ClipData.Item item = data.getItemAt(0);
                return item.getText().toString();
            }
        }
        return null;
    }

    /**
     * 显示原生对话框,并处理某些事件
     */
    public static void showNativeAD(String json) {

        if (!Extends.isNullOrEmpty(json))
            getDialog().showAlertDialog(json);
    }
}
