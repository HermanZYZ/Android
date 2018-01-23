package com.example.administrator.threekingdomsdictionary;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class SplashActivity extends AppCompatActivity {

    private ImageView splash_img;
    private int resId[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        resId = new int[]{R.drawable.liubei,R.drawable.guanyu,R.drawable.zhangfei,
                R.drawable.zhugeliang,R.drawable.sunquan,R.drawable.zhouyu,
                R.drawable.sunce,R.drawable.caocao,R.drawable.sunshangxiang,
                R.drawable.lvbu
        };
//        resId = new int[]{R.drawable.liubei};
        splash_img = (ImageView) findViewById(R.id.splash_img);

        //获取读写权限
//        PictureTool.verifyStoragePermissions(this);

        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 123:
                        try {
                            // 载入图片
                            Log.i("1","ok");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };

        //第一次安装将drawable的人物放入文件夹中
        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    PictureTool.uploadDrawableToFile(SplashActivity.this, resId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mHandler.obtainMessage(123).sendToTarget();
            }
        };
        mThread.start();

        animationImage();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .taskExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                .taskExecutorForCachedImages(AsyncTask.THREAD_POOL_EXECUTOR)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .build();
        ImageLoader.getInstance().init(config);
    }

    private void animationImage()
    {
        float end = 1.13f;
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(splash_img,"scaleX",1f,end);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(splash_img,"scaleY",1f,end);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(3000).play(animatorX).with(animatorY);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Intent intent_1 = new Intent(SplashActivity.this,Login.class);
                startActivity(intent_1);
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],int []grantResults)
    {
        if(grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){

        }
        else{
            System.exit(0);
        }
        return;
    }
}
