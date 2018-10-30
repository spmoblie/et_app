package com.elite.selfhelp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elite.selfhelp.R;
import com.elite.selfhelp.activity.MainActivity;
import com.elite.selfhelp.entity.ShopEntity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder>{

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private Context mContext;
    private View mHeaderView;
    private Handler mHandler;
    private DecimalFormat df = null;
    private ArrayList<ShopEntity> mDatas;
    private OnItemClickListener mListener;

    public ShopAdapter(Context context, ArrayList<ShopEntity> datas, Handler handler) {
        super();
        mContext = context;
        mHandler = handler;
        df = new DecimalFormat("0.00");
        if (datas != null) {
            mDatas = datas;
        } else {
            mDatas = new ArrayList<ShopEntity>();
        }
    }

    public void updateDatas(ArrayList<ShopEntity> datas){
        if (datas != null) {
            mDatas = datas;
        } else {
            mDatas = new ArrayList<ShopEntity>();
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // 创建头部View
        if(mHeaderView != null && i == TYPE_HEADER) return new ViewHolder(mHeaderView);
        // 创建一个View
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rv_main, viewGroup, false);
        // 创建一个ViewHolder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if(getItemViewType(i) == TYPE_HEADER) return;
        final int pos = getRealPosition(viewHolder);

        // 绑定数据到ViewHolder
        viewHolder.mTV_1.setText(mDatas.get(pos).getName());
        viewHolder.mTV_2.setText(mContext.getString(R.string.goods_curr_rmb) + df.format(Double.valueOf(mDatas.get(pos).getPrice())));
        viewHolder.mTV_3.setText(mContext.getString(R.string.goods_Price_x) + String.valueOf(mDatas.get(pos).getNum()));
        viewHolder.mTV_4.setText(mContext.getString(R.string.goods_curr_rmb) + df.format(Double.valueOf(mDatas.get(pos).getSubtotal())));

        viewHolder.mTV_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHandler != null) {
                    Message msg = new Message();
                    msg.what = MainActivity.HANDLER_901;
                    Bundle bundle = new Bundle();
                    bundle.putInt("pos", pos);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                }
            }
        });

        viewHolder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHandler != null) {
                    Message msg = new Message();
                    msg.what = MainActivity.HANDLER_902;
                    Bundle bundle = new Bundle();
                    bundle.putInt("pos", pos);
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                }
            }
        });

        /*if (i == getItemCount() - 1) {
            viewHolder.ll_main.setBackgroundColor(mContext.getResources().getColor(R.color.lower_text_color));
        } else {
            viewHolder.ll_main.setBackgroundColor(mContext.getResources().getColor(R.color.text_color_white));
        }*/
    }
    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mDatas.size() : mDatas.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.main_rv_item_root)
        public LinearLayout ll_main;

        @BindView(R.id.main_rv_item_tv_1)
        public TextView mTV_1;

        @BindView(R.id.main_rv_item_tv_2)
        public TextView mTV_2;

        @BindView(R.id.main_rv_item_tv_3)
        public TextView mTV_3;

        @BindView(R.id.main_rv_item_tv_4)
        public TextView mTV_4;

        @BindView(R.id.main_rv_item_tv_5)
        public TextView mTV_5;

        public ViewHolder(View itemView) {
            super(itemView);
            if(itemView == mHeaderView) return;
            ButterKnife.bind(this, itemView);
        }
    }

    interface OnItemClickListener {
        void onItemClick(int position, String data);
    }
}
