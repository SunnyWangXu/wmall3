package com.wjhgw.config;

public class ApiInterface {

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

}
