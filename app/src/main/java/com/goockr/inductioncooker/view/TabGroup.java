package com.goockr.inductioncooker.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class TabGroup extends LinearLayout {

	public TabGroup(Context context, AttributeSet attrs, int defStyleAttr,
					int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	public TabGroup(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public TabGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TabGroup(Context context) {
		super(context);
		init();
	}

	private void init() {
		setOrientation(HORIZONTAL);
	}

	int mCheckedId = -1;
	OnTabGroupCheckedListener onTabGroupCheckedListener;

	public void setOnTabGroupCheckedListener(
			OnTabGroupCheckedListener onTabGroupCheckedListener) {
		this.onTabGroupCheckedListener = onTabGroupCheckedListener;
	}

	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		if (child instanceof TabItem) {
			final TabItem tab = (TabItem) child;
			if (tab.isChecked()) {
				check(tab.getId());
			}
		}
		super.addView(child, index, params);
	}

	public void check(int checkId) {
		if (mCheckedId == checkId) {
			return;
		}
		setCheckedStateForView(mCheckedId, false);
		setCheckedId(checkId);
		mCheckedId = checkId;
		if (onTabGroupCheckedListener != null)
		{
			onTabGroupCheckedListener.onChecked(checkId);
		}
	}

	private void setCheckedId(int id) {
		View checkedView = findViewById(id);
		if (checkedView != null && checkedView instanceof TabItem) {
			((TabItem) checkedView).setChecked(true);
		}
	}

	private void setCheckedStateForView(int viewId, boolean checked) {
		View checkedView = findViewById(viewId);
		if (checkedView != null && checkedView instanceof TabItem) {
			((TabItem) checkedView).setChecked(checked);
		}
	}

	public interface OnTabGroupCheckedListener {
		public void onChecked(int checkedId);
	}
}
