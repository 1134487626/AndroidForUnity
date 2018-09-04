package com.my.lqPlayer.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.json.JSONException;
import org.json.JSONObject;

import TencentPlayer.WeChat;
import UnityBasic.Debug;

/**
 * Created by Administrator on 2018/8/1.
 * 微信支付
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

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

    @Override
    public void onResp(BaseResp baseResp) {

        JSONObject data = new JSONObject();
        try {
            data.put("errCode", baseResp.errCode);
            WeChat.payFun.invoke(data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Debug.log("注意：微信支付反馈信息转JSON出错！");
        }
        this.finish();
    }
}
