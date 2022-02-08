package com.foo.diary_android.emoticon;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.foo.diary_android.R;
import com.foo.diary_android.diary.EnumEmoticonData;
import com.prolificinteractive.materialcalendarview.CalendarDay;

public class EmoticonDialog extends Dialog {

    private TextView title;
    private TextView content;
    private ImageView emoticonImg;

    private int emoticonId;
    private String titleText;
    private String contentText;
    private int img;

    public EmoticonDialog(@NonNull Context context, int emoticonId, String title, String content, int img) {
        super(context);
        this.emoticonId = emoticonId;
        this.titleText=title;
        this.contentText=content;
        this.img=img;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_emoticon);

        title = findViewById(R.id.dialog_emoticon_title);
        content = findViewById(R.id.dialog_emoticon_content);
        emoticonImg = findViewById(R.id.dialog_emoticon_img);

        title.setText(titleText);
        content.setText(contentText);
        emoticonImg.setImageResource(img);
    }
}
