package com.narrowstudio.wonski.linguastic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent newIntent = new Intent("com.narrowstudio.wonski.linguastic.SERVICE_ACTION").putExtra("actionname", intent.getAction());
        context.sendBroadcast(newIntent);
    }


}
