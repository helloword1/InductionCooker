package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.adapter.MoreAdapter;
import com.goockr.inductioncooker.adapter.NotiseAdapter;
import com.goockr.inductioncooker.models.MoreAdapterModel;
import com.goockr.inductioncooker.models.MySection;
import com.goockr.inductioncooker.models.NotiseAdapterModel;
import com.goockr.inductioncooker.models.NotiseModel;
import com.goockr.inductioncooker.models.NotiseSection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Created by CMQ on 2017/6/21.
 */

public class NoticeFragment extends Fragment {

    View view;

  //  @BindView(R.id.fragment_notice_rv)
    RecyclerView mRecyclerView;
    private NotiseAdapter adapter;

    private List<NotiseSection> mData;

    private static String TAG = "NoticeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_notice, container, false);

        initData();

        initUI();

        return view;
    }

    private void initData() {

        List<NotiseSection> list = new ArrayList<>();
        list.add(new NotiseSection(true, "Section 1", false));

        list.add(new NotiseSection(new NotiseAdapterModel(R.mipmap.notice_icon_avatar_default,"有人在限用时间内打开电磁炉！", new Date())));
        list.add(new NotiseSection(new NotiseAdapterModel(R.mipmap.notice_icon_avatar_default2,"电磁炉温度超出限制温度！", new Date())));
        mData=list;

    }

    private void initUI() {

       mRecyclerView=(RecyclerView)view.findViewById(R.id.fragment_notice_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.addItemDecoration(new SpaceItemDecoration(0));
        NotiseAdapter sectionAdapter = new NotiseAdapter(R.layout.item_notise_content, R.layout.def_section_head, mData);

        mRecyclerView.setAdapter(sectionAdapter);

        sectionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NotiseSection mySection = mData.get(position);
                if (mySection.isHeader)
                    Toast.makeText(getActivity(), mySection.header, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), mySection.t.title, Toast.LENGTH_SHORT).show();
            }
        });
        sectionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(getActivity(), "onItemChildClick" + position, Toast.LENGTH_SHORT).show();
            }
        });

    }



}
