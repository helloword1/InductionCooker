package com.goockr.inductioncooker.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.adapter.NotiseAdapter;
import com.goockr.inductioncooker.lib.observer.NoticeObserval;
import com.goockr.inductioncooker.lib.observer.NoticeObserver;
import com.goockr.inductioncooker.models.NotiseAbStractSection;
import com.goockr.inductioncooker.models.NotiseAdapterModel;
import com.goockr.inductioncooker.utils.FileCache;
import com.goockr.inductioncooker.utils.NotNull;
import com.goockr.inductioncooker.utils.PreferencesUitls;
import com.goockr.inductioncooker.view.DialogView;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.goockr.inductioncooker.common.Common.NOTICE_COUNT;

/**
 * Created by CMQ on 2017/6/21.
 */

public class NoticeFragment extends Fragment implements NoticeObserver {
    View view;
    SwipeMenuRecyclerView mRecyclerView;
    private List<NotiseAbStractSection> mData;
    private NotiseAdapter sectionAdapter;
    private PercentRelativeLayout notiPercent;
    private int[] drablwIcon = {R.mipmap.notice_icon_avatar_1, R.mipmap.notice_icon_avatar_2, R.mipmap.notice_icon_avatar_3,
            R.mipmap.notice_icon_avatar_4, R.mipmap.notice_icon_avatar_5, R.mipmap.notice_icon_avatar_6, R.mipmap.notice_icon_avatar_7};
    private final String SAVE_ITEM = "NOTICE_JSON";
    private FileCache fileCache;
    private JSONArray notice_json;
    private String currentNotice;
    private TextView textView;
    private int unLookCount = 0;
    private onAlertListener listener;
    private PreferencesUitls instance;
    private int length;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notice, container, false);
        initData();
        initUI();
        return view;
    }

    private void initData() {
        fileCache = FileCache.get(getActivity());
        instance = PreferencesUitls.getInstance(getActivity());

        mData = new ArrayList<>();
    }

    private void initUI() {
        getNOticeData();
        mRecyclerView = (SwipeMenuRecyclerView) view.findViewById(R.id.fragment_notice_rv);
        notiPercent = (PercentRelativeLayout) view.findViewById(R.id.notiPercent);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.useDefaultLoadMore(); // 使用默认的加载更多的View。
        mRecyclerView.setLoadMoreListener(new SwipeMenuRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
            }
        });

        sectionAdapter = new NotiseAdapter(R.layout.item_notise_content, R.layout.def_section_head, mData);
        sectionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NotiseAbStractSection mySection = mData.get(position);
                if (mySection.isHeader) {
                    Toast.makeText(getActivity(), mySection.header, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), mySection.t.title, Toast.LENGTH_SHORT).show();
                }
            }
        });
        sectionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(getActivity(), "onItemChildClick" + position, Toast.LENGTH_SHORT).show();
            }
        });
        // 菜单点击监听。
        mRecyclerView.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                menuBridge.closeMenu();
                // 左侧还是右侧菜单。
                int direction = menuBridge.getDirection();
                if (direction == -1) {//删除
                    int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                    int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                    showTurnOn("是否删除此通知", adapterPosition);
                }
            }
        });
        // 设置监听器。
        mRecyclerView.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
                rightMenu.addMenuItem(new SwipeMenuItem(getActivity()).setText("删除").
                        setTextColor(getActivity().getResources().getColor(R.color.white)).setHeight(MATCH_PARENT).setWidth(180).setBackgroundColorResource(R.color.colorRed).setTextSize(15)); // 在Item右侧添加一个菜单。
            }
        });
        if (mData.size() == 0) {
            setEmptyView();
        } else if (NotNull.isNotNull(textView)) {
            textView.setVisibility(View.GONE);
        }
        mRecyclerView.setAdapter(sectionAdapter);
    }

    private void showTurnOn(String msg, final int potion) {
        final DialogView dialogView = DialogView.getSingleton();
        dialogView.setContext(getActivity());
        View view = dialogView.showCustomDialong(R.layout.dialog_power_change);
        TextView tvCancel = (TextView) view.findViewById(R.id.tvCancel);
        TextView tvContent = (TextView) view.findViewById(R.id.tvContent);
        TextView alert_title = (TextView) view.findViewById(R.id.alert_title);
        alert_title.setText("通知");
        tvContent.setText(msg);
        TextView tvCommit = (TextView) view.findViewById(R.id.tvCommit);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismissDialong();
            }
        });
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                mData.remove(potion);
                removeDate(potion);//删除
                sectionAdapter.notifyDataSetChanged();
                if (mData.size() == 0) {
                    setEmptyView();
                }
                dialogView.dismissDialong();

            }
        });

    }

    private void setEmptyView() {
        textView = new TextView(getActivity());
        textView.setText("暂时还没有内容哦");
        textView.setTextSize(15);
        textView.setTextColor(getResources().getColor(R.color.colormain));
        textView.setGravity(Gravity.CENTER);
        notiPercent.addView(textView);
    }

    public void setObservable(NoticeObserval o) {
        o.registerObserver(this);
    }


    @Override
    public void update(String succeedStr) {
        if (NotNull.isNotNull(currentNotice) && TextUtils.equals(currentNotice, succeedStr)) {
            return;
        }
        currentNotice = succeedStr;
        Log.d(TAG, "update: " + succeedStr);
        saveNotice(succeedStr);
    }

    private String[] getStringArray() {
        return getActivity()
                .getResources().getStringArray(R.array.noticeArr);
    }

    private void saveNotice(String json) {
        notice_json = fileCache.getAsJSONArray(SAVE_ITEM);
        try {
            JSONObject object = new JSONObject(json);
            if (!NotNull.isNotNull(notice_json)) {
                notice_json = new JSONArray();
                notice_json.put(0, object);
            } else {
                //跟上一条数据一致
                if (TextUtils.equals(notice_json.get(notice_json.length() - 1).toString(), json)) {
                    Log.d(TAG, "saveNotice: ");
                    return;
                }
                length++;
                notice_json.put(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listener.alertListener(Math.abs(length));
        fileCache.put(SAVE_ITEM, notice_json);
        getNOticeData();

    }

    private void getNOticeData() {
        if (!NotNull.isNotNull(notice_json)) {
            notice_json = fileCache.getAsJSONArray(SAVE_ITEM);
        }
        if (!NotNull.isNotNull(notice_json)) {
            return;
        }
        mData.clear();
        if (notice_json.length() == 0) {
            return;
        }
        for (int i = 0; i < notice_json.length(); i++) {
            try {
                JSONObject jsonObject = notice_json.getJSONObject(i);
                String warm = jsonObject.getString("warm");
                String deviceId = jsonObject.getString("deviceId");
                String textDevice="";
                if (TextUtils.equals("0",deviceId)){
                    textDevice="(左炉) ";
                }else{
                    textDevice="(右炉) ";
                }
                if (warm.contains("___")) {
                    String[] strings = warm.split("___");
                    Integer index = Integer.valueOf(strings[0].substring(1, 2));
                    mData.add(new NotiseAbStractSection(new NotiseAdapterModel(drablwIcon[index - 1], textDevice+getStringArray()[index - 1], new Date(Long.valueOf(strings[1])))));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (NotNull.isNotNull(textView)) {
            textView.setVisibility(View.GONE);
        }
        Collections.reverse(mData);
        if (NotNull.isNotNull(sectionAdapter)) {
            sectionAdapter.notifyDataSetChanged();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void removeDate(int position) {
        if (NotNull.isNotNull(notice_json)) {
            notice_json.remove(position);
        } else {
            notice_json = fileCache.getAsJSONArray(SAVE_ITEM);
        }
        if (NotNull.isNotNull(notice_json)) {
            fileCache.put(SAVE_ITEM, notice_json);
            instance.putValue(NOTICE_COUNT, notice_json.length());
        } else {
            fileCache.clear();
            instance.putValue(NOTICE_COUNT, 0);
        }
    }

    public void set2Zero() {
        length=0;
    }

    public interface onAlertListener {
        void alertListener(int length);
    }

    public void setOnAlertListener(onAlertListener listener) {
        this.listener = listener;
    }
}
