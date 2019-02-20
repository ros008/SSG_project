package com.example.kimsujin.ssg_project;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkService {

    // GET/POST/DELETE/PUT 메소드들을 인터페이스에 구현하여 사용할 수 있다.
    @GET("/api/consumer/")
    // JSON Array를 리턴하므로 List<>가 되었다
    Call<List<PriceIndex>> priceindice(@Query("format") String format);

    @GET("/api/pum-list/")
    Call<List<Item>> getPumList(@Query("format") String format);

    @POST("/api/UserInputActivity/")
    Call<ResponseBody> postPumList(@Body PostPreferPum body);

    @GET("/api/detailActivity/{itemid}")
    Call<List<RecommendItem>> getRecItemList(@Path("itemid") String path);

/*    @POST("/api/versions/")
    Call<Version> post_version(@Body Version version);

    @GET("/api/versions/")
    Call<List<Version>> get_version();

    @GET("/api/versions/{pk}/")
    Call<Version> get_pk_version(@Path("pk") int pk);*/

}
