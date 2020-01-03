package edu.tjrac.swant.wjzx.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;
import edu.tjrac.swant.wjzx.aidl.bean.IRemoteService;
import edu.tjrac.swant.wjzx.aidl.bean.IRemoteServiceCallback;

/**
 * Created by wpc on 2020-01-02.
 */
public class RemoteService extends Service {


    private final IRemoteService.Stub binder= new IRemoteService.Stub(){

        @Override
        public int pid() throws RemoteException {
            return 0;
        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void hello() throws RemoteException {

        }

        @Override
        public void registerCallback(IRemoteServiceCallback callback) throws RemoteException {

        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
