package com.enqos.atc.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.enqos.atc.R;

public class AppProgressDialog extends Dialog {

    public AppProgressDialog(Context context) {
        super(context);
    }

    @Override
    public void show() {
        super.show();
        setContentView(R.layout.progress_dialog_layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }


    }
}
