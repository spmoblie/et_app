package com.elite.display01.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elite.display01.R;
import com.elite.display01.entity.BillEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder>{

    private Context mContext;
    // 数据集
    private ArrayList<BillEntity> mDatas;

    public BillAdapter(Context context, ArrayList<BillEntity> datas) {
        super();
        mContext = context;
        if (datas != null) {
            mDatas = datas;
        } else {
            mDatas = new ArrayList<BillEntity>();
        }
    }

    public void updateDatas(ArrayList<BillEntity> datas){
        if (datas != null) {
            mDatas = datas;
        } else {
            mDatas = new ArrayList<BillEntity>();
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // 创建一个View
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_main_rv, viewGroup, false);
        // 创建一个ViewHolder
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        // 绑定数据到ViewHolder
        viewHolder.mTV_1.setText(mDatas.get(i).getBillName());
        viewHolder.mTV_3.setText(mContext.getString(R.string.bill_currency_rmb) + mDatas.get(i).getBillPrice());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.rv_item_tv_1)
        public TextView mTV_1;

        @BindView(R.id.rv_item_tv_2)
        public TextView mTV_2;

        @BindView(R.id.rv_item_tv_3)
        public TextView mTV_3;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
