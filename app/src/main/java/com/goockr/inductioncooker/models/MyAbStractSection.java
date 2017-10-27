package com.goockr.inductioncooker.models;

import com.chad.library.adapter.base.entity.AbStractSectionEntity;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public class MyAbStractSection extends AbStractSectionEntity<MoreAdapterModel> {
    private boolean isMore;

    public MyAbStractSection(boolean isHeader, String header, boolean isMroe) {
        super(isHeader, header);
        this.isMore = isMroe;
    }

    public MyAbStractSection(MoreAdapterModel t) {
        super(t);
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean mroe) {
        isMore = mroe;
    }
}
