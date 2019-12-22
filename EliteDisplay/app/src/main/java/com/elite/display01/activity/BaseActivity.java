package com.elite.display01.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.elite.display01.AppApplication;
import com.elite.display01.R;
import com.elite.display01.utils.CommonTools;
import com.elite.display01.utils.DialogManager;
import com.elite.display01.utils.LogUtil;
import com.elite.display01.utils.serialport.SerialPortUtil;

import butterknife.ButterKnife;

/**
 * Created by Beck on 2018/1/26.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static final String TAG = "BaseActivity";

    private DialogManager dm = null;
    private int dialogWidth;

    public static final int DIALOG_CONFIRM = 456; //全局对话框“确定”
    public static final int DIALOG_CANCEL = 887; //全局对话框“取消”

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        LogUtil.i(TAG, "onCreate");

        dm = DialogManager.getInstance(this);
        dialogWidth = AppApplication.screenWidth * 1/4;
    }

    /**
     * 打开串口
     */
    protected void openSerialPort(){
        SerialPortUtil.openSerialPort();
    }

    /**
     * 关闭串口
     */
    protected void closeSerialPort(){
        SerialPortUtil.closeSerialPort();
    }

    /**
     * 重启串口
     */
    protected void restartSerialPort(){
        closeSerialPort();
        openSerialPort();
    }

    /**
     * 接收到串口数据
     * @param hexData
     */
    protected void receiveDatas(String hexData){};

    /**
     * 弹出带输入框的对话框
     * @param title 标题
     * @param inputType 输入类型
     * @param isVanish 可否销毁
     * @param handler 回调
     */
    protected void showEditDialog(String title, int inputType, boolean isVanish, Handler handler) {
        if (dm != null) {
            dm.showEditDialog(title, dialogWidth, inputType, isVanish, handler);
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

    /**
     * 弹出消息
     */
    protected void showToast(String content) {
        showToast(content, 3000);
    }

    /**
     * 弹出消息
     */
    protected void showToast(String content, long time) {
        CommonTools.showToast(content, time);
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
        LogUtil.i(TAG, "onDestroy");
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
