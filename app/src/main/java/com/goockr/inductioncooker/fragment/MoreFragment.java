package com.goockr.inductioncooker.fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.adapter.MoreAdapter;
import com.goockr.inductioncooker.models.MoreAdapterModel;
import com.goockr.inductioncooker.models.MySection;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_more, container, false);

        initData();

        mRecyclerView=(RecyclerView)view.findViewById(R.id.fragment_more_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.addItemDecoration(new SpaceItemDecoration(0));
        MoreAdapter sectionAdapter = new MoreAdapter(R.layout.item_section_content, R.layout.def_section_head, mData);

        mRecyclerView.setAdapter(sectionAdapter);

        sectionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                MySection mySection = mData.get(position);
//                if (mySection.isHeader)
//                    Toast.makeText(getActivity(), mySection.header, Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(getActivity(), mySection.t.title, Toast.LENGTH_SHORT).show();

                switch (position)
                {
                    case (1):
                       // initPermissions();
                        getActivity().startActivity(new Intent(getActivity(),CaptureActivity.class));
                        break;
                    case (3):
                        break;
                    case (4):
                        break;
                    case (5):
                        break;
                    case (6):
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

        List<MySection> list = new ArrayList<>();
        list.add(new MySection(true, "Section 1", false));
        list.add(new MySection(new MoreAdapterModel("添加设备", "",true)));
        list.add(new MySection(true, "Section 2", true));
        list.add(new MySection(new MoreAdapterModel("用户名", "xx",true)));
        list.add(new MySection(new MoreAdapterModel("修改登录密码", "",true)));
        list.add(new MySection(new MoreAdapterModel("版本", "V1.0",true)));
        list.add(new MySection(new MoreAdapterModel("厂家信息", "",true)));

        mData=list;
    }




}
