package com.example.androidloginjava;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LoginForm {

    public static native boolean Check(String str, String str2);

    public static void Start(final Context context) {
        //Load lib file
        System.loadLibrary("yourlib");

        //Create shared preferences to remember user and pass
        final SharedPreferences sharedPreferences = context.getSharedPreferences("SavePref", 0);
        String string = sharedPreferences.getString("User", null);
        String string2 = sharedPreferences.getString("Pass", null);

        //Create LinearLayout
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        //Create username edittext field
        EditText editTextUser = new EditText(context);
        if (string != null && !string.isEmpty()) {
            editTextUser.setText(string);
        }
        editTextUser.setHint("Username...");

        //Create password edittext field
        EditText editTextPass = new EditText(context);
        if (string2 != null && !string2.isEmpty()) {
            editTextPass.setText(string2);
        }
        editTextPass.setHint("Password...");
        editTextPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        //Create checkbox
        CheckBox checkBox = new CheckBox(context);
        checkBox.setChecked(true);
        checkBox.setText("Remember me");

        //Create button
        Button button = new Button(context);
        button.setText("Login");

        //Create button
        Button button2 = new Button(context);
        button2.setText("Cancel");

        linearLayout.addView(editTextUser);
        linearLayout.addView(editTextPass);
        linearLayout.addView(checkBox);
        linearLayout.addView(button);
        linearLayout.addView(button2);

        //Create alertdialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Login");
        builder.setCancelable(false);
        builder.setView(linearLayout);
        AlertDialog show = builder.show();

        final EditText editText3 = editTextUser;
        final EditText editText4 = editTextPass;
        final CheckBox checkBox2 = checkBox;
        final AlertDialog alertDialog = show;

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String user = editText3.getText().toString().trim();
                String pass = editText4.getText().toString().trim();
                    /*if (user.isEmpty() || pass.isEmpty()) {
                        alertDialog.dismiss();
                    }*/
                boolean isChecked = checkBox2.isChecked();
                SharedPreferences.Editor edit = sharedPreferences.edit();
                if (isChecked) {
                    edit.putString("User", user);
                    edit.putString("Pass", pass);
                    edit.apply();
                } else {
                    edit.clear();
                    edit.apply();
                }
                //Check if user and pass match in native lib
                if (Check(user, pass)) {
                    alertDialog.dismiss();
                    //Here you can do something after login
                } else {
                    Toast.makeText(context, "Username or password is incorrect", Toast.LENGTH_LONG).show();
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Process.killProcess(Process.myPid());
            }
        });
    }
}