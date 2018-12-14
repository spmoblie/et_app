package com.elite.inventory.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elite.inventory.AppApplication;
import com.elite.inventory.R;
import com.elite.inventory.entity.BillEntity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder>{

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private Context mContext;
    private View mHeaderView;
    private DecimalFormat df;
    private int selectPos;
    private String currStr;
    private ArrayList<BillEntity> mDatas;
    private OnItemClickListener mListener;

    public BillAdapter(Context context, ArrayList<BillEntity> datas) {
        super();
        mContext = context;
        df = new DecimalFormat("0.00");
        currStr = AppApplication.getCurrency();
        if (datas != null) {
            mDatas = datas;
            this.selectPos = datas.size() - 1;
        } else {
            mDatas = new ArrayList<BillEntity>();
        }
    }

    public void updateDatas(ArrayList<BillEntity> datas, int selectPos){
        if (datas != null) {
            mDatas = datas;
        } else {
            mDatas = new ArrayList<BillEntity>();
        }
        if (selectPos >= 0 && selectPos < datas.size()) {
            this.selectPos = selectPos;
        } else {
            this.selectPos = datas.size() - 1;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rv_bill, viewGroup, false);
        // 创建一个ViewHolder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        if(getItemViewType(i) == TYPE_HEADER) return;
        final int pos = getRealPosition(viewHolder);

        // 绑定数据到ViewHolder
        viewHolder.tv_1.setText(mDatas.get(pos).getName());
        viewHolder.tv_2.setText(String.valueOf(mDatas.get(pos).getNum()));
        viewHolder.tv_3.setText(currStr + df.format(Double.valueOf(mDatas.get(pos).getPrice())));
        viewHolder.tv_4.setText(currStr + df.format(Double.valueOf(mDatas.get(pos).getSubtotal())));

        if (i == selectPos) {
            viewHolder.ll_root.setBackgroundResource(R.color.text_color_items);
        } else {
            viewHolder.ll_root.setBackgroundResource(R.color.text_color_white);
        }

        if (i == 0) {
            viewHolder.iv_line_top.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iv_line_top.setVisibility(View.GONE);
        }

        viewHolder.iv_num_ass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(i, "num_ass");
                }
            }
        });
        viewHolder.iv_num_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(i, "num_add");
                }
            }
        });
        viewHolder.ll_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(i, "num_root");
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

        @BindView(R.id.bill_rv_item_root)
        public LinearLayout ll_root;

        @BindView(R.id.bill_rv_item_tv_1)
        public TextView tv_1;

        @BindView(R.id.bill_rv_item_tv_2)
        public TextView tv_2;

        @BindView(R.id.bill_rv_item_tv_3)
        public TextView tv_3;

        @BindView(R.id.bill_rv_item_tv_4)
        public TextView tv_4;

        @BindView(R.id.bill_rv_item_line_top)
        public ImageView iv_line_top;

        @BindView(R.id.bill_rv_item_iv_ass)
        public ImageView iv_num_ass;

        @BindView(R.id.bill_rv_item_iv_add)
        public ImageView iv_num_add;

        public ViewHolder(View itemView) {
            super(itemView);
            if(itemView == mHeaderView) return;
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String data);
    }
}
