package com.wjhgw.config;

public class ApiInterface {

    /**
     * 首页焦点图接口
     */
    public static final String Home_pager = "/mobile/index.php?act=index&op=index_ad";
    /**
     * 首页限时抢购
     */
    public static final  String Limit = "/mobile/index.php?act=index&op=time_limit_goods";
    /**
     * 限时抢购列表
     */
    public static final  String Limit_detail = "/mobile/index.php?act=promotion&op=get_xianshi_list";
    /**
     * 拍卖和团购
     */
    public static final String Auction_super_value = "/mobile/index.php?act=index&op=auction_super_value";
    /**
     * 折扣街
     */
    public static final String Group_Buy = "/mobile/index.php?act=index&op=groupbuy_list";
    /**
     * 主题街
     */
    public static final String Theme_street = "/mobile/index.php?act=index&op=theme_street";
    /**
     * 猜你喜欢接口
     */
    public static final String Guess_Like = "/mobile/index.php?act=index&op=guess_like";
    /**
     * 酒柜和购物车的消息条数
     */
    public static final String Main_message_num = "/mobile/index.php?act=member_info&op=message_num";

    /**
     * 商品列表接口
     */
    public static final String Goods_list = "/mobile/index.php?act=goods&op=goods_list";
    /**
     * 一级商品分类
     */
    public static final String Goods_class1 = "/mobile/index.php?act=goods_class&op=second_class";
    /**
     * 商品分类属性接口
     */
    public static final String Goods_attr = "/mobile/index.php?act=goods_class&op=goods_class_attr";
    /**
     * 登录接口
     */
    public static final String Login = "/mobile/index.php?act=login";
    /**
     * 验证手机号码是否已注册接口
     */
    public static final String VerificationRegistered = "/mobile/index.php?act=common&op=validate_phone";
    /**
     * 验证手机号码是否正确接口
     */
    public static final String Number = "http://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=";
    /**
     * 请求发送验证码接口
     */
    public static final String VerificationCode = "/mobile/index.php?act=common&op=req_validate_code";
    /**
     * 验证验证码是否正确接口
     */
    public static final String VerificationNumber = "/mobile/index.php?act=common&op=validate_code";
    /**
     * 注册接口
     */
    public static final String Registered = "/mobile/index.php?act=register";
    /**
     * 重置密码接口
     */
    public static final String ResetPassword = "/mobile/index.php?act=reset_password";

    /**
     * 获取用户信息
     */
    public static final String Get_member_base_info = "/mobile/index.php?act=member_info&op=get_member_base_info";

    /**
     * 个人资料 获取用户信息
     */
    public static final String MyLockBox = "/mobile/index.php?act=member_info&op=get_member_base_info";

    /**
     * 修改昵称
     */
    public static final String Change_nickname = "/mobile/index.php?act=member_info&op=change_nickname";
    /**
     * 上传图片
     */
    public static final String Member_image = "/mobile/index.php?act=upload&op=member_image";
    /**
     * 删除上传的图片
     */
    public static final String Del_img = "/mobile/index.php?act=upload&op=del_img";
    /**
     * 校验登录密码
     */
    public static final String Check_password = "/mobile/index.php?act=member_info&op=check_password";
    /**
     * 修改登录密码
     */
    public static final String Change_pwd = "/mobile/index.php?act=member_info&op=change_pwd";
    /**
     * 设置支付密码
     */
    public static final String Set_paypwd = "/mobile/index.php?act=member_info&op=set_paypwd";
    /**
     * 地址列表接口
     */
    public static final String Address_list = "/mobile/index.php?act=member_address&op=address_list";
    /**
     * 地址删除接口
     */
    public static final String Address_del = "/mobile/index.php?act=member_address&op=address_del";
    /**
     * 设置默认地址
     */
    public static final String Set_add_def = "/mobile/index.php?act=member_address&op=set_add_def";
    /**
     * 编辑地址接口
     */
    public static final String Address_edit = "/mobile/index.php?act=member_address&op=address_edit";
    /**
     * 修改绑定手机号
     */
    public static final String Change_mobile = "/mobile/index.php?act=member_info&op=change_mobile";
    /**
     * 新增收货地址接口
     */
    public static final String Add_address = "/mobile/index.php?act=member_address&op=address_add";
    /**
     * 讯搜
     */
    public static final String Auto_complete = "/mobile/index.php?act=xs&op=auto_complete";
    /**
     * 热点词汇
     */
    public static final String Hot_search = "/mobile/index.php?act=search&op=hot_search";

    /**
     * 退出登录接口
     */
    public static final String Exit_login = "/mobile/index.php?act=logout";
    /**
     * 平台查询
     */
    public static final String Act_search = "/mobile/index.php?act=search";
    /**
     * 获取未完成处理的订单数量
     */
    public static final String Order_amount = "/mobile/index.php?act=member_order&op=order_amount";
    /**
     * 购物车列表接口
     */
    public static final String Cart_list = "/mobile/index.php?act=member_cart&op=cart_list";
    /**
     * 购物车修改数量接口
     */
    public static final String Cart_edit_quantity = "/mobile/index.php?act=member_cart&op=cart_edit_quantity";
    /**
     * 购物车删除接口
     */
    public static final String Cart_del = "/mobile/index.php?act=member_cart&op=cart_del";
    /**
     * 购物车添加接口
     */
    public static final String Cart_add = "/mobile/index.php?act=member_cart&op=cart_add";
    /**
     * 收藏添加接口
     */
    public static final String Favorites_add = "/mobile/index.php?act=member_favorites&op=favorites_add";
    /**
     * 购买第一步接口
     */
    public static final String Buy_step1 = "/mobile/index.php?act=member_buy&op=buy_step1";
    /**
     * 购买第二步接口
     */
    public static final String Buy_step2 = "/mobile/index.php?act=member_buy&op=buy_step2";
    /**
     * 添加发票
     */
    public static final String Add_invoice = "/mobile/index.php?act=member_invoice&op=invoice_add";

