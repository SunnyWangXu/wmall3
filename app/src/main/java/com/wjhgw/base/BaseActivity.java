package com.wjhgw.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wjhgw.R;

/**
 * Created by Lance on 15/8/19.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
