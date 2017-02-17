package com.dcch.sharebike.utils;

import org.json.JSONException;
import org.json.JSONObject;


public class JsonUtils {

	/**
	 * @param result
	 * @return 服务器返回是否成功
	 */
	public static boolean isSuccess(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
//			int messagecode = jsonObject.optInt("messagecode",0);
			String messagecode = jsonObject.optString("messagecode");
			if (messagecode.equals("1") ){
				return true;
			} else {
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param result
	 * @return 获取调用失败的信息
	 */
	public static String getErrorMessage(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			return jsonObject.optString("restr", "");
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}
}
