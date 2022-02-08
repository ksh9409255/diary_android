package com.foo.diary_android.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.foo.diary_android.MainActivity;
import com.foo.diary_android.R;
import com.foo.diary_android.mode.ModeActivity;
import com.foo.diary_android.register.RegisterActivity;
import com.foo.diary_android.service.MemberCheckResponse;
import com.foo.diary_android.service.MemberDto;
import com.foo.diary_android.service.Service;
import com.kakao.sdk.auth.TokenManagerProvider;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private ImageButton login;

    public MemberDto memberUser = new MemberDto();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Service.BASE_RUL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Service service = retrofit.create(Service.class);

    public void getMember(Long id){
        service.getMember(id,MainActivity.accessToken).enqueue(new Callback<MemberDto>() {
            @Override
            public void onResponse(Call<MemberDto> call, Response<MemberDto> response) {
                MemberDto member = response.body();
                //memberUser.setMember(1L,"KIM",1);
                memberUser.setMember(member.getId(),member.getNickName(),member.getCategoryId());
                //Log.e("성공!!!",String.valueOf(memberUser.getNickName()));
                Intent intent = new Intent(getApplicationContext(), ModeActivity.class);
                intent.putExtra("user",memberUser);
                startActivity(intent);
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
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.main_btn_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // UserApiClient를 이용해 로그인 시도
                UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, new Function2<OAuthToken, Throwable, Unit>() {
                    @Override
                    public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {

                        if (throwable != null) {
                            Log.e("Debug", "로그인 실패!");
                        } else if (oAuthToken != null) {
                            MainActivity.accessToken=TokenManagerProvider.getInstance().getManager().getToken().getAccessToken().toString();
                            Log.e("Debug", MainActivity.accessToken);
                            // 로그인 성공 시 사용자 정보 받기
                            UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() { // me를 이용해 사용자 정보 가져오기
                                @Override
                                public Unit invoke(User user, Throwable throwable) { // user가 사용자 정보임임
                                    if (throwable != null) {
                                        Log.e("Deubg", "사용자 정보 받기 실패!");
                                    } else if (user != null) {
                                        Log.e("Debug", "사용자 정보 받기 성공!");
                                        service.checkId(user.getId(),MainActivity.accessToken).enqueue(new Callback<Boolean>() {
                                            @Override
                                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                                if(!response.body().booleanValue()){ // 회원가입 안됨
                                                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                                                    intent.putExtra("id",user.getId());
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else{
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
                                            }

                                            @Override
                                            public void onFailure(Call<Boolean> call, Throwable t) {
                                                t.printStackTrace();
                                                Log.e("실패","실패!");
                                            }
                                        });
                                    }
                                    return null;
                                }
                            });
                        }
                        return null;
                    }
                });
            }
        });
    }
}