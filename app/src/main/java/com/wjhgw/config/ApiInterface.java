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
     * 一级商品分类
     */
    public static final String Goods_class1 ="/mobile/index.php?act=goods_class&op=second_class";
    /**
     * 商品分类属性接口
     */
    public static final String Goods_attr ="/mobile/index.php?act=goods_class&op=goods_class_attr";
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
     *获取用户信息
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
    /**
     *设置支付密码
     */
    public static final String Set_paypwd  ="/mobile/index.php?act=member_info&op=set_paypwd";
    /**
     *地址列表接口
     */
    public static final String Address_list  ="/mobile/index.php?act=member_address&op=address_list";
    /**
     *地址删除接口
     */
    public static final String Address_del  ="/mobile/index.php?act=member_address&op=address_del";
    /**
     *设置默认地址
     */
    public static final String Set_add_def  ="/mobile/index.php?act=member_address&op=set_add_def";
    /**
     *编辑地址接口
     */
    public static final String Address_edit  ="/mobile/index.php?act=member_address&op=address_edit";
    /**
     *修改绑定手机号
     */
    public static final String Change_mobile  ="/mobile/index.php?act=member_info&op=change_mobile";
    /**
     *新增收货地址接口
     */
    public static final String Add_address  ="/mobile/index.php?act=member_address&op=address_add";
    /**
     *讯搜
     */
    public static final String Auto_complete  ="/mobile/index.php?act=xs&op=auto_complete";
    /**
     *热点词汇
     */
    public static final String Hot_search  ="/mobile/index.php?act=search&op=hot_search";

    /**
     *退出登录接口
     */
    public static final String Exit_login  ="/mobile/index.php?act=logout";
    /**
     *平台查询
     */
    public static final String Act_search  ="/mobile/index.php?act=search";
    /**
     *获取未完成处理的订单数量
     */
    public static final String Order_amount  ="/mobile/index.php?act=member_order&op=order_amount";
    /**
     *购物车列表接口
     */
    public static final String Cart_list  ="/mobile/index.php?act=member_cart&op=cart_list";
    /**
     *购物车修改数量接口
     */
    public static final String Cart_edit_quantity  ="/mobile/index.php?act=member_cart&op=cart_edit_quantity";
    /**
     *购物车删除接口
     */
    public static final String Cart_del  ="/mobile/index.php?act=member_cart&op=cart_del";
    /**
     *收藏添加接口
     */
    public static final String Favorites_add  ="/mobile/index.php?act=member_favorites&op=favorites_add";
    /**
     *收藏添加接口
     */
    public static final String Buy_step1  ="/mobile/index.php?act=member_buy&op=buy_step1";

}
