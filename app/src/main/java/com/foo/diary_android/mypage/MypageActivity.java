package com.foo.diary_android.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.foo.diary_android.MainActivity;
import com.foo.diary_android.OnSingleClickListener;
import com.foo.diary_android.R;
import com.foo.diary_android.service.MemberDto;
import com.foo.diary_android.service.Service;
import com.kakao.sdk.user.UserApiClient;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MypageActivity extends AppCompatActivity {

    private TextView nickName;
    private TextView characterText;
    private ImageButton backBtn;
    private Button editBtn;
    private Button logoutBtn;
    private Button signoutBtn;
    private ImageView myImg;

    private LogoutDialog logoutDialog;
    private SignoutDialog signoutDialog;

    private MemberDto user;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Service.BASE_RUL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Service service = retrofit.create(Service.class);

    private View.OnClickListener logoutOkListener;
    private View.OnClickListener signoutOkListener;

    private View.OnClickListener logoutNoListener = new View.OnClickListener() {
        public void onClick(View v) {
            logoutDialog.dismiss();
        }
    };

    private View.OnClickListener signoutNoListener = new View.OnClickListener() {
        public void onClick(View v) {
            signoutDialog.dismiss();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        user = (MemberDto) getIntent().getSerializableExtra("user");

        logoutBtn = findViewById(R.id.mypage_btn_logout);
        signoutBtn = findViewById(R.id.mypage_btn_signout);
        nickName = findViewById(R.id.mypage_nickname);
        backBtn = findViewById(R.id.mypage_btn_back);
        editBtn = findViewById(R.id.mypage_btn_edit);
        myImg = findViewById(R.id.mypage_character);
        characterText = findViewById(R.id.mypage_character_text);

        logoutBtn.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View v) {
                logoutOkListener = new OnSingleClickListener(){
                    @Override
                    public void onSingleClick(View v) {
                        UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                            @Override
                            public Unit invoke(Throwable throwable) {
                                if(throwable!=null){
                                    Log.e("Debug", "로그아웃 실패!");
                                }
                                else{
                                    Log.e("Debug", "로그아웃 성공!");
                                }
                                return null;
                            }
                        });
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                };
                logoutDialog = new LogoutDialog(MypageActivity.this,logoutOkListener,logoutNoListener);
                logoutDialog.show();
            }
        });

        signoutBtn.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View v) {
                signoutOkListener = new OnSingleClickListener(){
                    @Override
                    public void onSingleClick(View v) {
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
                        deleteMember();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                };
                signoutDialog = new SignoutDialog(MypageActivity.this,signoutOkListener,signoutNoListener);
                signoutDialog.show();
            }
        });

        backBtn.setOnClickListener(l->{
            finish();
            overridePendingTransition(R.anim.anim_slide_out_left,R.anim.anim_slide_out_right);
        });

        nickName.setText(user.getNickName());
        myImg.setImageResource(MainActivity.characterStore.get(user.getCategoryId()));
        characterText.setText(MainActivity.getCharacterName(user.getCategoryId())+"농부");
        setChBackground(user.getCategoryId());

        editBtn.setOnClickListener(l->{
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_in_left);
        });

    }

    public void deleteMember(){
        service.deleteMember(user.getId(),MainActivity.accessToken).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void setChBackground(int categoryId){
        if(categoryId==1||categoryId==2){
            characterText.setBackgroundResource(R.drawable.mypage_character_text_p);
        }
        else if(categoryId==3||categoryId==4){
            characterText.setBackgroundResource(R.drawable.mypage_character_text_sp);
        }
        else if(categoryId==5||categoryId==6){
            characterText.setBackgroundResource(R.drawable.mypage_character_text_abo);
        }
        else if(categoryId==7||categoryId==8){
            characterText.setBackgroundResource(R.drawable.mypage_character_text_ap);
        }
        else if(categoryId==9||categoryId==10){
            characterText.setBackgroundResource(R.drawable.mypage_character_text_man);
        }
    }
}