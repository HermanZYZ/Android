package com.example.zyz.lab9;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private EditText searchView;
    private Button clear;
    private Button fetch;
    private RecyclerView recyclerView;
    private CommonAdapter commonAdapter;
    private ProgressBar progressBar;

    List<Github> users = new ArrayList<>();
    Observer<Github> observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Permission.verifyInternetPermissions(this);

        ElemBinding();
        EvenBinding();

    }

    private void getInformation(String name)
    {
        Retrofit retrofit = ServiceFactory.createRetrofit();
        // 实例化接口
        ServiceFactory.GithubService github = retrofit.create(ServiceFactory.GithubService.class);
        Observable<Github> observable = github.getUser(name);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<Github>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {// 提交请求
                        Log.i("Github","Subscribe");
                        progressBar.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onNext(@NonNull Github github) {// 收到数据
                        users.add(github);
                        commonAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {// 出现错误
                        Toast.makeText(getApplicationContext(), "搜索的用户不存在", Toast.LENGTH_SHORT).show();
                        Log.e("Github",e.getMessage());
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onComplete() {// 请求结束
                        System.out.println("完成传输");
                        progressBar.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void ElemBinding()
    {
        searchView = (EditText) findViewById(R.id.search);
        clear = (Button) findViewById(R.id.clear);
        fetch = (Button) findViewById(R.id.fetch);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        progressBar = (ProgressBar) findViewById(R.id.process);

        commonAdapter = new CommonAdapter(users, this);

        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(commonAdapter);
    }

    private void EvenBinding()
    {
        commonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, ReposActivity.class);
                intent.putExtra("Name", users.get(position).getLogin());
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {
                users.remove(position);
                commonAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "成功删除!", Toast.LENGTH_SHORT).show();
            }
        });

        clear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                searchView.setText("");
                users.clear();
                commonAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        fetch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                String name = searchView.getText().toString();
                if(name.isEmpty())
                    Toast.makeText(getApplicationContext(), "名字不能为空", Toast.LENGTH_SHORT).show();
                else {
                    getInformation(name);
                    commonAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
