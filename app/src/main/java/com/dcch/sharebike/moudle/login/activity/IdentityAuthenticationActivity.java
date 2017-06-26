package com.dcch.sharebike.moudle.login.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.anton46.stepsview.StepsView;
import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.moudle.home.content.MyContent;
import com.dcch.sharebike.utils.AES;
import com.dcch.sharebike.utils.ClickUtils;
import com.dcch.sharebike.utils.InPutUtils;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.LogUtils;
import com.dcch.sharebike.utils.NetUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.hss01248.dialog.StyledDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 实名认证页面
 */

public class IdentityAuthenticationActivity extends BaseActivity {

    @BindView(R.id.IDCardNo)
    EditText IDCardNo;
    @BindView(R.id.btn_authentication)
    Button btnAuthentication;
    @BindView(R.id.userRealName)
    EditText userRealName;
    @BindView(R.id.iden_stepsView)
    StepsView mIdenStepsView;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private String realName;
    private String cardNum;
    private String uID;
    private String mToken;
//    private String encryptionName;
//    private String encryptionIDCARD;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_identity_authentication;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.realName_identity_title));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToLoginMain = new Intent(IdentityAuthenticationActivity.this, MainActivity.class);
                startActivity(backToLoginMain);
                finish();
            }
        });
        if (SPUtils.isLogin()) {
            uID = String.valueOf(SPUtils.get(App.getContext(), "userId", 0));
            mToken = (String) SPUtils.get(App.getContext(), "token", "");
        }

        mIdenStepsView.setLabels(MyContent.steps)
                .setBarColorIndicator(getResources().getColor(R.color.colorHeading))
                .setProgressColorIndicator(getResources().getColor(R.color.colorTitle))
                .setLabelColorIndicator(getResources().getColor(R.color.colorTitle))
                .setCompletedPosition(2)
                .drawView();
    }

    @Override
    protected void initListener() {
        IDCardNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().equals("") && editable != null) {
                    cardNum = editable.toString().trim();
                    if (!InPutUtils.IDCardValidate(cardNum)) {
                        btnAuthentication.setEnabled(false);
                        btnAuthentication.setBackgroundColor(getResources().getColor(R.color.btn_bg));
                    } else if (TextUtils.isEmpty(realName)) {
                        btnAuthentication.setEnabled(false);
                        btnAuthentication.setBackgroundColor(getResources().getColor(R.color.btn_bg));
                    } else if (InPutUtils.IDCardValidate(cardNum) && !TextUtils.isEmpty(realName)) {
                        btnAuthentication.setEnabled(true);
                        btnAuthentication.setBackgroundColor(getResources().getColor(R.color.btn_bg_other));
                    }
                }
            }
        });
        userRealName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().equals("")) {
                    realName = editable.toString().trim();
                }
            }
        });
    }

    @OnClick(R.id.btn_authentication)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_authentication:
                //实名认证的接口
                if (ClickUtils.isFastClick()) {
                    return;
                }
                if (NetUtils.isConnected(App.getContext())) {
                    StyledDialog.buildLoading(IdentityAuthenticationActivity.this, "认证中", true, false).show();
                    verifyRealName(uID, realName, cardNum, mToken);
                } else {
                    ToastUtils.showShort(this, getString(R.string.no_network_tip));
                }
                break;
        }
    }

    private void verifyRealName(final String uID, final String realName, String cardNum, String token) {
        try {
            String encode = URLEncoder.encode(realName, "utf-8");//"UTF-8"
            cardNum = AES.encrypt(cardNum.getBytes(), MyContent.key);
            Map<String, String> map = new HashMap<>();
            map.put("userId", uID);
            map.put("name", encode);
            map.put("IDcard", cardNum);
            map.put("token", token);
            LogUtils.d("参数", uID + "\n" + encode + "\n" + cardNum + "\n" + token);

            OkHttpUtils.post().url(Api.BASE_URL + Api.UPDATEUSERSTATUS).params(map).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    StyledDialog.dismissLoading();
                    ToastUtils.showShort(IdentityAuthenticationActivity.this, getString(R.string.server_tip));
                }

                @Override
                public void onResponse(String response, int id) {
                    Log.d("实名认证", response);
                    StyledDialog.dismissLoading();
                    if (JsonUtils.isSuccess(response)) {
                        if (uID != null && !uID.equals("")) {
                            Intent authentication = new Intent(IdentityAuthenticationActivity.this, AuthenticationOkActivity.class);
                            SPUtils.put(App.getContext(), "status", 1);
                            authentication.putExtra("userId", uID);
                            startActivity(authentication);
                            finish();
                        }

                    } else {
                        ToastUtils.showShort(IdentityAuthenticationActivity.this, getString(R.string.input_name_uuid_fail));
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backToLoginMain = new Intent(IdentityAuthenticationActivity.this, MainActivity.class);
        startActivity(backToLoginMain);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StyledDialog.dismiss();
    }
}
