package com.elite.inventory.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.elite.inventory.AppApplication;
import com.elite.inventory.AppManager;
import com.elite.inventory.R;
import com.elite.inventory.utils.LogUtils;
import com.elite.inventory.utils.ToastUtils;

import java.lang.reflect.Method;


public class BaseActivity extends AppCompatActivity {

    public static final String TAG = "BaseActivity";

    protected SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // 添加Activity到堆栈
        AppManager.getInstance().addActivity(this);

        // 创建动画
        //overridePendingTransition(R.anim.in_from_right, R.anim.anim_no_anim);
        LogUtils.i(TAG, "onCreate");

        sp = AppApplication.getSharedPreferences();
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
        ToastUtils.showToast(content, time);
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏
    }

    /**
     * 隐藏物理键盘
     */
    protected void toggleSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 禁止弹出软件盘，光标依然正常显示。
     */
    protected void disableShowSoftInput(EditText et_view) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            et_view.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(et_view, false);
            } catch (Exception e) {

            }
            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(et_view, false);
            } catch (Exception e) {

            }
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
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i(TAG, "onDestroy");
    }

    @Override
    public void finish() {
        super.finish();
        // 销毁动画
        //overridePendingTransition(R.anim.anim_no_anim, R.anim.out_to_left);
    }

    @Override
    public void setContentView(int layoutResID) {
        addSystemUIVisibleListener(); //添加监听
        setSystemUIVisible(false); //全屏显示
        super.setContentView(layoutResID);
        //Butter Knife初始化
        //ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        addSystemUIVisibleListener(); //添加监听
        setSystemUIVisible(false); //全屏显示
        super.setContentView(view);
        //Butter Knife初始化
        //ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        addSystemUIVisibleListener(); //添加监听
        setSystemUIVisible(false); //全屏显示
        super.setContentView(view, params);
        //Butter Knife初始化
        //ButterKnife.bind(this);
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
