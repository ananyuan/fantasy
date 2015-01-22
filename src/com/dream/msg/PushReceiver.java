package com.dream.msg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;

public class PushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            //添加其他case
            //......... 
            case PushConsts.GET_CLIENTID:
                // 获取ClientID(CID)
                String cid = bundle.getString("clientid");
                Log.d("GetuiSdkDemo", "Got ClientID---------------------------------:" + cid);
                // TODO:
                /* 第三方应用需要将ClientID上传到第三方服务器，并且将当前用户帐号和ClientID进行关联，
                	以便以后通过用户帐号查找ClientID进行消息推送。有些情况下ClientID可能会发生变化，为保证获取最新的ClientID，
                	请应用程序在每次获取ClientID广播后，都能进行一次关联绑定 */
                break;
            default:
                break;
        }
    }
}