package com.elite.display01.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.elite.display01.AppApplication;
import com.elite.display01.R;
import com.elite.display01.adapter.BillAdapter;
import com.elite.display01.config.AppConfig;
import com.elite.display01.entity.BillEntity;
import com.elite.display01.entity.ComBean;
import com.elite.display01.utils.ExceptionUtil;
import com.elite.display01.utils.FileManager;
import com.elite.display01.utils.LogUtil;
import com.elite.display01.utils.MyCountDownTimer;
import com.elite.display01.utils.NetworkUtil;
import com.elite.display01.utils.QRCodeUtil;
import com.elite.display01.utils.StringUtil;
import com.elite.display01.utils.serialport.MyFunc;
import com.lkl.sample.channelpay.nativeapi.CppSdkInterf;
import com.lkl.sample.channelpay.nativeapi.LKLCodePayQRTransCodeResult;
import com.lkl.sample.channelpay.nativeapi.ResponseCode;

import org.w3c.dom.Text;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    public static final String TAG = "MainActivity";
    public static MainActivity instance = null;

    @BindView(R.id.main_bt_switch)
    Button mainBtSwitch;

    @BindView(R.id.main_bt_setting)
    Button mainBtSetting;

    @BindView(R.id.main_vp)
    ViewPager mainVp;

    @BindView(R.id.main_vv)
    VideoView mainVv;

    private Context mContext = null;
    private SharedPreferences sp = null;
    private DecimalFormat df = null;

    //Image Video
    private Runnable mPagerAction;
    private boolean vpStop = false;
    private boolean initOk = false;
    private boolean isVideo = false;
    private int vdePathNum = 0;
    private int currentPos = 0;
    private int mSeekPosition = 0;
    private int idsSize, idsPosition, vprPosition;
    private ArrayList<String> videoPaths = new ArrayList<String>();
    private ArrayList<ImageView> vpViewLs = new ArrayList<ImageView>();
    private ArrayList<Bitmap> bitmapLs = new ArrayList<Bitmap>();

    //PopupWindow
    private PopupWindow infoPopupWindow, payPopupWindow;
    private View infoPopupView, payPopupView;
    private Animation popupAnimShow, popupAnimGone;
    private FrameLayout info_popup_view;
    private RelativeLayout pay_popup_view, rl_popup_total, rl_popup_payment, rl_popup_change;
    private TextView tv_popup_total, tv_popup_payment, tv_popup_change, tv_popup_time;
    private ImageView iv_pay_result, iv_qr_code;
    private RecyclerView mainRv;
    private BillAdapter myAdapter;
    private MyCountDownTimer mcdt;
    private int priceId = 1;
    private int stateCode = -1;
    private int runTime = 5000;
    private boolean stateOk = false;
    private String priceStr = "";
    private String password = "";
    private ArrayList<BillEntity> dataLs = new ArrayList<BillEntity>();

    //Port
    public int ComRecSize = 0;
    public byte[] ComRec = new byte[ComRecSize];

    //LKLPay
    private static final int CONSUME_SUCCESS = 123;
    private static final int REFRESH_SUCCESS = 234;
    private static final int QUERY_SUCCESS = 345;
    private static final int QUERY_UNKNOWN = 456;
    private static final int CLOSE_SUCCESS = 567;
    private static final int COLSE_UNKNOWN = 678;
    private static final int ERROR_REQUEST = 789;
    private static final int ERROR_TIMEOUT = 912;
    private Handler mHandler;
    private String mQRTradeNo;
    private CppSdkInterf interf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogUtil.i(TAG, "onCreate");

        //绑定注解（如果父类绑定后子类不需要再绑定）
        //ButterKnife.bind(this);

        //初始化数据源
        init();

        //初始化拉卡拉支付
        initLKLPaySdk();

        //打开串口
        openSerialPort();
    }

    private void init(){
        instance = this;
        mContext = this;
        mHandler = new MyHandler(this);

        mainBtSwitch.setOnClickListener(this);
        mainBtSetting.setOnClickListener(this);
        popupAnimShow = AnimationUtils.loadAnimation(mContext, R.anim.in_from_top);
        popupAnimGone = AnimationUtils.loadAnimation(mContext, R.anim.out_to_top);

        sp = AppApplication.getSharedPreferences();
        isVideo = sp.getBoolean(AppConfig.KEY_VIDEO, false);

        df = new DecimalFormat("0.00");

        initData();
    }

    private void initData(){
        //版本大于6.0的情况
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {
                    AppConfig.PERMISSION_READ_EXTERNAL_STORAGE,
                    AppConfig.PERMISSION_WRITE_EXTERNAL_STORAGE,
            };
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }

        //加载本地图片资源
        bitmapLs.clear();
        File imgFile = new File(AppConfig.SAVE_PATH_IMAGE);
        if (imgFile != null){
            File[] imgList = imgFile.listFiles();
            if (imgList != null && imgList.length > 0) {
                Bitmap bt;
                File file;
                for (int i = 0; i < imgList.length; i++){
                    file = imgList[i];
                    if (file != null){
                        String filePath = file.getAbsolutePath();
                        boolean isImg = FileManager.isImage(filePath);
                        if (isImg) {
                            bt = BitmapFactory.decodeFile(filePath);
                            if (bt != null){
                                bitmapLs.add(bt);
                            }
                        }
                    }
                }
            }
        }

        //加载本地视频资源
        videoPaths.clear();
        File vdeFile = new File(AppConfig.SAVE_PATH_VIDEO);
        if (vdeFile != null){
            File[] vdeList = vdeFile.listFiles();
            if (vdeList != null && vdeList.length > 0) {
                File file;
                for (int i = 0; i < vdeList.length; i++){
                    file = vdeList[i];
                    if (file != null){
                        videoPaths.add(file.getAbsolutePath());
                    }
                }
            }
        }
    }

    private void switchView() {
        if (isVideo){
            stopPlay();
            mainVv.setVisibility(View.GONE);
            mainVp.setVisibility(View.VISIBLE);
            isVideo = false;
            showViewPager();
        }else {
            mainVp.setVisibility(View.GONE);
            mainVv.setVisibility(View.VISIBLE);
            isVideo = true;
            startPlay();
        }
        sp.edit().putBoolean(AppConfig.KEY_VIDEO, isVideo).apply();
    }

    private void showViewPager(){
        if (mainVp == null)return;
        if (initOk) return;
        vpViewLs.clear();
        idsSize = bitmapLs.size();
        if (idsSize <= 0){
            showToast(getString(R.string.text_file_error));
            return;
        }
        if (idsSize == 2 || idsSize == 3) {
            bitmapLs.addAll(bitmapLs);
        }
        for (int i = 0; i < bitmapLs.size(); i++){
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageBitmap(bitmapLs.get(i));
            vpViewLs.add(imageView);
        }
        final boolean loop = vpViewLs.size() > 3 ? true:false;
        mainVp.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View layout;
                if (loop) {
                    layout = vpViewLs.get(position % vpViewLs.size());
                } else {
                    layout = vpViewLs.get(position);
                }
                mainVp.addView(layout);
                return layout;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View layout;
                if (loop) {
                    layout = vpViewLs.get(position % vpViewLs.size());
                } else {
                    layout = vpViewLs.get(position);
                }
                mainVp.removeView(layout);
            }

            @Override
            public int getCount() {
                if (loop) {
                    return Integer.MAX_VALUE;
                } else {
                    return vpViewLs.size();
                }
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
        mainVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (loop) {
                    vprPosition = position;
                    idsPosition = position % vpViewLs.size();
                    if (idsPosition == vpViewLs.size()) {
                        idsPosition = 0;
                        mainVp.setCurrentItem(0);
                    }
                } else {
                    idsPosition = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1){
                    vpStop = true;
                }

            }
        });
        if (loop){
            mainVp.setCurrentItem(vpViewLs.size() * 10);
            mPagerAction = new Runnable() {
                @Override
                public void run() {
                    if (!vpStop){
                        vprPosition++;
                        if (mainVp != null) {
                            mainVp.setCurrentItem(vprPosition);
                        }
                    }
                    vpStop = false;
                    if (mainVp != null) {
                        mainVp.postDelayed(mPagerAction, runTime);
                    }
                }
            };
            if (mainVp != null) {
                mainVp.postDelayed(mPagerAction, runTime);
            }
        }
        initOk = true;
    }

    private void startPlay(){
        if (mainVv == null) return;
        vdePathNum = videoPaths.size();
        if (vdePathNum <= 0){
            showToast(getString(R.string.text_file_error));
            return;
        }
        if (currentPos >= vdePathNum) { //确保不会越界
            currentPos = 0;
        }
        if (mSeekPosition <= 0) {
            MediaController mc = new MediaController(mContext);
            mc.show(10000);
            mc.setVisibility(View.INVISIBLE);
            mainVv.setMediaController(mc);
            //setVideoAreaSize();
            mainVv.setVideoURI(Uri.parse(videoPaths.get(currentPos)));
            mainVv.requestFocus();
            mainVv.start();
            mainVv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    //播放开始
                }
            });
            mainVv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //播放结束
                    mSeekPosition = 0;
                    if (vdePathNum > 1) {
                        currentPos++;
                        if (currentPos >= vdePathNum) {
                            currentPos = 0;
                        }
                        mainVv.setVideoURI(Uri.parse(videoPaths.get(currentPos)));
                    }
                    mainVv.start();
                }
            });
            mainVv.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    //播放出错
                    showToast(getString(R.string.text_video_error));
                    return true;
                }

            });
        } else {
            if (!mainVv.isPlaying()) {
                mainVv.seekTo(mSeekPosition);
                mainVv.start();
            }
        }
    }

    private void stopPlay(){
        if (mainVv != null && mainVv.isPlaying()) {
            mSeekPosition = mainVv.getCurrentPosition();
            mainVv.pause();
        }
    }

    /**
     * 弹出商品信息浮层
     */
    private void infoPopupShow(int typeCode){
        if (typeCode <= -1) return;
        if (typeCode > 0) {
            if (infoPopupWindow == null) {
                infoPopupView = LayoutInflater.from(mContext).inflate(R.layout.popup_info, null);
                //popupAnimShow = AnimationUtils.loadAnimation(mContext, R.anim.in_from_top);
                //popupAnimGone = AnimationUtils.loadAnimation(mContext, R.anim.out_to_top);

                info_popup_view = (FrameLayout) infoPopupView.findViewById(R.id.popup_info_main_fl);
                info_popup_view.startAnimation(popupAnimShow);

                mainRv = (RecyclerView) infoPopupView.findViewById(R.id.popup_info_rv);
                initRecyclerView();

                iv_pay_result = (ImageView) infoPopupView.findViewById(R.id.popup_info_iv_result);

                rl_popup_total = (RelativeLayout) infoPopupView.findViewById(R.id.popup_info_rl_total);
                tv_popup_total = (TextView) infoPopupView.findViewById(R.id.popup_info_total);
                rl_popup_payment = (RelativeLayout) infoPopupView.findViewById(R.id.popup_info_rl_payment);
                tv_popup_payment = (TextView) infoPopupView.findViewById(R.id.popup_info_payment);
                rl_popup_change = (RelativeLayout) infoPopupView.findViewById(R.id.popup_info_rl_change);
                tv_popup_change = (TextView) infoPopupView.findViewById(R.id.popup_info_change);

                infoPopupWindow = new PopupWindow(infoPopupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                infoPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                infoPopupWindow.setFocusable(false);
                infoPopupWindow.setOutsideTouchable(false);
                infoPopupWindow.setClippingEnabled(false); //允许弹出窗口超出屏幕范围
                infoPopupWindow.setWidth(AppApplication.screenWidth / 3); //设置弹窗的宽
                infoPopupWindow.showAtLocation(infoPopupView, Gravity.LEFT, 0, 0);
            } else if (!infoPopupWindow.isShowing()) {
                info_popup_view.startAnimation(popupAnimShow);
                infoPopupWindow.showAtLocation(infoPopupView, Gravity.LEFT, 0, 0);
            }
            if (typeCode == 1) { //删除数据
                delData();
            } else
            if (typeCode == 2) { //添加数据
                addData();
            }
        } else { //typeCode == 0
            dismissPopup();
        }
        showPayResult(0);
    }

    /**
     * 弹出扫码支付二维码浮层
     * @param qrBm
     */
    private void qrCpdePopupShow(Bitmap qrBm){
        if (payPopupWindow == null) {
            payPopupView = LayoutInflater.from(mContext).inflate(R.layout.popup_qr_code, null);
            pay_popup_view = (RelativeLayout) payPopupView.findViewById(R.id.popup_qr_code_main_rl);
            pay_popup_view.startAnimation(popupAnimShow);
            iv_qr_code = (ImageView) payPopupView.findViewById(R.id.popup_qr_code_iv);
            tv_popup_time = (TextView) payPopupView.findViewById(R.id.popup_qr_tv_time);

            payPopupWindow = new PopupWindow(payPopupView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            payPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            payPopupWindow.setFocusable(false);
            payPopupWindow.setOutsideTouchable(false);
            payPopupWindow.setClippingEnabled(false); //允许弹出窗口超出屏幕范围
            payPopupWindow.showAtLocation(payPopupView, Gravity.LEFT, 0, 0);
        } else if (!payPopupWindow.isShowing()) {
            pay_popup_view.startAnimation(popupAnimShow);
            payPopupWindow.showAtLocation(payPopupView, Gravity.LEFT, 0, 0);
        }
        if (iv_qr_code != null && qrBm != null) {
            iv_qr_code.setImageBitmap(qrBm);
            doQuery(); //查询支付结果
            startTimer(); //开启倒计时
        }
    }
    /**
     * 开启倒计时
     */
    private void startTimer() {
        mcdt = new MyCountDownTimer(tv_popup_time, 60000, 1000,
                new MyCountDownTimer.MyTimerCallback() {
                    @Override
                    public void onFinish() {
                        payPopupDismiss();
                    }
                });
        mcdt.start(); //开始倒计时
    }

    /**
     * 取消倒计时
     */
    private void stopTimer() {
        if (mcdt != null) {
            mcdt.cancel();
        }
    }

    /**
     * 关闭商品信息浮层
     */
    private void infoPopupDismiss(){
        clearData();
        if (infoPopupWindow != null && infoPopupWindow.isShowing()) {
            info_popup_view.startAnimation(popupAnimGone);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    infoPopupWindow.dismiss();
                }
            }, 500);
        }
    }

    /**
     * 关闭支付信息浮层
     */
    private void payPopupDismiss(){
        if (payPopupWindow != null && payPopupWindow.isShowing()) {
            pay_popup_view.startAnimation(popupAnimGone);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopTimer();
                    payPopupWindow.dismiss();
                }
            }, 500);
        }
    }

    /**
     * 关闭所有浮层
     */
    private void dismissPopup(){
        if (payPopupWindow != null && payPopupWindow.isShowing()) {
            pay_popup_view.startAnimation(popupAnimGone);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopTimer();
                    payPopupWindow.dismiss();
                    infoPopupDismiss();
                }
            }, 500);
        } else {
            infoPopupDismiss();
        }
    }

    private void initRecyclerView(){
        if (mainRv == null) return;
        // 创建布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 设置布局管理器
        mainRv.setLayoutManager(layoutManager);
        // 创建Adapter
        myAdapter = new BillAdapter(mContext, new ArrayList<BillEntity>());
        // 设置Adapter
        mainRv.setAdapter(myAdapter);
    }

    /**
     * 接收串口数据
     * @param hexData
     */
    public void receiveDatas(byte[] bytes, int size, String hexData) {
        //封装数据
        ComBean comData = new ComBean(bytes, size);
        //解析数据
        DispRecData(comData);
    }

    /**
     * 处理显示接收数据
     * @param comRecData
     */
    private void DispRecData(ComBean comRecData) {
        if (comRecData == null) return;

        int size = ComRecSize + comRecData.iRecSize;
        byte[] temp = addBytes(ComRec, comRecData.bRec);

        LogUtil.i(TAG, "收到新数据：" + MyFunc.ByteArrToHex(comRecData.bRec));
        if (ComRecSize > 0) {
            LogUtil.i(TAG, "与缓存数据合并：" + MyFunc.ByteArrToHex(temp));
        }

        int Cmd_CR_Pos = 0;
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                // 进行指令筛选
                if (temp[i] == (byte) 0x0c) {
                    // 0x0C 清屏
                    LogUtil.i(TAG, "筛选到清屏指令：" + MyFunc.Byte2Hex(temp[i]));

                    // 数据块尾部取到完整的清屏指令，清空指令缓存
                    if (size - i - 1 == 0) {
                        clearOrderCache();
                    }

                    // 清除数据并关闭浮层
                    infoPopupShow(0);
                }else
                if (temp[i] == (byte) 0x1b) {
                    // 0x1B
                    if (size - i > 1) {
                        int temp1size = 0;
                        byte[] temp1 = null;
                        switch (temp[i + 1]) {
                            case (byte) 0x40: //初始化
                                temp1 = new byte[2];
                                System.arraycopy(temp, i, temp1, 0, 2);
                                LogUtil.i(TAG, "筛选到初始化指令：" + MyFunc.ByteArrToHex(temp1));

                                // 数据块尾部取到完整的初始化指令，清空指令缓存
                                if (size - i - 2 == 0) {
                                    clearOrderCache();
                                }
                                break;
                            case (byte) 0x73: //小灯控制
                                temp1size = size - i;
                                temp1 = new byte[temp1size];
                                System.arraycopy(temp, i, temp1, 0, temp1size);
                                LogUtil.i(TAG, "筛选到小灯控制指令：" + MyFunc.ByteArrToHex(temp1));

                                if (size - i > 2) {
                                    switch (temp[i + 2]) {
                                        case (byte) 0x30: //全暗
                                            stateCode = 0;
                                            stateOk = true;
                                            break;
                                        case (byte) 0x31: //单价
                                            stateCode = 1;
                                            stateOk = true;
                                            break;
                                        case (byte) 0x32: //总计
                                            stateCode = 2;
                                            stateOk = true;
                                            break;
                                        case (byte) 0x33: //收款
                                            stateCode = 3;
                                            stateOk = true;
                                            break;
                                        case (byte) 0x34: //找零
                                            stateCode = 4;
                                            stateOk = true;
                                            break;
                                    }

                                    // 数据块尾部取到完整的小灯控制指令，清空指令缓存
                                    if (size - i - 3 == 0) {
                                        clearOrderCache();
                                    }
                                } else {
                                    addOrderCache(temp, size, i);
                                    break;
                                }
                                break;
                            case (byte) 0x51: //显示内容
                                temp1size = size - i;
                                temp1 = new byte[temp1size];
                                System.arraycopy(temp, i, temp1, 0, temp1size);
                                LogUtil.i(TAG, "筛选到送显示内容指令：" + MyFunc.ByteArrToHex(temp1));

                                // 查找送显示内容结束标志 0x0d
                                for (int j = 0; j < temp1size; j++) {
                                    // 0x0D 送显示内容结束
                                    if (temp1[j] == (byte) 0x0d) {
                                        LogUtil.i(TAG, "指令中找到结束标志");
                                        Cmd_CR_Pos = j;
                                        byte[] temp2 = new byte[j - 3];
                                        System.arraycopy(temp1, 3, temp2, 0, j - 3);
                                        priceStr = MyFunc.byteArrayToStr(temp2);
                                        if (!priceStr.isEmpty()) {
                                            priceStr = df.format(Double.valueOf(priceStr));
                                        }
                                        LogUtil.i(TAG, "显示内容：" + priceStr);

                                        // 数据块尾部取到完整的送显示内容指令，清空指令缓存
                                        if (size - i - j - 1 == 0) {
                                            clearOrderCache();
                                        }
                                        break;
                                    }
                                }
                                // 没找到 0x0d
                                if (Cmd_CR_Pos == 0) {
                                    addOrderCache(temp, size, i);
                                    break;
                                }
                                // 0x0d 位置归0
                                Cmd_CR_Pos = 0;
                                break;
                        }
                    } else {
                        addOrderCache(temp, size, i);
                        break;
                    }

                    if (stateOk && !priceStr.isEmpty()) {
                        // 新增数据并弹出浮层
                        infoPopupShow(2);
                    }
                }
            }
        }
    }

    /**
     * 清空指令缓存
     */
    public void clearOrderCache() {
        ComRecSize = 0;
        ComRec = new byte[ComRecSize];
        LogUtil.i(TAG, "到达数据块尾部，清空指令缓存");
    }

    /**
     * 加入指令缓存
     */
    public void addOrderCache(byte[] temp, int size, int i) {
        ComRecSize = size - i;
        ComRec = new byte[ComRecSize];
        System.arraycopy(temp, i, ComRec, 0, ComRecSize);
        LogUtil.i(TAG, "指令数据不全，加入指令缓存，等待下一包：" + MyFunc.ByteArrToHex(ComRec));
    }

    /**
     * 数据拼接
     */
    public byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }

    /**
     * 添加数据
     */
    private void addData(){
        if (stateCode == 0) { //全暗
            //clearData();
            return;
        }
        if (!stateOk || priceStr.equals("")) return;
        switch (stateCode) {
            case 1: //单价
                BillEntity billEn = new BillEntity(
                        priceId, getString(R.string.bill_text_unit_price) + priceId,
                        priceStr, 1, 0);
                priceId++;
                dataLs.add(billEn);
                updateDatas(dataLs);
                if (mainRv != null) {
                    mainRv.scrollToPosition(myAdapter.getItemCount() - 1);
                }
                break;
            case 2: //总价
                updateTotal(priceStr);
                break;
            case 3: //收款
                updatePayment(priceStr);
                break;
            case 4: //找零
                updateChange(priceStr);
                break;
        }
        stateCode = -1;
        stateOk = false;
        priceStr = "";
    }

    /**
     * 删除指定数据
     */
    private void delData(){
        if (dataLs.size() == 0) return;
        priceId--;
        dataLs.remove(dataLs.size() - 1);
        updateDatas(dataLs);
    }

    /**
     * 清除所有数据
     */
    private void clearData(){
        //清除状态标识
        stateCode = -1;
        stateOk = false;
        priceStr = "";
        //清空显示数据
        priceId = 1;
        dataLs.clear();
        if (infoPopupWindow != null) {
            updateDatas(dataLs);
            updateTotal(getString(R.string.bill_price_0));
            updatePayment(getString(R.string.bill_price_0));
            updateChange(getString(R.string.bill_price_0));
            rl_popup_total.setVisibility(View.INVISIBLE);
            rl_popup_payment.setVisibility(View.INVISIBLE);
            rl_popup_change.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 更新总价
     * @param totalStr
     */
    private void updateTotal(String totalStr){
        tv_popup_total.setText(getString(R.string.bill_currency_rmb) + totalStr);
        rl_popup_total.setVisibility(View.VISIBLE);
        //判定网络是否可用
        if (NetworkUtil.isNetworkAvailable()) {
            doConsume(totalStr);
        }
    }

    /**
     * 更新收款
     * @param paymentStr
     */
    private void updatePayment(String paymentStr){
        tv_popup_payment.setText(getString(R.string.bill_currency_rmb) + paymentStr);
        rl_popup_payment.setVisibility(View.VISIBLE);
    }

    /**
     * 更新找零
     * @param changeStr
     */
    private void updateChange(String changeStr){
        tv_popup_change.setText(getString(R.string.bill_currency_rmb) + changeStr);
        rl_popup_change.setVisibility(View.VISIBLE);
    }

    /**
     * 更新单价数集
     * @param datas
     */
    private void updateDatas(ArrayList<BillEntity> datas){
        if (myAdapter != null) {
            myAdapter.updateDatas(datas);
        }
    }

    /**
     * 执行消费请求
     */
    private void doConsume(String price) {
        if (StringUtil.priceIsNull(price)) return;
        if (price.contains(".")) {
            price = price.replace(".", "");
        }
        price = "00000000000" + price;
        price = price.substring(price.length() - 12, price.length());

        final String amount = price; //总计
        final String description = ""; //描述
        final String location = ""; //定位
        final String lklOrderNo = "Test" + new Date().getTime(); //订单号
        final String subject = ""; //主题
        try {
            new Thread(new Runnable(){
                public void run() {
                    //主扫支付
                    CppSdkInterf channel = new CppSdkInterf(getApplicationContext());
                    LogUtil.i(TAG, "Start qrConsume...");

                    final LKLCodePayQRTransCodeResult rslt = new LKLCodePayQRTransCodeResult();
                    int consumeRet = channel.doQRConsume(amount, lklOrderNo, subject, description, location, rslt);
                    LogUtil.i(TAG, "ResultCode = " + Integer.toString(consumeRet));

                    Message message = Message.obtain();
                    if (!rslt.qrCode_.isEmpty()) {
                        mQRTradeNo = rslt.getTradeOrderNo();
                        LogUtil.i(TAG, "mQRTradeNo = " + mQRTradeNo);
                        message.what = CONSUME_SUCCESS;
                        message.obj = rslt.qrCode_;
                    } else {
                        LogUtil.i(TAG, "qrConsume failure. Error = " + ResponseCode.POSP_ERR.get(consumeRet));
                        message.what = ERROR_REQUEST;
                        message.obj = consumeRet + ResponseCode.POSP_ERR.get(consumeRet);
                    }
                    message.setTarget(mHandler);
                    message.sendToTarget();
                }
            }).start();
        } catch (Exception e) {
            ExceptionUtil.handle(e);
        }
    }

    /**
     * 查询交易结果
     */
    private void doQuery() {
        try {
            new Thread(new Runnable(){
                public void run() {
                    //主扫查询
                    CppSdkInterf channel = new CppSdkInterf(getApplicationContext());
                    LogUtil.i(TAG, "Start qrQuery...");
                    LogUtil.i(TAG, "mQRTradeNo = " + mQRTradeNo);

                    final LKLCodePayQRTransCodeResult rslt = new LKLCodePayQRTransCodeResult();

                    int queryTimes = 18;
                    int queryRet = 0;
                    try {
                        while (queryTimes > 0) {
                            queryTimes--;
                            queryRet = channel.doQRQueryState(mQRTradeNo, "", "", rslt);
                            if (queryRet == 0) { //success
                                break;
                            } else { //retry
                                Thread.sleep(3000);
                                LogUtil.i(TAG, "Query left retry times = " + queryTimes);
                            }
                        }
                    } catch (Exception e) {
                        ExceptionUtil.handle(e);
                    }
                    LogUtil.i(TAG, "ResultCode = " + Integer.toString(queryRet));

                    Message message = Message.obtain();
                    message.what = (queryRet == 0) ? QUERY_SUCCESS : QUERY_UNKNOWN;
                    message.setTarget(mHandler);
                    message.sendToTarget();
                }
            }).start();
        } catch (Exception e) {
            ExceptionUtil.handle(e);
        }
    }

    /**
     * 刷新支付二维码
     */
    private void doRefresh() {
        try {
            new Thread(new Runnable(){
                public void run() {
                    //主扫刷新
                    CppSdkInterf channel = new CppSdkInterf(getApplicationContext());
                    LogUtil.i(TAG, "Start qrRefresh...");
                    LogUtil.i(TAG, "mQRTradeNo = " + mQRTradeNo);

                    LKLCodePayQRTransCodeResult rslt = new LKLCodePayQRTransCodeResult();
                    int refreshRet = channel.doQRRefreshCode(mQRTradeNo, "", rslt);
                    LogUtil.i(TAG, "ResultCode = " + Integer.toString(refreshRet));

                    Message message = Message.obtain();
                    if (refreshRet == 0) {
                        LogUtil.i(TAG, "Refresh success.");
                        message.what = REFRESH_SUCCESS;
                        message.obj = rslt.qrCode_;
                    } else {
                        LogUtil.i(TAG, "Refresh failure. Error = " + ResponseCode.POSP_ERR.get(refreshRet));
                        message.what = ERROR_REQUEST;
                        message.obj = refreshRet + ResponseCode.POSP_ERR.get(refreshRet);
                    }
                    message.setTarget(mHandler);
                    message.sendToTarget();
                }
            }).start();
        } catch (Exception e) {
            ExceptionUtil.handle(e);
        }
    }

    /**
     * 主扫关闭订单
     */
    private void doCloseOrder() {
        try {
            new Thread(new Runnable(){
                public void run() {
                    //主扫关单
                    CppSdkInterf channel = new CppSdkInterf(getApplicationContext());
                    LogUtil.i(TAG, "Start qrCloseOrder...");
                    LogUtil.i(TAG, "mQRTradeNo = " + mQRTradeNo);

                    LKLCodePayQRTransCodeResult rslt = new LKLCodePayQRTransCodeResult();
                    int closeOrderRet = channel.doQRCloseOrder(mQRTradeNo, rslt);
                    LogUtil.i(TAG, "ResultCode = " + Integer.toString(closeOrderRet));

                    Message message = Message.obtain();
                    if (closeOrderRet == 0) {
                        message.what = CLOSE_SUCCESS;
                        LogUtil.i(TAG, "Close order success.");
                    } else {
                        message.what = COLSE_UNKNOWN;
                        message.obj = closeOrderRet + ResponseCode.POSP_ERR.get(closeOrderRet);
                        LogUtil.i(TAG, "close order failure. err = " + ResponseCode.POSP_ERR.get(closeOrderRet));
                    }
                    message.setTarget(mHandler);
                    message.sendToTarget();
                }
            }).start();
        } catch (Exception e) {
            ExceptionUtil.handle(e);
        }
    }

    /**
     * 生成支付二维码
     */
    private void doCreateQrCode(String qrCode) {
        Bitmap qrBm = QRCodeUtil.createQRImage(qrCode, 300, 300, 2);
        if (qrBm != null) {
            qrCpdePopupShow(qrBm);
        }
    }

    /*private void doPostData() {
        HashMap<String, String> parameters = null;
        PayLoader payLoader = new PayLoader();
        payLoader.postPayDatas(parameters).subscribe(new Action1<String>() {
            @Override
            public void call(String result) {
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                LogUtil.i(TAG,"error message : " + throwable.getMessage());
                if (throwable instanceof Fault) {
                    Fault fault = (Fault) throwable;
                    if (fault.getErrorCode() == 404) {
                        //错误处理
                    } else
                    if (fault.getErrorCode() == 500) {
                        //错误处理
                    } else
                    if (fault.getErrorCode() == 501) {
                        //错误处理
                    }
                } else {
                    showToast(getString(R.string.pay_error_info));
                }
            }
        });
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_bt_switch:
                switchView();
                break;
            case R.id.main_bt_setting:
                startSettingActivity();
                break;
        }
    }

    private void startSettingActivity() {
        showEditDialog(getString(R.string.text_password_input), InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD, true,
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case DIALOG_CANCEL:
                                break;
                            case DIALOG_CONFIRM:
                                String inputPassword = (String) msg.obj;
                                if (password.equals(inputPassword)) {
                                    dismissDialog();
                                    startActivity(new Intent(mContext, SettingActivity.class));
                                } else {
                                    showToast(getString(R.string.text_password_error), 1000);
                                }
                                break;
                        }
                    }
                });
    }



    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i(TAG, "onResume");
        //模拟一个切换View的事件
        isVideo = !isVideo;
        switchView();
        //获取轮播时间
        runTime = (sp.getInt(AppConfig.KEY_TIME, AppConfig.DEFAULT_TIME))*1000;
        //获取管理密码
        password = sp.getString(AppConfig.KEY_PASSWORD, AppConfig.DEFAULT_PASSWORD);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.i(TAG, "onPause");
        if (isVideo){
            stopPlay();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG, "onDestroy");
        if (mainVv != null) {
            mainVv.stopPlayback();
        }
        //关闭浮层
        if (payPopupWindow != null && payPopupWindow.isShowing()) {
            payPopupWindow.dismiss();
        }
        if (infoPopupWindow != null && infoPopupWindow.isShowing()) {
            infoPopupWindow.dismiss();
        }
        //关闭串口
        closeSerialPort();
        //销毁实例
        instance = null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setSystemUIVisible(false);//全屏
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        setSystemUIVisible(false);//全屏
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*if (keyCode == KeyEvent.KEYCODE_BACK) {
            //退出
            return true;
        }*/
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 异步初始化拉卡拉支付
     */
    private void initLKLPaySdk() {
        interf = new CppSdkInterf(getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //1、定义
                    interf.getDBPath();
                    //2、设置设备ID
                    int st = interf.setTid(AppApplication.getOnlyID());
                    //3、激活  10209:商户已激活  10214:终端尚未激活，请重新激活
                    int at = interf.doActive(AppConfig.ACTIVE_CODE);
                    //4、签到
                    int lg = interf.doLogin();
                    LogUtil.i(TAG, "设备=" + st + " 激活=" + at + " 签到=" + lg);
                } catch (Exception e) {
                    ExceptionUtil.handle(e);
                }
            }
        }).start();
    }

    /**
     * 自定义消息处理机制
     */
    private static class MyHandler extends Handler {

        private WeakReference<MainActivity> mWeak;

        MyHandler(MainActivity activity) {
            mWeak = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CONSUME_SUCCESS: //创建二维码
                case REFRESH_SUCCESS: //刷新二维码
                    mWeak.get().doCreateQrCode((String) msg.obj);
                    break;
                case QUERY_SUCCESS: //支付成功
                    mWeak.get().payPopupDismiss();
                    mWeak.get().showPayResult(QUERY_SUCCESS);
                    break;
                case QUERY_UNKNOWN: //支付失败
                    mWeak.get().payPopupDismiss();
                    mWeak.get().showPayResult(QUERY_UNKNOWN);
                    break;
                case CLOSE_SUCCESS: //关单成功

                    break;
                case COLSE_UNKNOWN: //关单失败

                    break;
                case ERROR_REQUEST: //返回报错
                case ERROR_TIMEOUT: //网络超时
                    mWeak.get().showToast((String) msg.obj);
                    mWeak.get().showPayResult(ERROR_REQUEST);
                    break;
            }
        }
    }

    private void showPayResult(int result) {
        Bitmap bm = null;
        switch (result) {
            case QUERY_SUCCESS:
                bm = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_pay_ok);
                break;
            case QUERY_UNKNOWN:
                bm = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_pay_time);
                doCloseOrder();
                break;
            case ERROR_REQUEST:
                bm = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_pay_fail);
                doCloseOrder();
                break;
        }
        if (bm != null) {
            iv_pay_result.setImageBitmap(bm);
            iv_pay_result.setVisibility(View.VISIBLE);
        } else {
            iv_pay_result.setVisibility(View.GONE);
        }
    }
}
