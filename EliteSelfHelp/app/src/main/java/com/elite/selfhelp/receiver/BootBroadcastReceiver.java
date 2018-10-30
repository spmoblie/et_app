package com.elite.selfhelp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.elite.selfhelp.AppApplication;


/**
 * Created by user on 01/03/18.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {

    private static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            //开机自启动应用
            context.startActivity(context.getPackageManager()
                    .getLaunchIntentForPackage(AppApplication.getInstance().getPackageName()));
        }
    }
}
