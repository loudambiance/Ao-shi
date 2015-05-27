package co.loudbit.ao_shi;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class KillReceiver extends BroadcastReceiver {
    public KillReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AoShi.KillBlueTooth();
    }
}
