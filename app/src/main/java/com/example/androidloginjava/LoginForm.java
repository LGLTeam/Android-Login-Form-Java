package com.example.androidloginjava;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Process;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginForm {
    public static String URL = "https://example.com/login.php";

    public static native void Check();

    public static void Start(final Context context) {
        //Load lib file
        //System.loadLibrary("yourlib");

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            //Create shared preferences to remember user and pass
            final SharedPreferences sharedPreferences = context.getSharedPreferences("SavePref", 0);
            String struser = sharedPreferences.getString("User", null);
            String strpass = sharedPreferences.getString("Pass", null);
            Boolean rememberMe = sharedPreferences.getBoolean("RememberMe", false);

            //Create LinearLayout
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setBackgroundColor(Color.parseColor("#0098a8"));
            linearLayout.setPadding(4, 4, 4, 4);

            //Create username edittext field
            EditText editTextUser = new EditText(context);
            if (struser != null && !struser.isEmpty()) {
                editTextUser.setText(struser);
            }
            editTextUser.setHintTextColor(Color.parseColor("#444444"));
            editTextUser.setTextColor(Color.parseColor("#ffffff"));
            editTextUser.setHint("User");

            //Create password edittext field
            EditText editTextPass = new EditText(context);
            if (strpass != null && !strpass.isEmpty()) {
                editTextPass.setText(strpass);
            }
            editTextPass.setHintTextColor(Color.parseColor("#444444"));
            editTextPass.setTextColor(Color.parseColor("#ffffff"));
            editTextPass.setHint("Password");
            editTextPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            //Create checkbox
            CheckBox checkSaveLogin = new CheckBox(context);
            checkSaveLogin.setPadding(0, 5, 0, 5);
            checkSaveLogin.setTextSize(18);
            checkSaveLogin.setChecked(rememberMe);
            checkSaveLogin.setTextColor(Color.rgb(255, 255, 255));
            checkSaveLogin.setText("Save Login");

            //Create btnLogin
            final Button btnLogin = new Button(context);
            btnLogin.setBackgroundColor(Color.parseColor("#37bb83"));
            btnLogin.setTextColor(Color.rgb(255, 255, 255));
            btnLogin.setText("Login");

            //Create btnSair
            final Button btnClose = new Button(context);
            btnClose.setBackgroundColor(Color.parseColor("#b6d3d9"));
            btnClose.setTextColor(Color.rgb(255, 255, 255));
            btnClose.setText("Close");

            //Create username textView
            final TextView textStatus = new TextView(context);
            textStatus.setGravity(Gravity.CENTER_HORIZONTAL);
            textStatus.setBackgroundColor(Color.parseColor("#0098a8"));
            textStatus.setTextColor(Color.rgb(255, 255, 255));
            textStatus.setTextSize(17);
            textStatus.setText("Awaiting login!");

            //Add views
            linearLayout.addView(editTextUser);
            linearLayout.addView(editTextPass);
            linearLayout.addView(checkSaveLogin);
            linearLayout.addView(btnLogin);
            linearLayout.addView(btnClose);
            linearLayout.addView(textStatus);

            //Create alertdialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            //Title
            TextView title = new TextView(context);
            title.setText("YOUR TITLE\nLOGIN");
            title.setPadding(0, 20, 0, 20);
            title.setTextColor(Color.rgb(255, 255, 255));
            title.setGravity(Gravity.CENTER_HORIZONTAL);
            title.setTextSize(20);
            title.setTypeface(null, Typeface.BOLD);
            title.setBackgroundColor(Color.parseColor("#006177"));
            builder.setCustomTitle(title);

            builder.setCancelable(false);
            builder.setView(linearLayout);
            AlertDialog show = builder.show();

            final EditText editText3 = editTextUser;
            final EditText editText4 = editTextPass;
            final CheckBox checkSaveLogin2 = checkSaveLogin;
            final AlertDialog alertDialog = show;

            btnClose.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        btnClose.setBackgroundColor(Color.parseColor("#b6d3d9"));
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        btnClose.setBackgroundColor(Color.parseColor("#a4bbbf"));
                    }
                    return false;
                }
            });

            btnLogin.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        textStatus.setTextColor(Color.parseColor("#ffffff"));
                        textStatus.setText("logging in...");
                        btnLogin.setBackgroundColor(Color.parseColor("#37bb83"));
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        btnLogin.setBackgroundColor(Color.parseColor("#299164"));
                    }
                    return false;
                }
            });

            //Request PHP when pressing login button
            btnLogin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String user = editText3.getText().toString().trim();
                    String pass = editText4.getText().toString().trim();

                    boolean isChecked = checkSaveLogin2.isChecked();
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    if (isChecked) {
                        edit.putString("User", user);
                        edit.putString("Pass", pass);
                    } else {
                        edit.clear();
                    }
                    edit.putBoolean("RememberMe", isChecked);
                    edit.apply();

                    try {
                        String[] result = this.login(user, pass);
                        String status = result[0];
                        String hashS = result[1];
                        String MsgS = result[2];
                        String hashL = this.MD5_Hash(user + pass);

                        if (status.equals("1") && hashS.equals(hashL)) {
                            alertDialog.dismiss();
                            // Check();
                            Toast.makeText(context, MsgS, Toast.LENGTH_LONG).show();
                        } else {
                            textStatus.setTextColor(Color.rgb(255, 255, 0));
                            textStatus.setText(MsgS);
                        }
                    } catch (Exception e) {
                        textStatus.setTextColor(Color.rgb(200, 20, 20));
                        textStatus.setText(e.getMessage());
                    }
                }

                public String[] login(String user, String pass) throws IOException {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody formBody = new FormBody.Builder()
                            .add("user", user)
                            .add("pass", pass)
                            .build();

                    //URL to request
                    Request request = new Request.Builder()
                            .url(URL)
                            .post(formBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    String res = response.body().string();

                    return res.split("\\|");
                }

                public String MD5_Hash(String s) {
                    MessageDigest m = null;

                    try {
                        m = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                    m.update(s.getBytes(), 0, s.length());
                    String hash = new BigInteger(1, m.digest()).toString(16);
                    return hash;
                }
            });

            btnClose.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Process.killProcess(Process.myPid());
                }
            });
        }
    }
}