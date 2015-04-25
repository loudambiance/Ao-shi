package co.loudbit.ao_shi;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;


public class MainActivity extends Activity {

    final static boolean ALIVE = true;
    final static boolean DEAD = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        Button killbtn, revivebtn;
        ImageView sword, bt, btfail;
        ProgressBar progress;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            killbtn = (Button) rootView.findViewById(R.id.killbutton);
            sword = (ImageView) rootView.findViewById(R.id.sword);
            bt = (ImageView) rootView.findViewById(R.id.bt);
            btfail = (ImageView) rootView.findViewById(R.id.btfail);
            progress = (ProgressBar) rootView.findViewById(R.id.progressBar);
            killbtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    new KillBluetoothService().execute(killbtn);

                }
            });
            revivebtn = (Button) rootView.findViewById(R.id.revivebutton);
            revivebtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    new ReviveBluetoothService().execute(revivebtn);
                }
            });

            return rootView;
        }

        private class KillBluetoothService extends AsyncTask<Button,Void,Void> {

            protected Void doInBackground(Button... btn)
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
                return null;
            }
            protected void onPreExecute(){
                killbtn.setClickable(false);
                revivebtn.setClickable(false);
                sword.setVisibility(View.INVISIBLE);
                bt.setVisibility(View.INVISIBLE);
                btfail.setVisibility(View.INVISIBLE);
                progress.setVisibility(View.VISIBLE);
            }

            protected void onPostExecute(Void input)
            {
                killbtn.setClickable(true);
                revivebtn.setClickable(true);
                progress.setVisibility(View.INVISIBLE);
                sword.setVisibility(View.VISIBLE);
            }
        }

        private class ReviveBluetoothService extends AsyncTask<Button,Void,Void> {

            protected Void doInBackground(Button... btn)
            {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.enable();
                }
                try{Thread.sleep(3000);}catch(Exception e){Log.d("Aoshi", e.toString());}
                if (!mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.enable();
                }
                try{Thread.sleep(6000);}catch(Exception e){Log.d("Aoshi", e.toString());}
                return null;
            }

            protected void onPreExecute() {
                bt.setVisibility(View.INVISIBLE);
                btfail.setVisibility(View.INVISIBLE);
                killbtn.setClickable(false);
                revivebtn.setClickable(false);
                sword.setVisibility(View.INVISIBLE);
                progress.setVisibility(View.VISIBLE);
            }

            protected void onPostExecute(Void input)
            {
                progress.setVisibility(View.INVISIBLE);
                killbtn.setClickable(true);
                revivebtn.setClickable(true);
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter.isEnabled()) {
                    bt.setVisibility(View.VISIBLE);
                }else{
                    btfail.setVisibility(View.VISIBLE);
                }
            }
        }

    }
}
