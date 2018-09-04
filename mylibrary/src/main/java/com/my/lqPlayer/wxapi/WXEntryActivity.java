package com.my.lqPlayer.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import TencentPlayer.WeChat;
import UnityBasic.Debug;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/7/26.
 * 微信登录Activity
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeChat.iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WeChat.iwxapi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    //  第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp baseResp) {

//        Debug.log("微信登录回调！");
        SendAuth.Resp resp = (SendAuth.Resp) baseResp;//获取微信传回的code
        JSONObject data = new JSONObject();
        try {
            data.put("errCode", baseResp.errCode);
            data.put("respCode", resp.code);
            WeChat.loginFun.invoke(data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Debug.log("注意：微信登录反馈信息转JSON出错！");
        }
        this.finish();
    }

}
