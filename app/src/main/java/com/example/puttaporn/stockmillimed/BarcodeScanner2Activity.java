package com.example.puttaporn.stockmillimed;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BarcodeScanner2Activity extends AppCompatActivity implements HttpRequestCallback {
    public  static final int REQUEST_QR_SCAN = 4;
    public  static final int REQUEST_QR_SCAN2 = 4;
    String itemcode,headtype,di_itemname, lotitem,typeorder;
    TextView tv_itemcode,tv_itemname,tv_lotitem,tv_head,tv_tv2,tv_amount222;
    Button bt_ScanAg,bt_submit_getdialog;
    EditText et_inoutamount;
    int typeINOUT,amount22222=5000;
    Spinner sp_typeinout;
    String storge,storage_true;
    TextView tv_di_storage;

    int totalINOUT;
    ArrayList<String> typelist = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner2);
        Bundle bundle = getIntent().getExtras();


        tv_head = (TextView)findViewById(R.id.tv_headbarcode2);
        tv_itemcode = (TextView)findViewById(R.id.tv_mt_itemcodeOUT);
        tv_itemname = (TextView)findViewById(R.id.tv_mt_itemnameOUT);
        tv_lotitem = (TextView)findViewById(R.id.tv_lot2);
        tv_amount222 = (TextView)findViewById(R.id.tv_amount222);
        sp_typeinout = (Spinner)findViewById(R.id.sp_mt_type_out);
        bt_ScanAg = (Button)findViewById(R.id.bt_mt_scan_ag_out);
        bt_submit_getdialog = (Button) findViewById(R.id.bt_mt_ok_out);
        et_inoutamount = (EditText)findViewById(R.id.et_mt_inoutnumber_out);
        tv_tv2 = (TextView) findViewById(R.id.textView2);
        et_inoutamount.setVisibility(View.INVISIBLE);
        tv_tv2.setVisibility(View.INVISIBLE);



        Intent intent =     new Intent("com.google.zxing.client.android.SCAN");
        startActivityForResult(Intent.createChooser(intent , "Scan with"),1);

        if(bundle!=null) {
            typeINOUT = bundle.getInt("typeINOUT");
        }
        if(typeINOUT==1){
            headtype = "IN";
            tv_head.setText(headtype);
            materialType_IN();
        } if(typeINOUT==2){
            headtype = "OUT";
            tv_head.setText(headtype);
            materialType_OUT();
        }

        bt_ScanAg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =     new Intent("com.google.zxing.client.android.SCAN");
                startActivityForResult(Intent.createChooser(intent , "Scan with"),1);
            }
        });

        bt_submit_getdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String test = et_inoutamount.getText().toString();
                if(test.matches("")) {
                    Toast.makeText(BarcodeScanner2Activity.this, "คุณไม่ได้กรอกข้อมูล 'จำนวน'", Toast.LENGTH_SHORT).show();
                    return;
                }
                int takeinoutif = Integer.parseInt(et_inoutamount.getText().toString());
                if(headtype.matches("OUT"))
                {
                    //tv_di_storage.setText("อยู่ที่ : "+storage_true);
                    int totalINOUTif = amount22222 - takeinoutif;
                    if(totalINOUTif<0)
                    {
                        Toast.makeText(BarcodeScanner2Activity.this, "จำนวนที่ต้องการเบิกไม่พอ!!", Toast.LENGTH_SHORT).show();
                        return;

                    }
                }
                if(headtype.matches("IN"))
                {
                    Intent intent =     new Intent("com.google.zxing.client.android.SCAN");
                    startActivityForResult(Intent.createChooser(intent , "Scan with"),2);
                    //String di_storage = storge;
                }

