package com.foo.diary_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.foo.diary_android.login.LoginActivity;
import com.foo.diary_android.mode.ModeActivity;
import com.foo.diary_android.register.RegisterActivity;
import com.foo.diary_android.service.MemberCheckResponse;
import com.foo.diary_android.service.MemberDto;
import com.foo.diary_android.service.Service;
import com.kakao.sdk.auth.TokenManagerProvider;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    public MemberDto memberUser = new MemberDto();

    public static Map<Integer,Integer> characterStore = new HashMap<>();
    public static Map<Integer,Integer> emoticonStore = new HashMap<>();

    public static String accessToken;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Service.BASE_RUL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Service service = retrofit.create(Service.class);

    public static String getCharacterName(int categoryId) {
        int RcategoryId = 0;
        if(categoryId%2==0){
            RcategoryId=categoryId-1;
        }
        else{
            RcategoryId=categoryId;
        }
        switch (RcategoryId){
            case 1: return "감자";
            case 3: return "고구마";
            case 5: return "아보카도";
            case 7: return "사과";
            case 9: return "귤";
            default : return "알수없음";
        }
    }

    public void getMember(Long id){
        service.getMember(id,accessToken).enqueue(new Callback<MemberDto>() {
            @Override
            public void onResponse(Call<MemberDto> call, Response<MemberDto> response) {
                MemberDto member = response.body();
                if(member==null){ // 카카오 회원 탈퇴 필요
                    UserApiClient.getInstance().unlink(new Function1<Throwable, Unit>() {
                        @Override
                        public Unit invoke(Throwable throwable) {
                            if(throwable!=null){
                                Log.e("Debug", "탈퇴 실패!");
                            }
                            else{
                                Log.e("Debug", "탈퇴 성공!");
                            }
                            return null;
                        }
                    });
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    //memberUser.setMember(1L,"KIM",1);
                    memberUser.setMember(member.getId(),member.getNickName(),member.getCategoryId());
                    //Log.e("성공!!!",String.valueOf(memberUser.getNickName()));
                    Intent intent = new Intent(getApplicationContext(), ModeActivity.class);
                    intent.putExtra("user",memberUser);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<MemberDto> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KakaoSdk.init(this, "비공개"); // sdk 초기화

        initCharacter();
        initEmoticon();

        UserApiClient.getInstance().accessTokenInfo((accessTokenInfo, error) -> {
            if (error != null) {
                Log.e("Debug", "토큰 정보 보기 실패", error); // 탈퇴 혹은 회원가입 안함
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
            else if (accessTokenInfo != null) {
                Log.e("엑세스토큰!!",String.valueOf(TokenManagerProvider.getInstance().getManager().getToken().getAccessToken()));
                accessToken=TokenManagerProvider.getInstance().getManager().getToken().getAccessToken().toString();
                Log.i("Debug", accessToken);
                UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>(){
                    @Override
                    public Unit invoke(User user, Throwable throwable) {
                        if (throwable != null) {
                            Log.e("Deubg", "사용자 정보 받기 실패!");
                        } else if (user != null) {
                            Log.e("Debug", "사용자 정보 받기 성공!");
                            getMember(user.getId());
                        }
                        return null;
                    }
                });
            }
            return null;
        });
    }

    private void initEmoticon() {
        emoticonStore.put(1,R.drawable.em_potato_angry); // height 크기 110
        emoticonStore.put(2,R.drawable.em_potato_cry);
        emoticonStore.put(3,R.drawable.em_potato_fun);
        emoticonStore.put(4,R.drawable.em_potato_ignore);
        emoticonStore.put(5,R.drawable.em_potato_lol);
        emoticonStore.put(6,R.drawable.em_potato_love);
        emoticonStore.put(7,R.drawable.em_potato_sick);
        emoticonStore.put(8,R.drawable.em_potato_tire);
        emoticonStore.put(9,R.drawable.em_potato_yammy);
        emoticonStore.put(10,R.drawable.em_swpotato_angry);
        emoticonStore.put(11,R.drawable.em_swpotato_cry);
        emoticonStore.put(12,R.drawable.em_swpotato_fun);
        emoticonStore.put(13,R.drawable.em_swpotato_ignore);
        emoticonStore.put(14,R.drawable.em_swpotato_lol);
        emoticonStore.put(15,R.drawable.em_swpotato_love);
        emoticonStore.put(16,R.drawable.em_swpotato_sick);
        emoticonStore.put(17,R.drawable.em_swpotato_surp);
        emoticonStore.put(18,R.drawable.em_swpotato_tire);
        emoticonStore.put(19,R.drawable.em_abocado_angry);
        emoticonStore.put(20,R.drawable.em_abocado_cry);
        emoticonStore.put(21,R.drawable.em_abocado_fun);
        emoticonStore.put(22,R.drawable.em_abocado_ignore);
        emoticonStore.put(23,R.drawable.em_abocado_lol);
        emoticonStore.put(24,R.drawable.em_abocado_love);
        emoticonStore.put(25,R.drawable.em_abocado_plea);
        emoticonStore.put(26,R.drawable.em_abocado_sick);
        emoticonStore.put(27,R.drawable.em_abocado_tire);
        emoticonStore.put(28,R.drawable.em_apple_angry);
        emoticonStore.put(29,R.drawable.em_apple_cry);
        emoticonStore.put(30,R.drawable.em_apple_fun);
        emoticonStore.put(31,R.drawable.em_apple_ignore);
        emoticonStore.put(32,R.drawable.em_apple_lol);
        emoticonStore.put(33,R.drawable.em_apple_love);
        emoticonStore.put(34,R.drawable.em_apple_plea);
        emoticonStore.put(35,R.drawable.em_apple_sick);
        emoticonStore.put(36,R.drawable.em_apple_tire);
        emoticonStore.put(37,R.drawable.em_mandarin_angry);
        emoticonStore.put(38,R.drawable.em_mandarin_cry);
        emoticonStore.put(39,R.drawable.em_mandarin_fun);
        emoticonStore.put(40,R.drawable.em_mandarin_ignore);
        emoticonStore.put(41,R.drawable.em_mandarin_lol);
        emoticonStore.put(42,R.drawable.em_mandarin_love);
        emoticonStore.put(43,R.drawable.em_mandarin_sick);
        emoticonStore.put(44,R.drawable.em_mandarin_sleep);
        emoticonStore.put(45,R.drawable.em_mandarin_tire);
    }

    public void initCharacter(){
        characterStore.put(1,R.drawable.ch_charactor_potato_m);
        characterStore.put(2,R.drawable.ch_charactor_potato_f);

        characterStore.put(3,R.drawable.ch_charactor_swpotato_m);
        characterStore.put(4,R.drawable.ch_charactor_swpotato_f);

        characterStore.put(5,R.drawable.ch_charactor_abocado_m);
        characterStore.put(6,R.drawable.ch_charactor_abocado_f);

        characterStore.put(7,R.drawable.ch_charactor_apple_m);
        characterStore.put(8,R.drawable.ch_charactor_apple_f);

        characterStore.put(9,R.drawable.ch_charactor_mandarin_m);
        characterStore.put(10,R.drawable.ch_charactor_mandarin_f);
    }
}