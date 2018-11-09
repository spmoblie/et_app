package com.elite.inventory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bigkoo.pickerview.TimePickerView;
import com.elite.inventory.AppManager;
import com.elite.inventory.R;
import com.elite.inventory.config.AppConfig;
import com.elite.inventory.utils.StringUtils;
import com.elite.inventory.utils.TimeUtils;
import com.elite.inventory.utils.ToastUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.et_input_data)
    EditText etInputData;

    @BindView(R.id.et_input_work)
    EditText etInputWork;

    @BindView(R.id.et_input_user)
    EditText etInputUser;

    @BindView(R.id.et_input_password)
    EditText etInputPassword;

    @BindView(R.id.et_input_petty)
    EditText etInputPetty;

    @BindView(R.id.iv_input_work_2)
    ImageView ivInputWork2;

    @BindView(R.id.iv_input_password_2)
    ImageView ivInputPassword2;

    @BindView(R.id.iv_login_number_01)
    ImageView ivLoginNumber01;

    @BindView(R.id.iv_login_number_02)
    ImageView ivLoginNumber02;

    @BindView(R.id.iv_login_number_03)
    ImageView ivLoginNumber03;

    @BindView(R.id.iv_login_number_04)
    ImageView ivLoginNumber04;

    @BindView(R.id.iv_login_number_05)
    ImageView ivLoginNumber05;

    @BindView(R.id.iv_login_number_06)
    ImageView ivLoginNumber06;

    @BindView(R.id.iv_login_number_07)
    ImageView ivLoginNumber07;

    @BindView(R.id.iv_login_number_08)
    ImageView ivLoginNumber08;

    @BindView(R.id.iv_login_number_09)
    ImageView ivLoginNumber09;

    @BindView(R.id.iv_login_number_dot)
    ImageView ivLoginNumberDot;

    @BindView(R.id.iv_login_number_00)
    ImageView ivLoginNumber00;

    @BindView(R.id.iv_login_number_quit)
    ImageView ivLoginNumberQuit;

    @BindView(R.id.iv_login_number_delete)
    ImageView ivLoginNumberDelete;

    @BindView(R.id.iv_login_number_clear)
    ImageView ivLoginNumberClear;

    @BindView(R.id.iv_login_number_enter)
    ImageView ivLoginNumberEnter;

    TimePickerView pvTime;
    boolean isData, isWord, isUser, isPassWord, isPetty, orAB, isShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        AppManager.getInstance().addActivity(this);// 添加Activity到堆栈

        init();
        initEditText();
    }

    private void init() {
        //默认账户、密码
        sp.edit().putString(AppConfig.DEFAULT_USER, AppConfig.DEFAULT_PASSWORD).apply();
        //最近使用的班次
        orAB = sp.getBoolean(AppConfig.KEY_WORK, false);

        ivInputWork2.setOnClickListener(this);
        ivInputPassword2.setOnClickListener(this);
        ivLoginNumber00.setOnClickListener(this);
        ivLoginNumber01.setOnClickListener(this);
        ivLoginNumber02.setOnClickListener(this);
        ivLoginNumber03.setOnClickListener(this);
        ivLoginNumber04.setOnClickListener(this);
        ivLoginNumber05.setOnClickListener(this);
        ivLoginNumber06.setOnClickListener(this);
        ivLoginNumber07.setOnClickListener(this);
        ivLoginNumber08.setOnClickListener(this);
        ivLoginNumber09.setOnClickListener(this);
        ivLoginNumberDot.setOnClickListener(this);
        ivLoginNumberQuit.setOnClickListener(this);
        ivLoginNumberDelete.setOnClickListener(this);
        ivLoginNumberClear.setOnClickListener(this);
        ivLoginNumberEnter.setOnClickListener(this);
    }

    private void initEditText() {
        disableShowSoftInput(etInputData);
        disableShowSoftInput(etInputWork);
        disableShowSoftInput(etInputUser);
        disableShowSoftInput(etInputPassword);
        disableShowSoftInput(etInputPetty);

        //日期
        etInputData.setText(TimeUtils.getTimeStr("yyyy-MM-dd"));
        etInputData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        etInputData.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean b) {
                isData = b;
            }
        });

        //班次
        changeWorks();
        etInputWork.setFocusable(false);
        etInputWork.setFocusableInTouchMode(false);
        /*etInputWork.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean b) {
                isWord = b;
            }
        });*/

        //用户
        etInputUser.setFocusable(true);
        etInputUser.setFocusableInTouchMode(true);
        etInputUser.requestFocus();
        isUser = true;
        etInputUser.setText(sp.getString(AppConfig.KEY_USER, AppConfig.DEFAULT_USER));
        etInputUser.setSelection(etInputUser.length());
        etInputUser.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean b) {
                isUser = b;
            }
        });

        //密码
        isShow = false;
        etInputPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                isPassWord = b;
            }
        });

        //备用金
        etInputPetty.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean b) {
                isPetty = b;
            }
        });
    }

    /**
     * 显示日历控件
     */
    private void showDatePicker() {
        if (pvTime == null) {
            pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {

                @Override
                public void onTimeSelect(Date date, View v) {
                    if (etInputData != null) {
                        etInputData.setText(TimeUtils.dateToStr(date, "yyyy-MM-dd"));
                        etInputData.setSelection(etInputData.length());
                    }
                }

            })
                    .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
//                    .setTitleText("Title")//标题文字
//                    .setTitleColor(Color.BLACK)//标题文字颜色
                    .setTitleBgColor(getColor(R.color.back_color_lc_08))//标题背景颜色 Night mode
                    .setTitleSize(22)//标题文字大小
                    .setContentSize(22)//滚轮文字大小
                    .setSubmitText("确定")//确认按钮文字
                    .setSubmitColor(0xFFF58807)//确定按钮文字颜色
                    .setCancelText("取消")//取消按钮文字
                    .setCancelColor(0xFFF58807)//取消按钮文字颜色
                    .setDividerColor(0xFFF58807)//居中横条边框颜色
                    .setTextColorOut(0xFFF58807)//非居中日期文字颜色
                    .setTextColorCenter(0xFFF58807)//居中日期文字颜色
//                    .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
//                    .isCyclic(true)//是否循环滚动
                    .setBgColor(getColor(R.color.back_color_lc_08))//滚轮背景颜色 Night mode
//                    .setDate(selectedDate)// 如果不设置的话，默认是系统时间
//                    .setRangDate(startDate,endDate)//起始终止年月日设定
//                    .setLabel("年","月","日","时","分","秒")//默认设置为年月日时分秒
                    .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                    .isDialog(true)//是否显示为对话框样式
                    .build();
            pvTime.show();
        } else {
            pvTime.show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_input_work_2:
                orAB = !orAB;
                changeWorks();
                sp.edit().putBoolean(AppConfig.KEY_WORK, orAB).apply();
                break;
            case R.id.iv_input_password_2:
                isShow = !isShow;
                updatePasswordStatus();
                break;
            case R.id.iv_login_number_00:
                inputText("0");
                break;
            case R.id.iv_login_number_01:
                inputText("1");
                break;
            case R.id.iv_login_number_02:
                inputText("2");
                break;
            case R.id.iv_login_number_03:
                inputText("3");
                break;
            case R.id.iv_login_number_04:
                inputText("4");
                break;
            case R.id.iv_login_number_05:
                inputText("5");
                break;
            case R.id.iv_login_number_06:
                inputText("6");
                break;
            case R.id.iv_login_number_07:
                inputText("7");
                break;
            case R.id.iv_login_number_08:
                inputText("8");
                break;
            case R.id.iv_login_number_09:
                inputText("9");
                break;
            case R.id.iv_login_number_dot:
                inputText(".");
                break;
            case R.id.iv_login_number_quit:
                inputText("quit");
                break;
            case R.id.iv_login_number_delete:
                inputText("delete");
                break;
            case R.id.iv_login_number_clear:
                inputText("clear");
                break;
            case R.id.iv_login_number_enter:
                inputText("enter");
                break;
        }
    }

    private void inputText(String str) {
        switch (str) {
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case ".":
                if (isData) {
                    etInputData.getText().insert(etInputData.getSelectionStart(), str);
                } else
                if (isWord) {
                    etInputWork.getText().insert(etInputWork.getSelectionStart(), str);
                } else
                if (isUser) {
                    etInputUser.getText().insert(etInputUser.getSelectionStart(), str);
                } else
                if (isPassWord) {
                    etInputPassword.getText().insert(etInputPassword.getSelectionStart(), str);
                } else
                if (isPetty) {
                    etInputPetty.getText().insert(etInputPetty.getSelectionStart(), str);
                }
                break;
            case "delete":
                int index;
                if (isData) {
                    index = etInputData.getSelectionStart();
                    if (index > 0) {
                        etInputData.getText().delete(index-1, index);
                    }
                } else
                if (isWord) {
                    index = etInputWork.getSelectionStart();
                    if (index > 0) {
                        etInputWork.getText().delete(index-1, index);
                    }
                } else
                if (isUser) {
                    index = etInputUser.getSelectionStart();
                    if (index > 0) {
                        etInputUser.getText().delete(index-1, index);
                    }
                } else
                if (isPassWord) {
                    index = etInputPassword.getSelectionStart();
                    if (index > 0) {
                        etInputPassword.getText().delete(index-1, index);
                    }
                } else
                if (isPetty) {
                    index = etInputPetty.getSelectionStart();
                    if (index > 0) {
                        etInputPetty.getText().delete(index-1, index);
                    }
                }
                break;
            case "clear":
                if (isData) {
                    etInputData.getText().clear();
                } else
                if (isWord) {
                    etInputWork.getText().clear();
                } else
                if (isUser) {
                    etInputUser.getText().clear();
                } else
                if (isPassWord) {
                    etInputPassword.getText().clear();
                } else
                if (isPetty) {
                    etInputPetty.getText().clear();
                }
                break;
            case "quit": //退出应用
                AppManager.getInstance().AppExit(getApplicationContext());
                break;
            case "enter":
                /*String et_data = etInputData.getText().toString();
                if (StringUtils.isNull(et_data)) {
                    ToastUtils.showToast("请选择日期", 1000);
                    return;
                }
                String et_user = etInputUser.getText().toString();
                if (StringUtils.isNull(et_user)) {
                    ToastUtils.showToast("请输入用户名", 1000);
                    return;
                }
                String et_password = etInputPassword.getText().toString();
                if (StringUtils.isNull(et_password)) {
                    ToastUtils.showToast("请输入密码", 1000);
                    return;
                }
                String et_petty = etInputPetty.getText().toString();
                if (StringUtils.isNull(et_petty)) {
                    ToastUtils.showToast("请输入备用金", 1000);
                    return;
                }
                if (!sp.contains(et_user)) {
                    ToastUtils.showToast("用户名不存在", 1000);
                    return;
                }
                String passwordStr = sp.getString(et_user, "");
                if (!passwordStr.equals(et_password)) {
                    ToastUtils.showToast("密码错误", 1000);
                    return;
                }
                //记录最近登录的用户名
                sp.edit().putString(AppConfig.KEY_USER, et_user).apply();
                //记录当前账户的备用金
                sp.edit().putString(AppConfig.KEY_PETTY, et_petty).apply();*/
                //进入主页
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                //关闭此页
                finish();
                break;
        }
    }

    /**
     * 切换班次
     */
    private void changeWorks() {
        if (orAB) {
            etInputWork.setText("B");
        } else {
            etInputWork.setText("A");
        }
        ivInputWork2.setSelected(orAB);
    }

    /**
     * 密码状态
     */
    private void updatePasswordStatus() {
        if (isShow) { //显示密码
            etInputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }else { //隐藏密码
            etInputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        etInputPassword.setSelection(etInputPassword.length()); //调整光标至最后
        ivInputPassword2.setSelected(isShow);
    }

}
