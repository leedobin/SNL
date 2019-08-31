package com.four_leader.snl.util;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {

//    @POST()
//    @Field()
//    @FormUrlEncoded


    @GET("USER/login.php")
    Call<String> doLogin(@Query("user_id") String email,
                         @Query("user_pw") String pwd
    );

    // 전체 카테고리 목록 조회
    @GET("CATEGORY/get_category.php")
    Call<String> getCategory();

    // 유저 보유 카테고리 조회
    @GET("SCRIPT/categoryList.php")
    Call<String> getUserCategory(@Query("userSeq") String userSeq);

    // 유저 카테고리 설정
    @GET("CATEGORY/set_user_category.php")
    Call<String> setUserCategory(@Query("user_seq") String userSeq,
                                 @Query("category_seq") String categorySeq);

    //댓글 음성 파일 정보 전송
    @GET("COMMENT/writeComment.php")
    Call<String> recordInfo(@Query("comment_seq") String commentSeq,
                            @Query("script_seq") String scriptSeq,
                            @Query("user_seq") String userSeq,
                            @Query("comment_file_name") String commentFilename);
}