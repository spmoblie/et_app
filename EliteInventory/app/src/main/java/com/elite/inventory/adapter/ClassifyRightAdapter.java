package com.elite.inventory.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elite.inventory.R;
import com.elite.inventory.entity.GoodsEntity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassifyRightAdapter extends RecyclerView.Adapter<ClassifyRightAdapter.ViewHolder>{

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private Context mContext;
    private View mHeaderView;
    private DecimalFormat df = null;
    private ArrayList<GoodsEntity> mDatas;
    private OnItemClickListener mListener;

    public ClassifyRightAdapter(Context context, ArrayList<GoodsEntity> datas) {
        super();
        mContext = context;
        df = new DecimalFormat("0.00");
        if (datas != null) {
            mDatas = datas;
        } else {
            mDatas = new ArrayList<GoodsEntity>();
        }
    }

    public void updateDatas(ArrayList<GoodsEntity> datas){
        if (datas != null) {
            mDatas = datas;
        } else {
            mDatas = new ArrayList<GoodsEntity>();
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rv_classify_right, viewGroup, false);
        // 创建一个ViewHolder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if(getItemViewType(i) == TYPE_HEADER) return;
        final int pos = getRealPosition(viewHolder);
        final GoodsEntity data = mDatas.get(pos);

        // 绑定数据到ViewHolder
        viewHolder.mTV_1.setText(data.getName());
        viewHolder.mTV_2.setText(data.getUpc());
        viewHolder.mTV_3.setText(mContext.getString(R.string.goods_curr_rmb) + df.format(Double.valueOf(data.getPrice())));

        viewHolder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(pos, data);
                }
            }
        });
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

        @BindView(R.id.classify_rv_item_root)
        public LinearLayout ll_main;

        @BindView(R.id.classify_rv_item_tv_1)
        public TextView mTV_1;

        @BindView(R.id.classify_rv_item_tv_2)
        public TextView mTV_2;

        @BindView(R.id.classify_rv_item_tv_3)
        public TextView mTV_3;

        public ViewHolder(View itemView) {
            super(itemView);
            if(itemView == mHeaderView) return;
            ButterKnife.bind(this, itemView);
        }
    }

    public void AddHeaderItem(List<GoodsEntity> items){
        mDatas.addAll(0,items);
        notifyDataSetChanged();
    }

    public void AddFooterItem(List<GoodsEntity> items){
        mDatas.addAll(items);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, GoodsEntity data);
    }
}
