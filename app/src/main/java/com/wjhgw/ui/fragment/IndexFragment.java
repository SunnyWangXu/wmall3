package com.wjhgw.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.wjhgw.R;
import com.wjhgw.business.api.Goods_Request;
import com.wjhgw.business.response.BusinessResponse;
import com.wjhgw.config.ApiInterface;
import com.wjhgw.ui.image.LoadImageByVolley;
import com.wjhgw.ui.view.listview.MyListView;
import com.wjhgw.ui.view.listview.XListView.IXListViewListener;
import com.wjhgw.ui.view.listview.adapter.IndexPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

public class IndexFragment extends Fragment implements BusinessResponse, IXListViewListener,
        View.OnClickListener, RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private View messageLayout;
    private LinearLayout MenuLayout;
    private ImageView mImageView;
    private LoadImageByVolley load;
    private Goods_Request dataModel;
    private MyListView mListView;
    private RelativeLayout indexViewPageLayout;
    private ViewPager indexPager;
    private IndexPagerAdapter mPagerAdapter;
    private RadioGroup group;
    private static final int HANDLERID = 1;
    private Handler handler;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        messageLayout = inflater.inflate(R.layout.index_layout, container, false);
        /**
         * 加载视图
         */
        setInflaterView();

        dataModel = new Goods_Request(getActivity());
        dataModel.addResponseListener(this);

        indexPager.addOnPageChangeListener(this);
        group.setOnCheckedChangeListener(this);

//        new Handler().sendMessageDelayed();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == HANDLERID) {

                    indexPager.setCurrentItem(indexPager.getCurrentItem() + 1);

                    sendEmptyMessageDelayed(HANDLERID, 3000);
                }
            }
        };

        listAddHeader();

        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this, 1);
        mListView.setRefreshTime();
        mListView.setAdapter(null);

        return messageLayout;
    }

    /**
     * 给ListView添加视图
     */
    private void listAddHeader() {
        mListView = (MyListView) messageLayout.findViewById(R.id.index_listview);
        mListView.addHeaderView(indexViewPageLayout);
        mListView.addHeaderView(MenuLayout);
    }

    /**
     * 视图加载 要添加的视图
     */

    private void setInflaterView() {
        MenuLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.index_menu, null);
        indexViewPageLayout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.index_page_layout, null);
        indexPager = (ViewPager) indexViewPageLayout.findViewById(R.id.pager);
        mPagerAdapter = new IndexPagerAdapter(getActivity());
        indexPager.setAdapter(mPagerAdapter);
        group = (RadioGroup) indexViewPageLayout.findViewById(R.id.radio_group);
    }

    @Override
    public void OnMessageResponse(String url, String response, JSONObject status) throws JSONException {
        if (url.equals(ApiInterface.Goods_list)) {
            /*if (status == null){
                ArrayList<goods_list_data> list= dataModel.goodsList;
				String goodslist = list.get(1).goods_name;
				String s = list.get(1).goods_id;
				String s1 = list.get(1).goods_id;
			}else{
				if (status.getString("code").equals("10000")) {
					ArrayList<goods_list_data> list= dataModel.goodsList;
					String goodslist = list.get(1).goods_name;
					String s = list.get(1).goods_id;
					Toast.makeText(getActivity(), status.getString("msg"), Toast.LENGTH_LONG).show();
				} else if (status.getString("code").equals("600100")) {
					Toast.makeText(getActivity(), status.getString("msg"), Toast.LENGTH_LONG).show();
				}
			}*/

        }
    }

    @Override
    public void onRefresh(int id) {

    }

    @Override
    public void onLoadMore(int id) {

    }

    @Override
    public void onClick(View v) {

    }

    //RadioGroup监听
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == indexPager.getCurrentItem()) {
            indexPager.setCurrentItem(checkedId);
        }
    }

    //ViewPager 监听
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int[] pointIDs = {R.id.point_1, R.id.point_2, R.id.point_3, R.id.point_4};
        group.check(pointIDs[position % 4]);

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onStart() {
        super.onStart();
        handler.sendEmptyMessageDelayed(HANDLERID, 3000);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(HANDLERID);
    }
}
