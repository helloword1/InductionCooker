package com.goockr.inductioncooker.adapter;


import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.models.MoreAdapterModel;
import com.goockr.inductioncooker.models.MySection;

import java.util.List;

/**
 * Created by CMQ on 2017/6/22.
 */

public class MoreAdapter extends BaseSectionQuickAdapter<MySection, BaseViewHolder> {

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
    protected void convertHead(BaseViewHolder helper, final MySection item) {
        if (item.header.equals("Section 1")) {
            helper.setVisible(R.id.card_view, false);
            helper.setHeight(R.id.card_view, 1);
        } else {
            //helper.setVisible(R.id.card_view, true);
            helper.setVisible(R.id.card_view, false);
            helper.setHeight(R.id.card_view, 64);
        }
    }


    @Override
    protected void convert(BaseViewHolder helper, MySection item) {
        MoreAdapterModel model = (MoreAdapterModel) item.t;
//        switch (helper.getLayoutPosition() %
//                2) {
//            case 0:
//                helper.setImageResource(R.id.iv, R.mipmap.m_img1);
//                break;
//            case 1:
//                helper.setImageResource(R.id.iv, R.mipmap.m_img2);
//                break;
//
//        }
//        helper.setText(R.id.tv, video.getName());

        helper.setText(R.id.item_section_content_title_tv, model.title);

        helper.setText(R.id.item_section_content_des_tv, model.des);

        helper.setVisible(R.id.item_section_content_go_iv, model.isGo);


    }


}
