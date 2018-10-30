// Generated code from Butter Knife. Do not modify!
package com.elite.weights.activity;

import android.view.SurfaceView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.Finder;
import com.elite.weights.R;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class MainActivity_ViewBinding<T extends MainActivity> implements Unbinder {
  protected T target;

  public MainActivity_ViewBinding(T target, Finder finder, Object source) {
    this.target = target;

    target.rl_main = finder.findRequiredViewAsType(source, R.id.rl_main, "field 'rl_main'", RelativeLayout.class);
    target.surfaceView = finder.findRequiredViewAsType(source, R.id.sv_camera, "field 'surfaceView'", SurfaceView.class);
    target.iv_show = finder.findRequiredViewAsType(source, R.id.camera_iv_show, "field 'iv_show'", ImageView.class);
    target.bt_rfid = finder.findRequiredViewAsType(source, R.id.main_bt_test_rfid, "field 'bt_rfid'", Button.class);
    target.bt_print = finder.findRequiredViewAsType(source, R.id.main_bt_test_print, "field 'bt_print'", Button.class);
    target.bt_setting = finder.findRequiredViewAsType(source, R.id.main_bt_setting, "field 'bt_setting'", Button.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.rl_main = null;
    target.surfaceView = null;
    target.iv_show = null;
    target.bt_rfid = null;
    target.bt_print = null;
    target.bt_setting = null;

    this.target = null;
  }
}
