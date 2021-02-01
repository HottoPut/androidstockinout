package com.example.puttaporn.stockmillimed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChannelScanActivity extends AppCompatActivity {

     ArrayList<String> typelist = new ArrayList<String>();
    Button bt_summit;
    TextView tv_head;
    Spinner sp_type;
    String typeselect,typehead;
    int typeinout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_scan);
        Bundle bundle = getIntent().getExtras();
        sp_type = (Spinner)findViewById(R.id.sp_type);
       bt_summit = (Button)findViewById(R.id.bt_summitsp);
       tv_head = (TextView)findViewById(R.id.tv_head);

        if(bundle!=null)
        {
            typeinout = bundle.getInt("typeINOUT");
        }


        if(typeinout==1){
            typehead = "IN";
            tv_head.setText(typehead);
            materialType_IN();
        } if(typeinout==2){
            typehead = "OUT";
            tv_head.setText(typehead);
            materialType_OUT();
        }


        final ArrayAdapter<String> selecttype = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,typelist);
        sp_type.setAdapter(selecttype);

        sp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ChannelScanActivity.this,"Select :"+selecttype.getItem(position),Toast.LENGTH_LONG).show();
                typeselect = selecttype.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bt_summit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChannelScanActivity.this,BarcodeScanerActivity.class);
                intent.putExtra("TypeSelect",typeselect);
                intent.putExtra("TypeINOUT",typeinout);
                startActivity(intent);
            }
        });

    }

    private void materialType_IN(){

        typelist.add("คืนขาย");
        typelist.add("คืนผลิต");
        typelist.add("ของใหม่");
        typelist.add("ย้าย");

    }
    private void materialType_OUT(){
        typelist.add("ขาย");
        typelist.add("เบิกผลิต");
        typelist.add("Sample");
        typelist.add("ย้าย");

    }
}
