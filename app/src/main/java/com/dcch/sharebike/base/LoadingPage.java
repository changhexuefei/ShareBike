package com.dcch.sharebike.base;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dcch.sharebike.R;

import java.util.List;


public abstract class LoadingPage extends FrameLayout {

	//1.提供4种显示状态
	private static final int PAGE_STATE_LOADING = 1;
	private static final int PAGE_STATE_ERROR = 2;
	private static final int PAGE_STATE_EMPTY = 3;
	private static final int PAGE_STATE_SUCCESS = 4;

	//当前显示的状态：默认显示正在加载的页面
	private int page_state_current = 1;

	//2.提供4种不同显示界面
	private View loadingView;
	private View errorView;
	private View emptyView;
	private View successView;

	private Context mContext;
	private LayoutParams params;

	public LoadingPage(Context context) {
		this(context, null);
	}

	public LoadingPage(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		init();
	}

	private void init() {
		params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		if (loadingView == null) {
			loadingView = View.inflate(mContext, R.layout.loading_loading_view, null);
			addView(loadingView, params);
		}

		if (errorView == null) {
			errorView = View.inflate(mContext, R.layout.loading_error_view, null);
			addView(errorView, params);
		}

		if (emptyView == null) {
			emptyView = View.inflate(mContext, R.layout.loading_empty_view, null);
			addView(emptyView, params);
		}


		//选择要加载显示的界面
		showSafePage();
	}

	private void showSafePage() {
		((Activity) mContext).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showPage();
			}
		});
	}

	//保证此方法在主线程中执行
	private void showPage() {
		loadingView.setVisibility(page_state_current == PAGE_STATE_LOADING ? View.VISIBLE : View.GONE);
		errorView.setVisibility(page_state_current == PAGE_STATE_ERROR ? View.VISIBLE : View.GONE);
		emptyView.setVisibility(page_state_current == PAGE_STATE_EMPTY ? View.VISIBLE : View.GONE);

		//如果加载成功的界面没有初始化，需要初始化
		if (successView == null) {
			successView = View.inflate(mContext, layoutId(), null);//内部使用Activity的实例作为Context实例
			addView(successView, params);
		}

		successView.setVisibility(page_state_current == PAGE_STATE_SUCCESS ? View.VISIBLE : View.GONE);
	}

	public abstract int layoutId();

	private ResultState resultState;

	//联网操作，根据返回的情况，决定page_state_current
	public void show() {
		String c = c();
		if (TextUtils.isEmpty(c)) {
			resultState = ResultState.SUCCESS;
			resultState.setContent("");
			loadPage();
		}
//		else{
//			HttpUtils.post(c(), a(), params(), new JsonResponseHandler() {
//				@Override
//				public void onError(Call call, Exception e, int id) {
//					resultState = ResultState.ERROR;
//					resultState.setContent("");
//					loadPage();
//				}
//
//				@Override
//				public void onSuccess(String response, int id) {
//					if (TextUtils.isEmpty(response)) {
//						resultState = ResultState.EMPTY;
//						resultState.setContent("");
//
//					} else {
//						Log.e("TAG", response);
//						resultState = ResultState.SUCCESS;
//						resultState.setContent(response);
//					}
//					loadPage();
//				}
//			});


		}

//		else {
//			HttpUtils.post(c(), a(), params(), new JsonResponseHandler() {
//			} {
//				@Override
//				public void onError(Call call, Exception e, int id) {
//					resultState = ResultState.ERROR;
//					resultState.setContent("");
//					loadPage();
//				}
//
//				@Override
//				public void onSuccess(String content, int id) {
//					if (TextUtils.isEmpty(content)) {
//						resultState = ResultState.EMPTY;
//						resultState.setContent("");
//
//					} else {
//						Log.e("TAG", content);
//						resultState = ResultState.SUCCESS;
//						resultState.setContent(content);
//					}
//					loadPage();
//				}
//			});
//		}
//	}

	private void loadPage() {
		switch (resultState) {
			case ERROR:
				page_state_current = PAGE_STATE_ERROR;
				break;
			case EMPTY:
				page_state_current = PAGE_STATE_EMPTY;
				break;
			case SUCCESS:
				page_state_current = PAGE_STATE_SUCCESS;
				break;
		}

		showSafePage();

		//如果是加载成功的状态
		if (page_state_current == PAGE_STATE_SUCCESS) {
			onSuccess(resultState, successView);
		}

	}

	protected abstract void onSuccess(ResultState resultState, View successView);

	//提供get请求的请求参数
	protected abstract List<String> params();

	//接口类型
	protected abstract String c();

	//接口名称
	protected abstract String a();

	//提供内部的枚举类
	public enum ResultState {
		ERROR(2), EMPTY(3), SUCCESS(4);

		private int state;

		ResultState(int state) {
			this.state = state;
		}

		private String content;//保存的内部数据

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}
}
