package com.example.administrator.threekingdomsdictionary;

import android.app.ActivityOptions;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Visibility;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class Login extends AppCompatActivity {

    private DBManager mgr;
    final private static User admin = new User("admin","admin");

    final private static int USER_CORRECT = 0;//正确输入用户信息
    final private static int USER_EMPTY_NAME_ERROR = 1;//输入用户名为空
    final private static int USER_EMPTY_PASSWORD_ERROE = 2;//输入的密码为空
    final private static int USER_WRONG = 3;//用户名不存在或者密码错误
    final private static int USER_EMPTY_NAME_PASSWORD_ERROR=4;//用户名和密码都为空
    @InjectView(R.id.et_username)
    EditText etUsername;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.bt_go)
    Button btGo;
    @InjectView(R.id.cv)
    CardView cv;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    @InjectView(R.id.login_layout_anonymous)
    Button login_layout_anonymous;
    @InjectView(R.id.username_wrapper)
    TextInputLayout username_wrapper;
    @InjectView(R.id.password_wrapper)
    TextInputLayout password_wrapper;
    @InjectView(R.id.et_username)
    EditText username;
    @InjectView(R.id.et_password)
    EditText password;

    private ServiceConnection sc;
    private IBinder mBinder;

    private void bind_service() {
        Intent intent = new Intent(this, MusicServer.class);
        startService(intent);
        bindService(intent, sc, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Permission.verifyStoragePermissions(this);

        //初始化DBManager
        mgr = new DBManager(this,admin,true);

        ButterKnife.inject(this);
        // 启动bgm
        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                Log.d("service", "connected");
                mBinder = service;
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mBinder = null;
                sc = null;
            }
        };
        bind_service();
    }

    @OnClick({R.id.bt_go, R.id.fab, R.id.login_layout_anonymous})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    startActivity(new Intent(this, Register.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, Register.class));
                }
                break;
            case R.id.bt_go://判断用户是否存在于数据库
                User usertemp=new User(username.getText().toString(),password.getText().toString());
                int check_res = checkUser(usertemp);
                if (check_res == USER_CORRECT)
                {
                    username_wrapper.setErrorEnabled(false);
                    password_wrapper.setErrorEnabled(false);
                    Toast.makeText(Login.this,"登录成功",Toast.LENGTH_SHORT).show();
                    Explode explode = new Explode();
                    explode.setDuration(500);
                    //getWindow().setExitTransition(explode);
                    getWindow().setEnterTransition(explode);
                    ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
                    Intent i2 = new Intent(this, PeopleList.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user",usertemp);
                    i2.putExtras(bundle);
                    startActivity(i2, oc2.toBundle());
                }
                else if(check_res == USER_EMPTY_NAME_ERROR)
                {
                    username_wrapper.setErrorEnabled(true);
                    username_wrapper.setError("用户名不能为空");
                    password_wrapper.setErrorEnabled(false);

//                    Toast.makeText(Login.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                }
                else if(check_res == USER_EMPTY_PASSWORD_ERROE)
                {
                    password_wrapper.setErrorEnabled(true);
                    password_wrapper.setError("密码不能为空");
                    username_wrapper.setErrorEnabled(false);
//                    Toast.makeText(Login.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }
                else if(check_res == USER_EMPTY_NAME_PASSWORD_ERROR)
                {
                    password_wrapper.setErrorEnabled(true);
                    password_wrapper.setError("密码不能为空");
                    username_wrapper.setErrorEnabled(true);
                    username_wrapper.setError("用户名不能为空");
                }
                else if( check_res == USER_WRONG)
                {
                    username_wrapper.setErrorEnabled(false);
                    password_wrapper.setErrorEnabled(true);
                    password_wrapper.setError("用户名不存在或者密码错误");
                   // Toast.makeText(Login.this,"用户名不存在或者密码错误",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.login_layout_anonymous:
                Intent intent_1 = new Intent(Login.this, PeopleList.class);//跳转至人物列表people_list_layout界面
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",admin);
                intent_1.putExtras(bundle);
                startActivity(intent_1);
                break;
        }
    }
    private int checkUser(User user)
    {
        //查看用户是否输入正确
        if(user.getName().isEmpty()&&!user.getPassword().isEmpty()) return USER_EMPTY_NAME_ERROR;
        else if(!user.getName().isEmpty()&&user.getPassword().isEmpty())return USER_EMPTY_PASSWORD_ERROE;
        else if(user.getName().isEmpty()&&user.getPassword().isEmpty()) return USER_EMPTY_NAME_PASSWORD_ERROR;
        //还需要判断用户和数据库中是否匹配
        else
        {
            User user_query = mgr.query(user);
            if (user_query.getName() == null)return USER_WRONG;
            else if(!user_query.equals(user))return USER_WRONG;
        }
        return USER_CORRECT;
    }
}
