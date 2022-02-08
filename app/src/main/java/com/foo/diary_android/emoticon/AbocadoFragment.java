package com.foo.diary_android.emoticon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foo.diary_android.MainActivity;
import com.foo.diary_android.R;
import com.foo.diary_android.service.EmoticonDto;
import com.foo.diary_android.service.MemberDto;
import com.foo.diary_android.service.Service;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AbocadoFragment extends Fragment {

    RecyclerView mRecyclerView = null ;
    EmoticonAdapter mAdapter = null ;
    ArrayList<EmoticonData> mList = new ArrayList<EmoticonData>();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Service.BASE_RUL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Service service = retrofit.create(Service.class);

    private MemberDto user;

    private EmoticonDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_abocado, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();

        user = (MemberDto) bundle.getSerializable("user");

        mRecyclerView = view.findViewById(R.id.emoticon_rc);

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        mAdapter = new EmoticonAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),4));
        dataInit();

        mAdapter.setOnItemClickListener(new EmoticonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                dialog = new EmoticonDialog(view.getContext()
                        ,mList.get(position).getEmoticonNum()
                        ,mList.get(position).getName(),mList.get(position).getContent()
                        ,mList.get(position).getEmoticonImage());
                dialog.show();
                mAdapter.notifyItemRangeChanged(0,position);
                mAdapter.notifyItemRangeChanged(position+1,10);
            }
        });
    }

    public void dataInit(){

        service.getEmoticonByCategoryId(5).enqueue(new Callback<List<EmoticonDto>>() {
            @Override
            public void onResponse(Call<List<EmoticonDto>> call, Response<List<EmoticonDto>> response) {
                List<EmoticonDto> emoticonDtoList = response.body();
                if(emoticonDtoList.size()!=0){
                    for(EmoticonDto emoticonDto : emoticonDtoList){
                        mAdapter.addItem(emoticonDto.getId(),emoticonDto.getName(),
                                MainActivity.emoticonStore.get(emoticonDto.getId()),emoticonDto.getDescription());
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<EmoticonDto>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
