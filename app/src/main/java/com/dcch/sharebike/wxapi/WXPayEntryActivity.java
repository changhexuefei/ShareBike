package com.dcch.sharebike.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dcch.sharebike.app.App;
import com.dcch.sharebike.moudle.home.content.MyContent;
import com.dcch.sharebike.moudle.login.activity.IdentityAuthenticationActivity;
import com.dcch.sharebike.moudle.user.activity.WalletInfoActivity;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "微信微信";
    private IWXAPI api;
    private int mStatus;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStatus = (Integer) SPUtils.get(App.getContext(), "status", 0);
//        setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, MyContent.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        LogUtils.d("微信支付", "到我了");
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinisherrCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                if (mStatus == 0) {
                    LogUtils.d("走着了", "zheli");
                    Intent intent = new Intent(WXPayEntryActivity.this, IdentityAuthenticationActivity.class);
                    intent.putExtra("PAYCODE", resp.errCode + "");
                    ToastUtils.showShort(App.getContext(), "支付成功！");
                    SPUtils.put(App.getContext(), "cashStatus", 1);
                    startActivity(intent);
                } else if (mStatus == 1) {
                    Intent intent = new Intent(WXPayEntryActivity.this, WalletInfoActivity.class);
                    intent.putExtra("PAYCODE", resp.errCode + "");
                    ToastUtils.showShort(App.getContext(), "支付成功！");
                    SPUtils.put(App.getContext(), "cashStatus", 1);
                    startActivity(intent);
                }
                this.finish();
            } else if (resp.errCode == -1) {//支付失败
                ToastUtils.showShort(App.getContext(), "支付失败！" + resp.errCode);
                this.finish();
            } else {//取消
                ToastUtils.showShort(App.getContext(), "支付取消！");
                this.finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}