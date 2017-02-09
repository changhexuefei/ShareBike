package com.dcch.sharebike.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 14369 on 2016/12/9.
 */

public class Md5Utils {

	/**
	 * md5加密
	 *
	 * @param str 要加密的字符串
	 * @return
	 */
	public static String md5(String str) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(str.getBytes("UTF-8"));
			byte[] encryption = md5.digest();

			StringBuffer strBuf = new StringBuffer();
			for (int i = 0; i < encryption.length; i++) {
				if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
					strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
				} else {
					strBuf.append(Integer.toHexString(0xff & encryption[i]));
				}
			}
			return strBuf.toString();
		} catch (NoSuchAlgorithmException e) {
			return "";
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
}
