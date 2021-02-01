package com.example.puttaporn.stockmillimed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BarcodeScanerActivity extends AppCompatActivity implements HttpRequestCallback{
    public  static final int REQUEST_QR_SCAN = 4;
    TextView tv_itemcode,tv_headbarcode,tv_itemname,tv_lot,tv_amount,tv_number;
    Button bt_scan,bt_save;
    EditText ed_amount;
    String headbarcode,itemcode;
    int typeINOUT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scaner);
        Bundle bundle = getIntent().getExtras();
        typeINOUT =bundle.getInt("typeINOUT");

        tv_itemcode = (TextView)findViewById(R.id.tv_result);
        tv_itemname= (TextView)findViewById(R.id.tv_result2);
        tv_lot = (TextView)findViewById(R.id.tv_result3);
        tv_headbarcode = (TextView)findViewById(R.id.tv_headbarcode);
        bt_scan = (Button)findViewById(R.id.bt_scan);
        bt_save = (Button)findViewById(R.id.bt_save);
        tv_number = (TextView)findViewById(R.id.tv_number);
        tv_amount = (TextView)findViewById(R.id.tv_amount);
        ed_amount = (EditText) findViewById(R.id.et_amount);

        bt_save.setVisibility(View.INVISIBLE);
        tv_itemcode.setVisibility(View.INVISIBLE);
        tv_itemname.setVisibility(View.INVISIBLE);
        tv_lot.setVisibility(View.INVISIBLE);

        tv_amount.setVisibility(View.INVISIBLE);
        tv_number.setVisibility(View.INVISIBLE);
        ed_amount.setVisibility(View.INVISIBLE);

        if(bundle!=null) {
            headbarcode = bundle.getString("TypeSelect");


        }else{
            headbarcode = "ค่าไม่มาโว้ยยยยยยยยยยยยยยยย";
            //Intent intent = new Intent(BarcodeScanerActivity.this,MainActivity.class);
            //startActivity(intent);
        }
        tv_headbarcode.setText(headbarcode);
        bt_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =     new Intent("com.google.zxing.client.android.SCAN");
                startActivityForResult(Intent.createChooser(intent , "Scan with"),REQUEST_QR_SCAN);

            }
        });

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = ed_amount.getText().toString();
                if(amount.matches(""))
                {
                    Toast.makeText(BarcodeScanerActivity.this, "กรุณากรอกจำนวนสินค้า", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                    {
                        Intent i = new Intent(BarcodeScanerActivity.this,ChannelScanActivity.class);
                        //i.putExtra("typeINOUT2",typeINOUT);
                        Toast.makeText(BarcodeScanerActivity.this,"Save",Toast.LENGTH_LONG).show();
                        //startActivity(i);
                    }




            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode
            , Intent intent) {
        if (requestCode == REQUEST_QR_SCAN && resultCode == RESULT_OK) {
            String contents = intent.getStringExtra("SCAN_RESULT");
            itemcode = contents.substring(0,contents.indexOf("@"));
            String lotitem = contents.substring(contents.indexOf("@")+1,contents.length());
            new BackgoundWorker(BarcodeScanerActivity.this).execute("getitem",itemcode);
            tv_lot.setText("Lot : "+lotitem);
            tv_itemcode.setText("Item Code : "+itemcode);
            tv_lot.setVisibility(View.VISIBLE);
            tv_itemcode.setVisibility(View.VISIBLE);

        }
        if(requestCode == REQUEST_QR_SCAN && resultCode == RESULT_OK )
        {

        }
    }

    @Override
    public void onResult(String[] result, ArrayList<Object> objectses) {
        if(result[0]== BackgoundWorker.TRUE){
            tv_itemname.setText("Item Name : "+result[1]);
            tv_itemname.setVisibility(View.VISIBLE);
            bt_save.setVisibility(View.VISIBLE);

            tv_amount.setVisibility(View.VISIBLE);
            tv_number.setVisibility(View.VISIBLE);
            ed_amount.setVisibility(View.VISIBLE);


        }
    }
}
