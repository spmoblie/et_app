package com.elite.selfhelp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elite.selfhelp.R;
import com.elite.selfhelp.adapter.OrderAdapter;
import com.elite.selfhelp.entity.GoodsEntity;
import com.elite.selfhelp.entity.InvoiceEntity;
import com.elite.selfhelp.entity.OrderEntity;
import com.elite.selfhelp.entity.ShopEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by user on 13/06/18.
 */

public class OrderActivity extends BaseActivity {

    private static final String TAG = "OrderActivity";

    @BindView(R.id.title_iv_back)
    ImageView iv_back;

    @BindView(R.id.title_tv_title)
    TextView tv_title;

    @BindView(R.id.order_rv_lists)
    RecyclerView rv_order;

    private Context mContext;
    private OrderAdapter orderAdapter;

    private ArrayList<OrderEntity> al_order = new ArrayList<OrderEntity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        init();
        initData();
        initRecyclerView();
    }

    private void init() {
        mContext = this;

        tv_title.setText(getString(R.string.goods_print_ticket));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
            }
        });
    }

    private void initData() {
        List<OrderEntity> dbLists = db_order.getNoPrintDatas();
        if (dbLists != null) {
            al_order.addAll(dbLists);
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager_1 = new LinearLayoutManager(this);
        layoutManager_1.setOrientation(LinearLayoutManager.VERTICAL);
        rv_order.setLayoutManager(layoutManager_1);
        orderAdapter = new OrderAdapter(mContext, al_order);
        orderAdapter.setOnItemClickListener(new CLOnItemClickListener());
        rv_order.setAdapter(orderAdapter);
    }

    class CLOnItemClickListener implements OrderAdapter.OnItemClickListener {
        @Override
        public void onItemClick(int position, OrderEntity data) {
            if (data != null) {
                List<ShopEntity> lists = new ArrayList<ShopEntity>();
                ShopEntity se;
                String shopTag = data.getShopTag();
                String[] tags = shopTag.split(",");
                for (int i = 0; i < tags.length; i++) {
                    String s = tags[i];
                    if (s.contains("_")) {
                        String[] ss = s.split("_");
                        GoodsEntity ge = db_goods.find(ss[0]);
                        int num = Integer.valueOf(ss[1]);
                        if (ge != null && num > 0) {
                            se = new ShopEntity();
                            se.setName(ge.getName());
                            se.setPrice(ge.getPrice());
                            se.setNum(num);
                            se.setSubtotal(se.getPrice() * num);
                            lists.add(se);
                        }
                    }
                }
                InvoiceEntity ie = new InvoiceEntity();
                ie.setShopLists(lists);
                ie.setPrintSn(data.getPrintSn());
                ie.setCreateTime(data.getCreateTime());
                ie.setTotalPrice(data.getPriceTotal());
                int status = stratPrint(ie) >= 0 ? 1 : 0;

                if (status == 1) { //补打印成功
                    al_order.remove(position);
                    orderAdapter.updateDatas(al_order);
                    data.setPrintStatus(status);
                    db_order.update(data);
                }
            }
        }
    }

}
