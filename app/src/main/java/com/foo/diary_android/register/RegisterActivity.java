package com.foo.diary_android.register;

import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.foo.diary_android.MainActivity;
import com.foo.diary_android.OnSingleClickListener;
import com.foo.diary_android.R;
import com.foo.diary_android.mode.ModeActivity;
import com.foo.diary_android.service.MemberDto;
import com.foo.diary_android.service.Service;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private SlidingUpPanelLayout slidingUpPanelLayout;

    private ImageView characterImg;
    private EditText nickName;
    private Button characterBtn;
    private Button doneBtn;
    private ImageButton closeBtn;
    private Button selectBtn;
    private Button validBtn;

    private Long userId;
    private int categoryId=0;
    private int categoryImg;
    private MemberDto user;
    private String registNickName="";

    private RegisterDialog registerDialog;
    private InfoDialog infoDialog;

    private View.OnClickListener okListener;

    private View.OnClickListener noListener = new View.OnClickListener() {
        public void onClick(View v) {
            registerDialog.dismiss();
        }
    };

    private View.OnClickListener infoOkListener = new View.OnClickListener() {
        public void onClick(View v) {
            infoDialog.dismiss();
        }
    };


    RecyclerView mRecyclerView = null ;
    CategoryAdapter mAdapter = null ;
    ArrayList<CategoryData> mList = new ArrayList<CategoryData>();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Service.BASE_RUL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Service service = retrofit.create(Service.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        slidingUpPanelLayout = findViewById(R.id.register_slide);

        userId = getIntent().getLongExtra("id",0L);

        Log.e("아이디",String.valueOf(userId));

        characterImg = findViewById(R.id.register_character);
        nickName = findViewById(R.id.register_nickname);
        characterBtn = findViewById(R.id.register_select_character);
        doneBtn = findViewById(R.id.register_done);
        closeBtn = findViewById(R.id.register_close_btn);
        selectBtn = findViewById(R.id.register_btn_select);
        mRecyclerView = findViewById(R.id.register_character_rc);
        validBtn = findViewById(R.id.register_valid);

        mAdapter = new CategoryAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

        mAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                categoryId = mList.get(position).getCategoryId();
                categoryImg = mList.get(position).getCategoryImg();
                selectBtn.setVisibility(View.VISIBLE);
                mAdapter.notifyItemRangeChanged(0,position);
                mAdapter.notifyItemRangeChanged(position+1,10);
            }
        });

        dataInit();

        closeBtn.setOnClickListener(l->{
            categoryId=0;
            categoryImg=0;
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        });

        doneBtn.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View v) {
                if(!registNickName.equals("")){
                    if(registNickName.equals(String.valueOf(nickName.getText()))){
                        if(String.valueOf(nickName.getText())!=null&&categoryId!=0){
                            okListener = new View.OnClickListener() {
                                public void onClick(View v) {
                                    user = new MemberDto(userId,String.valueOf(nickName.getText()),categoryId);
                                    Intent intent = new Intent(getApplicationContext(), ModeActivity.class);
                                    intent.putExtra("user",user);
                                    register(user);
                                    startActivity(intent);
                                    finish();
                                }
                            };
                            registerDialog = new RegisterDialog(RegisterActivity.this,okListener,noListener);
                            registerDialog.show();
                        }
                        else{
                            infoDialog = new InfoDialog(RegisterActivity.this,infoOkListener,
                                    "농부이름 혹은 농부를 선택해 주세요");
                            infoDialog.show();
                        }
                    }
                    else{
                        infoDialog = new InfoDialog(RegisterActivity.this,infoOkListener,
                                "농부이름 중복확인 해주세요");
                        infoDialog.show();
                    }
                }
                else{
                    infoDialog = new InfoDialog(RegisterActivity.this,infoOkListener,
                            "농부이름 중복확인 해주세요");
                    infoDialog.show();
                }
            }
        });

        characterBtn.setOnClickListener(l->{
            selectBtn.setVisibility(View.INVISIBLE);
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        });

        selectBtn.setOnClickListener(l->{
            characterImg.setImageResource(categoryImg);
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        });

        validBtn.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View v) {
                if(nickName.getText().toString().equals("")){
                    infoDialog = new InfoDialog(RegisterActivity.this,infoOkListener,
                            "농부이름을 입력하세요");
                    infoDialog.show();
                }
                else{
                    if(nickName.getText().toString().length()>10){
                        infoDialog = new InfoDialog(RegisterActivity.this,infoOkListener,
                                "농부이름은 최대 10자입니다");
                        nickName.setText(null);
                        infoDialog.show();
                    }
                    else{
                        service.validMemberNickName(nickName.getText().toString()).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if(response.body().booleanValue()){ // 중복안됨
                                    registNickName=nickName.getText().toString();
                                    infoDialog = new InfoDialog(RegisterActivity.this,infoOkListener,
                                            "사용 가능한 이름입니다");
                                    infoDialog.show();
                                }
                                else{ //중복됨
                                    nickName.setText(null);
                                    infoDialog = new InfoDialog(RegisterActivity.this,infoOkListener,
                                            "농부이름이 중복되었습니다");
                                    infoDialog.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }
                }
            }
        });
    }

    public void dataInit(){
        mAdapter.addItem(1,"남자감자",R.drawable.ch_charactor_potato_m);
        mAdapter.addItem(2,"여자감자",R.drawable.ch_charactor_potato_f);
        mAdapter.addItem(3,"남자고구마",R.drawable.ch_charactor_swpotato_m);
        mAdapter.addItem(4,"여자고구마",R.drawable.ch_charactor_swpotato_f);
        mAdapter.addItem(5,"남자아보카도",R.drawable.ch_charactor_abocado_m);
        mAdapter.addItem(6,"여자아보카도",R.drawable.ch_charactor_abocado_f);
        mAdapter.addItem(7,"남자사과",R.drawable.ch_charactor_apple_m);
        mAdapter.addItem(8,"여자사과",R.drawable.ch_charactor_apple_f);
        mAdapter.addItem(9,"남자귤",R.drawable.ch_charactor_mandarin_m);
        mAdapter.addItem(10,"여자귤",R.drawable.ch_charactor_mandarin_f);
        mAdapter.notifyDataSetChanged();
    }

    public void register(MemberDto user){
        service.registerMember(user, MainActivity.accessToken).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("성공","성공!!");
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}