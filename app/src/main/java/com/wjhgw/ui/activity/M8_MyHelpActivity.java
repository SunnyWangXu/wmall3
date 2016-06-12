package com.wjhgw.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
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
import com.wjhgw.business.bean.FeedBack;
import com.wjhgw.business.bean.Nickname;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.dialog.FeedBackTypeDialog;
import com.wjhgw.ui.dialog.GalleryDialog;
import com.wjhgw.utils.GalleryConstants;
import com.wjhgw.utils.GalleryUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

/**
 * 意见反馈Activity
 */
public class M8_MyHelpActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llFeedBackType;
    private TextView tvFeedBackType;
    private FeedBackTypeDialog feedBackTypeDialog;
    private GalleryDialog mDialog;
    private ImageView ivFeedback;
    private Bitmap bitmap; //上传的图片
    private LinearLayout llFeedbackCommit;
    private EditText edFeedbackContent;
    private EditText edFeedbackPhone;
    private String feedback_image = "";
    private int feedback_type = 0;
    private ImageView ivDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        mDialog = new GalleryDialog(this);
    }

    @Override
    public void onInit() {
        setUp();
        setTitle("意见反馈");
    }

    @Override
    public void onFindViews() {
        llFeedBackType = (LinearLayout) findViewById(R.id.ll_feedback_type);
        tvFeedBackType = (TextView) findViewById(R.id.tv_feedback_type);
        edFeedbackContent = (EditText) findViewById(R.id.ed_feedback_content);
        edFeedbackPhone = (EditText) findViewById(R.id.ed_feedback_phone);
        ivFeedback = (ImageView) findViewById(R.id.iv_feedback);
        ivDelete = (ImageView) findViewById(R.id.iv_feedback_delete);
        llFeedbackCommit = (LinearLayout) findViewById(R.id.ll_feedback_commit);
    }

    @Override
    public void onInitViewData() {

    }

    @Override
    public void onBindListener() {
        llFeedBackType.setOnClickListener(this);
        ivFeedback.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        llFeedbackCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_feedback_type:
                feedback_type++;
                feedBackTypeDialog = new FeedBackTypeDialog(this, tvFeedBackType);
                feedBackTypeDialog.show();

                break;

            case R.id.iv_feedback:
                mDialog.Get_pictures_Dialog();

                break;

            case R.id.iv_feedback_delete:
                /**
                 * 删除意见反馈图片
                 */
                deleteImage();

                break;

            case R.id.ll_feedback_commit:
                if (edFeedbackContent.getText().length() == 0) {

                    showToastShort("请输入您的宝贵意见");
                } else {
                    /**
                     * 请求反馈意见
                     */
                    loadFeedBackAdd();
                }
                break;
            default:
                break;
        }

    }

    /**
     * 删除意见反馈图片
     */
    private void deleteImage() {
        StartLoading();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        params.addBodyParameter("usage_number", "2");
        if (feedback_image != "") {
            params.addBodyParameter("img_name", feedback_image);
        }
        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Del_img, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                Dismiss();
                if (responseInfo.result != null) {
                    Nickname nickname = gson.fromJson(responseInfo.result, Nickname.class);
                    if (nickname.status.code == 10000) {
                        ivFeedback.setImageResource(R.mipmap.ic_upload);
                        ivDelete.setVisibility(View.GONE);

                    } else {
                        overtime(nickname.status.code, nickname.status.msg);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }


    /**
     * 请求反馈意见
     */
    private void loadFeedBackAdd() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("key", getKey());
        if (edFeedbackContent.getText().toString().length() != 0) {
            params.addBodyParameter("content", edFeedbackContent.getText().toString());
        }
        if (feedback_type == 0) {
            params.addBodyParameter("feedback_type", "1");
        } else {
            params.addBodyParameter("feedback_type", feedBackTypeDialog.MarkType);
        }
        params.addBodyParameter("feedback_image", feedback_image);
        params.addBodyParameter("con_info", edFeedbackPhone.getText().toString());

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Feedback_add, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                FeedBack feedBack = gson.fromJson(responseInfo.result, FeedBack.class);
                if (feedBack.status.code == 10000) {
                    showToastShort("提交成功，感谢您的宝贵意见");
                    finish();
                } else {
                    overtime(feedBack.status.code, feedBack.status.msg);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            bitmap = data.getParcelableExtra("data");

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
                    StartLoading();
                    RequestParams params = new RequestParams();
                    params.addBodyParameter("key", getKey());
                    params.addBodyParameter("usage_number", "2");
                    params.addBodyParameter("data", input, tempFile.length(), sur);
                    /**
                     * 上传反馈意见图片
                     */
                    loadFeedBackImage(params);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 上传反馈意见图片
     */
    private void loadFeedBackImage(RequestParams params) {

        APP.getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, BaseQuery.serviceUrl() + ApiInterface.Member_image, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new Gson();
                Dismiss();
                if (responseInfo.result != null) {
                    Nickname nickname = gson.fromJson(responseInfo.result, Nickname.class);
                    if (nickname.status.code == 10000) {
                        ivFeedback.setImageBitmap(bitmap);
                        ivDelete.setVisibility(View.VISIBLE);

                        feedback_image = nickname.datas;
                    } else {
                        overtime(nickname.status.code, nickname.status.msg);
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }
}
