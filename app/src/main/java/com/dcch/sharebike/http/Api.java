package com.dcch.sharebike.http;


public class Api {

//    	public static final String BASE_URL = "http://192.168.1.138:8080/MavenSSM/mobile/";
    	public static final String BASE_URL = "http://192.168.1.130:8080/MavenSSM/mobile/";
//    public static final String BASE_URL = "http://114.112.86.38:8080/MavenSSM/mobile/";
    //获取验证码
    public static final String REGISTER = "register.do?";
    //注册登录
    public static final String SAVEUSER = "saveUser.do?";
    //请求车辆信息
    public static final String GINPUT = "findBicycle.do?";
    //预约用车
    public static final String BOOKBIKE = "addBookingCar.do?";
    //取消预约用车
    public static final String CANCELBOOK = "updateBookingCar.do?";
    //扫码开锁
    public static final String OPENSCAN = "openScan.do?";
    //根据用户ID查询预约单车信息
    public static final String SEARCHBOOKING = "searchBooking.do?";
    //生成用车订单
    public static final String RENTALORDER = "addCarRentalOrder.do?";
    //查询用户预约车辆的次数
    public static final String BOOKINGNUMBER = "getBookingCarCountByPhone.do?";

}
