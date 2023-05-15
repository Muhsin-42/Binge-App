package com.example.binge.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.binge.R;
import com.example.binge.databinding.CustomeLoadDialogBinding;

public class CustomeLoadDialog extends Activity{
    Activity activity;
    AlertDialog dialog;

    public CustomeLoadDialog(Activity activity) {
        this.activity = activity;
    }
    public void startCustomeLoadDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custome_load_dialog,null));
        dialog = builder.create();
        dialog.show();
    }
    public void dissmissCustomeLoadDialog()
    {
        dialog.dismiss();
    }
}
