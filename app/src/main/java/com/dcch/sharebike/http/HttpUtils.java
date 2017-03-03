package com.dcch.sharebike.http;

import android.util.Log;

import com.dcch.sharebike.http.response.ResponseHandler;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Created by 14369 on 2016/12/9.
 */

public class HttpUtils {

//	private static final String SPLIT = "&";
//
	/**
	 * 初始化 okHttpClient
	 *
	 * @param okHttpClient
	 */
	public static void init(OkHttpClient okHttpClient) {
		OkHttpUtils.initClient(okHttpClient);
	}

	/**
	 * 异步post请求
	 *
	 * //@param c       接口类型
	 * @param a 	  接口名称
	 * @param param  接口参数
	 * @param handler 接口回调
	 * @return 本次请求的tag，用于取消请求
	 */
	public static String post(String a,  Map<String,String> param, final ResponseHandler handler) {
		String t = time();
//		String p = "";
//		if (params != null) {
//			for (int i = 0; i < params.size(); i++) {
//				p += params.get(i);
//				if (i < params.size() - 1) {
//					p += SPLIT;
//				}
//			}
//			LogUtils.e(p);
//		}
//		String j = Md5Utils.md5(c + a + t + Api.KEY);
		 param = new HashMap<>();
		param.put("a", a);
		param.put("t", t);
//		param.put("c", c);
//		param.put("j", j);
//		param.put("p", Base64.encodeToString(p.getBytes(), Base64.DEFAULT));
		Log.d("555555",param.toString());
		OkHttpUtils.post().url(Api.BASE_URL).params(param).tag(t).build().execute(new StringCallback() {
			@Override
			public void onError(Call call, Exception e, int id) {
				handler.onError(call, e, id);
			}

			@Override
			public void onResponse(String response, int id) {
				handler.onSuccess(response, id);
			}

			@Override
			public void inProgress(float progress, long total, int id) {
				handler.inProgress(progress, total, id);
			}
		});
		return t;
	}

	/**
	 * 异步post请求
	 *
	 * // @param c       接口类型
	 * @param a 	  接口名称
	 * @param handler 接口回调
	 * @return 本次请求的tag，用于取消请求
	 */
	public static String post(String a, final ResponseHandler handler) {
		return post(a,  null, handler);
	}

	/**
	 * 取消所有以context为Tag的请求
	 *
	 * @param tag 请求的tag
	 */
	public static void cancel(String tag) {
		OkHttpUtils.getInstance().cancelTag(tag);
	}

	/**
	 * 获取时间戳（秒数）
	 *
	 * @return
	 */
	private static String time() {
		return (System.currentTimeMillis() / 1000) + "";
	}
}
