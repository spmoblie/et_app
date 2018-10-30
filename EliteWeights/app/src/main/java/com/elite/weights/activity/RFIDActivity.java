package com.elite.weights.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elite.weights.R;
import com.elite.weights.adapter.RFIDAdapter;
import com.elite.weights.config.AppConfig;
import com.elite.weights.utils.ExceptionUtil;
import com.elite.weights.utils.SerialPortUtil;
import com.elite.weights.utils.StringUtils;
import com.elite.weights.utils.ToastUtils;
import com.module.interaction.ModuleConnector;
import com.nativec.tools.ModuleManager;
import com.rfid.RFIDReaderHelper;
import com.rfid.ReaderConnector;
import com.rfid.rxobserver.RXObserver;
import com.rfid.rxobserver.ReaderSetting;
import com.rfid.rxobserver.bean.RXOperationTag;
import com.util.StringTool;

import java.util.ArrayList;

import butterknife.BindView;

public class RFIDActivity extends BaseActivity {

    @BindView(R.id.title_iv_back)
    ImageView iv_title_back;

    @BindView(R.id.title_tv_name)
    TextView tv_title_name;

    @BindView(R.id.rfid_rv_rfids)
    RecyclerView rv_rfids;

    @BindView(R.id.rfid_tv_weight)
    TextView tv_weight;

    @BindView(R.id.rfid_tv_read)
    TextView tv_read;

    @BindView(R.id.rfid_tv_write)
    TextView tv_write;

    Context mContext;
    RFIDAdapter rfidAdapter;
    ArrayList<RXOperationTag> al_rfid = new ArrayList<RXOperationTag>();

    ModuleConnector connector;
    RFIDReaderHelper mReader;
    byte op = 0;
    boolean isWriteData = false;
    boolean isUHFStatus = false;
    RXObserver rxObserver = new RXObserver(){

        @Override
        protected void onOperationTag(RXOperationTag tag) {
            //当执行readTag,writeTag,lockTag 或者 killTag 等操作标签指令函数时会回调该方法，当一次操作多张标签时会多次回调
            //返回数据RXOperationTag tag 参考API文档
            if (tag != null) {
                al_rfid.add(tag);
            }
            mHandler.sendEmptyMessage(1006);
        }

        @Override
        protected void onOperationTagEnd(int operationTagCount) {
            //当执行readTag,writeTag,lockTag 或者 killTag 等操作标签指令函数结束时会回调该方法
            //operationTagCount 为操作的标签数量
        }

        @Override
        protected void refreshSetting(ReaderSetting readerSetting) {
            if (readerSetting != null && readerSetting.btAryOutputPower.length > 0) {
                op = readerSetting.btAryOutputPower[0];
                mHandler.sendEmptyMessage(1008);
            }
        }
    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1006:
                    if (isWriteData) {
                        isWriteData = false;
                        startWeights();
                    }
                    rfidAdapter.updateDatas(al_rfid);
                    break;
                case 1008:
                    ToastUtils.showToast("当前功率：" + String.valueOf(op), 1000);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfid);

