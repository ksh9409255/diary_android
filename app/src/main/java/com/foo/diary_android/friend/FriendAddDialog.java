package com.foo.diary_android.friend;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.foo.diary_android.MainActivity;
import com.foo.diary_android.OnSingleClickListener;
import com.foo.diary_android.R;
import com.foo.diary_android.mode.NewFriendDialog;
import com.foo.diary_android.register.InfoDialog;
import com.foo.diary_android.register.RegisterActivity;
import com.foo.diary_android.service.FriendAndroidDto;
import com.foo.diary_android.service.Member;
import com.foo.diary_android.service.MemberDto;
import com.foo.diary_android.service.Service;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FriendAddDialog extends Dialog {

    private EditText nickName;
    private Button addBtn;

    private MemberDto user;

    private Context context;

    private InfoDialog infoDialog;
    private NewFriendDialog dialog;

    private View.OnClickListener dialogOkListener;
    private View.OnClickListener dialogNoListener = new View.OnClickListener() {
        public void onClick(View v) {
            dialog.dismiss();
        }
    };

    private View.OnClickListener infoOkListener = new View.OnClickListener() {
        public void onClick(View v) {
            infoDialog.dismiss();
        }
    };

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Service.BASE_RUL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Service service = retrofit.create(Service.class);

    public FriendAddDialog(@NonNull Context context, MemberDto user) {
        super(context);
        this.context=context;
        this.user=user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_friend);

        nickName = findViewById(R.id.dialog_friend_edit);
        addBtn = findViewById(R.id.dialog_friend_btn);

        addBtn.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View v) {
                if(nickName.getText().toString().equals("")){
                    infoDialog = new InfoDialog(context,infoOkListener,
                            "닉네임을 입력하세요");
                    infoDialog.show();
                }
                else{
                    if(nickName.getText().toString().equals(user.getNickName())){
                        infoDialog = new InfoDialog(context,infoOkListener,
                                "자신을 추가할 수 없습니다.");
                        infoDialog.show();
                    }
                    else{
                        Member member = new Member(user.getId(),user.getNickName(),user.getCategoryId());
                        dialogOkListener = new OnSingleClickListener() {
                            @Override
                            public void onSingleClick(View v) {
                                service.addFriend(new FriendAndroidDto(member,nickName.getText().toString()),MainActivity.accessToken).enqueue(new Callback<Boolean>() {
                                    @Override
                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                        Log.e("결과",response.body().toString());
                                        if(response.body().booleanValue()){ //정상추가
                                            dialog.dismiss();
                                            dismiss();
                                        }
                                        else{
                                            dialog.dismiss();
                                            infoDialog = new InfoDialog(context,infoOkListener,
                                                    "중복요청되었거나 존재하지 않는 닉네임 입니다");
                                            infoDialog.show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Boolean> call, Throwable t) {
                                        t.printStackTrace();
                                    }
                                });
                            }
                        };
                        dialog = new NewFriendDialog(context,dialogOkListener,dialogNoListener,
                                "친구를 추가하시겠습니까?");
                        dialog.show();
                    }
                }
            }
        });
    }
}
