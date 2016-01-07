package com.wjhgw.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wjhgw.R;
import com.wjhgw.ui.activity.J0_SelectGiveObjectActivity;

/**
 * 酒柜Fragment
 */
public class CabinetFragment extends Fragment implements View.OnClickListener {

    private TextView tvGivePeople;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View View = inflater.inflate(R.layout.cabinet_layout, null);

        tvGivePeople = (TextView) View.findViewById(R.id.tv_give_people);
        tvGivePeople.setOnClickListener(this);

        return View;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_give_people:

                intent.setClass(getActivity(), J0_SelectGiveObjectActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
