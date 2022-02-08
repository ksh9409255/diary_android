package com.foo.diary_android.mypage;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.foo.diary_android.R;

public class SignoutDialog extends Dialog {
    private TextView textMsg;
    private ImageButton btnOk;
    private ImageButton btnNo;

    private View.OnClickListener okClickListener;
    private View.OnClickListener noClickListener;

    public SignoutDialog(@NonNull Context context,
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

        textMsg.setText("탈퇴 하시겠습니까?");

        btnOk.setOnClickListener(okClickListener);
        btnNo.setOnClickListener(noClickListener);
    }
}
