package com.example.puttaporn.stockmillimed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity implements HttpRequestCallback{
    Button bt_ok;
    EditText ed_custcd;
    TextView tv_cuname;
    String custcode,typeselect;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ed_custcd = findViewById(R.id.ed_custcode);
        tv_cuname = findViewById(R.id.tv_custname);
        bt_ok = findViewById(R.id.bt_summit);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            typeselect = bundle.getString("TypeSelect");

        }else{
            typeselect = "ค่าไม่มาโว้ยยยยยยยยยยยยยยยย";
        }
        tv_cuname.setText(typeselect);
    }

    @Override
    public void onResult(String[] result, ArrayList<Object> objectses) {
        if(result[0]== BackgoundWorker.TRUE){
            //tv_cuname.setText(result[1]);
        }
    }
}
