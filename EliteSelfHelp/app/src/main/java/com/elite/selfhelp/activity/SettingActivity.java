package com.elite.selfhelp.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.elite.selfhelp.R;
import com.elite.selfhelp.config.AppConfig;
import com.elite.selfhelp.utils.ExceptionUtils;
import com.elite.selfhelp.utils.PrinterUtils;
import com.elite.selfhelp.utils.SerialPortUtils;
import com.elite.selfhelp.utils.StringUtils;
import com.elite.selfhelp.utils.TimeUtils;
import com.elite.selfhelp.utils.ZxingUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android_serialport_api.SerialPortFinder;
import butterknife.BindView;

/**
 * Created by user on 10/04/18.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    private static final int PRINTER_TYPE_USB = AppConfig.PRINTER_TYPE_USB;
    private static final int PRINTER_TYPE_SERIAL = AppConfig.PRINTER_TYPE_SERIAL;

    public static SettingActivity instance = null;

    @BindView(R.id.title_iv_back)
    ImageView iv_back;

    @BindView(R.id.title_tv_title)
    TextView tv_title;

    @BindView(R.id.setting_item_tv_01)
    TextView tv_item_01;

    @BindView(R.id.setting_item_tv_02)
    TextView tv_item_02;

    @BindView(R.id.setting_item_tv_03)
    TextView tv_item_03;

    @BindView(R.id.setting_item_tv_04)
    TextView tv_item_04;

    @BindView(R.id.setting_show_rl)
    RelativeLayout rl_show;

    Context mContext;

    View printerView;
    Button bt_connect;
    TextView tv_usb, tv_serial, tv_size_1, tv_size_2, tv_size_3;
    Spinner sp_usb_port, sp_serial_port, sp_serial_baud;
    ArrayList<UsbDevice> al_device = null;
    ArrayList<String> al_device_name = null;
    UsbDevice usbDevice = null;
    boolean isSetType = true;
    int usbSpPos, protSpPos, baudSpPos, baudCode;
    String protPath;
    String[] protPaths;
    List<Integer> baudLists;
    private static final int[] BAUDRATES = {50, 75, 110, 134, 150, 200, 300, 600, 1200, 1800, 2400, 4800, 9600,
            19200, 38400, 57600, 115200, 230400, 460800, 500000, 576000, 921600,
            1000000, 1152000, 1500000, 2000000, 2500000, 3000000, 3500000, 4000000};

    View printView;
    TextView tv_edit_1, tv_edit_2, tv_edit_3, tv_edit_4, tv_edit_5, tv_edit_6, tv_edit_7, tv_edit_8;
    EditText et_edit_1, et_edit_2, et_edit_3, et_edit_4, et_edit_5, et_edit_6, et_edit_7;
    TextView tv_show_1, tv_show_2, tv_show_3, tv_show_4, tv_show_5, tv_show_6, tv_show_7;
    TextView tv_code, tv_time;
    ImageView iv_bar_code;

    View payView, versionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        setContentView(R.layout.activity_setting);

        init();
        onClick(tv_item_01);
    }

    private void init() {
        instance = this;
        mContext = this;
        tv_title.setText(getString(R.string.setting_title));

        iv_back.setOnClickListener(this);
        tv_item_01.setOnClickListener(this);
        tv_item_02.setOnClickListener(this);
        tv_item_03.setOnClickListener(this);
        tv_item_04.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_iv_back:
                finish();
                break;
            case R.id.setting_item_tv_01:
                updateItemState(1);
                initPrinterView();
                break;
            case R.id.setting_item_tv_02:
                updateItemState(2);
                initPrintView();
                break;
            case R.id.setting_item_tv_03:
                updateItemState(3);
                initPayView();
                break;
            case R.id.setting_item_tv_04:
                updateItemState(4);
                initVersionView();
                break;
            case R.id.printer_tv_usb:
                if (getType() == PRINTER_TYPE_USB) {
                    // 取消USB
                    if (isConnected()) { //USB已连接
                        isSetType = false;
                        closeUsbDevice();
                    }
                    setType(-1);
                } else {
                    // 选择USB
                    if (isConnected()) { //串口已连接
                        isSetType = false;
                        closeSerialPort();
                    }
                    setType(PRINTER_TYPE_USB);
                }
                updateTypeState();
                break;
            case R.id.printer_tv_serial:
                if (getType() == PRINTER_TYPE_SERIAL) {
                    // 取消串口
                    if (isConnected()) { //串口已连接
                        isSetType = false;
                        closeSerialPort();
                    }
                    setType(-1);
                } else {
                    // 选择串口
                    if (isConnected()) { //USB已连接
                        isSetType = false;
                        closeUsbDevice();
                    }
                    setType(PRINTER_TYPE_SERIAL);
                }
                updateTypeState();
                break;
            case R.id.printer_bt_connect:
                if (getType() == PRINTER_TYPE_USB) {
                    if (isConnected()) {
                        closeUsbDevice();
                    } else {
                        openUsbDevice(usbDevice, true);
                    }
                } else
                if (getType() == PRINTER_TYPE_SERIAL) {
                    if (isConnected()) {
                        closeSerialPort();
                    } else {
                        openSerialPort(true);
                    }
                } else {
                    showToast("请选择打印机类型");
                }
                break;
            case R.id.printer_tv_size_1:
                setSize(AppConfig.PRINT_WIDTH_58);
                updateSizeState();
                break;
            case R.id.printer_tv_size_2:
                setSize(AppConfig.PRINT_WIDTH_80);
                updateSizeState();
                break;
            case R.id.printer_tv_size_3:
                setSize(AppConfig.PRINT_WIDTH_100);
                updateSizeState();
                break;
            case R.id.setting_print_edit_tv_1:
                updateStencilState(et_edit_1, tv_edit_1, tv_show_1, AppConfig.KEY_EDIT_SHOW_1);
                break;
            case R.id.setting_print_edit_tv_2:
                updateStencilState(et_edit_2, tv_edit_2, tv_show_2, AppConfig.KEY_EDIT_SHOW_2);
                break;
            case R.id.setting_print_edit_tv_3:
                updateStencilState(et_edit_3, tv_edit_3, tv_show_3, AppConfig.KEY_EDIT_SHOW_3);
                break;
            case R.id.setting_print_edit_tv_4:
                updateStencilState(et_edit_4, tv_edit_4, tv_show_4, AppConfig.KEY_EDIT_SHOW_4);
                break;
            case R.id.setting_print_edit_tv_5:
                updateStencilState(et_edit_5, tv_edit_5, tv_show_5, AppConfig.KEY_EDIT_SHOW_5);
                break;
            case R.id.setting_print_edit_tv_6:
                updateStencilState(et_edit_6, tv_edit_6, tv_show_6, AppConfig.KEY_EDIT_SHOW_6);
                break;
            case R.id.setting_print_edit_tv_7:
                updateStencilState(et_edit_7, tv_edit_7, tv_show_7, AppConfig.KEY_EDIT_SHOW_7);
                break;
            case R.id.setting_print_edit_tv_8:
                if (tv_edit_8.isSelected()) {
                    tv_edit_8.setSelected(false);
                    iv_bar_code.setVisibility(View.GONE);
                } else {
                    tv_edit_8.setSelected(true);
                    iv_bar_code.setVisibility(View.VISIBLE);
                }
                sp.edit().putBoolean(AppConfig.KEY_EDIT_SHOW_8, tv_edit_8.isSelected()).apply();
                break;
        }
    }

    /**
     * 更新设置模块选择项
     */
    private void updateItemState(int item) {
        tv_item_01.setSelected(false);
        tv_item_01.setBackgroundResource(R.color.text_color_white);
        tv_item_02.setSelected(false);
        tv_item_02.setBackgroundResource(R.color.text_color_white);
        tv_item_03.setSelected(false);
        tv_item_03.setBackgroundResource(R.color.text_color_white);
        tv_item_04.setSelected(false);
        tv_item_04.setBackgroundResource(R.color.text_color_white);
        switch (item) {
            case 1:
                tv_item_01.setSelected(true);
                tv_item_01.setBackgroundResource(R.color.back_color_state);
                break;
            case 2:
                tv_item_02.setSelected(true);
                tv_item_02.setBackgroundResource(R.color.back_color_state);
                break;
            case 3:
                tv_item_03.setSelected(true);
                tv_item_03.setBackgroundResource(R.color.back_color_state);
                break;
            case 4:
                tv_item_04.setSelected(true);
                tv_item_04.setBackgroundResource(R.color.back_color_state);
                break;
        }
    }

    /**
     * 更新打印机类型选择项
     */
    private void updateTypeState() {
        tv_usb.setSelected(false);
        tv_serial.setSelected(false);
        switch (getType()) {
            case PRINTER_TYPE_USB:
                tv_usb.setSelected(true);
                break;
            case PRINTER_TYPE_SERIAL:
                tv_serial.setSelected(true);
                break;
        }
    }

    /**
     * 更新打印纸张尺码选择项
     */
    private void updateSizeState() {
        tv_size_1.setSelected(false);
        tv_size_2.setSelected(false);
        tv_size_3.setSelected(false);
        switch (getSize()) {
            case AppConfig.PRINT_WIDTH_58:
                tv_size_1.setSelected(true);
                break;
            case AppConfig.PRINT_WIDTH_80:
                tv_size_2.setSelected(true);
                break;
            case AppConfig.PRINT_WIDTH_100:
                tv_size_3.setSelected(true);
                break;
        }
    }

    /**
     * 更新连接状态描述
     */
    public void updateConnectStatus() {
        if (bt_connect == null) return;
        if (isConnected()) {
            bt_connect.setText(getString(R.string.setting_text_connect_no));
            bt_connect.setBackground(getResources().getDrawable(R.drawable.shape_bg_buttom_debar_4));
        } else {
            bt_connect.setText(getString(R.string.setting_text_connect));
            bt_connect.setBackground(getResources().getDrawable(R.drawable.shape_bg_buttom_state_4));
            setVID(-1);
            if (isSetType) {
                setType(-1);
            }
        }
        isSetType = true;
        updateTypeState();
    }

    /**
     * 初始化打印配置View
     */
    private void initPrinterView() {
        if (printerView == null) {
            printerView = LayoutInflater.from(mContext).inflate(R.layout.layout_printer, rl_show, false);

            tv_usb = (TextView) printerView.findViewById(R.id.printer_tv_usb);
            tv_usb.setOnClickListener(this);
            tv_serial = (TextView) printerView.findViewById(R.id.printer_tv_serial);
            tv_serial.setOnClickListener(this);
            tv_size_1 = (TextView) printerView.findViewById(R.id.printer_tv_size_1);
            tv_size_1.setOnClickListener(this);
            tv_size_2 = (TextView) printerView.findViewById(R.id.printer_tv_size_2);
            tv_size_2.setOnClickListener(this);
            tv_size_3 = (TextView) printerView.findViewById(R.id.printer_tv_size_3);
            tv_size_3.setOnClickListener(this);
            bt_connect = (Button) printerView.findViewById(R.id.printer_bt_connect);
            bt_connect.setOnClickListener(this);

            initSpinner();
            updateSizeState();
            updateConnectStatus();
        }
        showView(printerView);
    }

    private void initSpinner(){
        if (printerView == null) return;
        // USB打印机
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> devices = manager.getDeviceList();
        al_device = new ArrayList<UsbDevice>();
        al_device_name = new ArrayList<String>();
        for (UsbDevice device : devices.values()) {
            if (PrinterUtils.isUsbPrinter(device)) { //匹配USB打印机型号
                al_device.add(device);
                al_device_name.add(device.getDeviceName());
                if (getVID() == device.getVendorId()) {
                    usbSpPos = al_device_name.size() - 1;
                }
            }
        }
        if (usbSpPos >= 0 && usbSpPos < al_device.size()) {
            usbDevice = al_device.get(usbSpPos);
        } else if (isConnected() && getType() == PRINTER_TYPE_USB) {
            closeUsbDevice(); //USB打印机连接被断开
        }
        ArrayAdapter aa_usb = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, al_device_name);
        sp_usb_port = (Spinner) printerView.findViewById(R.id.printer_sp_usb_port);
        sp_usb_port.setAdapter(aa_usb);
        sp_usb_port.setSelection(usbSpPos, true);
        sp_usb_port.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos >= 0 && pos < al_device.size()) {
                    usbDevice = al_device.get(pos);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 串口打印机
        protPath = sp.getString(AppConfig.KEY_PORT, AppConfig.DEFAULT_PORT);
        protPaths = (new SerialPortFinder()).getAllDevicesPath();
        if (protPaths != null) {
            for (int i = 0; i < protPaths.length; i++){
                if (protPath.equals(protPaths[i])){ //定位
                    protSpPos = i;
                }
            }
        }
        ArrayAdapter aa_port = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, protPaths);
        sp_serial_port = (Spinner) printerView.findViewById(R.id.printer_sp_serial_port);
        sp_serial_port.setAdapter(aa_port);
        sp_serial_port.setSelection(protSpPos, true);
        sp_serial_port.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                protSpPos = pos;
                if (pos >= 0 && pos < protPaths.length) {
                    protPath = protPaths[pos];
                    if (SerialPortUtils.testSerialPort(new File(protPath))) {
                        sp.edit().putString(AppConfig.KEY_PORT, protPath).apply(); //串口可用则缓存
                        if (isConnected() && getType() == PRINTER_TYPE_SERIAL) {
                            restartSerialPort(); //已连接则重启
                        }
                    } else {
                        showToast(getString(R.string.setting_test_fail));
                    }
                }
                // 改变显示的字体属性
                /*TextView itemView = (TextView) view;
                itemView.setTextSize(26);
                itemView.setTextColor(getResources().getColor(R.color.text_color_redss));*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        baudCode = sp.getInt(AppConfig.KEY_BAUD, AppConfig.DEFAULT_BAUD);
        baudLists = new ArrayList<Integer>();
        for (int i = 0; i < BAUDRATES.length; i++){
            int baud = BAUDRATES[i];
            if (baudCode == baud) { //定位
                baudSpPos = i;
            }
            baudLists.add(baud);
        }
        ArrayAdapter aa_baud = new ArrayAdapter(this, android.R.layout.simple_list_item_1, baudLists);
        sp_serial_baud = (Spinner) printerView.findViewById(R.id.printer_sp_serial_baud);
        sp_serial_baud.setAdapter(aa_baud);
        sp_serial_baud.setSelection(baudSpPos, true);
        sp_serial_baud.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                baudSpPos = position;
                if (position >= 0 && position < baudLists.size()) {
                    baudCode = baudLists.get(position);
                    sp.edit().putInt(AppConfig.KEY_BAUD, baudCode).apply();
                    if (isConnected() && getType() == PRINTER_TYPE_SERIAL) {
                        restartSerialPort(); //已连接则重启
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 初始化小票模板View
     */
    private void initPrintView() {
        if (printView == null) {
            printView = LayoutInflater.from(mContext).inflate(R.layout.layout_print, rl_show, false);

            tv_edit_1 = (TextView) printView.findViewById(R.id.setting_print_edit_tv_1);
            tv_edit_2 = (TextView) printView.findViewById(R.id.setting_print_edit_tv_2);
            tv_edit_3 = (TextView) printView.findViewById(R.id.setting_print_edit_tv_3);
            tv_edit_4 = (TextView) printView.findViewById(R.id.setting_print_edit_tv_4);
            tv_edit_5 = (TextView) printView.findViewById(R.id.setting_print_edit_tv_5);
            tv_edit_6 = (TextView) printView.findViewById(R.id.setting_print_edit_tv_6);
            tv_edit_7 = (TextView) printView.findViewById(R.id.setting_print_edit_tv_7);
            tv_edit_8 = (TextView) printView.findViewById(R.id.setting_print_edit_tv_8);
            tv_edit_1.setOnClickListener(this);
            tv_edit_2.setOnClickListener(this);
            tv_edit_3.setOnClickListener(this);
            tv_edit_4.setOnClickListener(this);
            tv_edit_5.setOnClickListener(this);
            tv_edit_6.setOnClickListener(this);
            tv_edit_7.setOnClickListener(this);
            tv_edit_8.setOnClickListener(this);

            et_edit_1 = (EditText) printView.findViewById(R.id.setting_print_edit_et_1);
            et_edit_2 = (EditText) printView.findViewById(R.id.setting_print_edit_et_2);
            et_edit_3 = (EditText) printView.findViewById(R.id.setting_print_edit_et_3);
            et_edit_4 = (EditText) printView.findViewById(R.id.setting_print_edit_et_4);
            et_edit_5 = (EditText) printView.findViewById(R.id.setting_print_edit_et_5);
            et_edit_6 = (EditText) printView.findViewById(R.id.setting_print_edit_et_6);
            et_edit_7 = (EditText) printView.findViewById(R.id.setting_print_edit_et_7);
            et_edit_1.addTextChangedListener(new MyTextWatcher(et_edit_1));
            et_edit_2.addTextChangedListener(new MyTextWatcher(et_edit_2));
            et_edit_3.addTextChangedListener(new MyTextWatcher(et_edit_3));
            et_edit_4.addTextChangedListener(new MyTextWatcher(et_edit_4));
            et_edit_5.addTextChangedListener(new MyTextWatcher(et_edit_5));
            et_edit_6.addTextChangedListener(new MyTextWatcher(et_edit_6));
            et_edit_7.addTextChangedListener(new MyTextWatcher(et_edit_7));

            ScrollView ll_stencil = (ScrollView) printView.findViewById(R.id.setting_print_stencil);
            tv_code = (TextView) ll_stencil.findViewById(R.id.stencil_tv_code);
            tv_time = (TextView) ll_stencil.findViewById(R.id.stencil_tv_time);
            iv_bar_code = (ImageView) ll_stencil.findViewById(R.id.stencil_iv_bar_code);
            iv_bar_code.setImageBitmap(ZxingUtils.creatBarcode("0123456789", 400, 100));
            if (sp.getBoolean(AppConfig.KEY_EDIT_SHOW_8, false)) {
                tv_edit_8.setSelected(true);
                iv_bar_code.setVisibility(View.VISIBLE);
            } else {
                tv_edit_8.setSelected(false);
                iv_bar_code.setVisibility(View.GONE);
            }

            tv_show_1 = (TextView) ll_stencil.findViewById(R.id.stencil_tv_1);
            tv_show_2 = (TextView) ll_stencil.findViewById(R.id.stencil_tv_2);
            tv_show_3 = (TextView) ll_stencil.findViewById(R.id.stencil_tv_3);
            tv_show_4 = (TextView) ll_stencil.findViewById(R.id.stencil_tv_4);
            tv_show_5 = (TextView) ll_stencil.findViewById(R.id.stencil_tv_5);
            tv_show_6 = (TextView) ll_stencil.findViewById(R.id.stencil_tv_6);
            tv_show_7 = (TextView) ll_stencil.findViewById(R.id.stencil_tv_7);
            defaultStencilState(et_edit_1, tv_edit_1, tv_show_1, AppConfig.KEY_EDIT_SHOW_1, AppConfig.KEY_EDIT_TEXT_1);
            defaultStencilState(et_edit_2, tv_edit_2, tv_show_2, AppConfig.KEY_EDIT_SHOW_2, AppConfig.KEY_EDIT_TEXT_2);
            defaultStencilState(et_edit_3, tv_edit_3, tv_show_3, AppConfig.KEY_EDIT_SHOW_3, AppConfig.KEY_EDIT_TEXT_3);
            defaultStencilState(et_edit_4, tv_edit_4, tv_show_4, AppConfig.KEY_EDIT_SHOW_4, AppConfig.KEY_EDIT_TEXT_4);
            defaultStencilState(et_edit_5, tv_edit_5, tv_show_5, AppConfig.KEY_EDIT_SHOW_5, AppConfig.KEY_EDIT_TEXT_5);
            defaultStencilState(et_edit_6, tv_edit_6, tv_show_6, AppConfig.KEY_EDIT_SHOW_6, AppConfig.KEY_EDIT_TEXT_6);
            defaultStencilState(et_edit_7, tv_edit_7, tv_show_7, AppConfig.KEY_EDIT_SHOW_7, AppConfig.KEY_EDIT_TEXT_7);

            LinearLayout ll_bills_1 = (LinearLayout) ll_stencil.findViewById(R.id.stencil_lo_bill_1);
            String note_title = getString(R.string.print_note_title);
            String[] notes = note_title.split(";");
            TextView tv_bill_1_1 = (TextView) ll_bills_1.findViewById(R.id.print_tv_bill_1);
            tv_bill_1_1.setText(notes[0]);
            TextView tv_bill_1_2 = (TextView) ll_bills_1.findViewById(R.id.print_tv_bill_2);
            tv_bill_1_2.setText(notes[1]);
            TextView tv_bill_1_3 = (TextView) ll_bills_1.findViewById(R.id.print_tv_bill_3);
            tv_bill_1_3.setText(notes[2]);
            TextView tv_bill_1_4 = (TextView) ll_bills_1.findViewById(R.id.print_tv_bill_4);
            tv_bill_1_4.setText(notes[3]);

            LinearLayout ll_bills_2 = (LinearLayout) ll_stencil.findViewById(R.id.stencil_lo_bill_2);
            TextView tv_bill_2_1 = (TextView) ll_bills_2.findViewById(R.id.print_tv_bill_1);
            tv_bill_2_1.setText(getString(R.string.print_bill_1));
            TextView tv_bill_2_2 = (TextView) ll_bills_2.findViewById(R.id.print_tv_bill_2);
            tv_bill_2_2.setText("30.00");
            TextView tv_bill_2_3 = (TextView) ll_bills_2.findViewById(R.id.print_tv_bill_3);
            tv_bill_2_3.setText("1");
            TextView tv_bill_2_4 = (TextView) ll_bills_2.findViewById(R.id.print_tv_bill_4);
            tv_bill_2_4.setText("30.00");

            LinearLayout ll_bills_3 = (LinearLayout) ll_stencil.findViewById(R.id.stencil_lo_bill_3);
            TextView tv_bill_3_1 = (TextView) ll_bills_3.findViewById(R.id.print_tv_bill_1);
            tv_bill_3_1.setText(getString(R.string.print_bill_2));
            TextView tv_bill_3_2 = (TextView) ll_bills_3.findViewById(R.id.print_tv_bill_2);
            tv_bill_3_2.setText("10.00");
            TextView tv_bill_3_3 = (TextView) ll_bills_3.findViewById(R.id.print_tv_bill_3);
            tv_bill_3_3.setText("2");
            TextView tv_bill_3_4 = (TextView) ll_bills_3.findViewById(R.id.print_tv_bill_4);
            tv_bill_3_4.setText("20.00");

            LinearLayout ll_bills_4 = (LinearLayout) ll_stencil.findViewById(R.id.stencil_lo_bill_4);
            TextView tv_bill_4_1 = (TextView) ll_bills_4.findViewById(R.id.print_tv_bill_1);
            tv_bill_4_1.setText(getString(R.string.print_total_price));
            TextView tv_bill_4_2 = (TextView) ll_bills_4.findViewById(R.id.print_tv_bill_2);
            tv_bill_4_2.setText("");
            TextView tv_bill_4_3 = (TextView) ll_bills_4.findViewById(R.id.print_tv_bill_3);
            tv_bill_4_3.setText("");
            TextView tv_bill_4_4 = (TextView) ll_bills_4.findViewById(R.id.print_tv_bill_4);
            tv_bill_4_4.setText("50.00");
        }

        showView(printView);
    }

    /**
     * 选项默认选中状态
     */
    private void defaultStencilState(EditText et_txt, TextView tv_yes, TextView tv_show, String key_show, String key_text) {
        if (et_txt == null || tv_yes == null || tv_show == null) return;
        if (sp.getBoolean(key_show, true)) {
            tv_yes.setSelected(true);
        } else {
            tv_yes.setSelected(false);
        }
        String tx = sp.getString(key_text, "");
        if (!StringUtils.isNull(tx)) {
            et_txt.setText(tx);
        }
        updateStencilView(et_txt, tv_yes, tv_show, key_show);
    }

    /**
     * 修改选项选中状态
     */
    private void updateStencilState(EditText et_txt, TextView tv_yes, TextView tv_show, String key) {
        if (et_txt == null || tv_yes == null || tv_show == null) return;
        if (tv_yes.isSelected()) {
            tv_yes.setSelected(false);
        } else {
            tv_yes.setSelected(true);
        }
        updateStencilView(et_txt, tv_yes, tv_show, key);
        sp.edit().putBoolean(key, tv_yes.isSelected()).apply();
    }

    /**
     * 修改选项View状态
     */
    private void updateStencilView(EditText et_txt, TextView tv_yes, TextView tv_show, String key) {
        if (et_txt == null || tv_yes == null || tv_show == null) return;
        if (tv_yes.isSelected()) {
            if (!et_txt.isFocusable()) {
                et_txt.setFocusable(true);
                et_txt.setFocusableInTouchMode(true);
                et_txt.requestFocus();
                et_txt.setBackground(getResources().getDrawable(R.drawable.shape_bg_border_state_4));
            }
            String et_str = et_txt.getText().toString();
            if (et_str.isEmpty()) {
                switch (key) {
                    case AppConfig.KEY_EDIT_SHOW_1:
                        et_str = getString(R.string.print_title);
                        break;
                    case AppConfig.KEY_EDIT_SHOW_2:
                        et_str = getString(R.string.print_number);
                        break;
                    case AppConfig.KEY_EDIT_SHOW_3:
                        et_str = getString(R.string.print_company_name);
                        break;
                    case AppConfig.KEY_EDIT_SHOW_4:
                        et_str = getString(R.string.print_company_urls);
                        break;
                    case AppConfig.KEY_EDIT_SHOW_5:
                        et_str = getString(R.string.print_company_adds);
                        break;
                    case AppConfig.KEY_EDIT_SHOW_6:
                        et_str = getString(R.string.print_company_call);
                        break;
                    case AppConfig.KEY_EDIT_SHOW_7:
                        et_str = getString(R.string.print_company_line);
                        break;
                }
            }
            tv_show.setText(et_str);
            tv_show.setVisibility(View.VISIBLE);
        } else {
            if (et_txt.isFocusable()) {
                et_txt.setFocusable(false);
                et_txt.setFocusableInTouchMode(false);
                et_txt.setBackground(getResources().getDrawable(R.drawable.shape_bg_border_debar_4));
            }
            tv_show.setVisibility(View.GONE);
        }
        updatePrintTime();
    }

    /**
     * 修改模板打印时间
     */
    private void updatePrintTime() {
        if (tv_time != null) {
            tv_time.setText(getString(R.string.print_time) + TimeUtils.getTimeStr("yyyy-MM-dd HH:mm:ss"));
        }
        if (tv_code != null) {
            tv_code.setText(getString(R.string.print_code) + "C" + TimeUtils.getNowStr("yyyy-MM-dd") + "0001");
        }
    }

    /**
     * 初始化支付配置View
     */
    private void initPayView() {
        if (payView == null) {
            payView = LayoutInflater.from(mContext).inflate(R.layout.layout_pay, rl_show, false);
            final EditText et_active = (EditText) payView.findViewById(R.id.setting_et_active);
            Button bt_active = (Button) payView.findViewById(R.id.setting_bt_active);
            bt_active.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String activeCode = et_active.getText().toString();
                    if (activeCode.isEmpty()) {
                        showToast(getString(R.string.setting_active_hint));
                        return;
                    }
                    payActive(activeCode);
                }
            });

            final EditText et_password = (EditText) payView.findViewById(R.id.setting_et_password);
            Button bt_password = (Button) payView.findViewById(R.id.setting_bt_password);
            bt_password.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String passwordCode = et_password.getText().toString();
                    if (passwordCode.isEmpty()) {
                        showToast(getString(R.string.setting_null_password), 1000);
                        return;
                    }
                    if (passwordCode.length() != 6) {
                        showToast(getString(R.string.setting_hint_password), 1000);
                        return;
                    }
                    sp.edit().putString(AppConfig.KEY_PASSWORD, passwordCode).apply();
                    showToast(getString(R.string.setting_succ_password), 1000);
                }
            });
        }
        showView(payView);
    }

    /**
     * 初始化支付配置View
     */
    private void initVersionView() {
        if (versionView == null) {
            versionView = LayoutInflater.from(mContext).inflate(R.layout.layout_version, rl_show, false);
            String versionName = "1.0.1";
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS);
                versionName = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                ExceptionUtils.handle(e);
            }
            TextView tv_version_name = (TextView) versionView.findViewById(R.id.version_tv_version_name);
            tv_version_name.setText(getString(R.string.setting_version_name) + versionName);
        }
        showView(versionView);
    }

    /**
     * 显示子模块
     */
    private void showView(View view) {
        rl_show.removeAllViews();
        if (view != null) {
            rl_show.addView(view);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    private class MyTextWatcher implements TextWatcher {

        private EditText mEditText;

        public MyTextWatcher(EditText et) {
            mEditText = et;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String show = s.toString();
            switch (mEditText.getId()) {
                case R.id.setting_print_edit_et_1:
                    sp.edit().putString(AppConfig.KEY_EDIT_TEXT_1, show).apply();
                    updateStencilView(et_edit_1, tv_edit_1, tv_show_1, AppConfig.KEY_EDIT_SHOW_1);
                    break;
                case R.id.setting_print_edit_et_2:
                    sp.edit().putString(AppConfig.KEY_EDIT_TEXT_2, show).apply();
                    updateStencilView(et_edit_2, tv_edit_2, tv_show_2, AppConfig.KEY_EDIT_SHOW_2);
                    break;
                case R.id.setting_print_edit_et_3:
                    sp.edit().putString(AppConfig.KEY_EDIT_TEXT_3, show).apply();
                    updateStencilView(et_edit_3, tv_edit_3, tv_show_3, AppConfig.KEY_EDIT_SHOW_3);
                    break;
                case R.id.setting_print_edit_et_4:
                    sp.edit().putString(AppConfig.KEY_EDIT_TEXT_4, show).apply();
                    updateStencilView(et_edit_4, tv_edit_4, tv_show_4, AppConfig.KEY_EDIT_SHOW_4);
                    break;
                case R.id.setting_print_edit_et_5:
                    sp.edit().putString(AppConfig.KEY_EDIT_TEXT_5, show).apply();
                    updateStencilView(et_edit_5, tv_edit_5, tv_show_5, AppConfig.KEY_EDIT_SHOW_5);
                    break;
                case R.id.setting_print_edit_et_6:
                    sp.edit().putString(AppConfig.KEY_EDIT_TEXT_6, show).apply();
                    updateStencilView(et_edit_6, tv_edit_6, tv_show_6, AppConfig.KEY_EDIT_SHOW_6);
                    break;
                case R.id.setting_print_edit_et_7:
                    sp.edit().putString(AppConfig.KEY_EDIT_TEXT_7, show).apply();
                    updateStencilView(et_edit_7, tv_edit_7, tv_show_7, AppConfig.KEY_EDIT_SHOW_7);
                    break;
            }
        }
    };
}
