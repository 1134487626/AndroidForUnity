package TencentPlayer;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.unity3d.player.UnityPlayer;

import org.json.JSONException;
import org.json.JSONObject;

import UnityBasic.Debug;
import UnityBasic.Delegate;
import UnityBasic.Extends;

/**
 * Created by Administrator on 2018/7/28.
 */

public final class WeChat {

    public static IWXAPI iwxapi;
    public static String AppID, AppSecret;
    public static Delegate loginFun, payFun;

    public static Boolean isActiveWX() {
        if (iwxapi == null) {
            Debug.dialog("未成功注册到微信！");
            return false;
        }
        if (!iwxapi.isWXAppInstalled()) {
            Debug.dialog("未安装到微信App！");
            return false;
        }
        if (!iwxapi.isWXAppSupportAPI()) {
            Debug.dialog("不支持该功能！");
            return false;
        }
        return true;
    }

    /**
     * 注册微信
     *
     * @param appID     设置微信 AppID
     * @param appSecret 设置微信 AppSecret
     */
    public static boolean setIWXAPI(String appID, String appSecret) {

        if (!Extends.isNullOrEmpty(appID, appSecret)) {
            AppID = appID;
            AppSecret = appSecret;
            iwxapi = WXAPIFactory.createWXAPI(UnityPlayer.currentActivity, AppID);
            iwxapi.registerApp(AppID);
            return true;
        }
        return false;
    }

    public static SendMessageToWX.Req getShare(String json) {

        try {
            JSONObject share = new JSONObject(json);
            WXMediaMessage message = new WXMediaMessage();
            message.title = share.getString("title");
            message.description = share.getString("description");
            WXShareType shareType = WXShareType.values()[share.getInt("type")];

            switch (shareType) {
                case text:
                    WXTextObject textObj = new WXTextObject();
                    textObj.text = share.getString("description");
                    message.mediaObject = textObj;
                    break;
                case img:
                    WXImageObject imageObj = new WXImageObject();
                    message.mediaObject = imageObj;
                    //设置缩略图
                    break;
                case webpage:
                    WXWebpageObject webpageObj = new WXWebpageObject();
                    webpageObj.webpageUrl = share.getString("webpageUrl");
                    //设置网页缩略图
                    break;
                case music:
                    WXMusicObject musicObj = new WXMusicObject();
                    musicObj.musicUrl = "音乐网址";
                    message.mediaObject = musicObj;
                    //设置音乐缩略图
                    break;
                case video:
                    WXVideoObject videoObj = new WXVideoObject();
                    videoObj.videoUrl = "视屏网址";
                    message.mediaObject = videoObj;
                    //设置视屏缩略图
                    break;
            }
            return getReq(shareType.toString(), message, share.getInt("scene"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static SendMessageToWX.Req getReq(String shareType, WXMediaMessage message, int scene) {

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = message;
        req.transaction = buildTransaction(shareType);
        req.scene = scene;
        return req;
    }

    private static String buildTransaction(String type) {
        return type == null ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
