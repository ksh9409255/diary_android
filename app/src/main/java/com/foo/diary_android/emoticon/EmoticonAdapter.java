package com.foo.diary_android.emoticon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.foo.diary_android.R;
import com.foo.diary_android.friend.FriendAdapter;
import com.foo.diary_android.friend.FriendData;

import java.util.ArrayList;

public class EmoticonAdapter extends RecyclerView.Adapter<EmoticonAdapter.ViewHolder>{

    private ArrayList<EmoticonData> mData = null;

    private EmoticonAdapter.OnItemClickListener mListener = null ;

    public EmoticonAdapter(ArrayList<EmoticonData> list){
        mData=list;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(EmoticonAdapter.OnItemClickListener listener) {
        this.mListener = listener ;
    }

    @NonNull
    @Override
    public EmoticonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.recycler_emoticon_item, parent, false) ;
        EmoticonAdapter.ViewHolder vh = new EmoticonAdapter.ViewHolder(view) ;
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull EmoticonAdapter.ViewHolder holder, int position) {
        EmoticonData item = mData.get(position) ;
        holder.emoticonImg.setImageResource(item.getEmoticonImage());
        if(holder.emoticonImg.isSelected()){
            holder.emoticonImg.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItem(int emoticonNum, String name, int emoticonImage, String content) {
        EmoticonData itemData = new EmoticonData(emoticonNum,name,emoticonImage,content);
        mData.add(itemData);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView emoticonImg;

        ViewHolder(View itemView) {
            super(itemView) ;

            emoticonImg = itemView.findViewById(R.id.emoticon_item_img) ;

            itemView.setOnClickListener(v->{
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    if(mListener != null) {
                        mListener.onItemClick(v, pos);
                        emoticonImg.setSelected(true);
                    }
                }
            });
        }
    }
}
