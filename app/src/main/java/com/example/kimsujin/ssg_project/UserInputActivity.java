package com.example.kimsujin.ssg_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserInputActivity extends AppCompatActivity {

    private final String BASE_URL = "https://2b026a0d.ngrok.io/";

    private Retrofit retrofit;
    private Toolbar toolbar;

    private ListView listview;

    private Button select_btn;

    private BackPressCloseHandler backPressCloseHandler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        backPressCloseHandler = new BackPressCloseHandler(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        listview = (ListView) findViewById(R.id.ListView);
        select_btn = (Button) findViewById(R.id.select_btn);

        /*
        toolbar.setTitle("선호 물품을 골라주세요.");
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setSubtitle("총3개");
        toolbar.setSubtitleTextColor(Color.GRAY);
        */

        setSupportActionBar(toolbar);
        // 뒤로가기 버튼
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final ArrayAdapter<String> adapter;

        // 리스트뷰에 담을 ID, NAME
        final List<String> list_id = new ArrayList<String>();
        final List<String> list_name = new ArrayList<String>();

        list_id.add("id1");
        list_id.add("id2");
        list_id.add("id3");

        list_name.add("name1");
        list_name.add("name2");
        list_name.add("name3");

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, list_name);

        // GSON 컨버터를 사용하는 REST 어댑터 생성
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // NetworkService API 인터페이스 생성
        NetworkService service = retrofit.create(NetworkService.class);

        Call<List<Item>> call = service.getPumList("json");
        // 앞서만든 요청을 수행
        call.enqueue(new Callback<List<Item>>() {
            @Override
            // 성공시
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                List<Item> pumlist = response.body();

                if(pumlist != null){
                    // 받아온 리스트를 순회하면서
                    for (Item item : pumlist) {
                        list_id.add(item.getPum_id());
                        list_name.add(item.getPum_name());
                    }
                }
                else{
                    Toast.makeText(UserInputActivity.this, "정보 받아오기 실패", Toast.LENGTH_LONG)
                            .show();
                }


                listview.setAdapter(adapter);
                listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            }

            @Override
            // 실패시
            public void onFailure(Call<List<Item>> call, Throwable t) {
                Toast.makeText(UserInputActivity.this, "정보 받아오기 실패", Toast.LENGTH_LONG)
                        .show();
            }
        });





        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getApplicationContext(), ((TextView) view).getText() + list_id[i], Toast.LENGTH_LONG).show();
            }
        });

        select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getApplicationContext(), "선택완료", Toast.LENGTH_LONG).show();
                SparseBooleanArray checkedPums = listview.getCheckedItemPositions();

                int checkedPumsCnt = 0;

                int count;
                count = adapter.getCount();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                StringBuilder sb = new StringBuilder();

                for(int i = 0; i < count; i++){
                    if(checkedPums.get(i)){
                        sb. append(list_id.get(i)+list_name.get(i));

                        Log.i("getintent"+checkedPumsCnt,list_id.get(i));
                        intent.putExtra("pum_id"+checkedPumsCnt,list_id.get(i));
                        intent.putExtra("pum_name"+checkedPumsCnt,list_name.get(i));

                        checkedPumsCnt++;
                    }
                }

                if(checkedPumsCnt == 3){
                    Toast.makeText(getApplicationContext(), "3개 항목 선택완료"+sb, Toast.LENGTH_LONG).show();

                    startActivity(intent);
                }
                else{
                     Toast.makeText(getApplicationContext(), "3개의 항목을 선택해주십시오.", Toast.LENGTH_LONG).show();

                }

                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                // intent.putExtra("state","launch");
                //startActivity(intent);
                // finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
}