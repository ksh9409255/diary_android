package com.foo.diary_android.mypage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.foo.diary_android.MainActivity;
import com.foo.diary_android.OnSingleClickListener;
import com.foo.diary_android.R;
import com.foo.diary_android.diaryMe.MeDiaryActivity;
import com.foo.diary_android.diaryMe.MeLookActivity;
import com.foo.diary_android.friend.FriendActivity;
import com.foo.diary_android.friend.FriendDelDialog;
import com.foo.diary_android.mode.ModeActivity;
import com.foo.diary_android.register.CategoryAdapter;
import com.foo.diary_android.register.CategoryData;
import com.foo.diary_android.register.InfoDialog;
import com.foo.diary_android.register.RegisterActivity;
import com.foo.diary_android.service.CategoryDto;
import com.foo.diary_android.service.MemberDto;
import com.foo.diary_android.service.Service;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {

    private ImageButton backBtn;
    private EditText editNickName;
    private ImageView myImg;
    private Button CharacterBtn;
    private Button CharacterSelectBtn;
    private Button OkBtn;
    private ImageButton closeBtn;
    private TextView characterText;
    private Button nickNameValid;

    private ProfileDialog profileDialog;
    private InfoDialog infoDialog;

    private MemberDto user;
    private String registNickName="";

    private SlidingUpPanelLayout slidingUpPanelLayout;

    private int selectCharacterImg;
    private int SelectCharacterIndex;

    RecyclerView mRecyclerView = null ;
    CategoryAdapter mAdapter = null ;
    ArrayList<CategoryData> mList = new ArrayList<CategoryData>();

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

    private View.OnClickListener okListener;

    private View.OnClickListener noListener = new View.OnClickListener() {
        public void onClick(View v) {
            profileDialog.dismiss();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = (MemberDto) getIntent().getSerializableExtra("user");

        slidingUpPanelLayout = findViewById(R.id.profile_main_frame);

        backBtn = findViewById(R.id.profile_btn_back);
        closeBtn = findViewById(R.id.profile_close_btn);
        myImg = findViewById(R.id.profile_character);
        editNickName = findViewById(R.id.profile_edit_nickname);
        CharacterBtn = findViewById(R.id.profile_btn_character);
        CharacterSelectBtn = findViewById(R.id.profile_btn_select);
        OkBtn = findViewById(R.id.profile_btn_ok);
        mRecyclerView = findViewById(R.id.profile_character_rc);
        characterText = findViewById(R.id.profile_character_text);
        nickNameValid = findViewById(R.id.profile_valid);

        mAdapter = new CategoryAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        RcDataInit(user.getId());

        editNickName.setText(user.getNickName());
        myImg.setImageResource(MainActivity.characterStore.get(user.getCategoryId()));
        characterText.setText(MainActivity.getCharacterName(user.getCategoryId())+"농부");
        CharacterBtn.setText(MainActivity.getCharacterName(user.getCategoryId()));
        setChBackground(user.getCategoryId());
        selectCharacterImg = MainActivity.characterStore.get(user.getCategoryId());
        SelectCharacterIndex = user.getCategoryId();

        mAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                CharacterSelectBtn.setVisibility(View.VISIBLE);
                selectCharacterImg = mList.get(position).getCategoryImg();
                SelectCharacterIndex = mList.get(position).getCategoryId();
                mAdapter.notifyItemRangeChanged(0,position);
                mAdapter.notifyItemRangeChanged(position+1,10);
            }
        });

        slidingUpPanelLayout.setFadeOnClickListener(l->{ // 투명한 뒷부분 클릭시 작동하는 메서드
            CharacterSelectBtn.setVisibility(View.INVISIBLE);
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN); // 페널 상태 설정 메서드
        });

        CharacterSelectBtn.setOnClickListener(l->{
            CharacterSelectBtn.setVisibility(View.INVISIBLE);
            myImg.setImageResource(selectCharacterImg);
            characterText.setText(MainActivity.getCharacterName(SelectCharacterIndex)+"농부");
            CharacterBtn.setText(MainActivity.getCharacterName(SelectCharacterIndex));
            setChBackground(user.getCategoryId());
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        });

        CharacterBtn.setOnClickListener(l->{
            CharacterSelectBtn.setVisibility(View.INVISIBLE);
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        });

        backBtn.setOnClickListener(l->{
            finish();
            overridePendingTransition(R.anim.anim_slide_out_left,R.anim.anim_slide_out_right);
        });

        closeBtn.setOnClickListener(l->{
            selectCharacterImg = MainActivity.characterStore.get(user.getCategoryId());
            SelectCharacterIndex = user.getCategoryId();
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        });

        nickNameValid.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View v) {
                if(editNickName.getText().toString().equals("")){
                    infoDialog = new InfoDialog(ProfileActivity.this,infoOkListener,
                            "농부이름을 입력하세요");
                    infoDialog.show();
                }
                else{
                    if(editNickName.getText().toString().length()>10){
                        infoDialog = new InfoDialog(ProfileActivity.this,infoOkListener,
                                "농부이름은 최대 10자입니다");
                        editNickName.setText(null);
                        infoDialog.show();
                    }
                    else{
                        service.validMemberNickName(editNickName.getText().toString()).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if(response.body().booleanValue()){ // 중복안됨
                                    registNickName=editNickName.getText().toString();
                                    infoDialog = new InfoDialog(ProfileActivity.this,infoOkListener,
                                            "사용 가능한 이름입니다");
                                    infoDialog.show();
                                }
                                else{ //중복됨
                                    editNickName.setText(null);
                                    infoDialog = new InfoDialog(ProfileActivity.this,infoOkListener,
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

        OkBtn.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View v){
                if(!registNickName.equals("")){
                    if(registNickName.equals(String.valueOf(editNickName.getText()))){
                        okListener = new View.OnClickListener() {
                            public void onClick(View v) {
                                MemberDto memberDto = new MemberDto(user.getId(),String.valueOf(editNickName.getText()),SelectCharacterIndex);
                                updateMember(memberDto);
                                Intent intent = new Intent(ProfileActivity.this, ModeActivity.class);
                                intent.putExtra("user",memberDto);
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_in_left);
                                finish();
                            }
                        };
                        profileDialog = new ProfileDialog(ProfileActivity.this,okListener,noListener);
                        profileDialog.show();
                    }
                    else{
                        infoDialog = new InfoDialog(ProfileActivity.this,infoOkListener,
                                "중복확인을 해주세요");
                        infoDialog.show();
                    }
                }
                else{
                    infoDialog = new InfoDialog(ProfileActivity.this,infoOkListener,
                            "농부이름이 비었습니다");
                    infoDialog.show();
                }
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        hideKeyboard();
        return super.dispatchTouchEvent(ev);
    }


    public void RcDataInit(Long id){
        service.getCategoryByMemberId(id,MainActivity.accessToken).enqueue(new Callback<List<CategoryDto>>() {
            @Override
            public void onResponse(Call<List<CategoryDto>> call, Response<List<CategoryDto>> response) {
                List<CategoryDto> categoryDtoList = response.body();
                for(CategoryDto categoryDto : categoryDtoList){
                    if(categoryDto.getId()%2==0){ // 여자
                        mAdapter.addItem(categoryDto.getId(),
                                categoryDto.getName(),
                                MainActivity.characterStore.get(categoryDto.getId()));
                        mAdapter.addItem(categoryDto.getId()-1,
                                categoryDto.getName(),
                                MainActivity.characterStore.get(categoryDto.getId()-1));
                    }
                    else{
                        mAdapter.addItem(categoryDto.getId(),
                                categoryDto.getName(),
                                MainActivity.characterStore.get(categoryDto.getId()));
                        mAdapter.addItem(categoryDto.getId()+1,
                                categoryDto.getName(),
                                MainActivity.characterStore.get(categoryDto.getId()+1));
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<CategoryDto>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void updateMember(MemberDto memberDto){
        service.updateMember(memberDto,MainActivity.accessToken).enqueue(new Callback<ResponseBody>() {
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