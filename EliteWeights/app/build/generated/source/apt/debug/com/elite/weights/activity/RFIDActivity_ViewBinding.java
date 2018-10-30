// Generated code from Butter Knife. Do not modify!
package com.elite.weights.activity;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Finder;
import com.elite.weights.R;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class RFIDActivity_ViewBinding<T extends RFIDActivity> implements Unbinder {
  protected T target;

  public RFIDActivity_ViewBinding(T target, Finder finder, Object source) {
    this.target = target;

    target.iv_title_back = finder.findRequiredViewAsType(source, R.id.title_iv_back, "field 'iv_title_back'", ImageView.class);
    target.tv_title_name = finder.findRequiredViewAsType(source, R.id.title_tv_name, "field 'tv_title_name'", TextView.class);
    target.rv_rfids = finder.findRequiredViewAsType(source, R.id.rfid_rv_rfids, "field 'rv_rfids'", RecyclerView.class);
    target.tv_weight = finder.findRequiredViewAsType(source, R.id.rfid_tv_weight, "field 'tv_weight'", TextView.class);
    target.tv_read = finder.findRequiredViewAsType(source, R.id.rfid_tv_read, "field 'tv_read'", TextView.class);
    target.tv_write = finder.findRequiredViewAsType(source, R.id.rfid_tv_write, "field 'tv_write'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.iv_title_back = null;
    target.tv_title_name = null;
    target.rv_rfids = null;
    target.tv_weight = null;
    target.tv_read = null;
    target.tv_write = null;

    this.target = null;
  }
}
