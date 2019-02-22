package com.example.kimsujin.ssg_project;

import android.content.Intent;
import android.graphics.Color;
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
import com.github.mikephil.charting.utils.ColorTemplate;

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

    private final String BASE_URL = "https://2366ac34.ngrok.io/";
    private final String SSG_URL = "http://m.emart.ssg.com/search.ssg?query=";

    private Retrofit retrofit;

    private EditText searchText;

    private Button pum1;
    private Button pum2;
    private Button pum3;

    private TextView pumtv1;
    private TextView pumtv2;
    private TextView pumtv3;


    private Button rec_pum1;
    private Button rec_pum2;

    private TextView rec_pumtv1;
    private TextView rec_pumtv2;

    private Toolbar toolbar;

    private LineChart lineChart;

    List<String> preferPumNameList;
    List<String> preferPumIDList;
    List<Float> preferPumRateList;

    // private BackPressCloseHandler backPressCloseHandler;

    String TAG = "PRICEINDEX";

    ActivityState myActivityState = (ActivityState) getApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        searchText = (EditText) findViewById(R.id.searchtext);

        pum1 = (Button) findViewById(R.id.pum1);
        pum2 = (Button) findViewById(R.id.pum2);
        pum3 = (Button) findViewById(R.id.pum3);

        pumtv1 = (TextView) findViewById(R.id.pumtv1);
        pumtv2 = (TextView) findViewById(R.id.pumtv2);
        pumtv3 = (TextView) findViewById(R.id.pumtv3);

        rec_pum1 = (Button) findViewById(R.id.pum_rise);
        rec_pum2 = (Button) findViewById(R.id.pum_decline);

        rec_pumtv1 = (TextView) findViewById(R.id.pumtv_rise);
        rec_pumtv2 = (TextView) findViewById(R.id.pumtv_decline);


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

        final float pum_rate0 = intent.getExtras().getFloat("rate0");
        final float pum_rate1 = intent.getExtras().getFloat("rate1");
        final float pum_rate2 = intent.getExtras().getFloat("rate2");

        preferPumNameList = new ArrayList<String>();
        preferPumIDList = new ArrayList<String>();
        preferPumRateList = new ArrayList<Float>();

        preferPumNameList.add(pum_name0);
        preferPumNameList.add(pum_name1);
        preferPumNameList.add(pum_name2);

        preferPumIDList.add(pum_id0);
        preferPumIDList.add(pum_id1);
        preferPumIDList.add(pum_id2);

        preferPumRateList.add(pum_rate0);
        preferPumRateList.add(pum_rate1);
        preferPumRateList.add(pum_rate2);

        pum1.setText(pum_name0);
        pum2.setText(pum_name1);
        pum3.setText(pum_name2);

        pumtv1.setText(String.valueOf(pum_rate0));
        pumtv2.setText(String.valueOf(pum_rate1));
        pumtv3.setText(String.valueOf(pum_rate2));

        if(pum_rate0>=0){
            pumtv1.setTextColor(Color.BLUE);
            pumtv1.append("%\u25B2");
        }
        else{
            pumtv1.setTextColor(Color.RED);
            pumtv1.append("%\u25BC");
        }
        if(pum_rate1>=0){
            pumtv2.setTextColor(Color.BLUE);
            pumtv2.append("%\u25B2");
        }
        else{
            pumtv2.setTextColor(Color.RED);
            pumtv2.append("%\u25BC");
        }
        if(pum_rate2>=0){
            pumtv3.setTextColor(Color.BLUE);
            pumtv3.append("%\u25B2");
        }
        else{
            pumtv3.setTextColor(Color.RED);
            pumtv3.append("%\u25BC");
        }

        final String rec_pum_rise_id = intent.getExtras().getString("max_rate_id");
        final String rec_pum_rise_name = intent.getExtras().getString("max_rate_name");
        final float rec_pum_rise_rate = intent.getExtras().getFloat("max_rate_rate");

        final String rec_pum_dec_id = intent.getExtras().getString("min_rate_id");
        final String rec_pum_dec_name = intent.getExtras().getString("min_rate_name");
        final float rec_pum_dec_rate = intent.getExtras().getFloat("min_rate_rate");


        rec_pum1.setText(rec_pum_rise_name);
        rec_pum2.setText(rec_pum_dec_name);

        rec_pumtv1.setText(String.valueOf(rec_pum_rise_rate));
        rec_pumtv2.setText(String.valueOf(rec_pum_dec_rate));

        if(rec_pum_rise_rate>=0){
            rec_pumtv1.setTextColor(Color.BLUE);
            rec_pumtv1.append("%\u25B2");
        }
        else{
            rec_pumtv1.setTextColor(Color.RED);
            rec_pumtv1.append("%\u25BC");
        }
        if(rec_pum_dec_rate>=0){
            rec_pumtv2.setTextColor(Color.BLUE);
            rec_pumtv2.append("%\u25B2");
        }
        else{
            rec_pumtv2.setTextColor(Color.RED);
            rec_pumtv2.append("%\u25BC");
        }

        Log.i("getintent",pum_id0+pum_id1+pum_id2);


        // GSON 컨버터를 사용하는 REST 어댑터 생성
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // NetworkService API 인터페이스 생성
        final NetworkService service = retrofit.create(NetworkService.class);

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
                    // dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                    dataset.setColor(R.color.color_primary_orange);

                    // dataset.setDrawCubic(true); //선 둥글게 만들기

                    // dataset.setDrawFilled(true); // 그래프 밑에 색깔 채우기

                    lineChart.getAxisRight().setDrawGridLines(false); // delete background grid
                    lineChart.getAxisLeft().setDrawGridLines(false); // delete background grid
                    lineChart.getXAxis().setDrawGridLines(false); // delete background grid

                    lineChart.setData(data);
                    lineChart.animateY(5000);
                }
                else{
                    Toast.makeText(MainActivity.this, "물가지수 정보 받아오기 실패\n네트워크 연결을 확인하세요", Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            // 실패시
            public void onFailure(Call<List<PriceIndex>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "물가지수 정보 받아오기 실패\n네트워크 연결을 확인하세요", Toast.LENGTH_LONG)
                        .show();
            }
        });


        // 버튼 리스너 등록
        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PumDetailActivity.class);
                switch (view.getId()) {
                    case R.id.pum1 :
                        intent.putExtra("pum_id",pum_id0);
                        intent.putExtra("pum_name",pum_name0);
                        startActivity(intent);

                        break ;
                    case R.id.pum2 :
                        intent.putExtra("pum_id",pum_id1);
                        intent.putExtra("pum_name",pum_name1);
                        startActivity(intent);

                        break ;
                    case R.id.pum3 :
                        intent.putExtra("pum_id",pum_id2);
                        intent.putExtra("pum_name",pum_name2);
                        startActivity(intent);

                        break ;
                    case R.id.pum_rise :
                        intent.putExtra("pum_id",rec_pum_rise_id);
                        intent.putExtra("pum_name",rec_pum_rise_name);
                        startActivity(intent);
                        break;
                    case R.id.pum_decline :
                        intent.putExtra("pum_id",rec_pum_dec_id);
                        intent.putExtra("pum_name",rec_pum_dec_name);
                        startActivity(intent);
                        break;
                }
            }
        } ;


        pum1.setOnClickListener(onClickListener);
        pum2.setOnClickListener(onClickListener);
        pum3.setOnClickListener(onClickListener);

        rec_pum1.setOnClickListener(onClickListener);
        rec_pum2.setOnClickListener(onClickListener);


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
