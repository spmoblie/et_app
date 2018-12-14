package com.elite.inventory.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.elite.inventory.AppApplication;
import com.elite.inventory.R;
import com.elite.inventory.config.AppConfig;
import com.elite.inventory.entity.BillEntity;
import com.elite.inventory.utils.StringUtils;
import com.elite.inventory.utils.ToastUtils;

import java.text.DecimalFormat;


public class GoodsDialog {

    private Context mContext;
    private Dialog mDialog;
    private DecimalFormat df;

    private String currStr;
    private int billNum = 1;
    private double billPrice = 0;


    public GoodsDialog(Context context) {
        this.mContext = context;
    }

    public void dismiss(){
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 弹出商品编辑对话框
     */
    public void showEditGoodsDialog(final BillEntity billEn, final Handler handler){
        // 销毁旧对话框
        dismiss();
        // 创建新对话框
        mDialog =  new Dialog(mContext, R.style.MyDialog);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContentView(R.layout.dialog_edit_goods);
        // 设置对话框的坐标及宽高
        LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = AppApplication.screenWidth / 3;
        mDialog.getWindow().setAttributes(lp);
        // 初始化对话框中的子控件
        df = new DecimalFormat("0.00");
        currStr = AppApplication.getCurrency();

        final TextView tv_name = (TextView) mDialog.findViewById(R.id.dialog_tv_bill_name);
        tv_name.setText(billEn.getName());

        final TextView tv_total = (TextView) mDialog.findViewById(R.id.dialog_tv_bill_total_2);
        tv_total.setText(currStr + df.format((billNum*billPrice)));

        final EditText et_num = (EditText) mDialog.findViewById(R.id.dialog_et_bill_num);
        et_num.setText(String.valueOf(billNum));
        et_num.setSelection(et_num.length());
        et_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (StringUtils.isNull(editable.toString())) {
                    ToastUtils.showToast(mContext.getString(R.string.text_input_num_null), 1000);
                    return;
                }
                billNum = Integer.valueOf(editable.toString());
                if (billNum <= 0) {
                    ToastUtils.showToast(mContext.getString(R.string.text_input_num_0), 1000);
                    return;
                }
                tv_total.setText(currStr + df.format((billNum*billPrice)));
            }
        });

        final EditText et_price = (EditText) mDialog.findViewById(R.id.dialog_et_bill_price);
        et_price.setText(String.valueOf(billPrice));
        et_price.setSelection(et_num.length());
        et_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (StringUtils.isNull(editable.toString())) {
                    ToastUtils.showToast(mContext.getString(R.string.text_input_price_null), 1000);
                    return;
                }
                if (".".equals(editable.toString())) {
                    ToastUtils.showToast(mContext.getString(R.string.text_input_price_error), 1000);
                    return;
                }
                billPrice = Double.valueOf(editable.toString());
                if (billPrice <= 0) {
                    ToastUtils.showToast(mContext.getString(R.string.text_input_price_0), 1000);
                    return;
                }
                tv_total.setText(currStr + df.format((billNum*billPrice)));
            }
        });

        final ImageButton iv_delete = (ImageButton) mDialog.findViewById(R.id.dialog_ib_delete);
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftInput(et_num);
                mDialog.dismiss();
            }
        });

        final Button left = (Button) mDialog.findViewById(R.id.dialog_button_cancel);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (handler != null) { //删除
                    handler.sendEmptyMessage(AppConfig.DIALOG_BT_LEFT);
                }
                hideSoftInput(et_num);
                mDialog.dismiss();
            }
        });

        final Button right = (Button) mDialog.findViewById(R.id.dialog_button_confirm);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (billNum <= 0) {
                    ToastUtils.showToast(mContext.getString(R.string.text_input_num_0), 1000);
                    return;
                }
                if (billPrice <= 0) {
                    ToastUtils.showToast(mContext.getString(R.string.text_input_price_0), 1000);
                    return;
                }
                billEn.setNum(billNum);
                billEn.setPrice(billPrice);
                billEn.setSubtotal(billNum*billPrice);
                if (handler != null) { //确定
                    Message msg = Message.obtain();
                    msg.what = AppConfig.DIALOG_BT_RIGHT;
                    msg.obj = billEn;
                    handler.sendMessage(msg);
                }
                hideSoftInput(et_num);
                mDialog.dismiss();
            }

        });
        // 显示对话框
        mDialog.show();
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏
    }

}
