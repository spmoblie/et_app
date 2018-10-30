package com.elite.selfhelp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elite.selfhelp.R;
import com.elite.selfhelp.entity.GoodsEntity;

import butterknife.BindView;

/**
 * Created by user on 09/04/18.
 */

public class GoodsActivity extends BaseActivity {

    private static final String TAG = "GoodsActivity";

    @BindView(R.id.goods_tv_01)
    TextView tv_01;

    @BindView(R.id.goods_tv_02)
    TextView tv_02;

    @BindView(R.id.goods_tv_03)
    TextView tv_03;

    @BindView(R.id.goods_tv_04)
    TextView tv_04;

    @BindView(R.id.goods_tv_05)
    TextView tv_05;

    @BindView(R.id.goods_tv_06)
    TextView tv_06;

    @BindView(R.id.goods_tv_07)
    TextView tv_07;

    @BindView(R.id.goods_tv_08)
    TextView tv_08;

    @BindView(R.id.goods_tv_09)
    TextView tv_09;

    @BindView(R.id.goods_tv_10)
    TextView tv_10;

    @BindView(R.id.goods_tv_11)
    TextView tv_11;

    @BindView(R.id.goods_tv_12)
    TextView tv_12;

    @BindView(R.id.goods_tv_13)
    TextView tv_13;

    @BindView(R.id.goods_iv_delete)
    ImageView iv_delete;

    @BindView(R.id.goods_iv_picture)
    ImageView iv_picture;

    private GoodsEntity data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        // 创建动画
        overridePendingTransition(R.anim.center_enlarge, R.anim.anim_no_anim);

        data = (GoodsEntity) getIntent().getSerializableExtra("data");

        init();
        initView();
    }

    private void init() {
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        if (data != null) {
            tv_01.setText(data.getName());
            tv_02.setText(data.getUpc());
            tv_03.setText(getString(R.string.goods_curr_rmb) + String.valueOf(data.getPrice()));
            tv_04.setText(data.getSaleUnit());
            tv_05.setText(data.getModel());
        } else {
            showToast("参数错误，请重试");
        }
    }

    @Override
    public void finish() {
        super.finish();
        // 销毁动画
        overridePendingTransition(R.anim.anim_no_anim, R.anim.center_narrow);
    }
}
