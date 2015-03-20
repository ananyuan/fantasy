package com.dream.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dream.util.Constant;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/** 微信客户端回调activity示例 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	private static final String TAG = "WXEntryActivity";
	
	// IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		api = WXAPIFactory.createWXAPI(this, Constant.APP_ID_WE_CHAT, false);
		api.handleIntent(getIntent(), this);
		super.onCreate(savedInstanceState);
	}
	@Override
	public void onReq(BaseReq arg0) { }

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "resp.errCode:" + resp.errCode + ",resp.errStr:" + resp.errStr);
		String result = "";
		
		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				result = "发送成功";
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result = "发送取消";
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = "发送被拒绝";
				break;
			default:
				result = "发送返回";
				break;
		}
		
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}
}