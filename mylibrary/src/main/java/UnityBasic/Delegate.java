package UnityBasic;

import com.unity3d.player.UnityPlayer;

/**
 * Created by Administrator on 2018/7/20.
 * Android 调用Unity3D中的方法
 */
public final class Delegate {

    private String varObject, varMethod;

    public Delegate() {
    }

    public Delegate(String object, String method) {
        setEvent(object, method);
    }

    /**
     * 是否为有效的使用类
     */
    public Boolean isValid() {
        return !Extends.isNullOrEmpty(varObject, varMethod);
    }

    /**
     * 尽量不使用“xx xxx”带有空格的名字
     *
     * @param object
     * @param method
     * @return 是否成功
     */
    public Boolean setEvent(String object, String method) {
        varObject = object;
        varMethod = method;
        Debug.log("设置Unity回调：" + varObject + ":" + varMethod);
        return isValid();
    }

    /**
     * @param varStr 调用Unity并返回参数 需要的返回值
     */
    public void invoke(String varStr) {

        if (isValid()) {
            UnityPlayer.UnitySendMessage(varObject, varMethod, varStr);
        }
    }

    /**
     * @param varStr 调用Unity并返回参数 需要的返回值
     */
    public void invoke2(String varStr) {

        if (isValid()) {
            UnityPlayer.UnitySendMessage(varObject, varMethod, varStr);
            Clear();
        }
    }

    /**
     * 清除对象名和方法名
     */
    public void Clear() {
        varObject = null;
        varMethod = null;
    }
}
