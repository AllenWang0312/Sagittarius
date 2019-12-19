package edu.tjrac.swant.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

/**
 * Created by wpc on 2018/1/31 0031.
 */

public class BlueToothService extends Service {

    BluetoothAdapter adapter;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        adapter=BluetoothAdapter.getDefaultAdapter();

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
