package com.dcch.sharebike.http;

/**
 * Created by 14369 on 2016/12/9.
 */

public class Api {

	public static final String BASE_URL = "http://192.168.1.130:8080/MavenSSM/mobile/";
	//获取验证码
	public static final String REGISTER = "register.do?";
	//注册登录
	public static final String SAVEUSER = "saveUser.do?";
	//请求车辆信息
	public static final String GINPUT = "findBicycle.do?";
	//预约用车
	public static  final  String BOOKBIKE ="addBookingCar.do?";
	//取消预约用车
	public static final String CANCELBOOK="updateBookingCar.do?";

	//

}
