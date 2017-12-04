package com.example.zyz.lab6;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Permission;
import java.text.SimpleDateFormat;

import static android.R.attr.y;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    private final int PLAYorPAUSE = 0;
    private final int STOP = 1;
    private final int EXIT = 2;
    private final int CURRENT = 3;
    private final int DRAG = 4;
    private final int LENGHT = 5;

    private SeekBar seekBar;
    private Button stop_button;
    private Button play_button;
    private Button exit_button;
    private TextView current_pos;
    private TextView end_pos;
    private TextView status_show;
    private Integer status;
    private Boolean is_changing_seek;
    private IBinder mbinder;
    private ServiceConnection mServerConnect;
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("mm:ss");
    private ObjectAnimator mObjectAnimator;
    private ObjectAnimator hObjectAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Immersion();
        verifyStoragePermissions(this);

        ElemBinding();
        EvenBinding();
        ServerInit();
        HandleInit();
    }

    private void Immersion()
    {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    private void  ElemBinding()
    {
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        stop_button = (Button)findViewById(R.id.stop_button);
        play_button = (Button)findViewById(R.id.play_button);
        exit_button = (Button)findViewById(R.id.exit_button);
        current_pos = (TextView)findViewById(R.id.current_pos);
        end_pos = (TextView)findViewById(R.id.end_pos);
        status_show = (TextView)findViewById(R.id.status);
        status = -1;
        is_changing_seek = false;
        //旋转动画，利用ObjectAnimator对控件控制
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        mObjectAnimator = ObjectAnimator.ofFloat(imageView,"rotation", 0, 359);
        mObjectAnimator.setDuration(20000);
        mObjectAnimator.setInterpolator(new LinearInterpolator());
        mObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        mObjectAnimator.setRepeatMode(ObjectAnimator.RESTART);
        mObjectAnimator.start();
        mObjectAnimator.pause();

        ImageView handle = (ImageView) findViewById(R.id.ic_handle);
        handle.setPivotX(handle.getWidth() / 4);
        handle.setPivotY(handle.getHeight() / 8);
        hObjectAnimator = ObjectAnimator.ofFloat(handle,"rotation", -30, 0);
        hObjectAnimator.setDuration(1000);
        hObjectAnimator.setInterpolator(new LinearInterpolator());
        hObjectAnimator.setRepeatCount(0);
        hObjectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        hObjectAnimator.start();
        hObjectAnimator.pause();

    }

    private void EvenBinding(){
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try {
                    Parcel reply = Parcel.obtain();
                    mbinder.transact(PLAYorPAUSE, null, reply, 0);
                    status = reply.readInt();
                    Refresh();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try {
                    Parcel reply = Parcel.obtain();
                    mbinder.transact(STOP, null, reply, 0);
                    status = reply.readInt();
                    Refresh();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDestroy();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                current_pos.setText(mSimpleDateFormat.format(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                is_changing_seek = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    Parcel data = Parcel.obtain();
                    data.writeInt(seekBar.getProgress());
                    mbinder.transact(DRAG, data, null, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                is_changing_seek=false;
            }
        });
    }

    private void ServerInit()
    {
        mServerConnect = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                mbinder = service;
                try {
                    Parcel reply = Parcel.obtain();
                    mbinder.transact(LENGHT, null, reply, 0); //get the duration of the song
                    int lenght = reply.readInt();//reply 一次性
                    seekBar.setMax(lenght);
                    end_pos.setText(mSimpleDateFormat.format(lenght));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mServerConnect = null;
            }
        };
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, mServerConnect, BIND_AUTO_CREATE);
    }

    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有读的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.READ_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有读的权限，去申请读的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);
        if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED))
            onDestroy();
        else
        {
            Toast.makeText(MainActivity.this, "已授权",Toast.LENGTH_SHORT).show();
        }
    }


    private void HandleInit()
    {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                if(!is_changing_seek) {
                    try {
                        Parcel reply = Parcel.obtain();
                        mbinder.transact(CURRENT, null, reply, 0);
                        seekBar.setProgress(reply.readInt());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                super.handleMessage(message);
            }
        };
        Thread mThread = new Thread() {
            @Override
            public void run() {
                while (true) {

                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (mServerConnect != null) {
                        handler.obtainMessage().sendToTarget();
                    }
                }
            }
        };
        mThread.start();
    }

    private void Refresh() throws RemoteException {
        switch (status)
        {
            case 0://play
                play_button.setBackgroundResource(R.drawable.pause);
                status_show.setText(R.string.playing);
                if (mObjectAnimator.isStarted())
                {
                    mObjectAnimator.resume();
                    hObjectAnimator.resume();
                }
                else
                {
                    mObjectAnimator.start();
                    hObjectAnimator.start();
                }
                break;
            case 1: //pause
                play_button.setBackgroundResource(R.drawable.play);
                status_show.setText(R.string.paused);
                mObjectAnimator.pause();
                hObjectAnimator.pause();
                hObjectAnimator.end();
                hObjectAnimator.start();
                hObjectAnimator.pause();
                break;
            case 2: //stop
                play_button.setBackgroundResource(R.drawable.play);
                status_show.setText(R.string.stopped);
                mObjectAnimator.pause();
                mObjectAnimator.end();
                hObjectAnimator.pause();
                hObjectAnimator.end();
                hObjectAnimator.start();
                hObjectAnimator.pause();
                break;
        }
    }

    @Override
    public void onDestroy() {
        try {
            mbinder.transact(EXIT, null, null, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        unbindService(mServerConnect);
        mServerConnect = null;
        this.finish();
        System.exit(0);
        super.onDestroy();
    }

    //点击返回键不销毁程序，而是后台运行
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent home = new Intent(Intent.ACTION_MAIN);
//            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            home.addCategory(Intent.CATEGORY_HOME);
//            startActivity(home);
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
