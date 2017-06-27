package com.goockr.inductioncooker.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.models.MoreAdapterModel;
import com.goockr.inductioncooker.models.MySection;
import com.goockr.inductioncooker.models.NotiseAdapterModel;
import com.goockr.inductioncooker.models.NotiseModel;
import com.goockr.inductioncooker.models.NotiseSection;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by CMQ on 2017/6/23.
 */

public class NotiseAdapter extends BaseSectionQuickAdapter<NotiseSection, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public NotiseAdapter(int layoutResId, int sectionHeadResId, List<NotiseSection> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, NotiseSection item) {
        if (item.header.equals("Section 1"))
        {
            helper.setVisible(R.id.card_view, false);
            helper.setHeight(R.id.card_view,1);
        }else {
            //helper.setVisible(R.id.card_view, true);
            helper.setVisible(R.id.card_view, false);
            helper.setHeight(R.id.card_view,64);
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, NotiseSection item) {

        NotiseAdapterModel model=(NotiseAdapterModel)item.t;

        helper.setBackgroundRes(R.id.item_notise_content_iv,model.imageId);

        helper.setText(R.id.item_notise_content_title_tv,model.title);

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd HH:mm");

        String str=sdf.format(model.date);

        helper.setText(R.id.item_notise_content_date_tv,str);



    }
}
