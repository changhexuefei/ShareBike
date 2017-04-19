package com.dcch.sharebike.utils;

import com.dcch.sharebike.app.App;

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
            String resultStatus = jsonObject.optString("resultStatus");
            if (resultStatus.equals("1")) {
                return true;
            } else if (resultStatus.equals("2")) {
                ToastUtils.showShort(App.getContext(),"您的账号已经在其他设备登录，您已经被迫下线");
                SPUtils.clear(App.getContext());
                SPUtils.put(App.getContext(),"islogin",false);
                SPUtils.put(App.getContext(),"isfirst",false);
                SPUtils.put(App.getContext(),"isStartGuide",true);
                return false;
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
