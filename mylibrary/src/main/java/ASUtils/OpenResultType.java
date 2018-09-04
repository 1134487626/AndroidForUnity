package ASUtils;

/**
 * Created by Administrator on 2018/7/23.
 */

public enum OpenResultType {

    Node,
    //提示跳转到手机设置定位的界面
    GPS,
    //提示打开相册选取某张图片,并返回给Unity
    PHOTOS,
    //提示打开摄像头拍摄并保存,并返回给Unity
    CAMERA,
    //提示打开默认的浏览器访问某个网址
    BROWSER,
    //提示未安装微信App
    NoWXApp,
}
