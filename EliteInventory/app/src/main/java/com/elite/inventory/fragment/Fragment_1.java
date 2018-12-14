package com.elite.inventory.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.ArrayMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.elite.inventory.AppApplication;
import com.elite.inventory.R;
import com.elite.inventory.adapter.BillAdapter;
import com.elite.inventory.config.AppConfig;
import com.elite.inventory.dialog.BillDialog;
import com.elite.inventory.dialog.DialogManager;
import com.elite.inventory.entity.BillEntity;
import com.elite.inventory.entity.GoodsEntity;
import com.elite.inventory.utils.ToastUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Fragment_1 extends Fragment implements View.OnClickListener {

    public static final String TAG = "Fragment_1";

    private static final int PAY_TYPE_1 = 1101;
    private static final int PAY_TYPE_2 = 1102;
    private static final int PAY_TYPE_3 = 1103;
    private static final int PAY_TYPE_4 = 1104;

    @BindView(R.id.fragment_1_rv_list)
    RecyclerView rv_list;

    @BindView(R.id.fragment_1_tv_1)
    TextView tv_save;

    @BindView(R.id.fragment_1_tv_2)
    TextView tv_take;

    @BindView(R.id.fragment_1_tv_3)
    TextView tv_cash_box;

    @BindView(R.id.fragment_1_tv_4)
    TextView tv_clear;

    @BindView(R.id.fragment_1_tv_total)
    TextView tv_total;

    @BindView(R.id.fragment_1_et_code)
    EditText et_code;

    @BindView(R.id.fragment_1_tv_right_2)
    TextView tv_right_2;

    @BindView(R.id.fragment_1_tv_right_3)
    TextView tv_right_3;

    @BindView(R.id.fragment_1_tv_right_4)
    TextView tv_right_4;

    @BindView(R.id.fragment_1_tv_right_5)
    TextView tv_right_5;

    @BindView(R.id.fragment_1_tv_right_7)
    TextView tv_right_7;

    @BindView(R.id.fragment_1_tv_right_8)
    TextView tv_right_8;

    @BindView(R.id.fragment_1_tv_right_9)
    TextView tv_right_9;

    @BindView(R.id.fragment_1_tv_right_10)
    TextView tv_right_10;

    @BindView(R.id.fragment_1_tv_total_pay)
    TextView tv_total_pay;

    @BindView(R.id.fragment_1_tv_total_detail)
    TextView tv_total_detail;

    @BindView(R.id.fragment_1_tv_confirm)
    TextView tv_confirm;

    private View view;
    private Unbinder unbinder;

    private Context mContext;
    private BillDialog bd;
    private DialogManager dm;
    private DecimalFormat df;
    private BillAdapter billAdapter;
    private String currStr;
    private int num = 0;
    private int sale = 0;
    private int payType = PAY_TYPE_2;
    private double total, saleTotal, derate, mantissa;

    private ArrayList<BillEntity> al_bill = new ArrayList<BillEntity>();
    private ArrayMap<String, Integer> am_bill = new ArrayMap<String, Integer>();

    public static Fragment_1 newInstance(String content) {
        Fragment_1 fragment = new Fragment_1();
        Bundle args = new Bundle();
        args.putString(TAG, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        df = new DecimalFormat("0.00");
        bd = new BillDialog(mContext);
        dm = DialogManager.getInstance(mContext);
        currStr = AppApplication.getCurrency();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_1, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();

        return view;
    }

    private void init() {
        tv_save.setOnClickListener(this);
        tv_take.setOnClickListener(this);
        tv_clear.setOnClickListener(this);
        tv_cash_box.setOnClickListener(this);
        tv_right_2.setOnClickListener(this);
        tv_right_3.setOnClickListener(this);
        tv_right_4.setOnClickListener(this);
        tv_right_5.setOnClickListener(this);
        tv_right_7.setOnClickListener(this);
        tv_right_8.setOnClickListener(this);
        tv_right_9.setOnClickListener(this);
        tv_right_10.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);

        updateTotal();
        initEditText();
        initRecyclerView();
        changeTextViewStatus(PAY_TYPE_2);
    }

    private void initEditText() {
        et_code.requestFocus();
        et_code.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    handleAddOrder();
                    return true;
                }
                return false;
            }
        });
    }

    private void initRecyclerView() {
        // 创建布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 设置布局管理器
        rv_list.setLayoutManager(layoutManager);
        // 创建Adapter
        billAdapter = new BillAdapter(mContext, al_bill);
        billAdapter.setOnItemClickListener(new MyOnItemClickListener());
        // 设置Adapter
        rv_list.setAdapter(billAdapter);
        rv_list.scrollToPosition(al_bill.size() - 1);
    }

    /**
     * 处理“扫描”事项
     */
    private void handleAddOrder() {
        String upc = et_code.getText().toString();
        if (upc.isEmpty()) {
            ToastUtils.showToast(getString(R.string.text_input_code), 1000);
            return;
        }
        addData(upc);
    }

    /**
     * 新增商品
     *
     * @param upc
     */
    private void addData(String upc) {
        //GoodsEntity goodsEn = db_goods.find(upc);
        GoodsEntity goodsEn = new GoodsEntity();
        goodsEn.setUpc(upc);
        goodsEn.setName(upc);
        goodsEn.setPrice(9.98);
        if (goodsEn != null) {
            int pos = -1;
            if (am_bill.containsKey(upc)) {
                pos = am_bill.get(upc);
            }
            if (pos >= 0 && pos < al_bill.size()) {
                al_bill.get(pos).setNum(al_bill.get(pos).getNum() + 1);
                al_bill.get(pos).setSubtotal(al_bill.get(pos).getPrice() * al_bill.get(pos).getNum());
            } else {
                BillEntity data = new BillEntity();
                data.setSku(goodsEn.getSku());
                data.setUpc(goodsEn.getUpc());
                data.setName(goodsEn.getName());
                data.setNum(1);
                data.setPrice(goodsEn.getPrice());
                data.setSubtotal(goodsEn.getPrice());
                al_bill.add(data);
                pos = al_bill.size() - 1;
                am_bill.put(upc, pos);
            }
            billAdapter.updateDatas(al_bill, pos);
            rv_list.scrollToPosition(al_bill.size()-1);
            updateTotal();
        } else {
            ToastUtils.showToast(getString(R.string.text_input_code_error), 1000);
        }
        et_code.setText("");
        hideSoftInput(et_code);
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_1_tv_1:
                break;
            case R.id.fragment_1_tv_2:
                break;
            case R.id.fragment_1_tv_3:
                break;
            case R.id.fragment_1_tv_4:
                if (num <= 0) return;
                dm.showTwoBtnDialog(null, getString(R.string.goods_clear_confirm, num),
                        null, null, AppApplication.screenWidth / 4, true, true,
                        new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                switch (msg.what) {
                                    case AppConfig.DIALOG_BT_LEFT:
                                        break;
                                    case AppConfig.DIALOG_BT_RIGHT:
                                        clearData();
                                        break;
                                }
                            }
                        });
                break;
            case R.id.fragment_1_tv_right_2:
                if (total <= 0) return;
                if (tv_right_2.isSelected()) {
                    setStatusFalse(tv_right_2);
                } else {
                    bd.showSaleDialog(getString(R.string.fragment_1_right_1), total, 1,
                            new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    switch (msg.what) {
                                        case AppConfig.DIALOG_BT_RIGHT:
                                            sale = (Integer) msg.obj;
                                            if (sale > 0) {
                                                changeTextViewStatus(tv_right_2, tv_right_3);
                                            }
                                            break;
                                    }
                                }
                            });
                }
                break;
            case R.id.fragment_1_tv_right_3:
                if (total <= 0) return;
                if (tv_right_3.isSelected()) {
                    setStatusFalse(tv_right_3);
                } else {
                    bd.showSaleDialog(getString(R.string.fragment_1_right_10), total, 2,
                            new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    switch (msg.what) {
                                        case AppConfig.DIALOG_BT_RIGHT:
                                            derate = (Double) msg.obj;
                                            if (derate > 0) {
                                                changeTextViewStatus(tv_right_3, tv_right_2);
                                            }
                                            break;
                                    }
                                }
                            });
                }
                break;
            case R.id.fragment_1_tv_right_4:
                changeTextViewStatus(tv_right_4, tv_right_5);
                break;
            case R.id.fragment_1_tv_right_5:
                changeTextViewStatus(tv_right_5, tv_right_4);
                break;
            case R.id.fragment_1_tv_right_7:
                changeTextViewStatus(PAY_TYPE_1);
                break;
            case R.id.fragment_1_tv_right_8:
                changeTextViewStatus(PAY_TYPE_2);
                break;
            case R.id.fragment_1_tv_right_9:
                changeTextViewStatus(PAY_TYPE_3);
                break;
            case R.id.fragment_1_tv_right_10:
                changeTextViewStatus(PAY_TYPE_4);
                break;
            case R.id.fragment_1_tv_confirm:
                break;
        }
    }

    private void setStatusFalse(TextView tv_onclick) {
        tv_onclick.setSelected(false);
        tv_onclick.setTextColor(getResources().getColor(R.color.text_color_greys, null));
        updateTotalView();
    }

    private void changeTextViewStatus(TextView tv_onclick, TextView tv_false) {
        tv_onclick.setSelected(!tv_onclick.isSelected());
        if (tv_onclick.isSelected()) {
            if (tv_false.isSelected()) {
                tv_false.setSelected(false);
                tv_false.setTextColor(getResources().getColor(R.color.text_color_greys, null));
            }
            tv_onclick.setTextColor(getResources().getColor(R.color.text_color_white, null));
        } else {
            tv_onclick.setTextColor(getResources().getColor(R.color.text_color_greys, null));
        }
        updateTotalView();
    }

    private void changeTextViewStatus(int typeCode) {
        tv_right_7.setSelected(false);
        tv_right_8.setSelected(false);
        tv_right_9.setSelected(false);
        tv_right_10.setSelected(false);
        tv_right_7.setTextColor(getResources().getColor(R.color.text_color_greys, null));
        tv_right_8.setTextColor(getResources().getColor(R.color.text_color_greys, null));
        tv_right_9.setTextColor(getResources().getColor(R.color.text_color_greys, null));
        tv_right_10.setTextColor(getResources().getColor(R.color.text_color_greys, null));
        payType = typeCode;
        switch (payType) {
            case PAY_TYPE_1:
                tv_right_7.setSelected(true);
                tv_right_7.setTextColor(getResources().getColor(R.color.text_color_white, null));
                break;
            case PAY_TYPE_2:
                tv_right_8.setSelected(true);
                tv_right_8.setTextColor(getResources().getColor(R.color.text_color_white, null));
                break;
            case PAY_TYPE_3:
                tv_right_9.setSelected(true);
                tv_right_9.setTextColor(getResources().getColor(R.color.text_color_white, null));
                break;
            case PAY_TYPE_4:
                tv_right_10.setSelected(true);
                tv_right_10.setTextColor(getResources().getColor(R.color.text_color_white, null));
                break;
        }
        updateTotalView();
    }

    private void updateTotalView() {
        StringBuffer sb_detail = new StringBuffer();
        switch (payType) {
            case PAY_TYPE_1:
                sb_detail.append(getString(R.string.fragment_1_right_21));
                break;
            case PAY_TYPE_2:
                sb_detail.append(getString(R.string.fragment_1_right_22));
                break;
            case PAY_TYPE_3:
                sb_detail.append(getString(R.string.fragment_1_right_23));
                break;
            case PAY_TYPE_4:
                sb_detail.append(getString(R.string.fragment_1_right_24));
                break;
        }
        sb_detail.append(getString(R.string.goods_pay));

        boolean bl_2 = tv_right_2.isSelected();
        boolean bl_3 = tv_right_3.isSelected();
        boolean bl_4 = tv_right_4.isSelected();
        boolean bl_5 = tv_right_5.isSelected();
        if (bl_2 || bl_3) {
            sb_detail.append("/");
        }
        if (bl_2) { //折扣
            derate = 0;
            saleTotal = total*(100-sale)/100;
            sb_detail.append(getString(R.string.fragment_1_right_11) + sale + "% -" + currStr + df.format(saleTotal));
        } else {
            sale = 0;
            saleTotal = 0;
            if (bl_3) { //减免
                saleTotal = derate;
                sb_detail.append(getString(R.string.fragment_1_right_12) + "-" + currStr + df.format(saleTotal));
            } else {
                derate = 0;
            }
        }
        if (bl_4 || bl_5) {
            sb_detail.append("/");
        }
        if (bl_4) {
            mantissa = ((total*100)%10)/100;
            sb_detail.append(getString(R.string.fragment_1_right_13) + "-" + currStr + df.format(mantissa));
        } else if (bl_5) {
            mantissa = ((total*100)%100)/100;
            sb_detail.append(getString(R.string.fragment_1_right_14) + "-" + currStr + df.format(mantissa));
        } else {
            mantissa = 0;
        }
        saleTotal += mantissa;
        tv_total_detail.setText(sb_detail.toString());
        tv_total_pay.setText(currStr + df.format(total - saleTotal));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (dm != null) {
            dm.clearInstance();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class MyOnItemClickListener implements BillAdapter.OnItemClickListener {

        @Override
        public void onItemClick(final int position, String data) {
            if (position < 0 || position >= al_bill.size()) return;
            int curNum = 0;
            switch (data) {
                case "num_ass":
                    curNum = al_bill.get(position).getNum();
                    if (curNum <= 1) {
                        deleteData(position);
                    } else {
                        curNum--;
                        updateData(position, curNum, al_bill.get(position).getPrice());
                    }
                    break;
                case "num_add":
                    curNum = al_bill.get(position).getNum();
                    curNum++;
                    updateData(position, curNum, al_bill.get(position).getPrice());
                    break;
                case "num_root":
                    billAdapter.updateDatas(al_bill, position);
                    BillEntity billEn = al_bill.get(position);
                    bd.showEditBillDialog(billEn, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what) {
                                case AppConfig.DIALOG_BT_LEFT: //删除
                                    deleteData(position);
                                    break;
                                case AppConfig.DIALOG_BT_RIGHT: //修改
                                    BillEntity newBillEn = (BillEntity) msg.obj;
                                    if (newBillEn != null) {
                                        updateData(position, newBillEn.getNum(), newBillEn.getPrice());
                                    }
                                    break;
                            }
                        }
                    });
                    break;
            }
        }

    }

    /**
     * 修改商品
     */
    private void updateData(int pos, int num, double price) {
        al_bill.get(pos).setNum(num);
        al_bill.get(pos).setPrice(price);
        al_bill.get(pos).setSubtotal(num * price);
        billAdapter.updateDatas(al_bill, pos);
        updateTotal();
    }

    /**
     * 删除商品
     */
    private void deleteData(int pos) {
        al_bill.remove(pos);
        am_bill.clear();
        // 更新标记位
        for (int i = 0; i < al_bill.size(); i++) {
            am_bill.put(al_bill.get(i).getUpc(), i);
        }
        billAdapter.updateDatas(al_bill, pos);
        updateTotal();
    }

    /**
     * 清空商品
     */
    private void clearData() {
        al_bill.clear();
        am_bill.clear();
        billAdapter.updateDatas(al_bill, -1);
        updateTotal();
    }

    /**
     * 更新总价
     */
    private void updateTotal() {
        num = 0;
        total = 0;
        for (int i = 0; i < al_bill.size(); i++) {
            BillEntity shopEn = al_bill.get(i);
            num += shopEn.getNum();
            total += shopEn.getSubtotal();
        }
        tv_total.setText(mContext.getString(R.string.goods_number_3, num) + "   " +
                currStr + df.format(Double.valueOf(total)));

        //价格变动时"减免"恢复默认状态
        derate = 0;
        setStatusFalse(tv_right_3);
        if (total > 0) {
            updateTotalView();
        } else {
            //价格置0时"折扣"恢复默认状态
            setStatusFalse(tv_right_2);
        }
    }

}
