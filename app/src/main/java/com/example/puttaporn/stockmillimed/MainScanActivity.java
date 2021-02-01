package com.example.puttaporn.stockmillimed;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainScanActivity extends AppCompatActivity {
    EditText et_scan;
    Button bt_enter_scan;
    TextView tv_test,tv_head;
    String itemcode,lotitem;
    boolean handled;

    String compcode,username,userigcode;
    int typeINOUT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_scan);
        Bundle bundle = getIntent().getExtras();
        et_scan = findViewById(R.id.et_scan);
        bt_enter_scan = findViewById(R.id.bt_enterscan);
        tv_test = findViewById(R.id.tv_test);
        tv_head = findViewById(R.id.tv_head);

        if (bundle != null) {
            typeINOUT = bundle.getInt("typeINOUT");
            compcode = bundle.getString("CompanyCOde");
            username = bundle.getString("Username").toUpperCase();
            userigcode = bundle.getString("UserIG_Code");

            //tv_username.setText("User : "+username);

            if (typeINOUT == 1) {
               String headtype = "IN";
                tv_head.setText(headtype);
            }else if(typeINOUT == 2){
                tv_head.setText("OUT");
            }
        }

        bt_enter_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typeINOUT == 1){
                    Intent intent = new Intent(MainScanActivity.this,StockINActivity.class);
                    //intent.putExtra("item_code",itemcode);
                    //intent.putExtra("lot",lotitem);
                    intent.putExtra("typeINOUT", typeINOUT);
                    intent.putExtra("Username", username);
                    intent.putExtra("UserIG_Code", userigcode);
                    intent.putExtra("CompanyCOde", compcode);

                    startActivity(intent);
                }else if (typeINOUT == 2)
                {
                    Intent intent = new Intent(MainScanActivity.this,StockOUTActivity.class);
                    //intent.putExtra("item_code",itemcode);
                    //intent.putExtra("lot",lotitem);
                    intent.putExtra("typeINOUT", typeINOUT);
                    intent.putExtra("Username", username);
                    intent.putExtra("UserIG_Code", userigcode);
                    intent.putExtra("CompanyCOde", compcode);
                    startActivity(intent);
                }
            }
        });

        et_scan.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                handled=false;
                if(EditorInfo.IME_ACTION_DONE==actionId || EditorInfo.IME_ACTION_UNSPECIFIED==actionId)
                {
                  String contents =  et_scan.getText().toString().toUpperCase();
                    boolean a = contents.contains("@");
                    itemcode = contents.substring(0, contents.indexOf("@"));
                    lotitem = contents.substring(contents.indexOf("@") + 1, contents.length());

                    tv_test.setText(itemcode+"++"+lotitem);
                    et_scan.setText("");
                    handled=true;

                    if(typeINOUT == 1){
                        Intent intent = new Intent(MainScanActivity.this,StockINActivity.class);
                        intent.putExtra("item_code",itemcode);
                        intent.putExtra("lot",lotitem);
                        intent.putExtra("typeINOUT", typeINOUT);
                        intent.putExtra("Username", username);
                        intent.putExtra("UserIG_Code", userigcode);
                        intent.putExtra("CompanyCOde", compcode);

                        startActivity(intent);
                    }else if (typeINOUT == 2)
                    {
                        Intent intent = new Intent(MainScanActivity.this,StockOUTActivity.class);
                        intent.putExtra("item_code",itemcode);
                        intent.putExtra("lot",lotitem);
                        intent.putExtra("typeINOUT", typeINOUT);
                        intent.putExtra("Username", username);
                        intent.putExtra("UserIG_Code", userigcode);
                        intent.putExtra("CompanyCOde", compcode);
                        startActivity(intent);
                    }



                }
                return false;
            }
        });
    }
}
