package com.chad.library.adapter.base.entity;

import java.io.Serializable;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public abstract class AbStractSectionEntity<T> implements Serializable {
    public boolean isHeader;
    public T t;
    public String header;

    public AbStractSectionEntity(boolean isHeader, String header) {
        this.isHeader = isHeader;
        this.header = header;
        this.t = null;
    }

    public AbStractSectionEntity(T t) {
        this.isHeader = false;
        this.header = null;
        this.t = t;
    }
}
