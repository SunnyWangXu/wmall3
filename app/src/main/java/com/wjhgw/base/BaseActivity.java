package com.wjhgw.base;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.wjhgw.R;
import com.wjhgw.ui.utils.SystemBarTintManager;
import com.wjhgw.ui.utils.SystemBarTintManager.SystemBarConfig;
import com.wjhgw.ui.view.NavigationActionBar;

/**
 * Created by Lance on 15/8/19.
 */
public class BaseActivity extends AppCompatActivity {
    protected NavigationActionBar navigationActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.action_bar_color));
            LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflator.inflate(R.layout.action_bar, new LinearLayout(this));
            ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            actionBar.setCustomView(v, layout);
            this.navigationActionBar = (NavigationActionBar) ((LinearLayout) actionBar.getCustomView()).getChildAt(0);
        }
//        initSystemBar();
    }

    protected void setActionBarTitle(String title) {
        this.navigationActionBar.setTitle(title);
    }

    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //使用颜色资源
            tintManager.setStatusBarTintResource(R.color.action_bar_color);
            //使用图片资源
//            tintManager.setStatusBarTintDrawable(getResources().getDrawable(R.drawable.ic_top_title_background, null));
            SystemBarConfig config = tintManager.getConfig();
        }
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onStart()
    {
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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    public void finish(boolean animation) {
        super.finish();
        if (animation) {
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        }
    }
}
