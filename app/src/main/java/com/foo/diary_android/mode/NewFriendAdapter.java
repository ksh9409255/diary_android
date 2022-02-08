package com.foo.diary_android.mode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foo.diary_android.MainActivity;
import com.foo.diary_android.OnSingleClickListener;
import com.foo.diary_android.R;
import com.foo.diary_android.friend.FriendData;
import com.foo.diary_android.register.InfoDialog;
import com.foo.diary_android.register.RegisterActivity;
import com.foo.diary_android.service.FriendAndroidDto;
import com.foo.diary_android.service.FriendDto;
import com.foo.diary_android.service.Member;
import com.foo.diary_android.service.MemberDto;
import com.foo.diary_android.service.Service;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewFriendAdapter extends RecyclerView.Adapter<NewFriendAdapter.ViewHolder>{

    private ArrayList<NewFriendData> mData = null;

    private NewFriendAdapter.OnItemClickListener mListener = null ;

    private NewFriendDialog acceptDialog;
    private NewFriendDialog rejectDialog;

    private Context parentContext;

    private View.OnClickListener acceptoKListener;

    private View.OnClickListener acceptNoListener = new View.OnClickListener() {
        public void onClick(View v) {
            acceptDialog.dismiss();
        }
    };

    private View.OnClickListener rejectoKListener;

    private View.OnClickListener rejectNoListener = new View.OnClickListener() {
        public void onClick(View v) {
            rejectDialog.dismiss();
        }
    };


    public NewFriendAdapter(ArrayList<NewFriendData> list){
        mData=list;
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Service.BASE_RUL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Service service = retrofit.create(Service.class);

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(NewFriendAdapter.OnItemClickListener listener) {
        this.mListener = listener ;
    }

    @NonNull
    @Override
    public NewFriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        parentContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.recycler_new_friend_item, parent, false) ;
        NewFriendAdapter.ViewHolder vh = new NewFriendAdapter.ViewHolder(view) ;
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NewFriendAdapter.ViewHolder holder, int position) {
        NewFriendData item = mData.get(position) ;
        holder.friendImg.setImageResource(item.getFriendImg());
        holder.friendNickName.setText(String.valueOf(item.getFriendNickName()));
        holder.acceptBtn.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View v){
                acceptoKListener = new OnSingleClickListener(){
                    @Override
                    public void onSingleClick(View v) {
                        Member memberDto1 = new Member(item.getUserId().getId(),item.getUserId().getNickName(),
                                item.getUserId().getCategoryId());
                        Member memberDto2 = new Member(item.getFriendId(),item.getFriendNickName(),item.getFriendImg());
                        FriendDto friendDto = new FriendDto(memberDto1,memberDto2);
                        acceptFriend(friendDto);
                        Intent intent = new Intent(parentContext,ModeActivity.class);
                        intent.putExtra("user",item.getUserId());
                        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                        parentContext.startActivity(intent);
                    }
                };
                acceptDialog = new NewFriendDialog(parentContext,acceptoKListener,acceptNoListener,
                        "친구추가를 하시겠습니까?");
                acceptDialog.show();
            }
        });
        holder.noBtn.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View v){
                rejectoKListener = new OnSingleClickListener(){
                    @Override
                    public void onSingleClick(View v) {
                        Member member = new Member(item.getUserId().getId(),item.getUserId().getNickName()
                                ,item.getUserId().getCategoryId());
                        FriendAndroidDto friendAndroidDto = new FriendAndroidDto(member,item.getFriendNickName());
                        service.removeFriend(friendAndroidDto, MainActivity.accessToken).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if(response.body().booleanValue()){
                                    Intent intent = new Intent(parentContext,ModeActivity.class);
                                    intent.putExtra("user",item.getUserId());
                                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    parentContext.startActivity(intent);
                                }
                            }
                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    }
                };
                rejectDialog = new NewFriendDialog(parentContext,rejectoKListener,rejectNoListener,
                        "친구추가를 하지 않겠습니까?");
                rejectDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItem(MemberDto user, Long id,int img, String nickName) {
        NewFriendData itemData = new NewFriendData(user,id, img,nickName);
        mData.add(itemData);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView friendImg;
        TextView friendNickName;
        ImageButton acceptBtn;
        ImageButton noBtn;

        ViewHolder(View itemView) {
            super(itemView) ;

            friendImg = itemView.findViewById(R.id.new_friend_item_img) ;
            friendNickName = itemView.findViewById(R.id.new_friend_item_nickname);
            acceptBtn = itemView.findViewById(R.id.new_friend_item_accept);
            noBtn = itemView.findViewById(R.id.new_friend_item_no);
        }
    }

    public void acceptFriend(FriendDto friendDto){
        service.acceptNewFriend(friendDto,MainActivity.accessToken).enqueue(new Callback<ResponseBody>() {
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
}