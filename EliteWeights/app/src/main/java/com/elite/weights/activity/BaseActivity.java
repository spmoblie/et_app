package com.elite.weights.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.elite.weights.AppApplication;
import com.elite.weights.R;
import com.elite.weights.utils.DialogManager;
import com.elite.weights.utils.LogUtils;
import com.elite.weights.utils.MyFunc;

import java.text.DecimalFormat;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    public static final String TAG = "BaseActivity";
    public static BaseActivity instance = null;

    protected SharedPreferences sp;
    protected DecimalFormat df;
    protected CountDownTimer openCDT;
    protected CountDownTimer stopCDT;
    protected boolean isStop = true;
    protected boolean isStart = false;
    protected double weightGo = 0.000;
    protected String weightDo = "";
    protected String defaultWeightStr = "0000.00";

    private DialogManager dm = null;
    private int dmWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        LogUtils.i(TAG, "onCreate");

        instance = this;
        sp = AppApplication.getSharedPreferences();
        df = new DecimalFormat("0.00");
        dm = DialogManager.getInstance(this);
        dmWidth = AppApplication.screenWidth * 1/4;

        // 称重定时器
        openCDT = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (defaultWeightStr.equals(weightDo)) {
                    stopWeights();
                    isStop = true;
                    //开启下电定时器
                    stopCDT.cancel();
                    stopCDT.start();
                } else {
                    startWeights();
                    isStop = false;
                    //关闭下电定时器
                    stopCDT.cancel();
                }
                isStart = false;
            }
        };
        // 下电定时器
        stopCDT = new CountDownTimer(60*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                stopUHFWorking();
            }
        };
    }

    public void receiveDatas(byte[] bytes) {
        //解析数据
        //DispRecData(bytes);
        DispRecData2(bytes);
    }

    private void DispRecData(byte[] bytes) {
        //01 02 53 20 30 30 2E 30 30 30 6B 67 61 03 04
        if (bytes.length < 12) return;
        byte[] weightDos = new byte[5];
        byte[] weightGos = new byte[6];
        byte[] weightHex = new byte[9];
        System.arraycopy(bytes, 4, weightDos, 0, 5);
        weightDo = MyFunc.byteArrayToStr(weightDos);
        System.arraycopy(bytes, 4, weightGos, 0, 6);
        weightGo = Double.parseDouble(MyFunc.byteArrayToStr(weightGos));
        System.arraycopy(bytes, 3, weightHex, 0, 9);
        String weightStr = MyFunc.byteArrayToStr(weightHex);
        String str_1 = weightStr.substring(0, 1);
        String str_2 = weightStr.substring(1, 2);
        if (str_2.equals("0")) {
            weightStr = str_1 + weightStr.substring(2, weightStr.length());
        }
        showWeights(str_1, weightStr, weightDo, weightGo);
    }

    private void DispRecData2(byte[] bytes) {
        //77 6E 2D 30 30 30 30 2E 33 38 6B 67 0D 0A
        if (bytes.length < 12) return;
        byte[] weightDos = new byte[7];
        //byte[] weightGos = new byte[7];
        byte[] weightHex = new byte[10];
        System.arraycopy(bytes, 3, weightDos, 0, 7);
        weightDo = MyFunc.byteArrayToStr(weightDos);
        //System.arraycopy(bytes, 3, weightGos, 0, 7);
        //weightGo = Double.parseDouble(MyFunc.byteArrayToStr(weightGos));
        System.arraycopy(bytes, 2, weightHex, 0, 10);
        String weightStr = MyFunc.byteArrayToStr(weightHex);
        String str_1 = weightStr.substring(0, 1);
        String str_2 = weightStr.substring(1, 2);
        String str_3 = weightStr.substring(2, 3);
        String str_4 = weightStr.substring(3, 4);
        int subPos = 1;
        if (str_2.equals("0")) {
            subPos = 2;
            if (str_3.equals("0")) {
                subPos = 3;
                if (str_4.equals("0")) {
                    subPos = 4;
                }
            }
        }
        if (str_1.equals("0")) {
            str_1 = "";
        }
        weightStr = str_1 + weightStr.substring(subPos, weightStr.length());
        showWeights(str_1, weightStr, weightDo, weightGo);
    }

    /**
     * 回调串口数据
     */
    protected void showWeights(String str_1, String weightStr, String weightDo, double weightGo) {};

    /**
     * 称重开始
     */
    protected void startWeights() {};

    /**
     * 称重结束
     */
    protected void stopWeights() {};

    /**
     * 暂停称重时下电
     */
    protected void stopUHFWorking() {};

    /**
     * 弹出带输入框的对话框
     * @param title 标题
     * @param inputType 输入类型
     * @param isVanish 可否销毁
     * @param handler 回调
     */
    protected void showEditDialog(String title, int inputType, boolean isVanish, Handler handler) {
        if (dm != null) {
            dm.showEditDialog(title, dmWidth, inputType, isVanish, handler);
        }
    }

    /**
     * 销毁对话框
     */
    protected void dismissDialog() {
        if (dm != null) {
            dm.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dm != null) {
            dm.clearInstance();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
        LogUtils.i(TAG, "onDestroy");
    }

    @Override
    public void setContentView(int layoutResID) {
        addSystemUIVisibleListener(); //添加监听
        setSystemUIVisible(false); //全屏显示
        super.setContentView(layoutResID);
        //Butter Knife初始化
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        addSystemUIVisibleListener(); //添加监听
        setSystemUIVisible(false); //全屏显示
        super.setContentView(view);
        //Butter Knife初始化
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        addSystemUIVisibleListener(); //添加监听
        setSystemUIVisible(false); //全屏显示
        super.setContentView(view, params);
        //Butter Knife初始化
        ButterKnife.bind(this);
    }

    /**
     * 添加沉浸式模式变化的监听
     */
    public void addSystemUIVisibleListener() {
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (!getSystemUIVisible()) {
                            //非全屏时保持全屏
                            setSystemUIVisible(false);
                        }
                    }
                });
    }

    /**
     * 设置状态栏和导航栏状态
     * @param show
     */
    public void setSystemUIVisible(boolean show) {
        int visibility = getWindow().getDecorView().getSystemUiVisibility();
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (show) {
            visibility &= ~flags;
        } else {
            visibility |= flags;
        }
        getWindow().getDecorView().setSystemUiVisibility(visibility);
    }

    /**
     * 判定是否全屏
     */
    public boolean getSystemUIVisible() {
        int visibility = getWindow().getDecorView().getSystemUiVisibility();
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        return (visibility & flags) == flags;
    }
}
