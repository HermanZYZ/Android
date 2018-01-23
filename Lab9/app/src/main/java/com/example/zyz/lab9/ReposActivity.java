package com.example.zyz.lab9;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ReposActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private ListView listView;
    private TextView title;
    private TextView subtitle;
    private TextView subsubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);

        listView = (ListView) findViewById(R.id.list);
        progressBar = (ProgressBar) findViewById(R.id.process);

        Intent intent = getIntent();
        String userName = intent.getStringExtra("Name");
        getRepos(userName);
    }

    private void getRepos(String name)
    {
        Retrofit retrofit = ServiceFactory.createRetrofit();
        // 实例化接口
        ServiceFactory.GithubService github = retrofit.create(ServiceFactory.GithubService.class);
        Observable<List<Repos>> observable = github.getUserRepos(name);
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Observer<List<Repos>>() {
                      @Override
                      public void onSubscribe(@NonNull Disposable d) {
                          Log.i("Github", "Subscribe");
                          progressBar.setVisibility(View.VISIBLE);
                          listView.setVisibility(View.INVISIBLE);
                      }

                      @Override
                      public void onNext(@NonNull List<Repos> reposes) {
                          List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
                          HashMap<String, String> hashMap;
                          for(Repos i : reposes)
                          {
                              hashMap = new HashMap<String, String>();
                              hashMap.put("title", i.getTitle());
                              hashMap.put("subtitle", i.getSub1());
                              hashMap.put("subsubtitle", i.getSub2());
                              data.add(hashMap);
                          }
                          SimpleAdapter simpleAdapter = new SimpleAdapter(ReposActivity.this, data, R.layout.card_view,
                                  new String[] {"title", "subtitle", "subsubtitle"}, new int[] {R.id.title, R.id.subtitle, R.id.subsubtitle});
                          listView.setAdapter(simpleAdapter);
                      }

                      @Override
                      public void onError(@NonNull Throwable e) {
                          Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                          Log.e("Github", e.getMessage());
                          progressBar.setVisibility(View.INVISIBLE);
                          finish();
                      }

                      @Override
                      public void onComplete() {
                          System.out.println("完成传输");
                          progressBar.setVisibility(View.INVISIBLE);
                          listView.setVisibility(View.VISIBLE);
                      }
                  });
    }
}
