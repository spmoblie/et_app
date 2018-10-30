package com.elite.selfhelp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elite.selfhelp.AppApplication;
import com.elite.selfhelp.R;
import com.elite.selfhelp.adapter.ShopAdapter;
import com.elite.selfhelp.config.AppConfig;
import com.elite.selfhelp.entity.BaseEntity;
import com.elite.selfhelp.entity.CategoryEntity;
import com.elite.selfhelp.entity.GoodsEntity;
import com.elite.selfhelp.entity.InvoiceEntity;
import com.elite.selfhelp.entity.OrderEntity;
import com.elite.selfhelp.entity.ShopEntity;
import com.elite.selfhelp.retrofit.HttpRequests;
import com.elite.selfhelp.utils.CommonTools;
import com.elite.selfhelp.utils.ExceptionUtils;
import com.elite.selfhelp.utils.JsonUtils;
import com.elite.selfhelp.utils.LogUtils;
import com.elite.selfhelp.utils.MyCountDownTimer;
import com.elite.selfhelp.utils.StringUtils;
import com.elite.selfhelp.utils.TimeUtils;
import com.lkl.sample.channelpay.nativeapi.CppSdkInterf;
import com.lkl.sample.channelpay.nativeapi.LKLCodePayQRTransCodeResult;
import com.lkl.sample.channelpay.nativeapi.ResponseCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    public static final int HANDLER_901 = 901;
    public static final int HANDLER_902 = 902;

    @BindView(R.id.main_rv_list)
    RecyclerView rv_list;

    @BindView(R.id.main_bt_pay)
    Button bt_pay;

    @BindView(R.id.main_bt_clear)
    Button bt_clear;

    @BindView(R.id.main_bt_add)
    Button bt_add;

    @BindView(R.id.main_bt_query)
    Button bt_query;

    @BindView(R.id.main_bt_print)
    Button bt_print;

    @BindView(R.id.main_bt_setting)
    Button bt_setting;

    @BindView(R.id.main_et_code)
    EditText et_code;

    @BindView(R.id.main_tv_total)
    TextView tv_total;

    private Context mContext;
    private DecimalFormat df = null;
    private ShopAdapter shopAdapter;
    private int num = 0;
    private double total = 0;
    private String password = "";
    private boolean isPay = false;

    private ArrayList<ShopEntity> al_shop = new ArrayList<ShopEntity>();
    private ArrayMap<String, Integer> am_shop = new ArrayMap<String, Integer>();

    //db
    private int page_goods = 1;
    private int page_category = 1;
    private ArrayList<GoodsEntity> al_goods = new ArrayList<GoodsEntity>();
    private ArrayList<CategoryEntity> al_category = new ArrayList<CategoryEntity>();

    //pay
    private PopupWindow payPopupWindow;
    private View payPopupView;
    private Animation popupAnimShow, popupAnimGone;
    private RelativeLayout pay_popup_view;
    private TextView tv_popup_time, tv_popup_cancel;
    private MyCountDownTimer mcdt;
    private String payAmount, payCode;
    private boolean isFinish = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initPrinter();
        payLogin();
    }

    private void init() {
        mContext = this;
        df = new DecimalFormat("0.00");
        popupAnimShow = AnimationUtils.loadAnimation(mContext, R.anim.in_from_left);
        popupAnimGone = AnimationUtils.loadAnimation(mContext, R.anim.out_to_right);

        bt_pay.setOnClickListener(this);
        bt_clear.setOnClickListener(this);
        bt_add.setOnClickListener(this);
        bt_query.setOnClickListener(this);
        bt_print.setOnClickListener(this);
        bt_setting.setOnClickListener(this);

        updateTotal();
        initEditText();
        initRecyclerView();
    }

    private void initEditText() {
        et_code.requestFocus();
        et_code.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (isPay) {
                        doPay();
                    } else {
                        handleAddOrder();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void initRecyclerView(){
        // 创建布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 设置布局管理器
        rv_list.setLayoutManager(layoutManager);
        // 创建Adapter
        shopAdapter = new ShopAdapter(mContext, al_shop, mHandler);
        // 设置Adapter
        rv_list.setAdapter(shopAdapter);
        // 添加Item间距
        //rv_list.addItemDecoration(new SpaceItemDecoration(10));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_bt_pay:
                confirmPay();
                break;
            case R.id.main_bt_clear:
                if (al_shop.size() == 0) return;
                showTwoBtnDialog(getString(R.string.confirm_clear), getString(R.string.confirm_clear_bill),
                        "", "", true, true, new Handler() {
                            @Override
                            public void handleMessage(Message dMsg) {
                                switch (dMsg.what) {
                                    case DIALOG_CANCEL:
                                        break;
                                    case DIALOG_CONFIRM:
                                        clearData();
                                        break;
                                }
                            }
                        });
                break;
            case R.id.main_bt_add:
                handleAddOrder();
                break;
            case R.id.main_bt_query:
                startActivity(new Intent(mContext, QueryActivity.class));
                break;
            case R.id.main_bt_print:
                /*InvoiceEntity ie = new InvoiceEntity();
                ie.setShopLists(al_shop);
                ie.setTotalPrice(String.valueOf(total));
                int printResult = stratPrint(ie);
                showToast(String.valueOf(printResult));*/
                startActivity(new Intent(mContext, OrderActivity.class));
                break;
            case R.id.main_bt_setting:
                /*showEditDialog(getString(R.string.text_password_input), InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD, true,
                        new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                switch (msg.what) {
                                    case DIALOG_CANCEL:
                                        break;
                                    case DIALOG_CONFIRM:
                                        String inputPassword = (String) msg.obj;
                                        if (password.equals(inputPassword)) {
                                            dismissDialog();
                                            startActivity(new Intent(mContext, SettingActivity.class));
                                        } else {
                                            showToast(getString(R.string.text_password_error), 1000);
                                        }
                                        break;
                                }
                            }
                        });*/
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
        }
    }

    /**
     * 处理“确认付款”事项
     */
    private void confirmPay() {
        payAmount = df.format(Double.valueOf(total));
        //payAmount = "0.01";
        if (StringUtils.priceIsNull(payAmount)) return;
        //payPopupShow();
        createOrder();
    }

    /**
     * 弹出支付引导浮层
     */
    private void payPopupShow(){
        isPay = true;
        if (payPopupWindow == null) {
            payPopupView = LayoutInflater.from(mContext).inflate(R.layout.popup_qr_code, null);
            pay_popup_view = (RelativeLayout) payPopupView.findViewById(R.id.popup_pay_main_rl);
            pay_popup_view.startAnimation(popupAnimShow);
            tv_popup_time = (TextView) payPopupView.findViewById(R.id.popup_pay_tv_time);
            tv_popup_cancel = (TextView) payPopupView.findViewById(R.id.popup_pay_tv_cancel);
            tv_popup_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    payPopupDismiss();
                }
            });

            payPopupWindow = new PopupWindow(payPopupView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            payPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            payPopupWindow.setFocusable(false);
            payPopupWindow.setOutsideTouchable(false);
            payPopupWindow.setClippingEnabled(false); //允许弹出窗口超出屏幕范围
            payPopupWindow.showAtLocation(payPopupView, Gravity.LEFT, 0, 0);
        } else if (!payPopupWindow.isShowing()) {
            pay_popup_view.startAnimation(popupAnimShow);
            payPopupWindow.showAtLocation(payPopupView, Gravity.LEFT, 0, 0);
        }
        startTimer(); //开启倒计时
    }

    /**
     * 关闭支付信息浮层
     */
    private void payPopupDismiss(){
        isPay = false;
        if (payPopupWindow != null && payPopupWindow.isShowing()) {
            pay_popup_view.startAnimation(popupAnimGone);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopTimer();
                    payPopupWindow.dismiss();
                }
            }, 1000);
        }
    }

    /**
     * 开启倒计时
     */
    private void startTimer() {
        mcdt = new MyCountDownTimer(tv_popup_time, 60000, 1000,
                new MyCountDownTimer.MyTimerCallback() {
                    @Override
                    public void onFinish() {
                        payPopupDismiss();
                    }
                });
        mcdt.start(); //开始倒计时
    }

    /**
     * 取消倒计时
     */
    private void stopTimer() {
        if (mcdt != null) {
            mcdt.cancel();
        }
    }

    /**
     * 获取付款码，启动被扫支付
     */
    private void doPay() {
        if (!isFinish) return;
        isFinish = false;
        //取数字付款码
        payCode = et_code.getText().toString();
        et_code.setText("");
        hideSoftInput(et_code);
        if (!StringUtils.isNumeric(payCode)) return;

        //取付款金额
        if (payAmount.contains(".")) {
            payAmount = payAmount.replace(".", "");
        }
        payAmount = "00000000000" + payAmount;
        payAmount = payAmount.substring(payAmount.length() - 12, payAmount.length());

        showLoadingDialog();
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    //定期签到
                    regularLogin();
                    //被扫支付
                    CppSdkInterf channel = new CppSdkInterf(getApplicationContext());
                    LogUtils.i("LKLPay", "Start Consume...");
                    LogUtils.i("LKLPay", "amount = " + payAmount + " code = " + payCode);

                    LKLCodePayQRTransCodeResult rslt = new LKLCodePayQRTransCodeResult();
                    int consumeRet = channel.doConsume(payAmount, "", payCode, "", "", rslt);
                    LogUtils.i("LKLPay", "ResultCode = " + Integer.toString(consumeRet));

                    subscriber.onNext(consumeRet);
                } catch (Exception e) {
                    hideLoadingDialog();
                    ExceptionUtils.handle(e);
                }
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onNext(final Integer resultCode) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingDialog();
                        if (resultCode == 0) {
                            createOrder();
                            payPopupDismiss();
                        } else {
                            showToast("扫码支付失败：" + ResponseCode.POSP_ERR.get(resultCode));
                        }
                        isFinish = true;
                    }
                }, 500);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onCompleted() {

            }
        });
    }

    /**
     * 创建订单
     */
    private void createOrder() {
        String printSn = AppApplication.getPrintCode();
        String timeStr = TimeUtils.getTimeStr("yyyy-MM-dd HH:mm:ss");
        //打印小票
        InvoiceEntity ie = new InvoiceEntity();
        ie.setShopLists(al_shop);
        ie.setPrintSn(printSn);
        ie.setCreateTime(timeStr);
        ie.setTotalPrice(String.valueOf(total));
        int status = stratPrint(ie) >= 0 ? 1 : 0;

        OrderEntity orderEn = new OrderEntity();
        orderEn.setCreateTime(timeStr);
        orderEn.setOrderNo("No" + TimeUtils.getReplaceStr(timeStr));
        orderEn.setCurrency(getString(R.string.goods_curr_rmb));
        orderEn.setPriceTotal(String.valueOf(total));
        orderEn.setPrintSn(printSn);
        orderEn.setPrintStatus(status);
        orderEn.setShopNum(num);
        orderEn.setShopLists(al_shop);

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < al_shop.size(); i++) {
            sb.append(al_shop.get(i).getUpc());
            sb.append("_");
            sb.append(al_shop.get(i).getNum());
            if (i < al_shop.size() - 1) {
                sb.append(",");
            }
        }
        orderEn.setShopTag(sb.toString());

        clearData(); //清空列表
        db_order.save(orderEn); //订单入库
    }

    /**
     * 处理“加入清单”事项
     */
    private void handleAddOrder() {
        String upc = et_code.getText().toString();
        if (upc.isEmpty()) {
            CommonTools.showToast(getString(R.string.text_input_code), 1000);
            return;
        }
        addData(upc);
    }

    /**
     * 新增商品
     * @param upc
     */
    private void addData(String upc) {
        GoodsEntity goodsEn = db_goods.find(upc);
        if (goodsEn != null) {
            int pos = -1;
            if (am_shop.containsKey(upc)) {
                pos = am_shop.get(upc);
            }
            if (pos >= 0 && pos < al_shop.size()) {
                al_shop.get(pos).setNum(al_shop.get(pos).getNum() + 1);
                al_shop.get(pos).setSubtotal(al_shop.get(pos).getPrice() * al_shop.get(pos).getNum());
            } else {
                ShopEntity data = new ShopEntity();
                data.setSku(goodsEn.getSku());
                data.setUpc(goodsEn.getUpc());
                data.setName(goodsEn.getName());
                data.setNum(1);
                data.setPrice(goodsEn.getPrice());
                data.setSubtotal(goodsEn.getPrice());
                al_shop.add(data);
                am_shop.put(upc, al_shop.size() - 1);
            }
            shopAdapter.updateDatas(al_shop);
            updateTotal();
        } else {
            showToast(getString(R.string.text_input_code_error));
        }
        et_code.setText("");
        hideSoftInput(et_code);
    }

    /**
     * 清空所有
     */
    private void clearData() {
        al_shop.clear();
        am_shop.clear();
        shopAdapter.updateDatas(al_shop);
        updateTotal();
    }

    /**
     * 更新总价
     */
    private void updateTotal() {
        num = 0;
        total = 0;
        for (int i = 0; i < al_shop.size(); i ++) {
            ShopEntity shopEn = al_shop.get(i);
            num += shopEn.getNum();
            total += shopEn.getSubtotal();
        }
        tv_total.setText(mContext.getString(R.string.goods_curr_rmb) + df.format(Double.valueOf(total)));
    }

    /**
     * 加载商品数据
     */
    private void loadGoods() {
        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jsonObj =new JSONObject();
        try {
            jsonObj.put("currentPage", page_goods);
        } catch (JSONException e) {
            ExceptionUtils.handle(e);
        }
        map.put("data", jsonObj.toString());
        loadDatas("getgoods", map, HttpRequests.HTTP_POST, AppConfig.DATA_TYPE_GOODS_LISTS);
    }

    /**
     * 加载分类数据
     */
    private void loadCategory() {
        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jsonObj =new JSONObject();
        try {
            jsonObj.put("currentPage", page_category);
        } catch (JSONException e) {
            ExceptionUtils.handle(e);
        }
        map.put("data", jsonObj.toString());
        loadDatas("getcategory", map, HttpRequests.HTTP_POST, AppConfig.DATA_TYPE_CATEGORY_LISTS);
    }

    @Override
    protected void callbackDatas(JSONObject jsonObject, int dataType) {
        boolean isGoodsOk = false;
        boolean isCategoryOk = false;
        BaseEntity baseEn;
        try {
            switch (dataType) {
                case AppConfig.DATA_TYPE_GOODS_LISTS:
                    baseEn = JsonUtils.getGoodsLists(jsonObject);
                    if (baseEn != null) {
                        if (baseEn.isSuccess() && baseEn.getLists() != null) {
                            List<GoodsEntity> lists = baseEn.getLists();
                            if (lists.size() == 0) {
                                //数据已完
                                isGoodsOk = true;
                                LogUtils.i("Retrofit", "商品数据加载完成 —> " + al_goods.size());
                            } else {
                                al_goods.addAll(lists);
                                LogUtils.i("Retrofit", "商品数据加载成功 —> " + page_goods);
                                page_goods++;
                                //循环加载
                                loadGoods();
                            }
                        } else {
                            //加载失败
                            LogUtils.i("Retrofit", "商品数据加载失败 —> " + page_goods);
                        }
                    } else {
                        //加载失败
                        LogUtils.i("Retrofit", "商品数据加载失败 —> " + page_goods);
                    }
                    break;
                case AppConfig.DATA_TYPE_CATEGORY_LISTS:
                    baseEn = JsonUtils.getCategoryLists(jsonObject);
                    if (baseEn != null) {
                        if (baseEn.isSuccess() && baseEn.getLists() != null) {
                            List<CategoryEntity> lists = baseEn.getLists();
                            if (lists.size() == 0) {
                                //数据已完
                                isCategoryOk = true;
                                LogUtils.i("Retrofit", "分类数据加载完成 —> " + al_category.size());
                            } else {
                                al_category.addAll(lists);
                                LogUtils.i("Retrofit", "分类数据加载成功 —> " + page_category);
                                page_category++;
                                //循环加载
                                loadCategory();
                            }
                        } else {
                            //加载失败
                            LogUtils.i("Retrofit", "分类数据加载失败 —> " + page_category);
                        }
                    } else {
                        //加载失败
                        LogUtils.i("Retrofit", "分类数据加载失败 —> " + page_category);
                    }
                    break;
            }
        } catch (JSONException e) {
            ExceptionUtils.handle(e);
        }finally {
            // 商品明细数据
            if (isGoodsOk && al_goods.size() > 0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db_goods.deleteAll(); //清空缓存
                        for (int i = 0; i < al_goods.size(); i++) {
                            db_goods.update(al_goods.get(i)); //缓存数据
                        }
                        LogUtils.i("Retrofit", "商品数据缓存完成 —> " + al_goods.size());
                        page_goods = 1;
                        al_goods.clear();
                        sp.edit().putString(AppConfig.KEY_UPDATE_TIME, TimeUtils.getTimeStr("yyyy-MM-dd")).apply();
                    }
                }).start();
            }
            // 商品分类数据
            if (isCategoryOk && al_category.size() > 0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db_category.deleteAll(); //清空缓存
                        for (int i = 0; i < al_category.size(); i++) {
                            db_category.update(al_category.get(i)); //缓存数据
                        }
                        LogUtils.i("Retrofit", "分类数据缓存完成 —> " + al_category.size());
                        page_category = 1;
                        al_category.clear();
                        sp.edit().putString(AppConfig.KEY_UPDATE_TIME, TimeUtils.getTimeStr("yyyy-MM-dd")).apply();
                    }
                }).start();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        regularUpdate();
        //获取管理密码
        password = sp.getString(AppConfig.KEY_PASSWORD, AppConfig.DEFAULT_PASSWORD);
    }

    /**
     * 定期同步
     */
    private void regularUpdate() {
        String saveTime = sp.getString(AppConfig.KEY_UPDATE_TIME, "");
        String nowTime = TimeUtils.getTimeStr("yyyy-MM-dd");
        if (!StringUtils.isNull(saveTime)) {
            long interval = TimeUtils.getTwoDay(nowTime, saveTime);
            if (interval < 1) return; //时间周期1天
        }
        updateAllDatas();
    }

    private void updateAllDatas() {
        loadGoods();
        loadCategory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message mMsg) {
            switch (mMsg.what) {
                case HANDLER_901: //删除
                    final int delPos = mMsg.getData().getInt("pos");
                    showTwoBtnDialog(getString(R.string.confirm_delete), getString(R.string.confirm_delete_bill),
                            "", "", true, true, new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    switch (msg.what) {
                                        case DIALOG_CANCEL:
                                            break;
                                        case DIALOG_CONFIRM:
                                            if (delPos >= 0 && delPos < al_shop.size()) {
                                                al_shop.remove(delPos);
                                                shopAdapter.updateDatas(al_shop);
                                                updateTotal();
                                                // 更新标记位
                                                am_shop.clear();
                                                for (int i = 0; i < al_shop.size(); i++) {
                                                    am_shop.put(al_shop.get(i).getUpc(), i);
                                                }
                                            }
                                            break;
                                    }
                                }
                            });
                    break;
                case HANDLER_902: //修改
                    final int updpos = mMsg.getData().getInt("pos");
                    showEditDialog("修改商品数量", InputType.TYPE_CLASS_NUMBER,true,
                            new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                                    switch (msg.what) {
                                        case DIALOG_CANCEL:
                                            break;
                                        case DIALOG_CONFIRM:
                                            int newNum = Integer.valueOf((String)msg.obj);
                                            if (newNum > 0) {
                                                dismissDialog();
                                                if (updpos >= 0 && updpos < al_shop.size()) {
                                                    al_shop.get(updpos).setNum(newNum);
                                                    al_shop.get(updpos).setSubtotal(al_shop.get(updpos).getPrice() * newNum);
                                                    shopAdapter.updateDatas(al_shop);
                                                    updateTotal();
                                                }
                                            } else {
                                                showToast("数量不能为0");
                                            }
                                            break;
                                    }
                                }
                            });
                    break;
            }
        }
    };

}
