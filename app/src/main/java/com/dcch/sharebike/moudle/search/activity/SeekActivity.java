package com.dcch.sharebike.moudle.search.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dcch.sharebike.R;
import com.dcch.sharebike.base.BaseActivity;
import com.dcch.sharebike.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class SeekActivity extends BaseActivity implements TextWatcher {


    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.deleteWord)
    ImageButton deleteWord;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seek;
    }

    @Override
    protected void initData() {

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
        ToastUtils.showLong(this,editable.toString().trim());
    }
}
