package com.elite.inventory.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnKeyListener;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.elite.inventory.R;
import com.elite.inventory.config.AppConfig;
import com.elite.inventory.utils.StringUtils;

import java.util.Arrays;
import java.util.List;


public class DialogManager {

    private static DialogManager instance;
    private Context mContext;
    private Dialog mDialog;

    private DialogManager(Context context) {
        this.mContext = context;
    }

    private static synchronized void syncInit(Context context) {
        if (instance == null) {
            instance = new DialogManager(context);
        }
    }

    /**
     * 创建此对象请切记要在Activity的onPause()中调用clearInstance()销毁对象
     */
    public static DialogManager getInstance(Context context) {
        if (instance == null) {
            syncInit(context);
        }
        return instance;
    }

    public void clearInstance(){
        dismiss();
        instance = null;
    }

    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 弹出下载缓冲对话框
     *
     * @param width 对话框宽度
     * @param keylistener 物理键盘监听器
     */
    public void showLoadDialog(int width, OnKeyListener keylistener){
        // 销毁旧对话框
        /*dismiss();
        // 创建新对话框
        mDialog = new Dialog(mContext, R.style.MyDialog);
        mDialog.setCanceledOnTouchOutside(false);
        if (keylistener != null) {
            mDialog.setOnKeyListener(keylistener);
        }
        mDialog.setContentView(R.layout.dialog_download);
        // 设置对话框的坐标及宽高
        LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = width;
        mDialog.getWindow().setAttributes(lp);
        // 显示对话框
        mDialog.show();*/
    }

    /**
     * 弹出一个按钮的通用对话框
     *
     * @param contentStr 提示内容
     * @param width 对话框宽度
     * @param isCenter 提示内容是否居中
     * @param isVanish 点击框以外是否消失
     * @param keylistener 物理键盘监听器
     */
    public void showOneBtnDialog(String contentStr, int width, boolean isCenter,
                                 boolean isVanish, final Handler handler, OnKeyListener keylistener){
        // 销毁旧对话框
        /*dismiss();
        // 创建新对话框
        mDialog =  new Dialog(mContext, R.style.MyDialog);
        mDialog.setCanceledOnTouchOutside(isVanish);
        if (keylistener != null) {
            mDialog.setOnKeyListener(keylistener);
        }
        mDialog.setContentView(R.layout.dialog_btn_one);
        // 设置对话框的坐标及宽高
        LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = width;
        mDialog.getWindow().setAttributes(lp);
        // 初始化对话框中的子控件
        final TextView content = (TextView)mDialog.findViewById(R.id.dialog_content);
        content.setText(contentStr);
        if (!isCenter) { //不居中
            content.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        }
        final Button ok = (Button)mDialog.findViewById(R.id.dialog_button_ok);
        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (handler != null) { //确定
                    handler.sendEmptyMessage(BaseActivity.DIALOG_CONFIRM_CLICK);
                }
                dismiss();
            }
        });
        // 显示对话框
        mDialog.show();*/
    }

