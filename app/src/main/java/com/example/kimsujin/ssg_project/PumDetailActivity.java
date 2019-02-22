package com.example.kimsujin.ssg_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PumDetailActivity extends AppCompatActivity {

    private final String BASE_URL = "https://2366ac34.ngrok.io/";

    private Retrofit retrofit;

    private Toolbar toolbar;

    private TextView detail_name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        // 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        detail_name = (TextView) findViewById(R.id.detail_name);

        Intent intent = getIntent();

        String pum_id = intent.getExtras().getString("pum_id");
        String pum_name = intent.getExtras().getString("pum_name");

        detail_name.setText(pum_name);


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // NetworkService API 인터페이스 생성
        NetworkService service = retrofit.create(NetworkService.class);

        Call<List<RecommendItem>> call = service.getRecItemList(pum_id);
        // 앞서만든 요청을 수행
        call.enqueue(new Callback<List<RecommendItem>>() {
            @Override
            // 성공시
            public void onResponse(Call<List<RecommendItem>> call, Response<List<RecommendItem>> response) {
                List<RecommendItem> recommendItems = response.body();

                if(recommendItems != null){
                    // 받아온 리스트를 순회하면서
                    for (RecommendItem recommendItem : recommendItems) {

                    }
                }
                else{
                    Toast.makeText(PumDetailActivity.this, "품목 상세 정보 받아오기 실패\n네트워크 연결을 확인하세요.", Toast.LENGTH_LONG)
                            .show();
                }

            }

            @Override
            // 실패시
            public void onFailure(Call<List<RecommendItem>> call, Throwable t) {
                Toast.makeText(PumDetailActivity.this, "품목 상세 정보 받아오기 실패\n네트워크 연결을 확인하세요.", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
