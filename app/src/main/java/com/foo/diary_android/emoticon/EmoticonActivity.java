package com.foo.diary_android.emoticon;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.foo.diary_android.MainActivity;
import com.foo.diary_android.R;
import com.foo.diary_android.service.CategoryDto;
import com.foo.diary_android.service.EmoticonDto;
import com.foo.diary_android.service.MemberDto;
import com.foo.diary_android.service.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmoticonActivity extends AppCompatActivity {

    private final int fragment_potato = 1;
    private final int fragment_swpotato = 3;
    private final int fragment_abocado = 5;
    private final int fragment_apple = 7;
    private final int fragment_mandarin = 9;

    /*RecyclerView mRecyclerView = null ;
    EmoticonAdapter mAdapter = null ;
    ArrayList<EmoticonData> mList = new ArrayList<EmoticonData>();*/

    private TextView nickName;
    private EmoticonDialog dialog;
    private ImageButton btnBack;

    private ImageButton btnPotato;
    private ImageButton btnSwPotato;
    private ImageButton btnAbocado;
    private ImageButton btnApple;
    private ImageButton btnMandarin;
    private ImageView nothinImg;

    private FrameLayout frameLayout;

    private MemberDto user;

    private Map<Integer,Integer> userCategory;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Service.BASE_RUL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Service service = retrofit.create(Service.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoticon);

        user = (MemberDto) getIntent().getSerializableExtra("user");

        nickName = findViewById(R.id.emoticon_nickname);
        btnBack = findViewById(R.id.emoticon_btn_back);
        btnPotato = findViewById(R.id.fragment_btn_potato);
        btnSwPotato = findViewById(R.id.fragment_btn_swpotato);
        btnAbocado = findViewById(R.id.fragment_btn_abocado);
        btnApple = findViewById(R.id.fragment_btn_apple);
        btnMandarin = findViewById(R.id.fragment_btn_mandarin);
        frameLayout = findViewById(R.id.fragment_container);

        service.getCategoryByMemberId(user.getId(),MainActivity.accessToken).enqueue(new Callback<List<CategoryDto>>() {
            @Override
            public void onResponse(Call<List<CategoryDto>> call, Response<List<CategoryDto>> response) {
                List<CategoryDto> categoryDtoList = response.body();
                userCategory = new HashMap<>();
                for(CategoryDto categoryDto : categoryDtoList){
                    userCategory.put(categoryDto.getId(),categoryDto.getId());
                }
                FragmentView(initFragment());
            }

            @Override
            public void onFailure(Call<List<CategoryDto>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        btnPotato.setOnClickListener(l->{
            FragmentView(fragment_potato);
        });

        btnSwPotato.setOnClickListener(l->{
            FragmentView(fragment_swpotato);
        });

        btnAbocado.setOnClickListener(l->{
            FragmentView(fragment_abocado);
        });

        btnApple.setOnClickListener(l->{
            FragmentView(fragment_apple);
        });

        btnMandarin.setOnClickListener(l->{
            FragmentView(fragment_mandarin);
        });

        nickName.setText(user.getNickName()+"님의 재배작물");

        btnBack.setOnClickListener(l->{
            finish();
            overridePendingTransition(R.anim.anim_slide_out_left,R.anim.anim_slide_out_right);
        });
    }

    public int initFragment(){
        int res = 0;
        if(user.getCategoryId()==1||user.getCategoryId()==2){
            res=1;
        }
        else if(user.getCategoryId()==3||user.getCategoryId()==4){
            res=3;
        }
        else if(user.getCategoryId()==5||user.getCategoryId()==6){
            res=5;
        }
        else if(user.getCategoryId()==7||user.getCategoryId()==8){
            res=7;
        }
        else if(user.getCategoryId()==9||user.getCategoryId()==10){
            res=9;
        }
        return res;
    }


    private void FragmentView(int fragment) {

        //FragmentTransactiom를 이용해 프래그먼트를 사용합니다.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragment) {
            case 1:
                PotatoFragment potatoFragment = new PotatoFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("user", user);
                potatoFragment.setArguments(bundle1);
                frameLayout.setBackgroundColor(Color.parseColor("#FFF0BF"));
                transaction.replace(R.id.fragment_container, potatoFragment);
                transaction.commit();
                break;

            case 3:
                SweetPotatoFragment sweetPotatoFragment = new SweetPotatoFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("user", user);
                sweetPotatoFragment.setArguments(bundle2);
                frameLayout.setBackgroundColor(Color.parseColor("#4D77388D"));
                transaction.replace(R.id.fragment_container, sweetPotatoFragment);
                transaction.commit();
                break;

            case 5:
                AbocadoFragment abocadoFragment = new AbocadoFragment();
                Bundle bundle3 = new Bundle();
                bundle3.putSerializable("user", user);
                abocadoFragment.setArguments(bundle3);
                frameLayout.setBackgroundColor(Color.parseColor("#C6E0A4"));
                transaction.replace(R.id.fragment_container, abocadoFragment);
                transaction.commit();
                break;

            case 7:
                AppleFragment appleFragment = new AppleFragment();
                Bundle bundle4 = new Bundle();
                bundle4.putSerializable("user", user);
                appleFragment.setArguments(bundle4);
                frameLayout.setBackgroundColor(Color.parseColor("#FFA290"));
                transaction.replace(R.id.fragment_container, appleFragment);
                transaction.commit();
                break;

            case 9:
                MandarinFragment mandarinFragment = new MandarinFragment();
                Bundle bundle5 = new Bundle();
                bundle5.putSerializable("user", user);
                mandarinFragment.setArguments(bundle5);
                frameLayout.setBackgroundColor(Color.parseColor("#FFC984"));
                transaction.replace(R.id.fragment_container, mandarinFragment);
                transaction.commit();
                break;
        }

    }
}