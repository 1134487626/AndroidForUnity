package UnityBasic;

/**
 * Created by U3D on 2018/7/26.
 */

public final class Extends {

    /**
     * @param str
     * @return 返回字符串类型是否为空或者为""
     */
    public static Boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * @param params
     * @return 返回多个字符串类型是否有一个为空或者为""
     */
    public static Boolean isNullOrEmpty(String... params) {

        if (params.length > 0) {
            for (String temp : params) {
                if (Extends.isNullOrEmpty(temp)) return true;
            }
        }
        return false;
    }
}
