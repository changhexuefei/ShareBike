package com.dcch.sharebike.moudle.login.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.userPhone)
    EditText userPhone;
    @BindView(R.id.securityCode)
    EditText securityCode;
    @BindView(R.id.getSecurityCode)
    TextView getSecurityCode;
    @BindView(R.id.confirm)
    TextView confirm;
    @BindView(R.id.rules)
    TextView rules;
    @BindView(R.id.back)
    ImageView back;


    // 定义变量
    private String userName;
    public boolean isChange = false;
    private boolean tag = true;
    private int i = 60;
    Thread thread = null;

    /**
     * 客户端输入的验证码
     */
    private String valicationCode;
    /**
     * 服务器端获取的验证码
     */
    private static String serverValicationCode;
    /**
     * 注册时所带的参数
     */
    private Map<String, Object> registerParams = new HashMap<String, Object>();
    /**
     * 获取验证码时所带的参数
     */
    private Map<String, Object> codeParams = new HashMap<String, Object>();
    /**
     * 注册是否成功
     */
    private String regisgerStatus;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {

    }



    @Override
    protected void initListener() {
        confirm.setOnClickListener(this);
        getSecurityCode.setOnClickListener(this);

    }

    @OnClick({R.id.getSecurityCode, R.id.confirm, R.id.rules, R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.getSecurityCode:
                break;
            case R.id.confirm:
                ToastUtils.showLong(this, "手机验证成功！");
                //保存用户信息
//                SPUtils.put(this, "lastonline", info.getLastonline());
//                SPUtils.put(this, "nickname", info.getLastonline());
//                SPUtils.put(this, "phoneno", info.getPhoneno());
//                SPUtils.put(this, "regtime", info.getRegtime());
//                SPUtils.put(this, "status", info.getStatus());
//                SPUtils.put(this, "uid", info.getUid());
//                ToastUtils.showShort(this, "注册成功");
//                this.finish();

                Intent i1 = new Intent(this,RechargeActivity.class);
                startActivity(i1);
                finish();
                break;
            case R.id.rules:
                Intent intent = new Intent(this, AgreementActivity.class);
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
        }
    }



//    private void changeBtnGetCode() {
//        thread = new Thread() {
//            @Override
//            public void run() {
//                if (tag) {
//                    while (i > 0) {
//                        i--;
//                        if (LoginActivity.this == null) {
//                            break;
//                        }
//                        LoginActivity.this.runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        getSecurityCode.setClickable(false);
//                                    }
//                                });
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                    tag = false;
//                }
//                i = 60;
//                tag = true;
//                if (LoginActivity.this != null) {
//                    LoginActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            getSecurityCode.setClickable(true);
//                        }
//                    });
//                }
//            }
//        };
//        thread.start();
//
//    }

}
