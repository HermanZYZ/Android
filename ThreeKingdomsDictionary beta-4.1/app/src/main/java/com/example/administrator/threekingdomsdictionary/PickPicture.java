package com.example.administrator.threekingdomsdictionary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.ycl.chooseavatar.library.OnChoosePictureListener;
import com.ycl.chooseavatar.library.UpLoadHeadImageDialog;
import com.ycl.chooseavatar.library.YCLTools;

import java.security.Permission;

public class PickPicture extends AppCompatActivity implements OnChoosePictureListener {
    UpLoadHeadImageDialog up;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_picture);

        YCLTools.getInstance().setOnChoosePictureListener(this);
        up=new UpLoadHeadImageDialog(PickPicture.this);
        up.show();
     }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //must set like this
        YCLTools.getInstance().upLoadImage(requestCode, resultCode, data);
    }

    @Override
    public void OnChoose(String filePath) {
//        Toast.makeText(getApplicationContext(),"filePath:"+filePath,Toast.LENGTH_LONG).show();
//        Bundle bundle=new Bundle();
//        bundle.putSerializable("filePath",filePath);

        Intent intent=new Intent(PickPicture.this,AddPeople.class);
        intent.putExtra("filePath",filePath);
        setResult(101,intent);
        finish();
        //把filePath传到AddPeople
    }

    @Override
    public void OnCancel() {
        Intent intent=new Intent(PickPicture.this,AddPeople.class);
        startActivity(intent);
        finish();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
//        if (keyCode == KeyEvent.KEYCODE_BACK)
//        {
////            up.dismiss();
//            Intent intent=new Intent(PickPicture.this,AddPeople.class);
//            startActivity(intent);
////            Toast.makeText(PickPicture.this,"aaa",Toast.LENGTH_SHORT).show();
//            finish();
//        }
//        return super.onKeyDown(keyCode,event);
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
