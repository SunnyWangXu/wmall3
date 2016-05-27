package com.wjhgw.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wjhgw.R;
import com.wjhgw.base.BaseQuery;
import com.wjhgw.ui.activity.CurrencyWebViewActivity;
import com.wjhgw.ui.activity.F0_Shop_nearbyActivity;

/**
 * 发现Fragment
 */
public class FindFragment extends Fragment {
    private View rootView;
    private FrameLayout fl_redeem_code;
    private FrameLayout fl_shop_nearby;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.find_layout, container, false);

        fl_redeem_code = (FrameLayout)rootView.findViewById(R.id.fl_redeem_code);
        fl_redeem_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CurrencyWebViewActivity.class);
                intent.putExtra("name" , "礼品兑换码");
                intent.putExtra("url" , BaseQuery.serviceUrl()+"/wap/index.php?act=exchange&op=code");
                startActivity(intent);
            }
        });
        fl_shop_nearby = (FrameLayout)rootView.findViewById(R.id.fl_shop_nearby);
        fl_shop_nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),F0_Shop_nearbyActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
