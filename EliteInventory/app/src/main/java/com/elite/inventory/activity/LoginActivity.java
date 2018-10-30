package com.elite.inventory.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.elite.inventory.R;

import java.lang.reflect.Method;

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

    boolean isData, isWord, isUser, isPassWord, isPetty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        init();
        initEditText();
    }

    private void init() {
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

        etInputData.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean b) {
                isData = b;
            }
        });
        etInputWork.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean b) {
                isWord = b;
            }
        });
        etInputUser.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean b) {
                isUser = b;
            }
        });
        etInputPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                isPassWord = b;
            }
        });
        etInputPetty.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean b) {
                isPetty = b;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
            case "quit":
                break;
            case "enter":
                break;
        }
    }

}
