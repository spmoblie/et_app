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
import android.widget.ImageView;
import android.widget.TextView;

import com.elite.inventory.AppApplication;
import com.elite.inventory.R;
import com.elite.inventory.config.AppConfig;
import com.elite.inventory.entity.BillEntity;
import com.elite.inventory.utils.StringUtils;
import com.elite.inventory.utils.ToastUtils;

import java.text.DecimalFormat;


public class BillDialog {

    private Context mContext;
    private Dialog mDialog;
    private DecimalFormat df;
    private TextView tv_sale_show, tv_sale_total;

    private int billNum = 1;
    private int typeCode, sale;
    private double total, derate;
    private double billPrice = 0;
    private String currStr;
    private StringBuffer sb_derate;


    public BillDialog(Context context) {
        this.mContext = context;
    }

    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 弹出折扣/减免对话框
     */
    public void showSaleDialog(String title, double total, int code, final Handler handler){
        // 销毁旧对话框
        dismiss();
        // 创建新对话框
        mDialog =  new Dialog(mContext, R.style.MyDialog);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setContentView(R.layout.dialog_sale);
        // 初始化对话框中的子控件
        df = new DecimalFormat("0.00");
        currStr = AppApplication.getCurrency();
        sb_derate = new StringBuffer();
        this.total = total;
        this.typeCode = code;

        TextView tv_title = (TextView) mDialog.findViewById(R.id.dialog_sale_title);
        tv_title.setText(title);
        tv_sale_show = (TextView) mDialog.findViewById(R.id.dialog_sale_show);
        switch (typeCode) {
            case 1:
                tv_sale_show.setText("%");
                break;
            case 2:
                tv_sale_show.setText("-" + currStr + "0");
                break;
        }
        tv_sale_total = (TextView) mDialog.findViewById(R.id.dialog_sale_total);
        tv_sale_total.setText(mContext.getString(R.string.goods_sum) + currStr + df.format(total));
        // 关闭
        ImageButton bt_close = (ImageButton) mDialog.findViewById(R.id.dialog_sale_close);
        bt_close.setOnClickListener(new MyOnClickListener());
        // 按键
        ImageView iv_number_1 = (ImageView) mDialog.findViewById(R.id.dialog_iv_number_01);
        ImageView iv_number_2 = (ImageView) mDialog.findViewById(R.id.dialog_iv_number_02);
        ImageView iv_number_3 = (ImageView) mDialog.findViewById(R.id.dialog_iv_number_03);
        ImageView iv_number_4 = (ImageView) mDialog.findViewById(R.id.dialog_iv_number_04);
        ImageView iv_number_5 = (ImageView) mDialog.findViewById(R.id.dialog_iv_number_05);
        ImageView iv_number_6 = (ImageView) mDialog.findViewById(R.id.dialog_iv_number_06);
        ImageView iv_number_7 = (ImageView) mDialog.findViewById(R.id.dialog_iv_number_07);
        ImageView iv_number_8 = (ImageView) mDialog.findViewById(R.id.dialog_iv_number_08);
        ImageView iv_number_9 = (ImageView) mDialog.findViewById(R.id.dialog_iv_number_09);
        ImageView iv_number_dot = (ImageView) mDialog.findViewById(R.id.dialog_iv_number_dot);
        ImageView iv_number_0 = (ImageView) mDialog.findViewById(R.id.dialog_iv_number_00);
        ImageView iv_number_00 = (ImageView) mDialog.findViewById(R.id.dialog_iv_number_000);
        ImageView iv_number_delete = (ImageView) mDialog.findViewById(R.id.dialog_iv_number_delete);
        ImageView iv_number_clear = (ImageView) mDialog.findViewById(R.id.dialog_iv_number_clear);
        ImageView iv_number_enter = (ImageView) mDialog.findViewById(R.id.dialog_iv_number_enter);
        iv_number_1.setOnClickListener(new MyOnClickListener());
        iv_number_2.setOnClickListener(new MyOnClickListener());
        iv_number_3.setOnClickListener(new MyOnClickListener());
        iv_number_4.setOnClickListener(new MyOnClickListener());
        iv_number_5.setOnClickListener(new MyOnClickListener());
        iv_number_6.setOnClickListener(new MyOnClickListener());
        iv_number_7.setOnClickListener(new MyOnClickListener());
        iv_number_8.setOnClickListener(new MyOnClickListener());
        iv_number_9.setOnClickListener(new MyOnClickListener());
        iv_number_dot.setOnClickListener(new MyOnClickListener());
        iv_number_0.setOnClickListener(new MyOnClickListener());
        iv_number_00.setOnClickListener(new MyOnClickListener());
        iv_number_delete.setOnClickListener(new MyOnClickListener());
        iv_number_clear.setOnClickListener(new MyOnClickListener());
        iv_number_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (handler != null) {
                    Message msg = Message.obtain();
                    msg.what = AppConfig.DIALOG_BT_RIGHT;
                    switch (typeCode) {
                        case 1:
                            msg.obj = sale;
                            break;
                        case 2:
                            msg.obj = derate;
                            break;
                    }
                    handler.sendMessage(msg);
                }
                dismiss();
            }
        });
        // 显示对话框
        mDialog.show();
    }

    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.dialog_sale_close:
                    dismiss();
                    break;
                case R.id.dialog_iv_number_01:
                    inputText("1");
                    break;
                case R.id.dialog_iv_number_02:
                    inputText("2");
                    break;
                case R.id.dialog_iv_number_03:
                    inputText("3");
                    break;
                case R.id.dialog_iv_number_04:
                    inputText("4");
                    break;
                case R.id.dialog_iv_number_05:
                    inputText("5");
                    break;
                case R.id.dialog_iv_number_06:
                    inputText("6");
                    break;
                case R.id.dialog_iv_number_07:
                    inputText("7");
                    break;
                case R.id.dialog_iv_number_08:
                    inputText("8");
                    break;
                case R.id.dialog_iv_number_09:
                    inputText("9");
                    break;
                case R.id.dialog_iv_number_dot:
                    inputText(".");
                    break;
                case R.id.dialog_iv_number_00:
                    inputText("0");
                    break;
                case R.id.dialog_iv_number_000:
                    inputText("00");
                    break;
                case R.id.dialog_iv_number_delete:
                    inputText("delete");
                    break;
                case R.id.dialog_iv_number_clear:
                    inputText("clear");
                    break;
            }
        }
    }

    private void inputText(String str) {
        switch (str) {
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
                switch (typeCode) {
                    case 1:
                        if (sale <= 0) {
                            sale = Integer.valueOf(str);
                        } else
                        if (sale < 10) {
                            sale = sale * 10 + Integer.valueOf(str);
                        }
                        tv_sale_show.setText(String.valueOf(sale) + "%");
                        break;
                    case 2:
                        //验证数值的合法性
                        String st = sb_derate.toString();
                        if (st.length() > 0) {
                            String fristChar = String.valueOf(st.charAt(0));
                            if (!st.contains(".") && fristChar.equals("0")) {
                                sb_derate.setLength(0);
                            }
                        }
                        sb_derate.append(str);
                        //截取小数点后两位
                        if (st.contains(".")) {
                            int lg = st.indexOf(".") + 3;
                            if (sb_derate.length() > lg) {
                                sb_derate.setLength(lg);
                                return;
                            }
                        }
                        //验证数值取值范围
                        double num = Double.valueOf(sb_derate.toString());
                        if (num > total) {
                            sb_derate.setLength(sb_derate.length() - str.length());
                            return;
                        }
                        derate = num;
                        tv_sale_show.setText("-" + currStr + sb_derate.toString());
                        break;
                }
                break;
            case "00":
                if (typeCode == 2) {
                    //验证数值的合法性
                    String st = sb_derate.toString();
                    if (st.length() <= 0) {
                        sb_derate.append("0");
                    } else
                    if (st.length() > 0) {
                        String fristChar = String.valueOf(st.charAt(0));
                        if (!st.contains(".") && fristChar.equals("0")) {
                            sb_derate.setLength(0);
                            sb_derate.append("0");
                        } else {
                            sb_derate.append(str);
                        }
                    }
                    //截取小数点后两位
                    if (st.contains(".")) {
                        int lg = st.indexOf(".") + 3;
                        if (sb_derate.length() > lg) {
                            sb_derate.setLength(lg);
                        }
                    }
                    //验证数值取值范围
                    double num = Double.valueOf(sb_derate.toString());
                    if (num > total) {
                        sb_derate.setLength(sb_derate.length()-1);
                        num = Double.valueOf(sb_derate.toString());
                        if (num > total) {
                            sb_derate.setLength(sb_derate.length()-1);
                            return;
                        }
                    }
                    derate = num;
                    tv_sale_show.setText("-" + currStr + sb_derate.toString());
                }
                break;
            case ".":
                if (typeCode == 2) {
                    if (sb_derate.toString().contains(".")) return;
                    if (sb_derate.length() <= 0) {
                        sb_derate.append("0.");
                    } else {
                        sb_derate.append(".");
                    }
                    tv_sale_show.setText("-" + currStr + sb_derate.toString());
                }
                break;
            case "delete":
                switch (typeCode) {
                    case 1:
                        if (sale >= 10) {
                            sale = (sale / 10) % 10;
                        } else
                        if (sale >= 0) {
                            sale = -1;
                        }
                        if (sale <= -1) {
                            tv_sale_show.setText("%");
                        } else {
                            tv_sale_show.setText(String.valueOf(sale) + "%");
                        }
                        break;
                    case 2:
                        String showStr = "";
                        if (sb_derate.length() > 0) {
                            sb_derate.setLength(sb_derate.length() - 1);
                            showStr = sb_derate.toString();
                            if (StringUtils.isNull(showStr)) {
                                derate = 0;
                            } else {
                                derate = Double.valueOf(showStr);
                            }
                        }
                        tv_sale_show.setText("-" + currStr + showStr);
                        break;
                }
                break;
            case "clear":
                switch (typeCode) {
                    case 1:
                        sale = -1;
                        tv_sale_show.setText("%");
                        break;
                    case 2:
                        derate = 0;
                        sb_derate.setLength(0);
                        tv_sale_show.setText("-" + currStr );
                        break;
                }
                break;
        }
    }

    /**
     * 弹出结账编辑对话框
     */
    public void showEditBillDialog(final BillEntity billEn, final Handler handler){
        if (billEn == null) return;
        // 销毁旧对话框
        dismiss();
        // 创建新对话框
        mDialog =  new Dialog(mContext, R.style.MyDialog);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContentView(R.layout.dialog_edit_bill);
        // 设置对话框的坐标及宽高
        LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = AppApplication.screenWidth / 3;
        mDialog.getWindow().setAttributes(lp);
        // 初始化对话框中的子控件
        df = new DecimalFormat("0.00");
        currStr = AppApplication.getCurrency();
        billNum = billEn.getNum();
        billPrice = billEn.getPrice();

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
                dismiss();
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
                dismiss();
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
                dismiss();
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
