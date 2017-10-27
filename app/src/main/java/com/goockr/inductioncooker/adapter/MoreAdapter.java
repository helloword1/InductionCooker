package com.goockr.inductioncooker.adapter;


import android.text.TextUtils;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.models.MoreAdapterModel;
import com.goockr.inductioncooker.models.MyAbStractSection;

import java.util.List;

/**
 * Created by CMQ on 2017/6/22.
 */

public class MoreAdapter extends BaseSectionQuickAdapter<MyAbStractSection, BaseViewHolder> {

    private final String section = "Section 1";
    private int anInt;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param sectionHeadResId The section head layout id for each item
     * @param layoutResId      The layout resource id of each item.
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public MoreAdapter(int layoutResId, int sectionHeadResId, List data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, final MyAbStractSection item) {
        if (TextUtils.equals(item.header, section)) {
            helper.setVisible(R.id.card_view, false);
            helper.setHeight(R.id.card_view, 1);
        } else {
            helper.setVisible(R.id.card_view, false);
            helper.setHeight(R.id.card_view, 64);
        }
    }


    @Override
    protected void convert(BaseViewHolder helper, MyAbStractSection item) {
        MoreAdapterModel model = item.t;
        anInt = 3;
        if (helper.getLayoutPosition() == anInt) {
            helper.setVisible(R.id.lineTop, true);
        } else {
            helper.setVisible(R.id.lineTop, false);
        }
        helper.setText(R.id.item_section_content_title_tv, model.title);

        helper.setText(R.id.item_section_content_des_tv, model.des);

        helper.setVisible(R.id.item_section_content_go_iv, model.isGo);
    }


}
