package com.dcch.sharebike.http.response;

import okhttp3.Call;

/**
 * 作者：zzw on 2016-10-14 11:50
 * QQ：1436942789
 * 邮箱：developer_zzw@163.com
 * 作用：${INPUT}
 */

public abstract class JsonResponseHandler implements ResponseHandler {

	@Override
	public abstract void onError(Call call, Exception e, int id);

	@Override
	public abstract void onSuccess(String response, int id);

	@Override
	public void inProgress(float progress, long total, int id) {

	}

}
