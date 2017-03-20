package com.dcch.sharebike.moudle.search.activity;

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
import com.dcch.sharebike.MainActivity;
import com.dcch.sharebike.R;
import com.dcch.sharebike.app.App;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.moudle.search.adapter.SearchAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SeekActivity extends BaseActivity implements TextWatcher {
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.deleteWord)
    ImageButton deleteWord;
    @BindView(R.id.myAddress)
    TextView myAddress;
    SuggestionSearch mSuggestionSearch;
    @BindView(R.id.infoList)
    ListView infoList;
    @BindView(R.id.seek_no_result)
    TextView mSeekNoResult;
    private SearchAdapter adapter;
    private List<SuggestionResult.SuggestionInfo> allSuggestions;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seek;
    }

    @Override
    protected void initData() {
        adapter = new SearchAdapter(this);
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(listener);
        Intent intent = getIntent();
        if(intent!=null){
            String address = intent.getStringExtra("address");
            myAddress.setText(address);

        }else {
            myAddress.setText("未知地址，请稍后再试");
        }

    }

    @Override
    protected void initListener() {
        search.addTextChangedListener(this);
    }

    @OnClick({R.id.cancel, R.id.deleteWord})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.deleteWord:
                search.setText("");
                break;
        }
    }

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
            infoList.setVisibility(View.GONE);
        }

    }

    OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
        public void onGetSuggestionResult(SuggestionResult res) {
            if (res == null || res.getAllSuggestions() == null) {
                mSeekNoResult.setVisibility(View.VISIBLE);
                infoList.setVisibility(View.GONE);
                return;
                //未找到相关结果
            }
            //获取在线建议检索结果
            infoList.setVisibility(View.VISIBLE);
            mSeekNoResult.setVisibility(View.GONE);
            allSuggestions = res.getAllSuggestions();
            adapter.setAllSuggestions(allSuggestions);
            infoList.setAdapter(adapter);
            infoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(App.getContext(), MainActivity.class);
                    intent.putExtra("clickItem", allSuggestions.get(i));
                    startActivity(intent);
                    finish();
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSuggestionSearch.destroy();
    }

}
