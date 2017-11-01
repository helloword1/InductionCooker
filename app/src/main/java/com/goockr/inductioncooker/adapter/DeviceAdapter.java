package com.goockr.inductioncooker.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.models.BaseDevice;
import com.goockr.inductioncooker.utils.NotNull;

import java.util.List;

/**
 * Created by LJN on 2017/10/18.
 */

public class DeviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<BaseDevice> datas;
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    public DeviceAdapter(Context context, List<BaseDevice> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TextViewHolder(inflater.inflate(R.layout.change_right_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        TextViewHolder textViewHolder = (TextViewHolder) holder;
        BaseDevice baseDevice = datas.get(position);
        textViewHolder.itemidtv.setText("ID:" + baseDevice.getDeviceId());
        textViewHolder.itemsectioncontenttitletv.setText(baseDevice.getDeviceName() + (position + 1));
        textViewHolder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NotNull.isNotNull(listener)){
                    listener.itemClickListener(position);
                }
            }
        });
        textViewHolder.cardview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (NotNull.isNotNull(listener)){
                    listener.itemLongClickListener(position);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    private class TextViewHolder extends RecyclerView.ViewHolder {
        private View lineTop;
        private TextView itemsectioncontenttitletv;
        private android.widget.ImageView itemsectioncontentgoiv;
        private TextView itemsectioncontentdestv;
        private TextView itemidtv;
        private android.support.percent.PercentRelativeLayout cardview;

        public TextViewHolder(View itemView) {
            super(itemView);
            this.cardview = (PercentRelativeLayout) itemView.findViewById(R.id.card_view);
            this.itemsectioncontentdestv = (TextView) itemView.findViewById(R.id.item_section_content_des_tv);
            this.itemsectioncontentgoiv = (ImageView) itemView.findViewById(R.id.item_section_content_go_iv);
            this.itemsectioncontenttitletv = (TextView) itemView.findViewById(R.id.item_section_content_title_tv);
            this.itemidtv = (TextView) itemView.findViewById(R.id.item_id_tv);
            this.lineTop = itemView.findViewById(R.id.lineTop);
        }
    }

    public interface OnItemClickListener {
        void itemClickListener(int position);
        void itemLongClickListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
