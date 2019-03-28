package com.errorerrorerror.esplightcontrol.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.CycleInterpolator;

import com.errorerrorerror.esplightcontrol.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

public final class DialogCreateUtil {

    private String title;
    private String negativeButtonText;
    private String positiveButtonText;
    private View viewDialog;
    private Context context;

    public DialogCreateUtil() {
    }

    public androidx.appcompat.app.AlertDialog getDialogCreated() {
        //Creates Dialog
        AlertDialog test = new  MaterialAlertDialogBuilder(this.context)
                .setTitle(this.title)
                .setMessage(null)    // Optional
                .setView(this.viewDialog)         // This one holds the EditText
                .setNegativeButton(negativeButtonText, null)
                .setPositiveButton(positiveButtonText, null)
                .setCancelable(false)
                .show();
        Objects.requireNonNull(test.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.dialog_shape));
        test.getWindow().setLayout(
                (int) context.getResources().getDisplayMetrics().density * 475,
                test.getWindow().getAttributes().height
        );

        return test;
    }

    public void shakeAnim(AlertDialog alertDialog) //Shakes dialog animation
    {
        Objects.requireNonNull(Objects.requireNonNull(alertDialog)
                .getWindow())
                .getDecorView()
                .animate()
                .translationX(16f)
                .setInterpolator(new CycleInterpolator(7f));
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNegativeButtonText(String negativeButtonText) {
        this.negativeButtonText = negativeButtonText;
    }

    public void setPositiveButtonText(String positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
    }

    public void setViewDialog(View viewDialog) {
        this.viewDialog = viewDialog;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
