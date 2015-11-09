package com.wjhgw.config;

public class ApiInterface {

    /**
     * 首页焦点图接口
     */
    public static final String Home_pager = "/mobile/index.php?act=index&op=index_ad";
    /**
     *拍卖和团购
     */
    public static final String Auction_super_value = "/mobile/index.php?act=index&op=auction_super_value";
    /**
     *折扣街
     */
    public static final String Group_Buy = "/mobile/index.php?act=index&op=groupbuy_list";/**
     *主题街
     */
    public static final String Theme_street = "/mobile/index.php?act=index&op=theme_street";
    /**
     * 猜你喜欢接口
     */
    public static final String Guess_Like = "/mobile/index.php?act=index&op=guess_like";

    /**
     * 商品列表接口
     */
    public static final String Goods_list  ="/mobile/index.php?act=goods&op=goods_list";
    /**
     * 一级分类接口
     */
    public static final String Goods_class  ="/mobile/index.php?act=goods_class&flag=1";
    /**
     * 指定分类接口
     */
    public static final String Goods_class1  ="/mobile/index.php?act=goods_class&flag=2";
    /**
     * 登录接口
     */
    public static final String Login  ="/mobile/index.php?act=login";
    /**
     * 验证手机号码是否已注册接口
     */
    public static final String VerificationRegistered  ="/mobile/index.php?act=common&op=validate_phone";
    /**
     * 验证手机号码是否正确接口
     */
    public static final String Number  ="http://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=";
    /**
     * 请求发送验证码接口
     */
    public static final String VerificationCode  ="/mobile/index.php?act=common&op=req_validate_code";
    /**
     * 验证验证码是否正确接口
     */
    public static final String VerificationNumber  ="/mobile/index.php?act=common&op=validate_code";
    /**
     * 注册接口
     */
    public static final String Registered  ="/mobile/index.php?act=register";
    /**
     * 重置密码接口
     */
    public static final String ResetPassword  ="/mobile/index.php?act=reset_password";

    /**
     * 重置密码接口
     */
    public static final String Get_member_base_info  ="/mobile/index.php?act=member_info&op=get_member_base_info";

    /**
     * 个人资料 获取用户信息
     */
    public static  final String  MyLockBox ="/mobile/index.php?act=member_info&op=get_member_base_info";

    /**
     * 修改昵称
     */
    public static final String Change_nickname  ="/mobile/index.php?act=member_info&op=change_nickname";
    /**
     * 上传图片
     */
    public static final String Member_image  ="/mobile/index.php?act=upload&op=member_image";
    /**
     *校验登录密码
     */
    public static final String Check_password  ="/mobile/index.php?act=member_info&op=check_password";
    /**
     *修改登录密码
     */
    public static final String Change_pwd  ="/mobile/index.php?act=member_info&op=change_pwd";



}
