package com.my.lqPlayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.my.lqPlayer.wxapi.WXEntryActivity;
import com.my.lqPlayer.wxapi.WXShareEntryActivity;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.unity3d.player.UnityPlayerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import ASUtils.DevelopUtils;
import TencentPlayer.WXShareType;
import TencentPlayer.WeChat;
import UnityBasic.Debug;
import UnityBasic.Delegate;

/**
 * Created by Administrator on 2018/7/27.
 * 主调用类
 */
public class MainActivity extends UnityPlayerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        DevelopUtils.getDialog().onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 微信登录
     *
     * @param json
     */
    public void sendReqWXLogin(String json) {

        try {
            JSONObject login = new JSONObject(json);
            Debug.log(json);
            if (WeChat.isActiveWX()) {
                // 发送授权登录信息，来获取code
                final SendAuth.Req req = new SendAuth.Req();
                req.scope = login.getString("scope"); //应用的作用域，获取个人信息
                req.state = login.getString("state");
                WeChat.iwxapi.sendReq(req);
                WeChat.loginFun = new Delegate(login.getString("game"), login.getString("method"));
                startActivity(new Intent(this, WXEntryActivity.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 微信支付
     *
     * @param json
     */
    public void sendReqWXPay(String json) {

        try {
            JSONObject pay = new JSONObject(json);
            if (WeChat.isActiveWX()) {
                final PayReq payReq = new PayReq();
                payReq.appId = WeChat.AppID;
                payReq.partnerId = pay.getString("partnerId"); //微信支付分配的商户号
                payReq.prepayId = pay.getString("prepayId");   //微信返回的支付交易会话ID
                payReq.nonceStr = pay.getString("nonceStr");   //随机字符串，不长于32位。推荐随机数生成算法
                payReq.timeStamp = pay.getString("timeStamp"); //时间戳
                payReq.sign = pay.getString("sign");           //签名
                payReq.packageValue = pay.getString("packageValue");//
                WeChat.iwxapi.sendReq(payReq);
                WeChat.payFun = new Delegate(pay.getString("game"), pay.getString("method"));
                startActivity(new Intent(this, WXEntryActivity.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //微信分享接口
    public void sendReqWXShare(String json) {

        if (WeChat.isActiveWX()) {
            SendMessageToWX.Req req = WeChat.getShare(json);
            if (req == null) Debug.log("分享处理失败！");
            else {
                WeChat.iwxapi.sendReq(req);
                startActivity(new Intent(this, WXShareEntryActivity.class));
            }
        }
    }
}