package com.foo.diary_android.diaryMe;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.foo.diary_android.R;

public class LookDialog extends Dialog {

    private TextView textMsg;
    private ImageButton btnOk;
    private ImageButton btnNo;

    private View.OnClickListener okClickListener;
    private View.OnClickListener noClickListener;


    public LookDialog(@NonNull Context context,
                      View.OnClickListener okClickListener,
                      View.OnClickListener noClickListener) {
        super(context);
        this.okClickListener=okClickListener;
        this.noClickListener=noClickListener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_look);

        textMsg = findViewById(R.id.dialog_look_text);
        btnOk = findViewById(R.id.dialog_btn_ok);
        btnNo = findViewById(R.id.dialog_btn_no);

        btnOk.setOnClickListener(okClickListener);
        btnNo.setOnClickListener(noClickListener);
    }

}
