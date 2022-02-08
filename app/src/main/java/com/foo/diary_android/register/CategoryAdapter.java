package com.foo.diary_android.register;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foo.diary_android.R;
import com.foo.diary_android.emoticon.EmoticonAdapter;
import com.foo.diary_android.emoticon.EmoticonData;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private ArrayList<CategoryData> mData = null;

    private CategoryAdapter.OnItemClickListener mListener = null ;

    public CategoryAdapter(ArrayList<CategoryData> list){
        mData=list;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(CategoryAdapter.OnItemClickListener listener) {
        this.mListener = listener ;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.recycler_character_item, parent, false) ;
        CategoryAdapter.ViewHolder vh = new CategoryAdapter.ViewHolder(view) ;
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        CategoryData item = mData.get(position) ;
        holder.characterImg.setImageResource(item.getCategoryImg());
        if(holder.characterImg.isSelected()){
            holder.characterImg.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addItem(int categoryId, String name, int categoryImg) {
        CategoryData itemData = new CategoryData(categoryId,categoryImg,name);
        mData.add(itemData);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView characterImg;

        ViewHolder(View itemView) {
            super(itemView) ;

            characterImg = itemView.findViewById(R.id.character_item_img) ;

            itemView.setOnClickListener(v->{
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    if(mListener != null) {
                        mListener.onItemClick(v, pos);
                        characterImg.setSelected(true);
                    }
                }
            });
        }
    }
}
