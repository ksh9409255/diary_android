package com.foo.diary_android.friend;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FriendDelDialog extends Dialog {
    private EditText nickName;
    private Button delBtn;

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

    public FriendDelDialog(@NonNull Context context, MemberDto user) {
        super(context);
        this.context=context;
        this.user=user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_friend);

        nickName = findViewById(R.id.dialog_friend_edit);
        delBtn = findViewById(R.id.dialog_friend_btn);

        delBtn.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View v){
                if(nickName.getText().toString().equals("")){
                    infoDialog = new InfoDialog(context,infoOkListener,
                            "닉네임을 입력하세요");
                    infoDialog.show();
                }
                else{
                    if(nickName.getText().toString().equals(user.getNickName())){
                        infoDialog = new InfoDialog(context,infoOkListener,
                                "자신을 삭제할 수 없습니다.");
                        infoDialog.show();
                    }
                    else{
                        Member member = new Member(user.getId(),user.getNickName(),user.getCategoryId());
                        dialogOkListener = new OnSingleClickListener(){
                            @Override
                            public void onSingleClick(View v) {
                                service.removeFriend(new FriendAndroidDto(member,nickName.getText().toString()), MainActivity.accessToken).enqueue(new Callback<Boolean>() {
                                    @Override
                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                        if(response.body().booleanValue()){ //정상추가
                                            Intent intent = new Intent(context,FriendActivity.class);
                                            intent.putExtra("user",user);
                                            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            context.startActivity(intent);
                                        }
                                        else{
                                            dialog.dismiss();
                                            infoDialog = new InfoDialog(context,infoOkListener,
                                                    "존재하지 않는 닉네임입니다");
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
                                "친구를 삭제하시겠습니까?");
                        dialog.show();
                    }
                }
            }
        });
    }
}
