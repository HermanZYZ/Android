package com.example.administrator.threekingdomsdictionary;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/11/20.
 */

public class PictureTool extends Activity
{
    /**
     * 将bitmap转化为file文件
     * @param bitmap
     * @param filePath
     * @return
     */
    public static File BitmapToFile(Bitmap bitmap,String filePath){
        File file = new File(filePath);
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);//PNG格式 100%完整性
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 将bitmap转化为file,并保存文件目录下
     * @param bitmap
     * @param filePath 存放的文件目录
     * @param fileName 文件名
     * @return
     */
    public static File BitmapToFile(Bitmap bitmap, String filePath,String fileName)
    {
        return BitmapToFile(bitmap,filePath+fileName);
    }

    /**
     * 将url转化为绝对路径
     * @param activity
     * @param uri
     * @return String
     */
    public static String UriToPath(Activity activity, Uri uri)
    {
        Cursor cursor = activity.managedQuery(uri,new String[]{MediaStore.Images.Media.DATA},null,null,null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
        return path;
    }

    /**
     * 从uri中提取bitmap对象
     * @param activity
     * @param uri
     * @return
     */
    public static Bitmap getBitmapFromUri(Activity activity,Uri uri)
    {
        String path = UriToPath(activity,uri);
        Bitmap bt = BitmapFactory.decodeFile(path);
        return bt;
    }

    /**
     * 从完整路径名转化为bitmap
     * @param path
     * @return
     */
    public static Bitmap getBitmapFromPath(Context context, String path)
    {
        if(new File(path).exists()) {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            Bitmap bm = BitmapFactory.decodeFile(path, opt);

            // 获取到这个图片的原始宽度和高度
            int picWidth = opt.outWidth;
            int picHeight = opt.outHeight;

            // 获取屏的宽度和高度
            DisplayMetrics dm2 = context.getResources().getDisplayMetrics();
            int screenWidth = dm2.widthPixels;
            int screenHeight = dm2.heightPixels;

            // isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
            opt.inSampleSize = 1;
            // 根据屏的大小和图片大小计算出缩放比例
            if (picWidth > picHeight) {
                if (picWidth > screenWidth)
                    opt.inSampleSize = picWidth / screenWidth;
            } else {
                if (picHeight > screenHeight)
                    opt.inSampleSize = picHeight / screenHeight;
            }

            // 这次再真正地生成一个有像素的，经过缩放了的bitmap
            opt.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(path, opt);
            return bm;
        }
        else
            return null;
    }

    /**
     * 将drawable文件复制到另一文件夹下 只用于第一次安装程序
     * @param activity
     * @param resId
     * @return
     */
    public static boolean uploadDrawableToFile(Activity activity,int resId,String type)
    {
        String path = activity.getFilesDir().toString()+"/"+activity.getResources().getResourceEntryName(resId)+type;
//        if(new File(path).exists())
//        {
//            return false;
//        }
        Bitmap bt = BitmapFactory.decodeResource(activity.getResources(),resId);
        File file = BitmapToFile(bt,path);
//        if(file == null || !file.exists())return false;
        return true;
    }

    /**
     * 第一次上传的10张图片
     * @param activity
     * @param resId
     * @return
     */
    public static boolean uploadDrawableToFile(Activity activity,int resId[])
    {
        String type = ".jpg";
        for(int id:resId)
        {
            if(uploadDrawableToFile(activity,id,type) == false)return false;
        }
        return true;
    }

    /**
     * get permission
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity)
    {
        try{
            int permission = ActivityCompat.checkSelfPermission(activity,"android.permission.READ_EXTERNAL_STORAGE");
            if(permission != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(activity,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
