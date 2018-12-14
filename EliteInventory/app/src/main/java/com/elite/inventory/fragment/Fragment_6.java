package com.elite.inventory.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elite.inventory.AppApplication;
import com.elite.inventory.R;
import com.elite.inventory.adapter.ClassifyLeftAdapter;
import com.elite.inventory.adapter.ClassifyRightAdapter;
import com.elite.inventory.adapter.SearchAdapter;
import com.elite.inventory.config.AppConfig;
import com.elite.inventory.db.CategoryDBService;
import com.elite.inventory.db.GoodsDBService;
import com.elite.inventory.db.OrderDBService;
import com.elite.inventory.dialog.DialogManager;
import com.elite.inventory.entity.CategoryEntity;
import com.elite.inventory.entity.GoodsEntity;
import com.elite.inventory.utils.StringUtils;
import com.elite.inventory.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Fragment_6 extends Fragment implements View.OnClickListener{

    public static final String TAG = "Fragment_6";

    private static final int DG_WIDTH = AppApplication.screenWidth/4;
    private static final int LOAD_DB_TOP_DATA = 1001;
    private static final int LOAD_DB_LEFT_DATA = 1002;
    private static final int LOAD_DB_RIGHT_DATA = 1003;
    private static final int LOAD_DB_KEYWORD_DATA = 1004;

    @BindView(R.id.query_hsv)
    HorizontalScrollView hsv_bar;

    @BindView(R.id.query_ll_bar)
    LinearLayout ll_bar;

    @BindView(R.id.query_rv_classify_left)
    RecyclerView rv_classify_left;

    @BindView(R.id.query_rv_classify_right)
    RecyclerView rv_classify_right;

    @BindView(R.id.query_srl_classify)
    SwipeRefreshLayout srl_classify;

    @BindView(R.id.query_et_keyword)
    EditText et_keyword;

    @BindView(R.id.query_rv_search)
    RecyclerView rv_search;

    @BindView(R.id.ib_new_add_1)
    ImageButton ib_new_add_1;

    @BindView(R.id.ib_new_add_2)
    ImageButton ib_new_add_2;

    @BindView(R.id.ib_new_add_3)
    ImageButton ib_new_add_3;

    @BindView(R.id.ib_new_edit_1)
    ImageButton ib_new_edit_1;

    @BindView(R.id.ib_new_edit_2)
    ImageButton ib_new_edit_2;

    private Context mContext;
    private DialogManager dm;
    private GoodsDBService db_goods;
    private CategoryDBService db_category;
    private CountDownTimer mCdt;
    private SearchAdapter searchAdapter;
    private ClassifyLeftAdapter clAdapter;
    private ClassifyRightAdapter crAdapter;

    private View view;
    private Unbinder unbinder;

    private int parentPos = 0;
    private int childPos = 0;
    private int parentId, childId;
    private String keyword;
    private boolean isNewAdd = false;
    private boolean isDelete = false;

    private ArrayList<TextView> al_tv = new ArrayList<TextView>();
    private ArrayList<CategoryEntity> al_top = new ArrayList<CategoryEntity>();
    private ArrayList<CategoryEntity> al_left = new ArrayList<CategoryEntity>();
    private ArrayList<GoodsEntity> al_goods = new ArrayList<GoodsEntity>();
    private ArrayList<GoodsEntity> al_word = new ArrayList<GoodsEntity>();

    public static Fragment_6 newInstance(String content) {
        Fragment_6 fragment = new Fragment_6();
        Bundle args = new Bundle();
        args.putString(TAG, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        dm = DialogManager.getInstance(mContext);
        db_goods = GoodsDBService.getInstance(mContext);
        db_category = CategoryDBService.getInstance(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_6, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();

        return view;
    }

    private void initEditText() {
        // 创建定时器
        mCdt = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                handleKeywordData();
            }
        };
        // 添加键盘监听器
        et_keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                    handleKeywordData();
                    return true;
                }
                return false;
            }
        });
        // 添加输入监听器
        et_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword = s.toString();
                mCdt.cancel();
                mCdt.start();
            }
        });
    }

    private void init() {
        ib_new_add_1.setOnClickListener(this);
        ib_new_add_2.setOnClickListener(this);
        ib_new_add_3.setOnClickListener(this);
        ib_new_edit_1.setOnClickListener(this);
        ib_new_edit_2.setOnClickListener(this);

        initData();
        initEditText();
        initRecyclerView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_new_add_1:
                dm.showEditDialog("新增首级分类名称", "", DG_WIDTH, InputType.TYPE_CLASS_TEXT,
                        new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                switch (msg.what) {
                                    case AppConfig.DIALOG_BT_LEFT:
                                        break;
                                    case AppConfig.DIALOG_BT_RIGHT:
                                        String name = (String) msg.obj;
                                        if (StringUtils.isNull(name)) {
                                            ToastUtils.showToast("名称不能为空", 1000);
                                            return;
                                        }
                                        if (db_category.isNameExist(name)) {
                                            ToastUtils.showToast("分类名称重复", 1000);
                                            return;
                                        }
                                        dm.dismiss();

                                        CategoryEntity ce_new = new CategoryEntity();
                                        ce_new.setClassName(name);
                                        ce_new.setParentId(0);
                                        db_category.save(ce_new);

                                        isNewAdd = true;
                                        loadCategoryTopData();
                                        break;
                                }
                            }
                        });
                break;
            case R.id.ib_new_add_2:
                dm.showEditDialog("新增子级分类名称", "", DG_WIDTH, InputType.TYPE_CLASS_TEXT,
                        new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                switch (msg.what) {
                                    case AppConfig.DIALOG_BT_LEFT:
                                        break;
                                    case AppConfig.DIALOG_BT_RIGHT:
                                        String name = (String) msg.obj;
                                        if (StringUtils.isNull(name)) {
                                            ToastUtils.showToast("分类名称不能为空", 1000);
                                            return;
                                        }
                                        if (db_category.isNameExist(name)) {
                                            ToastUtils.showToast("分类名称重复", 1000);
                                            return;
                                        }
                                        dm.dismiss();

                                        if (parentId <= 0) {
                                            ToastUtils.showToast("请先增加首级分类", 1000);
                                            return;
                                        }

                                        CategoryEntity ce_new = new CategoryEntity();
                                        ce_new.setClassName(name);
                                        ce_new.setParentId(parentId);
                                        db_category.save(ce_new);

                                        isNewAdd = true;
                                        loadCategoryLeftData(parentId);
                                        break;
                                }
                            }
                        });
                break;
            case R.id.ib_new_add_3:

                break;
            case R.id.ib_new_edit_1:
                if (parentId <= 0) return;
                CharSequence[] cs_1 = new CharSequence[]{"重命名", "删除首级分类"};
                dm.showListItemDialog(cs_1, AppApplication.screenWidth/6, true,
                        new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                switch (msg.what) {
                                    case 0: //重命名
                                        String oldName = al_top.get(parentPos).getClassName();
                                        dm.showEditDialog("修改首级分类名称", oldName, DG_WIDTH, InputType.TYPE_CLASS_TEXT,
                                                new Handler(){
                                                    @Override
                                                    public void handleMessage(Message msg) {
                                                        switch (msg.what) {
                                                            case AppConfig.DIALOG_BT_RIGHT:
                                                                String name = (String) msg.obj;
                                                                if (StringUtils.isNull(name)) {
                                                                    ToastUtils.showToast("名称不能为空", 1000);
                                                                    return;
                                                                }
                                                                if (db_category.isNameExist(name)) {
                                                                    ToastUtils.showToast("分类名称重复", 1000);
                                                                    return;
                                                                }
                                                                dm.dismiss();

                                                                CategoryEntity ce_new = al_top.get(parentPos);
                                                                ce_new.setClassName(name);
                                                                db_category.update(ce_new);

                                                                al_top.get(parentPos).setClassName(name);
                                                                initBarTitle();
                                                                break;
                                                        }
                                                    }
                                                });
                                        break;
                                    case 1: //删除
                                        dm.showTwoBtnDialog("", "确定要删除首级分类？",
                                                "", "", DG_WIDTH, true, true,
                                                new Handler() {
                                                    @Override
                                                    public void handleMessage(Message msg) {
                                                        switch (msg.what) {
                                                            case AppConfig.DIALOG_BT_RIGHT:
                                                                db_category.deleteChild(parentId);
                                                                db_category.delete(parentId);
                                                                childPos = 0;
                                                                loadCategoryTopData();
                                                                break;
                                                        }
                                                    }
                                                });
                                        break;
                                }
                            }
                        });
                break;
            case R.id.ib_new_edit_2:
                if (childId <= 0) return;
                CharSequence[] cs_2 = new CharSequence[]{"重命名", "删除子级分类"};
                dm.showListItemDialog(cs_2, AppApplication.screenWidth/6, true,
                        new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                switch (msg.what) {
                                    case 0: //重命名
                                        String oldName = al_left.get(childPos).getClassName();
                                        dm.showEditDialog("修改子级分类名称", oldName, DG_WIDTH, InputType.TYPE_CLASS_TEXT,
                                                new Handler(){
                                                    @Override
                                                    public void handleMessage(Message msg) {
                                                        switch (msg.what) {
                                                            case AppConfig.DIALOG_BT_RIGHT:
                                                                String name = (String) msg.obj;
                                                                if (StringUtils.isNull(name)) {
                                                                    ToastUtils.showToast("名称不能为空", 1000);
                                                                    return;
                                                                }
                                                                if (db_category.isNameExist(name)) {
                                                                    ToastUtils.showToast("分类名称重复", 1000);
                                                                    return;
                                                                }
                                                                dm.dismiss();

                                                                CategoryEntity ce_new = al_left.get(childPos);
                                                                ce_new.setClassName(name);
                                                                db_category.update(ce_new);

                                                                al_top.get(parentPos).getChildLs().get(childPos).setClassName(name);
                                                                al_left.get(childPos).setClassName(name);
                                                                clAdapter.updateDatas(al_left, childPos);
                                                                break;
                                                        }
                                                    }
                                                });
                                        break;
                                    case 1: //删除
                                        dm.showTwoBtnDialog("", "确定要删除子级分类？",
                                                "", "", DG_WIDTH, true, true,
                                                new Handler() {
                                                    @Override
                                                    public void handleMessage(Message msg) {
                                                        switch (msg.what) {
                                                            case AppConfig.DIALOG_BT_RIGHT:
                                                                isDelete = db_category.delete(childId);
                                                                loadCategoryLeftData(parentId);
                                                                break;
                                                        }
                                                    }
                                                });
                                        break;
                                }
                            }
                        });
                break;
        }
    }

    private void initData() {
        loadCategoryTopData();
    }

    private void initBarTitle() {
        ll_bar.removeAllViews();
        al_tv.clear();
        for (int i = 0; i < al_top.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_br_query, ll_bar, false);
            final int pos = i;
            final int classId = al_top.get(i).getClassId();
            final TextView tv_item = (TextView) view.findViewById(R.id.item_tv_bar);
            tv_item.setText(al_top.get(i).getClassName());
            al_tv.add(tv_item);
            if (pos == parentPos) {
                tv_item.setTextColor(getResources().getColor(R.color.text_color_state, null));
                tv_item.setBackgroundColor(getResources().getColor(R.color.back_color_block, null));
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parentPos = pos;
                    childPos = 0;
                    loadCategoryLeftData(classId);
                    for (int i = 0; i < al_tv.size(); i++) {
                        TextView tv_item = al_tv.get(i);
                        tv_item.setTextColor(getResources().getColor(R.color.text_color_white, null));
                        tv_item.setBackgroundColor(getResources().getColor(R.color.back_color_state, null));
                    }
                    tv_item.setTextColor(getResources().getColor(R.color.text_color_state, null));
                    tv_item.setBackgroundColor(getResources().getColor(R.color.back_color_block, null));
                }
            });
            ll_bar.addView(view);
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager_1 = new LinearLayoutManager(mContext);
        layoutManager_1.setOrientation(LinearLayoutManager.VERTICAL);
        rv_classify_left.setLayoutManager(layoutManager_1);
        clAdapter = new ClassifyLeftAdapter(mContext, al_left);
        clAdapter.setOnItemClickListener(new CLOnItemClickListener());
        rv_classify_left.setAdapter(clAdapter);

        LinearLayoutManager layoutManager_2 = new LinearLayoutManager(mContext);
        layoutManager_2.setOrientation(LinearLayoutManager.VERTICAL);
        rv_classify_right.setLayoutManager(layoutManager_2);
        crAdapter = new ClassifyRightAdapter(mContext, al_goods);
        crAdapter.setOnItemClickListener(new CROnItemClickListener());
        rv_classify_right.setAdapter(crAdapter);

        LinearLayoutManager layoutManager_3 = new LinearLayoutManager(mContext);
        layoutManager_3.setOrientation(LinearLayoutManager.VERTICAL);
        rv_search.setLayoutManager(layoutManager_3);
        searchAdapter = new SearchAdapter(mContext, al_word, keyword);
        searchAdapter.setOnItemClickListener(new SearchOnItemClickListener());
        rv_search.setAdapter(searchAdapter);

        // 添加事件监听器
        initLoadMoreListener();
        initPullRefreshListener();
    }

    /**
     * 上拉翻页监听
     */
    private void initLoadMoreListener() {
        rv_classify_right.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem ;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //判断RecyclerView的状态 是空闲时，同时，是最后一个可见的Item时才加载
                if(newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == crAdapter.getItemCount()){

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            /*List<GoodsEntity> footerDatas = new ArrayList<GoodsEntity>();
                            classifyAdapter.AddFooterItem(footerDatas);*/
                        }
                    }, 3000);

                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的Item
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    /**
     * 下拉刷新监听
     */
    private void initPullRefreshListener() {
        srl_classify.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        srl_classify.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /*List<ShopEntity> headDatas = new ArrayList<ShopEntity>();
                        classifyAdapter.AddHeaderItem(headDatas);*/
                        //刷新完成
                        srl_classify.setRefreshing(false);
                    }

                }, 1000);

            }
        });
    }

    /**
     * 加载一级分类数据
     */
    private void loadCategoryTopData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                al_top.clear();
                List<CategoryEntity> dbLists = db_category.getListData(0);
                if (dbLists != null) {
                    List<CategoryEntity> childs;
                    for (int i = 0; i < dbLists.size(); i++) {
                        childs = db_category.getListData(dbLists.get(i).getClassId());
                        if (childs != null) {
                            dbLists.get(i).setChildLs(childs);
                        }
                    }
                    al_top.addAll(dbLists);
                }
                mHandler.sendEmptyMessage(LOAD_DB_TOP_DATA);
            }
        }).start();
    }

    /**
     * 加载指定二级分类数据
     */
    private void loadCategoryLeftData(final int dataId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                al_left.clear();
                parentId = dataId;
                if (isNewAdd) { //新增 从数据库加载更新
                    al_top.get(parentPos).setChildLs(db_category.getListData(parentId));
                } else
                if (isDelete) { //删除 从缓存移除后更新
                    isDelete = false;
                    al_top.get(parentPos).getChildLs().remove(childPos);
                }
                al_left.addAll(al_top.get(parentPos).getChildLs());
                mHandler.sendEmptyMessage(LOAD_DB_LEFT_DATA);
            }
        }).start();

    }

    /**
     * 加载指定三级分类数据
     */
    private void loadCategoryRightData(final int dataId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                al_goods.clear();
                childId = dataId;
                List<GoodsEntity> dbLists = db_goods.getListData(childId);
                if (dbLists != null) {
                    al_goods.addAll(dbLists);
                }
                mHandler.sendEmptyMessage(LOAD_DB_RIGHT_DATA);
            }
        }).start();
    }

    /**
     * 处理关键字搜索事项
     */
    private void handleKeywordData() {
        if (mCdt != null) {
            mCdt.cancel();
        }
        hideSoftInput(et_keyword);
        if (!StringUtils.isNull(keyword)) {
            loadKeywordData();
        } else {
            clearKeywordData();
        }
    }

    /**
     * 匹配搜索关键字
     */
    private void loadKeywordData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                al_word.clear();
                List<GoodsEntity> dbLists = db_goods.getKeywordData(keyword);
                if (dbLists != null) {
                    al_word.addAll(dbLists);
                }
                mHandler.sendEmptyMessage(LOAD_DB_KEYWORD_DATA);
            }
        }).start();
    }

    /**
     * 清空关键字数集
     */
    private void clearKeywordData() {
        al_word.clear();
        if (searchAdapter != null) {
            searchAdapter.updateDatas(al_word, keyword);
        }
    }

    class CLOnItemClickListener implements ClassifyLeftAdapter.OnItemClickListener {
        @Override
        public void onItemClick(int position, CategoryEntity data) {
            if (data != null) {
                childPos = position;
                clAdapter.updateDatas(al_left, position);
                loadCategoryRightData(data.getClassId());
            }
        }
    }

    class CROnItemClickListener implements ClassifyRightAdapter.OnItemClickListener {
        @Override
        public void onItemClick(int position, GoodsEntity data) {
            startBillActivity(data);
        }
    }

    class SearchOnItemClickListener implements SearchAdapter.OnItemClickListener {
        @Override
        public void onItemClick(int position, GoodsEntity data) {
            startBillActivity(data);
        }
    }

    private void startBillActivity(GoodsEntity data) {
        if (data != null) {
            /*Intent intent = new Intent(mContext, GoodsActivity.class);
            intent.putExtra("data", data);
            startActivity(intent);*/
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message mMsg) {
            switch (mMsg.what) {
                case LOAD_DB_TOP_DATA:
                    // 加载默认分类数据
                    int ts = al_top.size();
                    if (ts <= 0) { //首级分类为空时清空所有缓存
                        parentId = 0;
                        parentPos = 0;
                        childId = 0;
                        childPos = 0;
                        al_left.clear();
                        clAdapter.updateDatas(al_left, childPos);
                    } else {
                        if (isNewAdd || parentPos >= ts) {
                            parentPos = ts - 1;
                        }
                        loadCategoryLeftData(al_top.get(parentPos).getClassId());
                    }
                    initBarTitle();
                    if (isNewAdd) {
                        isNewAdd = false;
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                hsv_bar.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                            }
                        },100L);
                    }
                    break;
                case LOAD_DB_LEFT_DATA:
                    if (clAdapter != null) {
                        int ls = al_left.size();
                        if (ls <= 0) { //子级分类为空时清空相对应的缓存
                            childId = 0;
                            childPos = 0;
                            al_goods.clear();
                            crAdapter.updateDatas(al_goods);
                        } else {
                            if (isNewAdd || childPos >= ls) {
                                childPos = ls - 1;
                            }
                            loadCategoryRightData(al_left.get(childPos).getClassId());
                        }
                        clAdapter.updateDatas(al_left, childPos);
                        if (isNewAdd) {
                            isNewAdd = false;
                            rv_classify_left.scrollToPosition(al_left.size()-1);
                        }
                    }
                    break;
                case LOAD_DB_RIGHT_DATA:
                    if (crAdapter != null) {
                        crAdapter.updateDatas(al_goods);
                    }
                    break;
                case LOAD_DB_KEYWORD_DATA:
                    if (al_word.size() == 0) {
                        ToastUtils.showToast(getString(R.string.text_input_keyword_null), 1000);
                    }
                    if (searchAdapter != null) {
                        searchAdapter.updateDatas(al_word, keyword);
                    }
                    break;
            }
        }
    };

    /**
     * 隐藏软键盘
     */
    private void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (dm != null) {
            dm.clearInstance();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
