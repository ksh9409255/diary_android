package com.foo.diary_android.service;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface Service {
    //final String BASE_RUL = "http://10.0.2.2:8080"; // 안드로이드의 로컬호스트로 연결
    final String BASE_RUL = "비공개";

    @Headers("Content-Type: application/json")
    @GET("/member/calendar")
    Call<List<UserCalendarData>> getUserCalendar(@Query("id") Long id);

    @Headers("Content-Type: application/json")
    @GET("/member/calendar/diary")
    Call<DiaryContentData> getDiaryContent(@Query("diaryid") Long id);

    /**
     * 회원가입 여부 확인
     * @param id : 확인할 카카오 id
     * @return : true/false
     */
    @Headers("Content-Type: application/json")
    @GET("/member/check")
    Call<Boolean> checkId(@Query("id") Long id,@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @GET("/member")
    Call<MemberDto> getMember(@Query("id") Long id,@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @POST("/member")
    Call<ResponseBody> registerMember(@Body MemberDto user,@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @DELETE("/member")
    Call<ResponseBody> deleteMember(@Query("id") Long id,@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @PUT("/member")
    Call<ResponseBody> updateMember(@Body MemberDto user,@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @GET("/member/nickname")
    Call<Boolean> validMemberNickName(@Query("nickName") String name);

    @Headers("Content-Type: application/json")
    @GET("/category/list")
    Call<List<CategoryDto>> getCategoryList();

    @Headers("Content-Type: application/json")
    @GET("/category/member/list")
    Call<List<CategoryDto>> getCategoryByMemberId(@Query("id") Long id,@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @GET("/emoticon/list")
    Call<List<EmoticonDto>> getEmoticonByCategoryId(@Query("categoryId") int categoryId);

    @Headers("Content-Type: application/json")
    @GET("/emoticon")
    Call<List<EmoticonDto>> getEmoticonAllList(@Query("id") Long id,@Header("Authorization") String token);

    /**
     * ======================================================================================
     * FRIEND CATEGORY까지 완료
     */
    @Headers("Content-Type: application/json")
    @GET("/friend/find")
    Call<List<MemberDto>> getFriendList(@Query("id") Long id,@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @POST("/friend/add")
    Call<Boolean> addFriend(@Body FriendAndroidDto friendAndroidDto,@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @GET("/friend/request")
    Call<List<MemberDto>> getNewFriend(@Query("id") Long id,@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @POST("/friend/save")
    Call<ResponseBody> acceptNewFriend(@Body FriendDto friendDto,@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @POST("/friend/remove")
    Call<Boolean> removeFriend(@Body FriendAndroidDto friendAndroidDto,@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @GET("/friend")
    Call<MemberDto> getFriend(@Query("id") Long id,@Header("Authorization") String token,@Query("userId") Long userId);

    /**
     ==================================================================
     */
    @Headers("Content-Type: application/json")
    @POST("/diary/save")
    Call<Long> addDiary(@Body DiaryDto diaryDto,@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @GET("/diary/findOne")
    Call<DiaryDto> findDiary(@Query("id") Long id);

    @Headers("Content-Type: application/json")
    @POST("/diary/modify")
    Call<ResponseBody> updateDiary(@Body DiaryModifyDto diaryModifyDto,@Header("Authorization") String token,@Query("userId") Long userId);

    @Headers("Content-Type: application/json")
    @GET("/diary/remove")
    Call<ResponseBody> removeDiary(@Query("id") Long id,@Header("Authorization") String token,@Query("userId") Long userId);

    @Headers("Content-Type: application/json")
    @GET("/diary/findAll")
    Call<List<DiaryFindDto>> findAllDiary(@Query("id") Long id,@Header("Authorization") String token);

}
