package com.dcch.sharebike.moudle.login.activity;

import android.content.Intent;
import android.graphics.Color;
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
import com.dcch.sharebike.utils.InPutUtils;
import com.dcch.sharebike.utils.JsonUtils;
import com.dcch.sharebike.utils.SPUtils;
import com.dcch.sharebike.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

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

public class IdentityAuthentication extends BaseActivity {

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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_identity_authentication;
    }

    @Override
    protected void initData() {
        mToolbar.setTitle("");
        mTitle.setText(getResources().getString(R.string.identity_area));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToLoginMain = new Intent(IdentityAuthentication.this, MainActivity.class);
                startActivity(backToLoginMain);
                finish();
            }
        });


        String userDetail = (String) SPUtils.get(App.getContext(), "userDetail", "");
        try {
            JSONObject object = new JSONObject(userDetail);
            int id = object.getInt("id");
            uID = String.valueOf(id);
        } catch (JSONException e) {
            e.printStackTrace();
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
                        btnAuthentication.setBackgroundColor(Color.parseColor("#c6bfbf"));
                    } else if (TextUtils.isEmpty(realName)) {
                        btnAuthentication.setEnabled(false);
                        btnAuthentication.setBackgroundColor(Color.parseColor("#c6bfbf"));
                    } else if (InPutUtils.IDCardValidate(cardNum) && !TextUtils.isEmpty(realName)) {
                        btnAuthentication.setEnabled(true);
                        btnAuthentication.setBackgroundColor(Color.parseColor("#F8941D"));
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
                if (!editable.toString().trim().equals("") && editable != null) {
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
                verifyRealName(uID, realName, cardNum);
                break;
        }
    }

    private void verifyRealName(String uID, final String realName, String cardNum) {
        try {
            String encode = URLEncoder.encode(realName, "utf-8");//"UTF-8"
            Map<String, String> map = new HashMap<>();
            map.put("userId", uID);
            map.put("name", encode);
            map.put("IDcard", cardNum);

            OkHttpUtils.post().url(Api.BASE_URL + Api.UPDATEUSERSTATUS).params(map).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Log.e("错误", e.getMessage());
                    ToastUtils.showShort(IdentityAuthentication.this, "服务器正忙，请稍后再试！");
                }

                @Override
                public void onResponse(String response, int id) {
                    Log.d("实名认证", response);
                    //{"code":"1"}
                    if (JsonUtils.isSuccess(response)) {
                        Intent authentication = new Intent(IdentityAuthentication.this, AuthenticationOkActivity.class);
                        startActivity(authentication);
                        finish();

                    } else {
                        ToastUtils.showShort(IdentityAuthentication.this, "对不起，实名验证失败，请输入正确的信息");
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
        Intent backToLoginMain = new Intent(IdentityAuthentication.this, MainActivity.class);
        startActivity(backToLoginMain);
        finish();
    }

}
