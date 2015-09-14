package com.wjhgw;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import com.wjhgw.base.BaseActivity;
import com.wjhgw.ui.fragment.ClassificationFragment;
import com.wjhgw.ui.fragment.DiscoveryFragment;
import com.wjhgw.ui.fragment.IndexFragment;
import com.wjhgw.ui.fragment.MessageFragment;
import com.wjhgw.ui.fragment.MineFragment;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{
    private FragmentTransaction ft;
    private MessageFragment messageFragment;
    private ClassificationFragment classificationFragment;
    private IndexFragment indexFragment;
    private DiscoveryFragment discoveryFragment;
    private MineFragment mineFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioGroup group = (RadioGroup) findViewById(R.id.radio_group);
        group.setOnCheckedChangeListener(this);

        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
        indexFragment = new IndexFragment();
        ft2.replace(R.id.content, indexFragment).commit();
        //this.setActionBarTitle("万嘉欢购");
        this.HideActionBar();
    }

    //RadioButton 点击事件
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        ft = getSupportFragmentManager().beginTransaction();
        switch (checkedId) {
            case R.id.home_button:
                if (indexFragment == null) {
                    indexFragment = new IndexFragment();
                }
                ft.replace(R.id.content, indexFragment).commit();
                break;

            case R.id.category_button:
                if (classificationFragment == null) {
                    classificationFragment = new ClassificationFragment();
                }
                ft.replace(R.id.content, classificationFragment).commit();
                break;

            case R.id.found_button:
                if (discoveryFragment == null) {
                    discoveryFragment = new DiscoveryFragment();
                }
                ft.replace(R.id.content, discoveryFragment).commit();
                break;

            case R.id.by_button:
            if (messageFragment == null) {
                messageFragment = new MessageFragment();
            }

            ft.replace(R.id.content, messageFragment).commit();
            break;
            case R.id.my_button:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                }
                ft.replace(R.id.content, mineFragment).commit();
                break;

            default:
                break;
        }

    }
}
