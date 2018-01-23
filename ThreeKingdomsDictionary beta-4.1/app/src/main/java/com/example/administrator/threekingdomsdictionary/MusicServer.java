package com.example.administrator.threekingdomsdictionary;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

/**
 * Created by renardbebe on 2017/11/19.
 */

public class MusicServer extends Service {

    public MediaPlayer mediaPlayer;
    public final IBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }
    }

        @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onStart(Intent intent,int startId){
        super.onStart(intent, startId);

        if(mediaPlayer==null){
            // R.raw.mmp是资源文件，MP3格式
            mediaPlayer = MediaPlayer.create(this, R.raw.victory);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        mediaPlayer.stop();
        super.onDestroy();
    }
}
