package com.elite.inventory.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elite.inventory.R;
import com.elite.inventory.entity.GoodsEntity;
import com.elite.inventory.utils.StringUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private Context mContext;
    private View mHeaderView;
    private String mKeyword;
    private ArrayList<GoodsEntity> mDatas;
    private OnItemClickListener mListener;

    public SearchAdapter(Context context, ArrayList<GoodsEntity> datas, String keyword) {
        super();
        mContext = context;
        mKeyword = keyword;
        if (datas != null) {
            mDatas = datas;
        } else {
            mDatas = new ArrayList<GoodsEntity>();
        }
    }

    public void updateDatas(ArrayList<GoodsEntity> datas, String keyword){
        mKeyword = keyword;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rv_query, viewGroup, false);
        // 创建一个ViewHolder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if(getItemViewType(i) == TYPE_HEADER) return;
        final int pos = getRealPosition(viewHolder);
        final GoodsEntity data = mDatas.get(pos);

        // 绑定数据到ViewHolder
        if (StringUtils.isNull(mKeyword)) {
            viewHolder.mTV_1.setText(data.getName());
        } else {
            String showStr = "";
            if (StringUtils.isNumeric(mKeyword)) {
                showStr = data.getUpc();
            } else {
                showStr = data.getName();
            }
            if (showStr.contains(mKeyword)) {
                int startPos = showStr.indexOf(mKeyword);
                SpannableString spannableString = new SpannableString(showStr);
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")),
                        startPos, startPos + mKeyword.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.mTV_1.setText(spannableString);
            } else {
                viewHolder.mTV_1.setText(showStr);
            }
        }
        // 设置事件监听器
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

        @BindView(R.id.query_rv_item_root)
        public LinearLayout ll_main;

        @BindView(R.id.query_rv_item_tv_1)
        public TextView mTV_1;

        public ViewHolder(View itemView) {
            super(itemView);
            if(itemView == mHeaderView) return;
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, GoodsEntity data);
    }
}
