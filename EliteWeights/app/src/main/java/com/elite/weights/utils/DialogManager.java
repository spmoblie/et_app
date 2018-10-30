package com.elite.weights.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.elite.weights.R;
import com.elite.weights.config.AppConfig;


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
     * 创建此对象请记得在Activity的onPause()中调用clearInstance()销毁对象
     */
    public static DialogManager getInstance(Context context) {
        if (instance == null) {
            syncInit(context);
        }
        return instance;
    }

    public void clearInstance(){
        instance = null;
    }

    public void dismiss(){
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏
    }

    /**
     * 弹出带输入框的对话框
     */
    public void showEditDialog(String titleStr, int width, int inputType, boolean isVanish, final Handler handler){
        // 销毁旧对话框
        dismiss();
        // 创建新对话框
        mDialog =  new Dialog(mContext, R.style.MyDialog);
        mDialog.setCanceledOnTouchOutside(isVanish);
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
        final Button left = (Button)mDialog.findViewById(R.id.dialog_button_cancel);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (handler != null) { //取消
                    handler.sendEmptyMessage(AppConfig.DIALOG_0002);
                }
                mDialog.dismiss();
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
                    msg.what = AppConfig.DIALOG_0001;
                    msg.obj = passStr;
                    handler.sendMessage(msg);
                }
                hideSoftInput(et_password); //隐藏软键盘
            }
        });
        // 显示对话框
        mDialog.show();
    }

}
