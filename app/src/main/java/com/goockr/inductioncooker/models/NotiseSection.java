package com.goockr.inductioncooker.models;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by CMQ on 2017/6/23.
 */

public class NotiseSection  extends SectionEntity<NotiseAdapterModel> {

    private boolean isMore;
    public NotiseSection(boolean isHeader, String header, boolean isMroe) {
        super(isHeader, header);
        this.isMore = isMroe;
    }

    public NotiseSection(NotiseAdapterModel t) {
        super(t);
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean mroe) {
        isMore = mroe;
    }

}
