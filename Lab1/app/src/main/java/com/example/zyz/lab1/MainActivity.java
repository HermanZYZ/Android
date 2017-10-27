package com.example.zyz.lab1;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private ImageView Image;
    private TextInputLayout ID;
    private TextInputLayout Password;
    private EditText ID_input;
    private EditText Password_input;
    private RadioGroup Select_iden;
    private Button Sign_in;
    private Button Sign_up;
    private boolean isStudent;

    private String DialogString[] = new String[]{"拍摄","从相册选择"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init(); //初始化，获取控件
        evenBinding();//
    }

    private void Init()
    {
        Image = (ImageView) findViewById(R.id.imageView);
        ID = (TextInputLayout) findViewById(R.id.number);
        Password = (TextInputLayout) findViewById(R.id.password);
        ID_input = (EditText) findViewById(R.id.number_input);
        Password_input = (EditText) findViewById(R.id.password_input);
        Select_iden = (RadioGroup) findViewById(R.id.radioGroup);
        Sign_in = (Button) findViewById(R.id.sign_in);
        Sign_up = (Button) findViewById(R.id.sign_up);
        isStudent = true;
    }

    private void Snackbar_Show(View view, String showstr)
    {
        Snackbar.make(view,showstr,Snackbar.LENGTH_SHORT).setAction("确定",new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getApplicationContext(), "Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
            }
        }).setActionTextColor(Color.BLUE).show();
    }


    private void evenBinding()
    {
        Image.setOnClickListener(new View.OnClickListener()
        {
           @Override
            public void onClick(final View view)
           {
               AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this)
                       .setTitle("上传头像")
                       .setNegativeButton("取消",new DialogInterface.OnClickListener()
                       {
                           @Override
                           public void onClick(DialogInterface dialog,int Select)
                           {
                               Toast.makeText(getApplicationContext(), "您选择了[取消]", Toast.LENGTH_SHORT).show();
                           }
                       }).setItems(DialogString, new DialogInterface.OnClickListener()
                       {
                           @Override
                           public void onClick(DialogInterface dialog, int Select)
                           {
                               Toast.makeText(getApplicationContext(),"您选择了["+DialogString[Select]+"]",Toast.LENGTH_SHORT).show();
                           }

                       });
               mBuilder.create().show();
           }

        });

        Select_iden.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radiogroup, @IdRes int checkedID)
            {
                isStudent = checkedID == R.id.student;
                String Show = "您选择了" + (isStudent ? "学生" : "教职工");;
                Snackbar_Show(radiogroup,Show);
            }

        });


        Sign_in.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String id = ID_input.getText().toString();
                String password = Password_input.getText().toString();
                if(id.isEmpty())
                    ID.setError("学号不能为空");
                else if(password.isEmpty())
                {
                    ID.setErrorEnabled(false);
                    Password.setError("密码不能为空");
                }
                else if(id.equals("123456") && password.equals("6666"))
                {
                    ID.setErrorEnabled(false);
                    Password.setErrorEnabled(false);
                    Snackbar_Show(view, "登陆成功");
                }
                else
                {
                    ID.setErrorEnabled(false);
                    Password.setErrorEnabled(false);
                    Snackbar_Show(view, "学号或密码错误");
                }
            }

        });

        Sign_up.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String Show = (isStudent ? "学生" : "教职工") + "注册功能尚未启用";
                if(isStudent)
                    Snackbar_Show(view, Show);
                else
                    Toast.makeText(getApplicationContext(),Show,Toast.LENGTH_SHORT).show();
            }

        });
    }
}
