package UnityBasic;

import com.unity3d.player.UnityPlayer;

public final class Debug {

    private static String gameObject, logMethod, popMethod;

    /**
     * @param varStr 返回输出Log
     */
    public static void log(String varStr) {

        if (!Extends.isNullOrEmpty(gameObject, gameObject))
            UnityPlayer.UnitySendMessage(gameObject, logMethod, varStr);
    }

    /**
     * @param varStr 返回提示字符串弹窗提示玩家
     */
    public static void dialog(String varStr) {

        if (!Extends.isNullOrEmpty(gameObject, popMethod))
            UnityPlayer.UnitySendMessage(gameObject, popMethod, varStr);
    }

    /**
     * @param object 调用Unity的对象名
     * @param log    调用Unity的log方法名
     * @param pop    调用Unity的弹窗log方法名
     */
    public static void setUnityDebug(String object, String log, String pop) {

        if (!Extends.isNullOrEmpty(object, log, pop)) {
            gameObject = object;
            logMethod = log;
            popMethod = pop;
            log(" 成功注册了 Android for UnityDebug");
        }
    }
}
