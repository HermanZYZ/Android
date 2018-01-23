package com.example.administrator.threekingdomsdictionary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Register extends AppCompatActivity {

    private DBManager mgr;
    private User user = new User();
    final static private User admin = new User("admin","admin");



    final static private int USER_ADD_SUCCESS = 0;//注册成功
    final static private int USER_NAME_EMPTY_ERROR = 1;//用户名为空
    final static private int USER_PASSWORD_EMPTY_ERROR = 2;//密码为空
    final static private int USER_REPEAT_EMPTY_ERROR = 3;//重复密码为空
    final static private int USER_NAME_EXISTS_ERROR = 4;//用户已存在
    final static private int USER_REPEAT_WRONG_ERROR = 5;//重复密码不正确
    final static private int USER_NAME_PASSWORD_EMPTY_ERROR=6;
    final static private int USER_PASSWORD_REPEATPASSWORD_EMPTY_ERROR=7;
    final static private int USER_NAME_REPEATPASSWORD_EMPTY_ERROR=8;
    final static private int USER_ALL_EMPTY=9;
    final static private int USER_ADD_FAILED = 10;//添加失败

    @InjectView(R.id.fab)
    FloatingActionButton fab;
    @InjectView(R.id.cv_add)
    CardView cvAdd;
    @InjectView(R.id.bt_go)
    Button next;
    @InjectView(R.id.username_wrapper)
    TextInputLayout username_wrapper;
    @InjectView(R.id.password_wrapper)
    TextInputLayout password_wrapper;
    @InjectView(R.id.repeatpassword_wrapper)
    TextInputLayout repeatpassword_wrapper;
    @InjectView(R.id.et_username)
    EditText username;
    @InjectView(R.id.et_password)
    EditText password;
    @InjectView(R.id.et_repeatpassword)
    EditText repeat_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }

        //初始化mgr
        mgr = new DBManager(this,admin,false);

        //浮动按钮点击事件
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check_num=checkInput(username.getText().toString(),password.getText().toString(),repeat_password.getText().toString());
                if(check_num==USER_ADD_SUCCESS)
                {
                    /*还需要将该用户加入数据库*/
                    mgr.closeDB();
                    mgr = new DBManager(Register.this,user,true);
                    Toast.makeText(Register.this,"注册成功",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Register.this, Login.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user",user);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
                else if(check_num==USER_NAME_EMPTY_ERROR)
                {
                    username_wrapper.setErrorEnabled(true);
                    username_wrapper.setError("用户名不能为空");
                    password_wrapper.setErrorEnabled(false);
                    repeatpassword_wrapper.setErrorEnabled(false);
//                    Toast.makeText(Register.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                }
                else if(check_num==USER_PASSWORD_EMPTY_ERROR)
                {
                    password_wrapper.setErrorEnabled(true);
                    password_wrapper.setError("密码不能为空");
                    username_wrapper.setErrorEnabled(false);
                    repeatpassword_wrapper.setErrorEnabled(false);
//                    Toast.makeText(Register.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }
                else if(check_num==USER_REPEAT_EMPTY_ERROR)
                {
                    repeatpassword_wrapper.setErrorEnabled(true);
                    repeatpassword_wrapper.setError("重复密码不能为空");
                    username_wrapper.setErrorEnabled(false);
                    password_wrapper.setErrorEnabled(false);
//                    Toast.makeText(Register.this,"重复密码不能为空",Toast.LENGTH_SHORT).show();
                }
                else if(check_num==USER_NAME_PASSWORD_EMPTY_ERROR)
                {
                    username_wrapper.setErrorEnabled(true);
                    username_wrapper.setError("用户名不能为空");
                    password_wrapper.setErrorEnabled(true);
                    password_wrapper.setError("密码不能为空");
                    repeatpassword_wrapper.setErrorEnabled(false);

                }
                else if(check_num==USER_PASSWORD_REPEATPASSWORD_EMPTY_ERROR)
                {
                    repeatpassword_wrapper.setErrorEnabled(true);
                    repeatpassword_wrapper.setError("重复密码不能为空");
                    password_wrapper.setErrorEnabled(true);
                    password_wrapper.setError("密码不能为空");
                    username_wrapper.setErrorEnabled(false);

                }
                else if(check_num==USER_NAME_REPEATPASSWORD_EMPTY_ERROR)
                {
                    username_wrapper.setErrorEnabled(true);
                    username_wrapper.setError("用户名不能为空");
                    repeatpassword_wrapper.setErrorEnabled(true);
                    repeatpassword_wrapper.setError("重复密码不能为空");
                    password_wrapper.setErrorEnabled(false);

                }
                else if(check_num==USER_ALL_EMPTY)
                {
                    username_wrapper.setErrorEnabled(true);
                    username_wrapper.setError("用户名不能为空");
                    repeatpassword_wrapper.setErrorEnabled(true);
                    repeatpassword_wrapper.setError("重复密码不能为空");
                    password_wrapper.setErrorEnabled(true);
                    password_wrapper.setError("密码不能为空");
                }
                else if(check_num==USER_REPEAT_WRONG_ERROR)
                {
                    repeatpassword_wrapper.setErrorEnabled(true);
                    repeatpassword_wrapper.setError("两次输入的密码不一致");
                    username_wrapper.setErrorEnabled(false);
                    password_wrapper.setErrorEnabled(false);
//                    Toast.makeText(Register.this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                }
//                else if(check_num==USER_ADD_FAILED)
//                {
//
//                }
                else if(check_num==USER_NAME_EXISTS_ERROR)
                {
                    repeatpassword_wrapper.setErrorEnabled(true);
                    repeatpassword_wrapper.setError("数据库已存在该ID");
                    username_wrapper.setErrorEnabled(false);
                    password_wrapper.setErrorEnabled(false);
//                    Toast.makeText(Register.this,"用户名已存在",Toast.LENGTH_SHORT).show();

                }


            }
        });
    }

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth()/2,0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd,cvAdd.getWidth()/2,0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                Register.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }
    @Override
    public void onBackPressed() {
        animateRevealClose();
    }
    private int checkInput(String name,String pw,String repeat_ps)
    {
        if(name.isEmpty()&&!pw.isEmpty()&&!repeat_ps.isEmpty())return USER_NAME_EMPTY_ERROR;
        else if(!name.isEmpty()&&pw.isEmpty()&&!repeat_ps.isEmpty())return USER_PASSWORD_EMPTY_ERROR;
        else if(!name.isEmpty()&&!pw.isEmpty()&&repeat_ps.isEmpty())return USER_REPEAT_EMPTY_ERROR;
        else if(name.isEmpty()&&pw.isEmpty()&&!repeat_ps.isEmpty()) return USER_NAME_PASSWORD_EMPTY_ERROR;
        else if(name.isEmpty()&&!pw.isEmpty()&&repeat_ps.isEmpty()) return USER_NAME_REPEATPASSWORD_EMPTY_ERROR;
        else if(!name.isEmpty()&&pw.isEmpty()&&repeat_ps.isEmpty()) return USER_PASSWORD_REPEATPASSWORD_EMPTY_ERROR;
        else if(name.isEmpty()&&pw.isEmpty()&&repeat_ps.isEmpty()) return USER_ALL_EMPTY;
        else if(!pw.equals(repeat_ps))return USER_REPEAT_WRONG_ERROR;
        //还需要一个查重操作
        else
        {
            user.setName(name);
            user.setPassword(pw);
            if(mgr.query(user).getName()!=null)return USER_NAME_EXISTS_ERROR;
        }
        return USER_ADD_SUCCESS;
    }

}

