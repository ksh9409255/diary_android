package com.foo.diary_android.register;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.foo.diary_android.R;

public class InfoDialog extends Dialog {
    private TextView textMsg;
    private ImageButton btnOk;
    private String content;

    private View.OnClickListener okClickListener;

    public InfoDialog(@NonNull Context context,
                      View.OnClickListener okClickListener,String content) {
        super(context);
        this.okClickListener=okClickListener;
        this.content=content;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info);

        textMsg = findViewById(R.id.dialog_look_text);
        btnOk = findViewById(R.id.dialog_btn_ok);

        textMsg.setText(content);

        btnOk.setOnClickListener(okClickListener);
    }
}
