package com.example.zyz.lab6;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static android.R.attr.data;
import static android.R.attr.path;

public class MusicService extends Service {

    private static MediaPlayer mediaPlayer = null;
    private IBinder iBinder;
    private final int PLAYorPAUSE = 0;
    private final int STOP = 1;
    private final int EXIT = 2;
    private final int CURRENT = 3;
    private final int DRAG = 4;
    private final int LENGHT = 5;

    public MusicService() {
        iBinder = new MyBinder();
        if (mediaPlayer == null)
        {
            mediaPlayer = new MediaPlayer();
        }
        try{
            mediaPlayer.reset();
            mediaPlayer.setDataSource("/storage/emulated/0/a.mp3");
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return iBinder;
    }

    private class MyBinder extends Binder
    {
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags)
        throws RemoteException {
        switch (code)
        {
            case PLAYorPAUSE:
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    //int i = getPosition();
                    reply.writeInt(1);//回传一个数据给activity
                }
                else
                {
                    mediaPlayer.start();
                    reply.writeInt(0);
                }
                break;
            case STOP: // stop
                try {
                    mediaPlayer.stop();
                    mediaPlayer.prepare();
                    mediaPlayer.seekTo(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                reply.writeInt(2);
                break;
            case EXIT: // exit
                onDestroy(); break;
            case CURRENT: // get current  point
                reply.writeInt(mediaPlayer.getCurrentPosition()); break;
            case DRAG: // seek to some point
                mediaPlayer.seekTo(data.readInt()); break;
            case LENGHT: // get the time lenght of the song
                reply.writeInt(mediaPlayer.getDuration()); break;
        }
        return super.onTransact(code, data, reply, flags);
    }
    }

    @Override
    public void onDestroy()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}
