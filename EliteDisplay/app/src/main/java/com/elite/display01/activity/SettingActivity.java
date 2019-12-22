package com.elite.display01.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.elite.display01.AppApplication;
import com.elite.display01.R;
import com.elite.display01.config.AppConfig;
import com.elite.display01.utils.CommonTools;
import com.elite.display01.utils.serialport.SerialPortUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android_serialport_api.SerialPortFinder;
import butterknife.BindView;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

/**
 * Created by Beck on 2018/2/1.
 */
public class SettingActivity extends BaseActivity implements OnClickListener{

    @BindView(R.id.setting_bt_back)
    ImageButton bt_back;

    @BindView(R.id.setting_sp_port)
    Spinner sp_port;

    @BindView(R.id.setting_sp_baud)
    Spinner sp_baud;

    @BindView(R.id.setting_tv_test_result)
    TextView tv_test_result;

    @BindView(R.id.setting_bt_test_start)
    Button bt_test_start;

    @BindView(R.id.setting_et_asur)
    EditText et_asur;

    @BindView(R.id.setting_et_time)
    EditText et_time;

    @BindView(R.id.setting_bt_password)
    Button bt_password;

    SerialPortFinder spf;
    SharedPreferences sp;
    String currProt;
    int currBaud, posProt, posBaud, time;
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
        sp = AppApplication.getSharedPreferences();
        currProt = sp.getString(AppConfig.KEY_PORT, AppConfig.DEFAULT_PORT);
        currBaud = sp.getInt(AppConfig.KEY_BAUD, AppConfig.DEFAULT_BAUD);

        bt_back.setOnClickListener(this);
        bt_password.setOnClickListener(this);
        bt_test_start.setOnClickListener(this);

        et_asur.setSelection(et_asur.getText().length());
        et_asur.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        time = sp.getInt(AppConfig.KEY_TIME, AppConfig.DEFAULT_TIME);
        et_time.setText(String.valueOf(time));
        et_time.setSelection(et_time.getText().length());
        et_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.isEmpty()) return;
                if (str.length() > 2) {
                    str = str.substring(0, 2);
                    et_time.setText(str);
                    et_time.setSelection(et_time.getText().length());
                }
                int t = Integer.valueOf(str);
                if (t >= 1 && t <= 60) {
                    time = t;
                    sp.edit().putInt(AppConfig.KEY_TIME, time).apply();
                } else {
                    CommonTools.showToast("时间区间为1～60秒", 3000);
                }
            }
        });

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
                        restartSerialPort();
                    }
                }
                // 改变显示的字体属性
                /*TextView itemView = (TextView) view;
                itemView.setTextSize(20);
                itemView.setTextColor(getResources().getColor(R.color.style_text_color));*/
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
                    restartSerialPort();
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
            case R.id.setting_bt_back:
                finish();
                break;
            case R.id.setting_bt_test_start:
                if (currProt.isEmpty()){
                    CommonTools.showToast(getString(R.string.text_select_serial_port), 2000);
                    return;
                }
                boolean result = SerialPortUtil.testSerialPort(new File(currProt));
                if (result) {
                    tv_test_result.setText(getString(R.string.setting_test_ok));
                }else {
                    tv_test_result.setText(getString(R.string.setting_test_fail));
                }
                break;
            case R.id.setting_bt_password:
                showEditDialog(getString(R.string.setting_hint_password), InputType.TYPE_CLASS_NUMBER, true,
                        new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                switch (msg.what) {
                                    case DIALOG_CANCEL:
                                        break;
                                    case DIALOG_CONFIRM:
                                        String pass = (String) msg.obj;
                                        if (pass.isEmpty()) {
                                            CommonTools.showToast(getString(R.string.setting_null_password), 1000);
                                            return;
                                        }
                                        if (pass.length() != 6) {
                                            CommonTools.showToast(getString(R.string.setting_hint_password), 1000);
                                            return;
                                        }
                                        dismissDialog();
                                        sp.edit().putString(AppConfig.KEY_PASSWORD, pass).apply();
                                        CommonTools.showToast(getString(R.string.setting_succ_password), 1000);
                                        break;
                                }
                            }
                        });
                break;
        }
    }
}
