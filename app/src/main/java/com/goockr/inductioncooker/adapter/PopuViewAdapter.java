package com.goockr.inductioncooker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goockr.inductioncooker.R;

import java.util.List;

/**
 * Created by Administrator on 2017/9/30.
 */

public class PopuViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final LayoutInflater inflate;
    private Context context;
    private List<String> datas;
    private OnGetAdapterListener listener;

    public PopuViewAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
        inflate = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IntenvoryViewHolder(new TextView(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        IntenvoryViewHolder viewHolder = (IntenvoryViewHolder) holder;
        viewHolder.textView.setText(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private class IntenvoryViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public IntenvoryViewHolder(View view) {
            super(view);
            textView = (TextView) view;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity=Gravity.CENTER;
            params.setMargins(13,13,13,13);
            textView.setLayoutParams(params);
            textView.setPadding(12, 12, 12, 12);
            textView.setTextColor(context.getResources().getColor(R.color.colorGrayText));
            textView.setTextSize(15);
            textView.setGravity(Gravity.CENTER);
        }
    }

    public interface OnGetAdapterListener {
        void itemClick(int position);
    }

    public void setoOnGetAdapterListener(OnGetAdapterListener listener) {
        this.listener = listener;
    }
}
