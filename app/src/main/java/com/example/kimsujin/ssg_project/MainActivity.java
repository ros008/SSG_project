package com.example.kimsujin.ssg_project;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final String BASE_URL = "https://2b026a0d.ngrok.io/";
    private final String SSG_URL = "http://m.emart.ssg.com/search.ssg?query=";

    private Retrofit retrofit;

    private TextView textView;
    private EditText searchText;
    private Button pum1;
    private Button pum2;
    private Button pum3;
    private Button rec_pum1;
    private Button rec_pum2;
    private Button rec_pum3;


    private Toolbar toolbar;

    private LineChart lineChart;

    List<String> preferPumNameList;
    List<String> preferPumIDList;

    // private BackPressCloseHandler backPressCloseHandler;

    String TAG = "PRICEINDEX";

    ActivityState myActivityState = (ActivityState) getApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        textView = (TextView) findViewById(R.id.tv1);
        searchText = (EditText) findViewById(R.id.searchtext);
        /*
        pum1 = (Button) findViewById(R.id.pum1);
        pum2 = (Button) findViewById(R.id.pum2);
        pum3 = (Button) findViewById(R.id.pum3);
        */
        rec_pum1 = (Button) findViewById(R.id.recommend_pum1);
        rec_pum2 = (Button) findViewById(R.id.recommend_pum2);
        rec_pum3 = (Button) findViewById(R.id.recommend_pum3);

        lineChart = (LineChart) findViewById(R.id.chart);

        // 툴바 설정
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //backPressCloseHandler = new BackPressCloseHandler(this);

        /*
        toolbar.setTitle("쓱바구니");
        toolbar.setTitleTextColor(Color.rgb(105,105,105));
        toolbar.setSubtitle("SSG");
        toolbar.setSubtitleTextColor(Color.rgb(205,205,205));
        */

        setSupportActionBar(toolbar);
        // 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        // UserInputActivity로 부터 받은 데이터 처리
        Intent intent = getIntent();
        JSONObject jsonObject = new JSONObject();

        final String pum_id0 = intent.getExtras().getString("pum_id0");
        final String pum_id1 = intent.getExtras().getString("pum_id1");
        final String pum_id2 = intent.getExtras().getString("pum_id2");

        final String pum_name0 = intent.getExtras().getString("pum_name0");
        final String pum_name1 = intent.getExtras().getString("pum_name1");
        final String pum_name2 = intent.getExtras().getString("pum_name2");

        preferPumNameList = new ArrayList<String>();
        preferPumIDList = new ArrayList<String>();

        preferPumNameList.add(pum_name0);
        preferPumNameList.add(pum_name1);
        preferPumNameList.add(pum_name2);

        preferPumIDList.add(pum_id0);
        preferPumIDList.add(pum_id1);
        preferPumIDList.add(pum_id2);

        ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_preferpum, R.id.lable, preferPumNameList);

        GridView gridView = (GridView) findViewById(R.id.gridview1);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView) view.findViewById(R.id.lable);
                Toast.makeText(MainActivity.this, i+", "+tv.getText(),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), PumDetailActivity.class);
                intent.putExtra("pum_id",preferPumIDList.get(i));
                intent.putExtra("pum_name",preferPumNameList.get(i));
                startActivity(intent);
            }
        });

        /*
        pum1.setText(pum_name0);
        pum2.setText(pum_name1);
        pum3.setText(pum_name2);
        */

        Log.i("getintent",pum_id0+pum_id1+pum_id2);



        // 사용자가 선택한 선호 품목으로 객체 만들어서 서버에 보냄
        PostPreferPum postPreferPum = new PostPreferPum(pum_id0, pum_id1, pum_id2);

        // GSON 컨버터를 사용하는 REST 어댑터 생성
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // NetworkService API 인터페이스 생성
        final NetworkService service = retrofit.create(NetworkService.class);

        // 선호 품목을 보내면, 등락률 크고 / 등락률 작고 / 할인률 큰 상품들을 받아서 보여주는 부분
        Call<ResponseBody> callPum = service.postPumList(postPreferPum);
        Log.i(TAG, jsonObject.toString());
        // 앞서만든 요청을 수행
        callPum.enqueue(new Callback<ResponseBody>() {
            @Override
            // 성공시
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {  // 여기부터 수정 필요

                if(response.body() != null){
                    Log.i("TEST", response.body().toString());
                    textView.append(response.body().toString());
                }
                else{
                    Toast.makeText(MainActivity.this, "정보 받아오기 실패2", Toast.LENGTH_LONG)
                            .show();
                    int StatusCode = response.code();
                    Log.i(TAG, "Status Code : " + StatusCode + " Error Message : " + response.errorBody());

                }
            }

            @Override
            // 실패시
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "정보 받아오기 완전 실패2", Toast.LENGTH_LONG)
                        .show();
                Log.i(TAG, "Fail Message : " + t.getMessage());
            }
        });


        // 물가 지수 그래프 정보 받는 부분 GET
        // 인터페이스에 구현한 메소드인 priceindex param 값을 넘기는 요청 만듬
        Call<List<PriceIndex>> callGraph = service.priceindice("json");
        // 앞서만든 요청을 수행
        callGraph.enqueue(new Callback<List<PriceIndex>>() {
            @Override
            // 성공시
            public void onResponse(Call<List<PriceIndex>> call, Response<List<PriceIndex>> response) {
                List<PriceIndex> priceindice = response.body();
                ArrayList<Entry> entries = new ArrayList<>();
                ArrayList<String> labels = new ArrayList<String>();

                if(priceindice != null){
                    // 받아온 리스트를 순회하면서
                    int i=0;
                    for (PriceIndex priceindex : priceindice) {
                        // 텍스트 뷰에 login 정보를 붙임
                        Log.i(TAG, "priceindex list : " + priceindex.getX());
                        Log.i(TAG, "priceindex list : " + priceindex.getY());

                        labels.add(priceindex.getX());
                        // 물가 지수 차트에 entry를 추가한다.
                        entries.add(new Entry(Float.parseFloat(priceindex.getY())-100, i));
                        i++;
                    }

                    LineDataSet dataset = new LineDataSet(entries, "소비자 물가 지수(2015기준)");

                    LineData data = new LineData(labels, dataset);
                    // dataset.setColors(ColorTemplate.LIBERTY_COLORS);
                    // dataset.setDrawCubic(true); //선 둥글게 만들기

                    // dataset.setDrawFilled(true); // 그래프 밑에 색깔 채우기

                    lineChart.getAxisRight().setDrawGridLines(false); // delete background grid
                    lineChart.getAxisLeft().setDrawGridLines(false); // delete background grid
                    lineChart.getXAxis().setDrawGridLines(false); // delete background grid

                    lineChart.setData(data);
                    lineChart.animateY(5000);
                }
                else{
                    Toast.makeText(MainActivity.this, "정보 받아오기 실패1", Toast.LENGTH_LONG)
                            .show();
                    textView.append(("1실패"+"\n"));
                }
            }

            @Override
            // 실패시
            public void onFailure(Call<List<PriceIndex>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "정보 받아오기 실패1", Toast.LENGTH_LONG)
                        .show();
            }
        });

        /*
        // 버튼 리스너 등록
        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PumDetailActivity.class);
                switch (view.getId()) {
                    case R.id.pum1 :
                        Toast.makeText(MainActivity.this, pum_id0+"  "+pum_name0, Toast.LENGTH_LONG)
                                .show();
                        intent.putExtra("pum_id",pum_id0);
                        intent.putExtra("pum_name",pum_name0);
                        startActivity(intent);

                        break ;
                    case R.id.pum2 :
                        Toast.makeText(MainActivity.this, pum_id1+"  "+pum_name1, Toast.LENGTH_LONG)
                                .show();
                        intent.putExtra("pum_id",pum_id1);
                        intent.putExtra("pum_name",pum_name1);
                        startActivity(intent);

                        break ;
                    case R.id.pum3 :
                        Toast.makeText(MainActivity.this, pum_id2+"  "+pum_name2, Toast.LENGTH_LONG)
                                .show();
                        intent.putExtra("pum_id",pum_id2);
                        intent.putExtra("pum_name",pum_name2);
                        startActivity(intent);

                        break ;
                    case R.id.recommend_pum1 :


                        break;
                    case R.id.recommend_pum2 :


                        break;
                    case R.id.recommend_pum3 :


                        break;
                }
            }
        } ;


        pum1.setOnClickListener(onClickListener);
        pum2.setOnClickListener(onClickListener);
        pum3.setOnClickListener(onClickListener);
        rec_pum1.setOnClickListener(onClickListener);
        rec_pum2.setOnClickListener(onClickListener);
        rec_pum3.setOnClickListener(onClickListener);
        */

        // 검색기능 구현
        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String s;
                    s = searchText.getText().toString();

                    String url = SSG_URL+s;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);

                    return true;
                }
                return false;
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

    /*
    // 뒤로 가기 버튼 두번 눌렀을 때
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
    */
}
