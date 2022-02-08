package com.foo.diary_android.friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.foo.diary_android.R;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>{

    private ArrayList<FriendData> mData = null;

    private OnItemClickListener mListener = null ;

    public FriendAdapter(ArrayList<FriendData> list){
        mData=list;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    @NonNull
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.recycler_friend_item, parent, false) ;
        FriendAdapter.ViewHolder vh = new FriendAdapter.ViewHolder(view) ;
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendAdapter.ViewHolder holder, int position) {
        FriendData item = mData.get(position) ;
        holder.friendImg.setImageResource(item.getFriendImg());
        holder.friendNickName.setText(String.valueOf(item.getFriendNickName()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItem(Long id,int img, String nickName) {
        FriendData itemData = new FriendData(id, img,nickName);
        mData.add(itemData);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView friendImg;
        TextView friendNickName;

        ViewHolder(View itemView) {
            super(itemView) ;

            friendImg = itemView.findViewById(R.id.friend_item_img) ;
            friendNickName = itemView.findViewById(R.id.friend_item_nickname);

            itemView.setOnClickListener(v->{
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    if(mListener != null) {
                        mListener.onItemClick(v, pos);
                    }
                }
            });
        }
    }
}
