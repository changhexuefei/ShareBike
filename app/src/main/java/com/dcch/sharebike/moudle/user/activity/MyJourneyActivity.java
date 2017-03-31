package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.http.Api;
import com.dcch.sharebike.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

public class MyJourneyActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.help)
    TextView help;
    @BindView(R.id.journey_list)
    ListView journeyList;
    @BindView(R.id.no_journey)
    TextView noJourney;
    private String mPhone;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_journey;
    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        if(intent!=null){
            mPhone = intent.getStringExtra("phone");
        }
    }

    @OnClick({R.id.back, R.id.help})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.help:
                ToastUtils.showShort(this,"我是帮助");
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Map<String,String> map = new HashMap<>();
        map.put("phone",mPhone);
        OkHttpUtils.post().url(Api.BASE_URL+Api.GETCARRENTALORDERBYPHONE).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {

            }
        });

    }
}
