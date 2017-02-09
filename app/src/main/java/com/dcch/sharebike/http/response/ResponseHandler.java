package com.dcch.sharebike.http.response;

import okhttp3.Call;

/**
 * 作者：zzw on 2016-10-14 11:44
 * QQ：1436942789
 * 邮箱：developer_zzw@163.com
 * 作用：
 */

public interface ResponseHandler {
	void onError(Call call, Exception e, int id);

	void onSuccess(String response, int id);

	void inProgress(float progress, long total, int id);
}
