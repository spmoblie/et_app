package com.elite.inventory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.elite.inventory.AppManager;
import com.elite.inventory.R;
import com.elite.inventory.fragment.Fragment_1;
import com.elite.inventory.fragment.Fragment_2;
import com.elite.inventory.fragment.Fragment_3;
import com.elite.inventory.fragment.Fragment_4;
import com.elite.inventory.fragment.Fragment_5;
import com.elite.inventory.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * DrawerLayout右侧菜单栏
 * 兼容到8
 */
public class MainActivity extends FragmentActivity {

    @BindView(R.id.bt_drawer)
    Button bt_Drawer;

    @BindView(R.id.drawer_view)
    ConstraintLayout drawerView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private String[] menus;
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
        menus = new String[]{
                "Home", "Fragment_1", "Fragment_2", "Fragment_3", "Fragment_4", "Fragment_5"
        };
        fragments = new ArrayList<Fragment>();
        fragments.add(HomeFragment.newInstance("Home"));
        fragments.add(Fragment_1.newInstance("Fragment_1"));
        fragments.add(Fragment_2.newInstance("Fragment_2"));
        fragments.add(Fragment_3.newInstance("Fragment_3"));
        fragments.add(Fragment_4.newInstance("Fragment_4"));
        fragments.add(Fragment_5.newInstance("Fragment_5"));
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
     * @param show
     */
    public void setSystemUIVisible(boolean show) {
        int visibility = getWindow().getDecorView().getSystemUiVisibility();
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
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
