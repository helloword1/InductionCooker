package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.activity.LoginActivity;
import com.goockr.inductioncooker.activity.UpdatePwdActivity;
import com.goockr.inductioncooker.adapter.MoreAdapter;
import com.goockr.inductioncooker.models.MoreAdapterModel;
import com.goockr.inductioncooker.models.MySection;
import com.goockr.inductioncooker.utils.NotNull;
import com.goockr.inductioncooker.utils.SharePreferencesUtils;
import com.goockr.ui.view.activity.CompanyIntroduceActivity;
import com.jinlin.zxing.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by CMQ on 2017/6/21.
 */

public class MoreFragment extends Fragment {

    View view;
    @BindView(R.id.fragment_more_rv)
    RecyclerView mRecyclerView;

    private List<MySection> mData;
    private MoreAdapter sectionAdapter;

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_more, container, false);
        mData = new ArrayList<>();


        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_more_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        sectionAdapter = new MoreAdapter(R.layout.item_section_content, R.layout.def_section_head, mData);
        mRecyclerView.setAdapter(sectionAdapter);

        sectionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case (1):
                        // initPermissions();
                        getActivity().startActivity(new Intent(getActivity(), CaptureActivity.class));
                        break;
                    case (3):
                        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                        break;
                    case (4):
                        getActivity().startActivity(new Intent(getActivity(), UpdatePwdActivity.class));
                        break;
                    case (5):
                        break;
                    case (6):

                        getActivity().startActivity(new Intent(getActivity(), CompanyIntroduceActivity.class));

                        break;


                }
            }
        });
        sectionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //Toast.makeText(getActivity(), "onItemChildClick" + position, Toast.LENGTH_SHORT).show();


            }
        });

        return view;
    }

    private void initData() {
        if (mData.size() != 0) {
            mData.clear();
        }
        List<MySection> list = new ArrayList<>();
        list.add(new MySection(true, "Section 1", false));
        list.add(new MySection(new MoreAdapterModel("添加设备", "", true)));
        list.add(new MySection(true, "Section 2", true));
        String userName = SharePreferencesUtils.getUserName();
        if (!NotNull.isNotNull(userName)) {
            userName = SharePreferencesUtils.getMobile();
        }
        list.add(new MySection(new MoreAdapterModel("用户名", userName, true)));
        list.add(new MySection(new MoreAdapterModel("修改登录密码", "", true)));
        list.add(new MySection(new MoreAdapterModel("版本", "V1.0", true)));
        list.add(new MySection(new MoreAdapterModel("厂家信息", "", true)));
        mData.addAll(list);
        sectionAdapter.notifyDataSetChanged();
    }

}
