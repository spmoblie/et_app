// Generated code from Butter Knife. Do not modify!
package com.elite.weights.adapter;

import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Finder;
import com.elite.weights.R;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class RFIDAdapter$ViewHolder_ViewBinding<T extends RFIDAdapter.ViewHolder> implements Unbinder {
  protected T target;

  public RFIDAdapter$ViewHolder_ViewBinding(T target, Finder finder, Object source) {
    this.target = target;

    target.ll_main = finder.findRequiredViewAsType(source, R.id.order_rv_item_root, "field 'll_main'", LinearLayout.class);
    target.mTV_1 = finder.findRequiredViewAsType(source, R.id.rfid_rv_item_tv_1, "field 'mTV_1'", TextView.class);
    target.mTV_2 = finder.findRequiredViewAsType(source, R.id.rfid_rv_item_tv_2, "field 'mTV_2'", TextView.class);
    target.mTV_3 = finder.findRequiredViewAsType(source, R.id.rfid_rv_item_tv_3, "field 'mTV_3'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.ll_main = null;
    target.mTV_1 = null;
    target.mTV_2 = null;
    target.mTV_3 = null;

    this.target = null;
  }
}
