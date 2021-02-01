package com.example.puttaporn.stockmillimed;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginMWSActivity extends AppCompatActivity implements HttpRequestCallback {


    EditText ed_username, ed_password;
    ProgressBar progressBar;
    TextInputLayout textInputLayout_username, textInputLayout_password;
    private int progress = 0;
    ProgressDialog progressDialog;
    Button bt_sign_in;
    String username = "", password = "";
    String useridlogin = "";
    Snackbar snackbar ;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mws);

        ed_username = findViewById(R.id.ed_username);
        ed_password = findViewById(R.id.ed_password);
        progressBar = findViewById(R.id.login_progressBar);
        bt_sign_in = findViewById(R.id.bt_singin);
        textInputLayout_username = findViewById(R.id.inputype_username);
        textInputLayout_password = findViewById(R.id.textInputLayout_password);
        textInputLayout_username.setErrorEnabled(true);
        textInputLayout_password.setErrorEnabled(true);

        bt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = ed_username.getText().toString().toLowerCase();
                password = ed_password.getText().toString().toLowerCase();
                if (username == "" || username.equals("") || username.isEmpty()) {
                    textInputLayout_username.setError("You need to enter a username");
                    return;
                }

                if (password == "" || password.equals("") || password.isEmpty()) {
                    textInputLayout_password.setError("You need to enter a password");
                    return;
                }

                snackbar = Snackbar.make(v,"Loading...",Snackbar.LENGTH_INDEFINITE).setAction("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                        onDestroy();
                    }
                });
                snackbar.show();
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        new BackgoundWorker(LoginMWSActivity.this).execute("getuserlogin", username, password);

                    }
                }).start();


            }
        });


    }

    @Override
    public void onResult(String[] result, ArrayList<Object> objectses) {

        if (result[0] == BackgoundWorker.TRUE && result[1] == "userlogin") {

            useridlogin = result[2];

            if (useridlogin == "fales" || useridlogin.equals("fales")) {
                //progressDialog.dismiss();
                snackbar.dismiss();
                // Toast.makeText(LoginMWSActivity.this, "Login false", Toast.LENGTH_SHORT).show();
                AlertDialog alertDialog = new AlertDialog.Builder(LoginMWSActivity.this).create();
                alertDialog.setTitle("Alert");

                alertDialog.setMessage("Your username or password is not correct.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ed_password.setText("");
                                textInputLayout_username.setErrorEnabled(false);
                                textInputLayout_password.setErrorEnabled(false);
                            }
                        });
                alertDialog.show();

            } else {
                //progressDialog.dismiss();
                snackbar.dismiss();
                new BackgoundWorker(LoginMWSActivity.this).execute("getuserigcode", useridlogin);
                ed_username.setText("");
                ed_password.setText("");
            }


        }
        if (result[0] == BackgoundWorker.TRUE && result[1] == "userigcode") {
            String userigcode;
            userigcode = result[2];

            if(userigcode.equals("LG") || userigcode.equals("ADMIN") || userigcode.equals("MT") || userigcode.equals("PM") || userigcode.equals("RM")|| userigcode.equals("FG"))
            {
                Intent i = new Intent(LoginMWSActivity.this, MainActivity.class);
                //Intent i = new Intent(LoginMWSActivity.this, MainActivity.class);
                Toast.makeText(LoginMWSActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                i.putExtra("Username", username);
                i.putExtra("UserIG_Code", userigcode);
                //i.putExtra("CompanyCOde",);
                startActivity(i);
                }//else{
//                AlertDialog alertDialog = new AlertDialog.Builder(LoginMWSActivity.this).create();
//                alertDialog.setTitle("Alert");
//
//                alertDialog.setMessage("Your user is not have permission in this program.");
//                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                ed_password.setText("");
//                                textInputLayout_username.setErrorEnabled(false);
//                                textInputLayout_password.setErrorEnabled(false);
//                            }
//                        });
//                alertDialog.show();
//            }
        }

    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder dBuilder = new AlertDialog.Builder(this);
        dBuilder.setTitle("Exit");
        dBuilder.setIcon(R.drawable.ic_launcher_foreground);
        dBuilder.setCancelable(true);
        dBuilder.setMessage("Do you want to exit?");
        dBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }
        });

        dBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dBuilder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        int id= android.os.Process.myPid();
        android.os.Process.killProcess(id);
    }
}
