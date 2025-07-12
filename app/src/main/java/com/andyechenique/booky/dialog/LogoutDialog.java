package com.andyechenique.booky.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.andyechenique.booky.R;

public class LogoutDialog {

    private Dialog dialog;

    public LogoutDialog(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCancelable(false);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }

    public void show() {
        if (!dialog.isShowing()) dialog.show();
    }

    public void dismiss() {
        if (dialog.isShowing()) dialog.dismiss();
    }
}