        init();
        initRFID();
        initRecyclerView();
    }

    private void init() {
        mContext = this;
        tv_title_name.setText("RFID测试");

        iv_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWeights();
            }
        });

        tv_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog("写标签内容", InputType.TYPE_CLASS_TEXT, true,
                        new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                switch (msg.what) {
                                    case AppConfig.DIALOG_0001: //确定
                                        String dataStr = (String) msg.obj;
                                        if (StringUtils.isNull(dataStr)){
                                            ToastUtils.showToast("数据不能为空", 1000);
                                            return;
                                        }
                                        if (dataStr.length() > 24){
                                            ToastUtils.showToast("数据长度最大24位", 1000);
                                            return;
                                        }
                                        writeRFIDTag(dataStr);
                                        dismissDialog();
                                        break;
                                    case AppConfig.DIALOG_0002: //取消

                                        break;
                                }
                            }
                        });
            }
        });
    }

    /**
     * 初始化RFID
     */
    private void initRFID() {
        connector = new ReaderConnector();
        if (connector.connectCom("dev/ttyS1", 115200)) {
            setUHFStatus(true);
            try {
                mReader = RFIDReaderHelper.getDefaultHelper();
                mReader.registerObserver(rxObserver);
            } catch (Exception e) {
                ExceptionUtil.handle(e);
            }
        } else {
            ToastUtils.showToast("RFID串口连接失败", 1000);
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        LinearLayoutManager layoutManager_1 = new LinearLayoutManager(this);
        layoutManager_1.setOrientation(LinearLayoutManager.VERTICAL);
        rv_rfids.setLayoutManager(layoutManager_1);
        rfidAdapter = new RFIDAdapter(mContext, al_rfid);
        rv_rfids.setAdapter(rfidAdapter);
    }

    private void clearData() {
        al_rfid.clear();
        rfidAdapter.updateDatas(al_rfid);
    }

    /**
     * 读RFID标签
     */
    private void readRFIDTag() {
        if (mReader != null) {
            byte[] psWord = new byte[] {00,00,00,00};
            mReader.readTag((byte)0xFF, (byte)0x03, (byte)0x02, (byte)0x06, psWord);
        }
    }

    /**
     * 写RFID标签
     */
    private void writeRFIDTag(String dataStr) {
        if (!isUHFStatus) {
            setUHFStatus(true); //上电
        }
        if (mReader != null) {
            byte[] psWord = new byte[] {00,00,00,00};
            byte btWordCnt = 0x00;
            byte[] btAryData = null;
            String[] result = null;
            try {
                result = StringTool.stringToStringArray(dataStr, 2);
                btAryData = StringTool.stringArrayToByteArray(result, result.length);
                btWordCnt = (byte)((result.length / 2 + result.length % 2) & 0xFF);
            } catch (Exception e) {
                ExceptionUtil.handle(e);
                return;
            }
            if (btAryData == null || btAryData.length <= 0) {
                ToastUtils.showToast("Data is error", 1000);
                return;
            }
            mReader.writeTag((byte)0xFF, psWord, (byte)0x03, (byte)0x02, btWordCnt, btAryData);
            isWriteData = true;
        }
    }

    /**
     * 查询功率
     */
    private void getOutputPower() {
        if (mReader != null) {
            mReader.getOutputPower((byte)0xFF);
        }
    }

    /**
     * 设置功率
     */
    private void setOutputPower(byte btOutputPower) {
        if (mReader != null) {
            mReader.setOutputPower((byte)0xFF, btOutputPower, (byte)0x00, (byte)0x00, (byte)0x00);
        }
    }

    /**
     * 设置RFID模组上电/下电
     */
    private void setUHFStatus(boolean flat) {
        ModuleManager.newInstance().setUHFStatus(flat);
        isUHFStatus = flat;
        if (flat) {
            ToastUtils.showToast("上电", 1000);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                ExceptionUtil.handle(e);
            }
        } else {
            ToastUtils.showToast("下电", 1000);
        }
    }

    @Override
    protected void showWeights(String str_1, String weightStr, String weightDo, double weightGo) {
        tv_weight.setText(weightStr);
        boolean isWeight = !defaultWeightStr.equals(weightDo);
        //暂停状态下开始称重 || 称重状态下停止称重
        if (!isStart && ((isStop && isWeight) || (!isStop && !isWeight))) {
            isStart = true;
            //开启称重定时器
            openCDT.cancel();
            openCDT.start();
        }
    }

    @Override
    protected void startWeights() {
        if (!isUHFStatus) {
            setUHFStatus(true); //上电
        }
        clearData();
        readRFIDTag();
    }

    @Override
    protected void stopWeights() {

    }

    @Override
    protected void stopUHFWorking() {
        if (isUHFStatus) {
            setUHFStatus(false); //下电
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReader != null) {
            mReader.unRegisterObserver(rxObserver);
        }
        if (connector != null) {
            connector.disConnect();
        }
        setUHFStatus(false);
        ModuleManager.newInstance().release();
    }
}