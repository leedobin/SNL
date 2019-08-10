package com.four_leader.snl.util;

import com.google.gson.JsonObject;

import org.json.JSONObject;

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

    // 유저 권한 허가 상태 조회
    @GET("AGREEMENT/getAgreement.php")
    Call<String> checkUserPermission(@Query("user_seq") String userSeq);

    // 카테고리 시퀀스에 따라 글 목록 조회(메인)
    @GET("SCRIPT/scriptList.php")
    Call<String> getScriptByCateSeq(@Query("category") String categorySeq);

    // 푸쉬권한 동의/거절
    @GET("USER/push_permission.php")
    Call<String> setPushPermission(@Query("userId") String userId,
                                   @Query("date") String date);

    // 해당 게시글의 댓글 목록 조회
    @GET("SCRIPT/DetailsOfScript.php")
    Call<String> getScriptComment(@Query("scriptSeq") String scriptSeq);

    // 글쓰기
    @GET("SCRIPT/writeScript.php")
    Call<String> writeScript(@Query("user_seq") String userSeq,
                             @Query("script_title") String title,
                             @Query("script_content") String content,
                             @Query("script_category_code") int categorySeq);

    // 글 좋아요
    @GET("SCRIPT/addLike.php")
    Call<String> addLike(@Query("user_seq") String userSeq,
                         @Query("script_seq") String scriptSeq);

    // 글 좋아요 취소
    @GET("SCRIPT/deleteLike.php")
    Call<String> deleteLike(@Query("user_seq") String userSeq,
                            @Query("script_seq") String scriptSeq);

    // 글 신고
    @GET("SCRIPT/reportScript.php")
    Call<String> reportScript(@Query("script_seq") String scriptSeq,
                              @Query("reporting_seq") String reportingSeq,
                              @Query("reported_seq") String reportedSeq,
                              @Query("report_memo") String reportMemo);

    // 댓글 신고
    @GET("COMMENT/reportComment.php")
    Call<String> reportComment(@Query("comment_seq") String commentSeq,
                               @Query("reporting_seq") String reportingSeq,
                               @Query("reported_seq") String reportedSeq,
                               @Query("report_memo") String reportMemo);


}