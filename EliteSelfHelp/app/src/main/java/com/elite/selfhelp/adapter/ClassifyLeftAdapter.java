package com.elite.selfhelp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elite.selfhelp.R;
import com.elite.selfhelp.entity.CategoryEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassifyLeftAdapter extends RecyclerView.Adapter<ClassifyLeftAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<CategoryEntity> mDatas;
    private OnItemClickListener mListener;
    private int index = 0;

    public ClassifyLeftAdapter(Context context, ArrayList<CategoryEntity> datas) {
        super();
        mContext = context;
        if (datas != null) {
            mDatas = datas;
        } else {
            mDatas = new ArrayList<CategoryEntity>();
        }
    }

    public void updateDatas(ArrayList<CategoryEntity> datas, int pos){
        if (datas != null) {
            mDatas = datas;
            index = pos;
        } else {
            mDatas = new ArrayList<CategoryEntity>();
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // 创建一个View
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rv_classify_left, viewGroup, false);
        // 创建一个ViewHolder
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final int pos = getRealPosition(viewHolder);
        final CategoryEntity data = mDatas.get(pos);

        // 绑定数据到ViewHolder
        viewHolder.mTV_1.setText(data.getName());
        if (index == pos) {
            viewHolder.mTV_1.setTextColor(mContext.getResources().getColor(R.color.text_color_redss));
        }else {
            viewHolder.mTV_1.setTextColor(mContext.getResources().getColor(R.color.text_color_white));
        }

        viewHolder.rl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(pos, data);
                }
            }
        });
    }
    public int getRealPosition(RecyclerView.ViewHolder holder) {
        return holder.getLayoutPosition();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.classify_left_item_root)
        public RelativeLayout rl_main;

        @BindView(R.id.classify_left_item_tv_name)
        public TextView mTV_1;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, CategoryEntity data);
    }
}
