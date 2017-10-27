package com.goockr.inductioncooker.models;

import com.chad.library.adapter.base.entity.AbStractSectionEntity;

/**
 * Created by CMQ on 2017/6/23.
 */

public class NotiseAbStractSection extends AbStractSectionEntity<NotiseAdapterModel> {

    private boolean isMore;
    public NotiseAbStractSection(boolean isHeader, String header, boolean isMroe) {
        super(isHeader, header);
        this.isMore = isMroe;
    }

    public NotiseAbStractSection(NotiseAdapterModel t) {
        super(t);
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean mroe) {
        isMore = mroe;
    }

}
