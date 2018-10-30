package com.elite.weights.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.elite.weights.AppApplication;
import com.elite.weights.R;
import com.elite.weights.config.AppConfig;
import com.elite.weights.utils.SerialPortUtil;
import com.nativec.tools.SerialPortFinder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

/**
 * Created by Beck on 2018/6/28.
 */
public class SettingActivity extends BaseActivity implements OnClickListener{

    @BindView(R.id.title_iv_back)
    ImageView iv_title_back;

    @BindView(R.id.title_tv_name)
    TextView tv_title_name;

    @BindView(R.id.setting_sp_port)
    Spinner sp_port;

    @BindView(R.id.setting_sp_baud)
    Spinner sp_baud;

    @BindView(R.id.setting_tv_test_result)
    TextView tv_test_result;

    @BindView(R.id.setting_bt_test_start)
    Button bt_test_start;

    SerialPortFinder spf;
    String currProt;
    int currBaud, posProt, posBaud;
    String[] protPaths;
    List<Integer> baudrateLs;
    private static final int[] BAUDRATES = {50, 75, 110, 134, 150, 200, 300, 600, 1200, 1800, 2400, 4800, 9600,
                                              19200, 38400, 57600, 115200, 230400, 460800, 500000, 576000, 921600,
                                              1000000, 1152000, 1500000, 2000000, 2500000, 3000000, 3500000, 4000000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(android.R.style.Theme_Light);
        setContentView(R.layout.activity_setting);

        initData();
    }

    private void initData(){
        spf = new SerialPortFinder();
        currProt = sp.getString(AppConfig.KEY_PORT, AppConfig.DEFAULT_PORT);
        currBaud = sp.getInt(AppConfig.KEY_BAUD, AppConfig.DEFAULT_BAUD);

        tv_title_name.setText(R.string.setting_title);
        iv_title_back.setOnClickListener(this);
        bt_test_start.setOnClickListener(this);

        //创建一个观察者
        Observer<String[]> observer = new Observer<String[]>() {
            @Override
            public void onCompleted() {
                initSpinner();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String[] s) {
                if (s != null) {
                    protPaths = s;
                }else {
                    protPaths = new String[]{};
                }
            }
        };
        //创建一个被观察者
        Observable observable = Observable.create(new Observable.OnSubscribe<String[]>() {
            @Override
            public void call(Subscriber<? super String[]> subscriber) {
                baudrateLs = new ArrayList<Integer>();
                for (int i = 0; i < BAUDRATES.length; i++){
                    int baud = BAUDRATES[i];
                    if (currBaud == baud) { //定位
                        posBaud = i;
                    }
                    baudrateLs.add(baud);
                }
                String[] str = spf.getAllDevicesPath();
                if (str != null) {
                    for (int i = 0; i < str.length; i++){
                        if (currProt.equals(str[i])){ //定位
                            posProt = i;
                        }
                    }
                }
                subscriber.onNext(str);
                subscriber.onCompleted();
            }
        });
        //建立订阅关系
        observable.subscribe(observer);
    }

    private void initSpinner(){
        ArrayAdapter aa_port = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, protPaths);
        sp_port.setAdapter(aa_port);
        sp_port.setSelection(posProt, true);
        sp_port.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_test_result.setText("");
                posProt = position;
                if (position >= 0 && position < protPaths.length) {
                    currProt = protPaths[position];
                    sp.edit().putString(AppConfig.KEY_PORT, currProt).apply();
                    // 如选择的串口可用则启用新串口
                    if (SerialPortUtil.testSerialPort(new File(currProt))) {
                        SerialPortUtil.closeSerialPort();
                        SerialPortUtil.openSerialPort();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter aa_baud = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, baudrateLs);
        sp_baud.setAdapter(aa_baud);
        sp_baud.setSelection(posBaud, true);
        sp_baud.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posBaud = position;
                if (position >= 0 && position < baudrateLs.size()) {
                    currBaud = baudrateLs.get(position);
                    sp.edit().putInt(AppConfig.KEY_BAUD, currBaud).apply();
                    SerialPortUtil.closeSerialPort();
                    SerialPortUtil.openSerialPort();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_iv_back:
                finish();
                break;
            case R.id.setting_bt_test_start:
                if (currProt.isEmpty()){
                    Toast.makeText(this, getString(R.string.setting_select_serial_port), Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean result = SerialPortUtil.testSerialPort(new File(currProt));
                if (result) {
                    tv_test_result.setText(getString(R.string.setting_test_ok));
                }else {
                    tv_test_result.setText(getString(R.string.setting_test_fail));
                }
                break;
        }
    }
}
