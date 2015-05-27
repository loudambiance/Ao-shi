package co.loudbit.ao_shi;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;

/**
 * Created by Daniel on 5/26/2015.
 */
public class AoShi {
    final static boolean ALIVE = true;
    final static boolean DEAD = false;

    public static void KillBlueTooth()
    {
        String search = "ps | grep -F com.android.bluetooth | grep -v -F grep | awk '{print $2}'";
        String[] killstr ={"su","-c", "kill -9 $(" + search +")"};
        String[] searchstr={"su","-c", search + " | grep -E [0-9]+"};
        try {
            boolean status = ALIVE;
            int ret = 0, lives=0;
            do {
                Process p = Runtime.getRuntime().exec(killstr);
                ret = p.waitFor();
                p = Runtime.getRuntime().exec(searchstr);
                ret = p.waitFor();
                if (ret != 0 || lives >=9) {
                    status=DEAD;
                }
                lives++;
            }while(status);
        }catch(Exception e)
        {
            Log.d("Aoshi", e.toString());
        }
        //Disable bluetooth
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.disable();
    }

    public static void ReviveBlueTooth()
    {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
        try{Thread.sleep(3000);}catch(Exception e){
            Log.d("Aoshi", e.toString());}
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
        try{Thread.sleep(6000);}catch(Exception e){Log.d("Aoshi", e.toString());}
    }
}
