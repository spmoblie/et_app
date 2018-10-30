package com.elite.inventory.utils;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.elite.inventory.AppApplication;
import com.elite.inventory.R;

public class ToastUtils {

    private static Toast toast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            toast.cancel();
            toast = null; //toast隐藏后，将其置为null
        }
    };

    /**
     * 显示Toast消息
     *
     * @param message 消息文本
     * @param time 消息显示的时长
     */
    public static void showToast(String message, long time) {
        Context ctx = AppApplication.getAppContext();
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_toast, null);
        TextView text = (TextView) view.findViewById(R.id.toast_message);
        text.setText(message);

        mHandler.removeCallbacks(r);
        if (toast == null){ //只有mToast==null时才重新创建，否则只需更改提示文字
            toast = new Toast(ctx);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, AppApplication.screenHeight / 5);
            toast.setView(view);
        }
        mHandler.postDelayed(r, time); //延迟隐藏toast
        toast.show();
    }

}