    /**
     * 订单列表接口
     */
    public static final String Order_list = "/mobile/index.php?act=member_order&op=order_list";
    /**
     * 退货/退款申请列表(售后)
     */
    public static final String Refund_return_list = "/mobile/index.php?act=return_refund&op=refund_return_list";
    /**
     * 退款退货详情
     */
    public static final String Refund_return_detail = "/mobile/index.php?act=return_refund&op=view";
    /**
     * 订单详情
     */
    public static final String Order_detail = "/mobile/index.php?act=member_order&op=order_detail";
    /**
     * 查询物流
     */
    public static final String Order_deliver = "/mobile/index.php?act=member_order&op=order_deliver";
    /**
     * 订单商品评价
     */
    public static final String Add_evaluate = "/mobile/index.php?act=member_evaluate&op=add_evaluate";
    /**
     * 订单确认收货接口
     */
    public static final String Order_receive = "/mobile/index.php?act=member_order&op=order_receive";
    /**
     * 订单取消接口
     */
    public static final String Order_cancel = "/mobile/index.php?act=member_order&op=order_cancel";
    /**
     * 删除订单接口
     */
    public static final String Order_delete = "/mobile/index.php?act=member_order&op=order_delete";
    /**
     * 催促卖家发货
     */
    public static final String Order_remind = "/mobile/index.php?act=member_order&op=order_remind";
    /**
     * 酒柜商品列表
     */
    public static final String Cab_list = "/mobile/index.php?act=member_cabinet&op=cab_list";
    /**
     * 验证支付密码
     */
    public static final String Test_Paypwd = "/mobile/index.php?act=member_info&op=check_paypwd";
    /**
     * 检查地址是否支持货到付款
     */
    public static final String Check_Address_Support = "/mobile/index.php?act=member_buy&op=change_address";
    /**
     * 充值卡充值
     */
    public static final String Rechargecard_add = "/mobile/index.php?act=member_info&op=rechargecard_add";
    /**
     * 用户充值卡明细
     */
    public static final String Rcb_log_list = "/mobile/index.php?act=predeposit&op=rcb_log_list";
    /**
     * 账户余额明细
     */
    public static final String Pd_log_list = "/mobile/index.php?act=predeposit&op=pd_log_list";
    /**
     * 判断是否登录过期
     */
    public static final String Check_logo_data = "/mobile/index.php?act=member_info&op=check_logo_data";
    /**
     * 用户收到的商品
     */
    public static final String Get_goods_list = "/mobile/index.php?act=member_cabinet&op=receive_goods_list";
    /**
     * 用户发出的商品
     */
    public static final String Send_gift_list = "/mobile/index.php?act=member_cabinet&op=send_gift_list";
    /**
     * 发出礼包领取情况
     */
    public static final String Send_goods_list = "/mobile/index.php?act=member_cabinet&op=send_goods_list";
    /**
     * 赠送创建礼包
     */
    public static final String Create_gift = "/mobile/index.php?act=member_cabinet&op=create_gift";
    /**
     * 订单支付接口
     */
    public static final String Member_payment = "/mobile/index.php?act=member_order&op=pay_order";

    /**
     * 商品收藏列表接口
     */
    public static final String Collect_goods_list = "/mobile/index.php?act=member_favorites&op=fav_goods_list";
    /**
     * 收藏删除接口
     */
    public static final String Favorites_del = "/mobile/index.php?act=member_favorites&op=favorites_del";
    /**
     * 售前退款第二步
     */
    public static final String Add_refund_all_step2 = "/mobile/index.php?act=return_refund&op=add_refund_all_step2";
    /**
     * 售后退款,退货第二步
     */
    public static final String Add_refund_step2 = "/mobile/index.php?act=return_refund&op=add_refund_step2";
    /**
     * 获取支持的快递列表接口
     */
    public static final String Express_list = "/mobile/index.php?act=common&op=express_list";
    /**
     * 用户退货发货接口
     */
    public static final String Refund_express = "/mobile/index.php?act=return_refund&op=ship";
    /**
     * 售前退款第一步
     */
    public static final String Add_refund_all_step1 = "/mobile/index.php?act=return_refund&op=add_refund_all_step1";
    /**
     * 售后退款,退货第一步
     */
    public static final String Add_refund_step1 = "/mobile/index.php?act=return_refund&op=add_refund_step1";
    /**
     * 退款/退货申请列表
     */
    public static final String Return_refund_list = "/mobile/index.php?act=return_refund&op=return_refund_list";
    /**
     * 通过paySn查询赠送他人的商品列表
     */
    public static final String Gift_goods_list = "/mobile/index.php?act=member_order&op=gift_goods_list";
    /**
     * 获取附近店铺列表
     */
    public static final String Nearby_stores_list = "/mobile/index.php?act=nearby_stores&op=nearby_stores_list";
    /**
     * 定位附近店铺
     */
    public static final String Nearby_stores = "/mobile/index.php?act=nearby_stores&op=nearby_stores";
    /**
     * 获取附件店铺内商品
     */
    public static final String Store_goods = "/mobile/index.php?act=nearby_stores&op=store_goods";
    /**
     * 获取分享商品信息
     */
    public static final String Get_share_info = "/mobile/index.php?act=search&op=get_share_info";
    /**
     * 意见反馈接口
     */
    public static final String Feedback_add = "/mobile/index.php?act=member_feedback&op=feedback_add";
}
