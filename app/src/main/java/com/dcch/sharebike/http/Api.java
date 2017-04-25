package com.dcch.sharebike.http;


public class Api {
    //    public static final String BASE_URL = "http://192.168.1.159:8080/MavenSSM/mobile/";
//    public static final String BASE_URL = "http://192.168.1.142:8080/MavenSSM/mobile/";
//    public static final String BASE_URL = "http://192.168.1.131:8080/MavenSSM/mobile/";
    public static final String BASE_URL = "http://114.112.86.38/MavenSSM/mobile/";
    //    获取验证码
    public static final String REGISTER = "register.do?";
    //注册登录
    public static final String SAVEUSER = "saveUser.do?";
    //认证押金
//    public static final String UPDATEUSERCASHSTATUS = "updateUserCashStatus.do?";
    //认证身份
    public static final String UPDATEUSERSTATUS = "updateUserAppStatus.do?";
    //    public static final String UPDATEUSERSTATUS = "updateUserStatus.do?";
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
    //上传文字和图片的接口
    public static final String ADDTROUBLEORDER = "addTroubleOrder.do?";
    //支付宝支付
    public static final String ALIPAY = "alipay.do?";
    //微信支付车费
    public static final String WEIXINPAY = "weixinpay.do?";
    //微信支付押金
    public static final String WEIXINCASHPAY = "weixinCashpay.do?";
    //修改用户昵称
    public static final String EDITUSER = "editAppUser.do?";
    //上传用户头像
    public static final String UPLOADAVATAR = "uploadAvatar.do?";
    //查询用户信息
    public static final String INFOUSER = "infoUser.do?";
    //用户充车费后改变状态的接口
//    public static final String RECHARGE = "recharge.do?";
    //骑行花费
    public static final String ORDERCAST = "orderCast.do?";
    //查询余额
    public static final String CHECKAGGREGATE = "checkAggregate.do?";
    //查询车辆编号
    public static final String CHECKBICYCLENO = "checkBicycleNo.do?";
    //查询是否有骑行订单
    public static final String SEARCHORDERING = "searchOrdering.do?";
    //行程列表
    public static final String GETCARRENTALORDERBYPHONE = "getCarRentalOrderByPhone.do?";
    //单个行程
    public static final String TRIPRECORD = "tripRecord.do?";
    //交易列表
    public static final String SEARCHPAYLIST = "searchPayList.do?";
    //微信退款
    public static final String REFUNDWXPAY = "refundWXPay.do?";
    //用户协议
    public static final String USERAGREEMENT = "http://www.70bikes.com/MavenSSM/Explain/userAgreement.jsp";
    //退款说明
    public static final String REFUNDEXPLAIN = "http://www.70bikes.com/MavenSSM/Explain/refundAgreement.jsp";
    //押金说明
    public static final String DEPOSITAGREEMENT = "http://www.70bikes.com/MavenSSM/Explain/depositAgreement.jsp";
    //充值协议
    public static final String RECHARGEAGREEMENT = "http://www.70bikes.com/MavenSSM/Explain/rechargeAgreement.jsp";
    public static final String VERSION = "http://192.168.1.131:8080/MavenSSM/version/version.xml";
}
