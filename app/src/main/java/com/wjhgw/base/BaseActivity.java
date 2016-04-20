package com.wjhgw.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.wjhgw.R;
import com.wjhgw.ui.activity.A0_LoginActivity;
import com.wjhgw.ui.dialog.LoadDialog;

/**
 * Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    private LoadDialog Dialog = new LoadDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 隐藏自带标题栏
         */
        this.getSupportActionBar().hide();
    }

    /**
     * 标题返回上一页
     */
    public void setUp() {
        setUpOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish(false);
            }
        });
    }

    public void setUpOnClickListener(View.OnClickListener listener) {
        View up = findViewById(R.id.iv_title_back);
        if (up != null) {
            up.setVisibility(View.VISIBLE);
            up.setOnClickListener(listener);
        }
    }

    /**
     * 设置标题文字
     *
     * @param title
     */
    public void setTitle(String title) {
        TextView tvTitle = (TextView) findViewById(R.id.tv_title_name);
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    /**
     * 拿到登陆Key
     *
     * @return
     */
    public String getKey() {
        return this.getSharedPreferences("key", this.MODE_APPEND).getString("key", "0");
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        // 调用顺序
        onInit();
        onFindViews();
        onInitViewData();
        onBindListener();
    }

    /**
     * 初始化 优先顺序：<br/>
     * <font color=red>onInit();</font><br/>
     * onFindViews();<br/>
     * onInitViewData();<br/>
     * onBindListener();<br/>
     */
    public abstract void onInit();

    /**
     * 查找控件 <br/>
     * 优先顺序：<br/>
     * onInit();<br/>
     * <font color=red>onFindViews();</font><br/>
     * onInitViewData();<br/>
     * onBindListener();<br/>
     */
    public abstract void onFindViews();

    /**
     * 初始化控件内容 优先顺序：<br/>
     * onInit();<br/>
     * onFindViews();<br/>
     * <font color=red>onInitViewData();</font><br/>
     * onBindListener();<br/>
     */
    public abstract void onInitViewData();

    /**
     * 注册控件事件 优先顺序：<br/>
     * onInit();<br/>
     * onFindViews();<br/>
     * onInitViewData();<br/>
     * <font color=red>onBindListener();</font><br/>
     */
    public abstract void onBindListener();

    /**
     * 打开滑动退出此Activity的功能 <功能详细描述>
     *
     * @param
     * @see [类、类#方法、类#成员]
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {

        super.onResume();
        /**
         * 友盟统计
         */
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {

        super.onPause();
        /**
         * 友盟统计
         */
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        /**
         * 让其不再保存Fragment的状态，达到fragment随MyActivity一起销毁的目的。
         */
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.push_right_in,
                R.anim.push_right_out);
    }
    /* public void startActivityForResult(Intent intent, int requestCode)
     {
         super.startActivityForResult(intent,requestCode);
         overridePendingTransition(R.anim.push_right_in,
                 R.anim.push_right_out);
     }*/

    @Override
    public void finish() {
        this.finish(true);
    }

    /**
     * 关闭进度条
     */
    public void Dismiss() {
        Dialog.dismiss();
    }

    /**
     * 开启进度条
     */
    public void StartLoading() {
        Dialog.ProgressDialog();
    }


    public void finish(boolean animation) {
        super.finish();
        if (animation) {
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }

    /**
     * 短提示
     *
     * @param message
     */
    public void showToastShort(String message) {
        if (message.equals("网络错误") || message.equals("请求失败")) {

        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 超时判断
     */
    public void overtime(int code, String msg) {
        if (code == 200103 || code == 200104) {
            Toast.makeText(this, "登录超时或未登录", Toast.LENGTH_SHORT).show();
            getSharedPreferences("key", MODE_APPEND).edit().putString("key", "0").commit();
            startActivity(new Intent(this, A0_LoginActivity.class));
        } else {
            showToastShort(msg);
        }
    }

    /**
     * 长提示
     *
     * @param message
     */
    public void showToastLong(String message) {
        if (message.equals("网络错误") || message.equals("请求失败")) {

        } else {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

    }
}