//                    Intent intent =     new Intent("com.google.zxing.client.android.SCAN");
//                    startActivityForResult(Intent.createChooser(intent , "Scan with"),2);
//                    //String di_storage = storge;

                    final Dialog dialog = new Dialog(BarcodeScanner2Activity.this);
                    dialog.setTitle("Millimed Stock");
                    dialog.setContentView(R.layout.dialog_layout);

                    final Button bt_ok = (Button) dialog.findViewById(R.id.button_submit);
                    Button bt_cancel = (Button) dialog.findViewById(R.id.button_cancel);
                    TextView tv_di_head = (TextView) dialog.findViewById(R.id.tv_dialog_head);
                    TextView tv_di_itemcode = (TextView) dialog.findViewById(R.id.tv_dialog_itemcode);
                    TextView tv_di_itemname = (TextView) dialog.findViewById(R.id.tv_dialog_itemname);
                    TextView tv_di_itemlot = (TextView) dialog.findViewById(R.id.tv_dialog_lot);
                    TextView tv_di_amount = (TextView) dialog.findViewById(R.id.tv_dialog_amount);
                    TextView tv_di_take = (TextView) dialog.findViewById(R.id.tv_dialog_take);
                    TextView tv_di_balance = (TextView) dialog.findViewById(R.id.tv_dialog_balance);
                     tv_di_storage = (TextView) dialog.findViewById(R.id.tv_dialog_storage);

                    tv_di_head.setText(typeorder);
                    tv_di_itemcode.setText("Item Code : " + itemcode);
                    tv_di_itemname.setText("Item Name : " + di_itemname);
                    tv_di_itemlot.setText("Lot : " + lotitem);
                    tv_di_amount.setText("เหลือในระบบ : " + amount22222);


                    int takeinout = Integer.parseInt(et_inoutamount.getText().toString());
                    if (headtype.equals("OUT")) {
                        totalINOUT = amount22222 - takeinout;
                            tv_di_balance.setText("ยอดคงเหลือ : " + String.valueOf(totalINOUT));

                            tv_di_take.setText("เบิกจำนวน : " + et_inoutamount.getText().toString());
                            //tv_di_storage.setText("นำออกจาก : " + di_storage);

                    }
                    if (headtype.equals("IN")) {
                        totalINOUT = amount22222 + takeinout;
                        tv_di_balance.setText("ยอดคงเหลือ : " + String.valueOf(totalINOUT));
                        tv_di_take.setText("นำเข้าจำนวน : " + et_inoutamount.getText().toString());
                        //tv_di_storage.setText("นำเข้าที่ : " + di_storage);
                        //bt_ok.setText("Keep at");
                    }

                    bt_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    bt_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(headtype.matches("IN"))
                            {
                                if(storage_true.matches("")){
                                    Toast.makeText(BarcodeScanner2Activity.this, "เลขที่เก็บไม่ถูกต้อง!!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else {
                                    Intent i = new Intent(BarcodeScanner2Activity.this, MainActivity.class);
                                    Toast.makeText(BarcodeScanner2Activity.this, "Insert Data Success", Toast.LENGTH_SHORT).show();
                                    startActivity(i);
                                }

                            }
                            if(headtype.matches("OUT")){
                                Intent i = new Intent(BarcodeScanner2Activity.this, MainActivity.class);
                                Toast.makeText(BarcodeScanner2Activity.this, "Insert Data Success", Toast.LENGTH_SHORT).show();
                                startActivity(i);
                            }


                                //ใส่ฟังชั่น Insert ข้อมูลลง Database
                                Intent i = new Intent(BarcodeScanner2Activity.this, MainActivity.class);
                                Toast.makeText(BarcodeScanner2Activity.this, "Insert Data Success", Toast.LENGTH_SHORT).show();
                                startActivity(i);


                        }
                    });

                    dialog.show();

            }
        });

        final ArrayAdapter<String> selecttype = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,typelist);
        sp_typeinout.setAdapter(selecttype);
        sp_typeinout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                et_inoutamount.setVisibility(View.VISIBLE);
                tv_tv2.setVisibility(View.VISIBLE);

                typeorder = selecttype.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if(headtype.matches("IN")) {
                String contents = intent.getStringExtra("SCAN_RESULT");

                itemcode = contents.substring(0, contents.indexOf("@"));
                lotitem = contents.substring(contents.indexOf("@") + 1, contents.length());
                new BackgoundWorker(BarcodeScanner2Activity.this).execute("getitem", itemcode);
                tv_lotitem.setText("Lot : " + lotitem);
                tv_itemcode.setText("Item Code : " + itemcode);
                tv_amount222.setText("จำนวนคงเหลือ : " + String.valueOf(amount22222));
            }
            if(headtype.matches("OUT"))
            {
                String contents = intent.getStringExtra("SCAN_RESULT");

                itemcode = contents.substring(0, contents.indexOf("@"));
                lotitem = contents.substring(contents.indexOf("@") + 1, contents.length());
                new BackgoundWorker(BarcodeScanner2Activity.this).execute("getitem", itemcode);
                new BackgoundWorker(BarcodeScanner2Activity.this).execute("getaddressitem", itemcode,lotitem);
                tv_lotitem.setText("Lot : " + lotitem);
                tv_itemcode.setText("Item Code : " + itemcode);
                tv_amount222.setText("จำนวนคงเหลือ : " + String.valueOf(amount22222));

            }

        }
        if (requestCode == 2 && resultCode == RESULT_OK)
        {
            storge = intent.getStringExtra("SCAN_RESULT");
            boolean a = storge.contains("@");
            if(a== true) {
                Toast.makeText(BarcodeScanner2Activity.this, "กรุณาสเกน QR Code ช่องเก็บของใหม่!!", Toast.LENGTH_SHORT).show();
                storage_true = "";
                return;
            }
            else if(a==false){
                if (headtype.equals("OUT")) {
                    //storage_true = storge;
                    //tv_di_storage.setText("นำออกจาก : " + storage_true);
                }
                if (headtype.equals("IN")) {
                    storage_true = storge;
                    tv_di_storage.setText("นำเข้าที่ : " + storage_true);
                }
            }
            //tv_amount222.setText(intent.getStringExtra("SCAN_RESULT"));
        }

    }




    @Override
    public void onResult(String[] result, ArrayList<Object> objectses) {
        if(result[0]== BackgoundWorker.TRUE&&result[1]=="getitem"){
            di_itemname = result[2];
            tv_itemname.setText(di_itemname);

        }
        if(result[0]== BackgoundWorker.TRUE&&result[1]=="addressitem"){
            storage_true=result[2];
            //tv_di_storage.setText(storage_true);
        }
    }

        private void materialType_IN(){

            typelist.add("คืนขาย");
            typelist.add("คืนผลิต");
            typelist.add("ย้าย");

        }
    private void materialType_OUT(){
        typelist.add("ขาย");
        typelist.add("เบิกผลิต");
        typelist.add("Sample");
        typelist.add("ย้าย");

    }
}
