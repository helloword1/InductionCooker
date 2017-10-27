package com.goockr.inductioncooker.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.models.NotiseAdapterModel;
import com.goockr.inductioncooker.models.NotiseAbStractSection;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by CMQ on 2017/6/23.
 */

public class NotiseAdapter extends BaseSectionQuickAdapter<NotiseAbStractSection, BaseViewHolder> {

    private String section;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public NotiseAdapter(int layoutResId, int sectionHeadResId, List<NotiseAbStractSection> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, NotiseAbStractSection item) {
        section = "Section 1";
        if (TextUtils.equals(item.header, section))
        {
            helper.setVisible(R.id.card_view, false);
            helper.setHeight(R.id.card_view,1);
        }else {
            helper.setVisible(R.id.card_view, false);
            helper.setHeight(R.id.card_view,64);
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, NotiseAbStractSection item) {

        NotiseAdapterModel model=item.t;

        helper.setBackgroundRes(R.id.item_notise_content_iv,model.imageId);

        helper.setText(R.id.item_notise_content_title_tv,model.title);

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd HH:mm");

        String str=sdf.format(model.date);

        helper.setText(R.id.item_notise_content_date_tv,str);



    }
}
