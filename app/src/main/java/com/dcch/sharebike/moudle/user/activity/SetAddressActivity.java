package com.dcch.sharebike.moudle.user.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.moudle.user.adapter.SetAddressAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SetAddressActivity extends BaseActivity {

    @BindView(R.id.set_cancel)
    Button setCancel;
    @BindView(R.id.set_search)
    EditText setSearch;
    @BindView(R.id.set_deleteWord)
    ImageButton setDeleteWord;
    @BindView(R.id.set_infoList)
    ListView setInfoList;
    @BindView(R.id.no_result)
    TextView mNoResult;
    SuggestionSearch mSuggestionSearch;

    private SetAddressAdapter mAdapter;
    private List<SuggestionResult.SuggestionInfo> allSuggestions;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_address;
    }

    @Override
    protected void initData() {
        mAdapter = new SetAddressAdapter(this);
        mSuggestionSearch = SuggestionSearch.newInstance();

    }

    @Override
    protected void initListener() {
        super.initListener();
        mSuggestionSearch.setOnGetSuggestionResultListener(listener);
        setSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputKeyWords = editable.toString().trim();
                if (!inputKeyWords.equals("") && inputKeyWords != null) {
                    // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                    mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                            .keyword(inputKeyWords)
                            .city("全国"));
                } else {
                    setInfoList.setVisibility(View.GONE);
                }
            }
        });

    }

    @OnClick({R.id.set_cancel, R.id.set_deleteWord})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.set_cancel:
                finish();
                break;
            case R.id.set_deleteWord:
                setSearch.setText("");
                break;
        }
    }

    OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
        public void onGetSuggestionResult(SuggestionResult res) {
            if (res == null || res.getAllSuggestions() == null) {
                mNoResult.setVisibility(View.VISIBLE);
                setInfoList.setVisibility(View.GONE);
                return;
                //未找到相关结果
            }
            //获取在线建议检索结果
            setInfoList.setVisibility(View.VISIBLE);
            mNoResult.setVisibility(View.GONE);
            allSuggestions = res.getAllSuggestions();
            mAdapter.setAllSuggestions(allSuggestions);
            setInfoList.setAdapter(mAdapter);
            setInfoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    SuggestionResult.SuggestionInfo suggestionInfo = allSuggestions.get(i);
                    String key = suggestionInfo.key;
                    String set_address = suggestionInfo.city + suggestionInfo.district;
                    Intent mIntent = new Intent();
                    mIntent.putExtra("item01", key);
                    mIntent.putExtra("item02", set_address);
                    // 设置结果，并进行传送
                    SetAddressActivity.this.setResult(0, mIntent);
                    SetAddressActivity.this.finish();
                }
            });
        }
    };

}
