// Generated code from Butter Knife. Do not modify!
package com.elite.weights.activity;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Finder;
import com.elite.weights.R;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class SettingActivity_ViewBinding<T extends SettingActivity> implements Unbinder {
  protected T target;

  public SettingActivity_ViewBinding(T target, Finder finder, Object source) {
    this.target = target;

    target.iv_title_back = finder.findRequiredViewAsType(source, R.id.title_iv_back, "field 'iv_title_back'", ImageView.class);
    target.tv_title_name = finder.findRequiredViewAsType(source, R.id.title_tv_name, "field 'tv_title_name'", TextView.class);
    target.sp_port = finder.findRequiredViewAsType(source, R.id.setting_sp_port, "field 'sp_port'", Spinner.class);
    target.sp_baud = finder.findRequiredViewAsType(source, R.id.setting_sp_baud, "field 'sp_baud'", Spinner.class);
    target.tv_test_result = finder.findRequiredViewAsType(source, R.id.setting_tv_test_result, "field 'tv_test_result'", TextView.class);
    target.bt_test_start = finder.findRequiredViewAsType(source, R.id.setting_bt_test_start, "field 'bt_test_start'", Button.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.iv_title_back = null;
    target.tv_title_name = null;
    target.sp_port = null;
    target.sp_baud = null;
    target.tv_test_result = null;
    target.bt_test_start = null;

    this.target = null;
  }
}
