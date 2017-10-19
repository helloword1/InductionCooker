package com.goockr.inductioncooker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goockr.inductioncooker.R;

import java.util.List;

/**
 * Created by LJN on 2017/10/18.
 */

public class DeviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<String> datas;
    private LayoutInflater inflater;

    public DeviceAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
        inflater= LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TextViewHolder(inflater.inflate(R.layout.item_devices,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextViewHolder textViewHolder = (TextViewHolder) holder;
        textViewHolder.tvDeviceName.setText("设备号："+datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private class TextViewHolder extends RecyclerView.ViewHolder {
        TextView tvDeviceName;
        public TextViewHolder(View itemView) {
            super(itemView);
            tvDeviceName = ((TextView) itemView.findViewById(R.id.tvDeviceName));
        }
    }
}
