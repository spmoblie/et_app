package com.elite.inventory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.elite.inventory.AppManager;
import com.elite.inventory.R;
import com.elite.inventory.fragment.Fragment_1;
import com.elite.inventory.fragment.Fragment_2;
import com.elite.inventory.fragment.Fragment_3;
import com.elite.inventory.fragment.Fragment_4;
import com.elite.inventory.fragment.Fragment_5;
import com.elite.inventory.fragment.Fragment_6;
import com.elite.inventory.fragment.Fragment_7;
import com.elite.inventory.fragment.Fragment_8;
import com.elite.inventory.fragment.Fragment_9;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * DrawerLayout右侧菜单栏
 * 兼容到8
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    @BindView(R.id.drawer_view)
    ConstraintLayout drawerView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.drawer_tv_1)
    TextView drawerTv1;

    @BindView(R.id.drawer_tv_2)
    TextView drawerTv2;

    @BindView(R.id.drawer_tv_3)
    TextView drawerTv3;

    @BindView(R.id.drawer_tv_4)
    TextView drawerTv4;

    @BindView(R.id.drawer_tv_5)
    TextView drawerTv5;

    @BindView(R.id.drawer_tv_6)
    TextView drawerTv6;

    @BindView(R.id.drawer_tv_7)
    TextView drawerTv7;

    @BindView(R.id.drawer_tv_8)
    TextView drawerTv8;

    @BindView(R.id.drawer_tv_9)
    TextView drawerTv9;

    @BindView(R.id.bt_drawer)
    ImageView bt_Drawer;

    @BindView(R.id.tv_title)
    TextView tv_title;

    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSystemUIVisibleListener(); //添加监听
        setSystemUIVisible(false); //全屏显示
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        AppManager.getInstance().addActivity(this);// 添加Activity到堆栈

        init();
        setListener();

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    private void init() {
        drawerTv1.setOnClickListener(this);
        drawerTv2.setOnClickListener(this);
        drawerTv3.setOnClickListener(this);
        drawerTv4.setOnClickListener(this);
        drawerTv5.setOnClickListener(this);
        drawerTv6.setOnClickListener(this);
        drawerTv7.setOnClickListener(this);
        drawerTv8.setOnClickListener(this);
        drawerTv9.setOnClickListener(this);

        fragments = new ArrayList<Fragment>();
        fragments.add(Fragment_1.newInstance(getString(R.string.main_item_01)));
        fragments.add(Fragment_2.newInstance(getString(R.string.main_item_02)));
        fragments.add(Fragment_3.newInstance(getString(R.string.main_item_03)));
        fragments.add(Fragment_4.newInstance(getString(R.string.main_item_04)));
        fragments.add(Fragment_5.newInstance(getString(R.string.main_item_05)));
        fragments.add(Fragment_6.newInstance(getString(R.string.main_item_06)));
        fragments.add(Fragment_7.newInstance(getString(R.string.main_item_07)));
        fragments.add(Fragment_8.newInstance(getString(R.string.main_item_08)));
        fragments.add(Fragment_9.newInstance(getString(R.string.main_item_09)));
    }

    private void setListener() {
        bt_Drawer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(drawerView)) {
                    drawerLayout.closeDrawer(drawerView);
                } else {
                    drawerLayout.openDrawer(drawerView);
                }
            }
        });
    }

    private void selectItem(int position) {
        Fragment fragment = fragments.get(position);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        drawerLayout.closeDrawer(drawerView);

        drawerTv1.setBackgroundResource(R.color.text_color_bg_no);
        drawerTv2.setBackgroundResource(R.color.text_color_bg_no);
        drawerTv3.setBackgroundResource(R.color.text_color_bg_no);
        drawerTv4.setBackgroundResource(R.color.text_color_bg_no);
        drawerTv5.setBackgroundResource(R.color.text_color_bg_no);
        drawerTv6.setBackgroundResource(R.color.text_color_bg_no);
        drawerTv7.setBackgroundResource(R.color.text_color_bg_no);
        drawerTv8.setBackgroundResource(R.color.text_color_bg_no);
        drawerTv9.setBackgroundResource(R.color.text_color_bg_no);

        switch (position) {
            case 0:
                tv_title.setText(getString(R.string.main_item_01));
                drawerTv1.setBackgroundResource(R.color.text_color_bg_se);
                break;
            case 1:
                tv_title.setText(getString(R.string.main_item_02));
                drawerTv2.setBackgroundResource(R.color.text_color_bg_se);
                break;
            case 2:
                tv_title.setText(getString(R.string.main_item_03));
                drawerTv3.setBackgroundResource(R.color.text_color_bg_se);
                break;
            case 3:
                tv_title.setText(getString(R.string.main_item_04));
                drawerTv4.setBackgroundResource(R.color.text_color_bg_se);
                break;
            case 4:
                tv_title.setText(getString(R.string.main_item_05));
                drawerTv5.setBackgroundResource(R.color.text_color_bg_se);
                break;
            case 5:
                tv_title.setText(getString(R.string.main_item_06));
                drawerTv6.setBackgroundResource(R.color.text_color_bg_se);
                break;
            case 6:
                tv_title.setText(getString(R.string.main_item_07));
                drawerTv7.setBackgroundResource(R.color.text_color_bg_se);
                break;
            case 7:
                tv_title.setText(getString(R.string.main_item_08));
                drawerTv8.setBackgroundResource(R.color.text_color_bg_se);
                break;
            case 8:
                tv_title.setText(getString(R.string.main_item_09));
                drawerTv9.setBackgroundResource(R.color.text_color_bg_se);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.drawer_tv_1:
                selectItem(0);
                break;
            case R.id.drawer_tv_2:
                selectItem(1);
                break;
            case R.id.drawer_tv_3:
                selectItem(2);
                break;
            case R.id.drawer_tv_4:
                selectItem(3);
                break;
            case R.id.drawer_tv_5:
                selectItem(4);
                break;
            case R.id.drawer_tv_6:
                selectItem(5);
                break;
            case R.id.drawer_tv_7:
                selectItem(6);
                break;
            case R.id.drawer_tv_8:
                selectItem(7);
                break;
            case R.id.drawer_tv_9:
                selectItem(8);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
     *
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
