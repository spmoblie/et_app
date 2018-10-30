package com.elite.selfhelp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elite.selfhelp.R;
import com.elite.selfhelp.adapter.ClassifyLeftAdapter;
import com.elite.selfhelp.adapter.ClassifyRightAdapter;
import com.elite.selfhelp.adapter.SearchAdapter;
import com.elite.selfhelp.entity.CategoryEntity;
import com.elite.selfhelp.entity.GoodsEntity;
import com.elite.selfhelp.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by user on 03/04/18.
 */

public class QueryActivity extends BaseActivity {

    private static final String TAG = "QueryActivity";
    private static final int LOAD_DB_LEFT_DATA = 1001;
    private static final int LOAD_DB_RIGHT_DATA = 1002;
    private static final int LOAD_DB_KEYWORD_DATA = 1003;

    @BindView(R.id.title_iv_back)
    ImageView iv_back;

    @BindView(R.id.title_tv_title)
    TextView tv_title;

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

    private Context mContext;
    private CountDownTimer mCdt;
    private SearchAdapter searchAdapter;
    private ClassifyLeftAdapter clAdapter;
    private ClassifyRightAdapter crAdapter;

    private String keyword;

    private ArrayList<TextView> al_tv = new ArrayList<TextView>();
    private ArrayList<CategoryEntity> al_top = new ArrayList<CategoryEntity>();
    private ArrayList<CategoryEntity> al_left = new ArrayList<CategoryEntity>();
    private ArrayList<GoodsEntity> al_goods = new ArrayList<GoodsEntity>();
    private ArrayList<GoodsEntity> al_word = new ArrayList<GoodsEntity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        init();
        initData();
        initBarTitle();
        initEditText();
        initRecyclerView();

        // 加载默认分类数据
        if (al_top.size() > 0) {
            loadCategoryLeftData(al_top.get(0).getCategoryId());
        }
    }

    private void init() {
        mContext = this;

        tv_title.setText(getString(R.string.goods_drugs_query));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
            }
        });
    }

    private void initData() {
        List<CategoryEntity> dbLists = db_category.getListData("0");
        if (dbLists != null) {
            al_top.addAll(dbLists);
        }
    }

    private void initBarTitle() {
        ll_bar.removeAllViews();
        al_tv.clear();
        for (int i = 0; i < al_top.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_br_query, ll_bar, false);
            final String categoryId = al_top.get(i).getCategoryId();
            final TextView tv_item = (TextView) view.findViewById(R.id.item_tv_bar);
            tv_item.setText(al_top.get(i).getName());
            al_tv.add(tv_item);
            if (i == 0) {
                tv_item.setTextColor(getResources().getColor(R.color.text_color_redss));
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadCategoryLeftData(categoryId);
                    for (int i = 0; i < al_tv.size(); i++) {
                        TextView tv_item = al_tv.get(i);
                        tv_item.setTextColor(getResources().getColor(R.color.text_color_white));
                    }
                    tv_item.setTextColor(getResources().getColor(R.color.text_color_redss));
                }
            });
            ll_bar.addView(view);
        }
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

    private void initRecyclerView() {
        LinearLayoutManager layoutManager_1 = new LinearLayoutManager(this);
        layoutManager_1.setOrientation(LinearLayoutManager.VERTICAL);
        rv_classify_left.setLayoutManager(layoutManager_1);
        clAdapter = new ClassifyLeftAdapter(mContext, al_left);
        clAdapter.setOnItemClickListener(new CLOnItemClickListener());
        rv_classify_left.setAdapter(clAdapter);

        LinearLayoutManager layoutManager_2 = new LinearLayoutManager(this);
        layoutManager_2.setOrientation(LinearLayoutManager.VERTICAL);
        rv_classify_right.setLayoutManager(layoutManager_2);
        crAdapter = new ClassifyRightAdapter(mContext, al_goods);
        crAdapter.setOnItemClickListener(new CROnItemClickListener());
        rv_classify_right.setAdapter(crAdapter);

        LinearLayoutManager layoutManager_3 = new LinearLayoutManager(this);
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
     * 加载指定分类数据
     */
    private void loadCategoryLeftData(final String parentId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                al_left.clear();
                List<CategoryEntity> dbLists = db_category.getListData(parentId);
                if (dbLists != null) {
                    al_left.addAll(dbLists);
                }
                mHandler.sendEmptyMessage(LOAD_DB_LEFT_DATA);
            }
        }).start();
    }

    /**
     * 加载指定分类数据
     */
    private void loadCategoryRightData(final String categoryId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                al_goods.clear();
                List<GoodsEntity> dbLists = db_goods.getListData(categoryId);
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
                clAdapter.updateDatas(al_left, position);
                loadCategoryRightData(data.getCategoryId());
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
            Intent intent = new Intent(mContext, GoodsActivity.class);
            intent.putExtra("data", data);
            startActivity(intent);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message mMsg) {
            switch (mMsg.what) {
                case LOAD_DB_LEFT_DATA:
                    if (clAdapter != null) {
                        clAdapter.updateDatas(al_left, 0);
                        if (al_left.size() > 0) {
                            loadCategoryRightData(al_left.get(0).getCategoryId());
                        } else {
                            al_goods.clear();
                            if (crAdapter != null) {
                                crAdapter.updateDatas(al_goods);
                            }
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
                        showToast(getString(R.string.text_input_keyword_null));
                    }
                    if (searchAdapter != null) {
                        searchAdapter.updateDatas(al_word, keyword);
                    }
                    break;
            }
        }
    };
}
