package com.wjhgw.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.wjhgw.APP;
import com.wjhgw.R;
import com.wjhgw.base.BaseActivity;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.business.bean.MyLockBox;
import com.wjhgw.business.bean.MyLockBoxData;
import com.wjhgw.business.bean.Nickname;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.dialog.GalleryDialog;
import com.wjhgw.ui.dialog.LoadDialog;
import com.wjhgw.utils.GalleryConstants;
import com.wjhgw.utils.GalleryUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;

/**
 * 个人资料管理Activity
 */
public class M0_MyLockBoxActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_nickname;
    private LinearLayout ll_picture;
    private LinearLayout ll_change_password;
    private LinearLayout ll_payment_password;
    private LinearLayout ll_mobile;
    private TextView tv_Nickname;
    private Intent intent;
    private GalleryDialog Dialog;
    private LoadDialog loadDialog;
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    private ImageView iv_Avatar;
    private TextView tv_Passwd_Strength;
    private TextView tv_Paypwd;
    private TextView tv_Mobile;
    private LinearLayout ll_Manage_Addres;
    private String paypwd = "0";
    private MyLockBoxData myLockBoxData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m0_my_lockbox_layout);

        Dialog = new GalleryDialog(this);
        loadDialog = new LoadDialog(this);

    }

    @Override
    public void onInit() {
        //finish
        setUp();
        setTitle("个人资料管理");
    }

    @Override
    public void onFindViews() {
        ll_nickname = (LinearLayout) findViewById(R.id.ll_nickname);
        ll_picture = (LinearLayout) findViewById(R.id.ll_picture);
        ll_change_password = (LinearLayout) findViewById(R.id.ll_change_password);
        ll_payment_password = (LinearLayout) findViewById(R.id.ll_payment_password);
        ll_mobile = (LinearLayout) findViewById(R.id.ll_mobile);

        ll_Manage_Addres = (LinearLayout) findViewById(R.id.ll_manage_address);

        iv_Avatar = (ImageView) findViewById(R.id.iv_avatar);
        tv_Nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_Passwd_Strength = (TextView) findViewById(R.id.tv_passwd_strength);
        tv_Paypwd = (TextView) findViewById(R.id.tv_paypwd);
        tv_Mobile = (TextView) findViewById(R.id.tv_mobile);


    }

    @Override
    public void onInitViewData() {


    }

    @Override
    public void onBindListener() {
        ll_nickname.setOnClickListener(this);
        ll_picture.setOnClickListener(this);
        ll_Manage_Addres.setOnClickListener(this);
        ll_change_password.setOnClickListener(this);
        ll_payment_password.setOnClickListener(this);
        ll_mobile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_nickname:
                intent = new Intent(this, M1_Modify_nicknameActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_change_password:
                intent = new Intent(this, M3_ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_manage_address:
                intent = new Intent(this, M2_Manage_Address_Activity.class);
                startActivity(intent);
                break;
            case R.id.ll_payment_password:
                if (myLockBoxData.member_mobile.length() > 0) {
                    intent = new Intent(this, VerificationCodeActivity.class);
                    intent.putExtra("Number", myLockBoxData.member_mobile);
                    intent.putExtra("use", "2");
                    intent.putExtra("paypwd", paypwd);
                    startActivity(intent);
                }
                break;
            case R.id.ll_mobile:
                if (myLockBoxData.member_mobile_bind.equals("0")) {
                    Intent intent1 = new Intent(this,A4_BindPhoneAcitivty.class);
                    intent1.putExtra("member_name", myLockBoxData.member_name);
                    startActivity(intent1);
                }else {
                    intent = new Intent(this, VerificationCodeActivity.class);
                    intent.putExtra("Number", myLockBoxData.member_mobile);
                    intent.putExtra("use", "3");
                    startActivity(intent);
                }
                break;
            case R.id.ll_picture:
                Dialog.Get_pictures_Dialog();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == GalleryConstants.PHOTO_RESOULT) {
            File picture = new File(Environment.getExternalStorageDirectory()
                    + "/temp.jpg");
            GalleryUtils.getInstance().cropPicture(this, Uri.fromFile(picture));
        }
        if (null == data) {
            return;
        }
        Uri uri = null;
        if (requestCode == GalleryConstants.KITKAT_LESS) {
            uri = data.getData();
            // 调用裁剪方法
            GalleryUtils.getInstance().cropPicture(this, uri);
        } else if (requestCode == GalleryConstants.KITKAT_ABOVE) {
            uri = data.getData();
            // 先将这个uri转换为path，然后再转换为uri
            String thePath = GalleryUtils.getInstance().getPath(this, uri);
            GalleryUtils.getInstance().cropPicture(this,
                    Uri.fromFile(new File(thePath)));
        } else if (requestCode == GalleryConstants.INTENT_CROP) {
            Bitmap bitmap = data.getParcelableExtra("data");

            File temp = new File(Environment.getExternalStorageDirectory()
                    .getPath() + "/wjhg/");// 自已缓存文件夹
            if (!temp.exists()) {
                temp.mkdir();
            }
            File tempFile = new File(temp.getAbsolutePath() + "/"
                    + Calendar.getInstance().getTimeInMillis() + ".jpg"); // 以时间秒为文件名

            // 图像保存到文件中
            FileOutputStream foutput = null;
            try {
                foutput = new FileOutputStream(tempFile);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, foutput)) {
                    String imgPath = tempFile.getAbsolutePath();
                    String sur = tempFile.getName();
                    FileInputStream input = new FileInputStream(imgPath);
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("key", this.getSharedPreferences("key", MODE_APPEND).getString("key", "0"));
                    params.addBodyParameter("data", input, tempFile.length(), sur);
                    /**
                     * 上传用户头像
                     */
                    load_member_image(params);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传用户头像
     */
    private void load_member_image(RequestParams params) {
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Member_image, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (responseInfo.result != null) {
                    Nickname nickname = gson.fromJson(responseInfo.result, Nickname.class);
                    if (nickname.status.code == 10000) {
                        showToastLong(nickname.status.msg);
                    }else {
                        overtime(nickname.status.code, nickname.status.msg);
                    }
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToastLong("网络错误");
            }
        });
    }

    /**
     * 把Bitmap转Byte
     */
    private InputStream Bitmap2IS(Bitmap bm) {
        baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
        return sbs;
    }

    /**
     * 请求个人资料
     */
    private void loadMyLockBox() {
        loadDialog.ProgressDialog();
        String key = getSharedPreferences("key", MODE_APPEND).getString("key", "0");
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", key);

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.MyLockBox, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                if (null != responseInfo) {
                    MyLockBox myLockBox = gson.fromJson(responseInfo.result, MyLockBox.class);
                    loadDialog.dismiss();
                    if (myLockBox.status.code == 10000) {
                        myLockBoxData = myLockBox.datas;
                        APP.getApp().getImageLoader().displayImage(myLockBoxData.member_avatar, iv_Avatar);
                        tv_Nickname.setText(myLockBoxData.member_nickname);
                        if (myLockBoxData.passwd_strength.equals("0")) {
                            tv_Passwd_Strength.setText("弱");
                        } else if (myLockBoxData.passwd_strength.equals("1")) {
                            tv_Passwd_Strength.setText("中");
                        } else if (myLockBoxData.passwd_strength.equals("2")) {
                            tv_Passwd_Strength.setText("强");
                        }
                        paypwd = myLockBoxData.paypwd;
                        if (myLockBoxData.paypwd.equals("0")) {
                            tv_Paypwd.setText("未设置");
                        } else {
                            tv_Paypwd.setText("已设置");
                        }
                        if(myLockBoxData.member_mobile_bind.equals("0")){
                            tv_Mobile.setText("未绑定");
                        }else {
                            tv_Mobile.setText(myLockBoxData.member_mobile);
                        }

                    }  else {
                        overtime(myLockBox.status.code, myLockBox.status.msg);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToastShort("网络错误");
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 请求个人资料
         */
        loadMyLockBox();
    }
}