    /**
     * 弹出两个按钮的通用对话框
     *
     * @param titleStr 对话框标题
     * @param contentStr 对话框内容
     * @param leftStr 对话框左边按钮文本
     * @param rightStr 对话框右边按钮文本
     * @param width 对话框宽度
     * @param isCenter 提示内容是否居中
     * @param isVanish 点击框以外是否消失
     */
    public void showTwoBtnDialog(String titleStr, String contentStr, String leftStr, String rightStr,
                                 int width, boolean isCenter, boolean isVanish, final Handler handler){
        // 销毁旧对话框
        dismiss();
        // 创建新对话框
        mDialog =  new Dialog(mContext, R.style.MyDialog);
        mDialog.setCanceledOnTouchOutside(isVanish);
        mDialog.setContentView(R.layout.dialog_btn_two);
        // 设置对话框的坐标及宽高
        LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = width;
        mDialog.getWindow().setAttributes(lp);
        // 初始化对话框中的子控件
        final TextView title = (TextView) mDialog.findViewById(R.id.dialog_title);
        if (!StringUtils.isNull(titleStr)) {
            title.setText(titleStr);
            title.setVisibility(View.VISIBLE);
        }
        final TextView content = (TextView)mDialog.findViewById(R.id.dialog_contents);
        content.setText(contentStr);
        if (!isCenter) { //不居中
            content.setGravity(Gravity.LEFT| Gravity.CENTER_VERTICAL);
        }
        final Button left = (Button)mDialog.findViewById(R.id.dialog_button_cancel);
        if (!StringUtils.isNull(leftStr)) {
            left.setText(leftStr);
        }
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (handler != null) { //取消
                    handler.sendEmptyMessage(AppConfig.DIALOG_BT_LEFT);
                }
                dismiss();
            }
        });
        final Button right = (Button)mDialog.findViewById(R.id.dialog_button_confirm);
        if (!StringUtils.isNull(rightStr)) {
            right.setText(rightStr);
        }
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (handler != null) { //确定
                    handler.sendEmptyMessage(AppConfig.DIALOG_BT_RIGHT);
                }
                dismiss();
            }
        });
        // 显示对话框
        mDialog.show();
    }

    /**
     * 弹出带输入框的对话框
     */
    public void showEditDialog(String titleStr, String contentStr, int width, int inputType, final Handler handler){
        // 销毁旧对话框
        dismiss();
        // 创建新对话框
        mDialog =  new Dialog(mContext, R.style.MyDialog);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContentView(R.layout.dialog_edit);
        // 设置对话框的坐标及宽高
        LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = width;
        mDialog.getWindow().setAttributes(lp);
        // 初始化对话框中的子控件
        final TextView title = (TextView) mDialog.findViewById(R.id.dialog_title);
        if (!StringUtils.isNull(titleStr)) {
            title.setText(titleStr);
            title.setVisibility(View.VISIBLE);
        }
        final EditText et_password = (EditText) mDialog.findViewById(R.id.dialog_et_password);
        et_password.setInputType(inputType);
        if (!StringUtils.isNull(contentStr)) {
            et_password.setText(contentStr);
            et_password.setSelectAllOnFocus(true);
            //et_password.setSelection(et_password.length());
        }
        final Button left = (Button)mDialog.findViewById(R.id.dialog_button_cancel);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (handler != null) { //取消
                    handler.sendEmptyMessage(AppConfig.DIALOG_BT_LEFT);
                }
                hideSoftInput(et_password);
                dismiss();
            }
        });
        final Button right = (Button)mDialog.findViewById(R.id.dialog_button_confirm);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passStr = et_password.getText().toString();
                if (passStr.isEmpty()) return;
                if (handler != null) { //确定
                    Message msg = Message.obtain();
                    msg.what = AppConfig.DIALOG_BT_RIGHT;
                    msg.obj = passStr;
                    handler.sendMessage(msg);
                }
                hideSoftInput(et_password);
                //dismiss();
            }
        });
        // 显示对话框
        mDialog.show();
    }

    /**
     * 弹出列表形式的对话框
     */
    public void showListItemDialog(CharSequence[] items, int width, final boolean isCenter, final Handler handler){
        // 销毁旧对话框
        dismiss();
        // 创建新对话框
        mDialog =  new Dialog(mContext, R.style.MyDialog);
        mDialog.setContentView(R.layout.dialog_listview);
        // 设置对话框的坐标及宽高
        LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = width;
        mDialog.getWindow().setAttributes(lp);
        // 初始化对话框中的子控件
        ListView lv = (ListView) mDialog.findViewById(R.id.dialog_list_lv);
        lv.setSelector(R.color.back_color_block);
        final List<CharSequence> itemList = Arrays.asList(items);
        @SuppressWarnings({ "unchecked", "rawtypes" })
        ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, itemList) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv_item = (TextView) view.findViewById(android.R.id.text1);
                tv_item.setPadding(30, 10, 30, 10);
                tv_item.setTextSize(18);
                if (position == itemList.size() - 1) {
                    tv_item.setTextColor(mContext.getResources().getColor(R.color.text_color_conte, null));
                } else {
                    tv_item.setTextColor(mContext.getResources().getColor(R.color.text_color_black, null));
                }
                if (!isCenter) { //不居中
                    tv_item.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
                } else {
                    tv_item.setGravity(Gravity.CENTER);
                }
                return view;
            }

        };
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (handler != null) {
                    handler.sendEmptyMessage(position);
                }
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
