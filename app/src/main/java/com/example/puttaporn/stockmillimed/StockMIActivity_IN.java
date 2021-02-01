package com.example.puttaporn.stockmillimed;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StockMIActivity_IN extends AppCompatActivity implements HttpRequestCallback{
    TextView tv_mi_head,tv_mi_itemcode,tv_mi_itemname,tv_mi_lot,tv_mi_amount,tv_mi_uom,tv_di_storage,tv_di_datetime;
    Spinner sp_mi_typein;
    EditText ed_mi_input;
    Button bt_mi_ok,bt_mi_scanag;
    int typeINOUT=0,amountIN=0,amountOUT=0,totalamount=0;
    String contents="",itemcode="",itemname="",lotitem="",headtype="",typeorder="";
    String di_itemname="",item_gi_code="",uomitem="",cr_date="",storge_qlc_id="",storage_true="";
    String testinsert ="",insertlocaitem="",max_qil_sys_id="",compcode="001",upd_uid="MILLIMED",cr_uid="MILLIMED";
    Boolean storgeboolean;
    ArrayList<String> typelist = new ArrayList<String>();
    SimpleDateFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_mi_in);
        Bundle bundle = getIntent().getExtras();
        tv_mi_head = findViewById(R.id.tv_mt_headIN);
        tv_mi_itemcode = findViewById(R.id.tv_mt_itemcode_IN);
        tv_mi_itemname = findViewById(R.id.tv_mt_itemname_IN);
        tv_mi_lot = findViewById(R.id.tv_mt_lotIN);
        tv_mi_amount = findViewById(R.id.tv_mt_amountIN);
        tv_mi_uom = findViewById(R.id.tv_mt_uomIN);
        sp_mi_typein = findViewById(R.id.spn_mt_typeIN);
        ed_mi_input = findViewById(R.id.et_mt_inoutnumberIN);

        bt_mi_ok = findViewById(R.id.bt_mt_okIN);
        bt_mi_scanag = findViewById(R.id.bt_mt_scan_agIN);

        Intent intent =     new Intent("com.google.zxing.client.android.SCAN");
        startActivityForResult(Intent.createChooser(intent , "Scan with"),1);

        if(bundle!=null) {
            typeINOUT = bundle.getInt("typeINOUT");
            //compcode = bundle.getString("CompanyCOde");
        }
        if(typeINOUT==1){
            headtype = "IN";
            tv_mi_head.setText(headtype);
            materialType_IN();
        }

        bt_mi_scanag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                startActivityForResult(Intent.createChooser(intent , "Scan with"),1);
            }
        });


        final ArrayAdapter<String> selecttype = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,typelist);
        sp_mi_typein.setAdapter(selecttype);
        sp_mi_typein.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeorder = selecttype.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                typeorder = selecttype.getItem(0);
            }
        });

        bt_mi_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String test = ed_mi_input.getText().toString();
                if(test.isEmpty() || test.length() == 0 || test.equals("")|| test.startsWith("0") ) {
                    Toast.makeText(StockMIActivity_IN.this, "คุณไม่ได้กรอกข้อมูล 'จำนวน'", Toast.LENGTH_SHORT).show();
                    return;
                }
                String test1 = tv_mi_itemcode.getText().toString();
                if(test1.matches("") || test1.isEmpty() || test1.length() == 0 || test1.equals(""))
                {
                    Toast.makeText(StockMIActivity_IN.this, "กรุณาสแกน QR Code ใหม่", Toast.LENGTH_SHORT).show();
                    return;
                }
                String test2 = tv_mi_itemname.getText().toString();
                if(test2.matches("") || test2.isEmpty() || test2.length() == 0 || test2.equals(""))
                {
                    Toast.makeText(StockMIActivity_IN.this, "กรุณาสแกน QR Code ใหม่", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent =     new Intent("com.google.zxing.client.android.SCAN");
                startActivityForResult(Intent.createChooser(intent , "Scan with"),2);
                //String di_storage = storge;



                final Dialog dialog = new Dialog(StockMIActivity_IN.this);
                dialog.setTitle("MI Stock");
                dialog.setContentView(R.layout.constraint_dialog_layout);
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);

                final Button bt_ok =  dialog.findViewById(R.id.bt_condi_ok);
                Button bt_cancel =  dialog.findViewById(R.id.bt_condi_cancel);
                TextView tv_di_head = dialog.findViewById(R.id.tv_condi_head);
                TextView tv_di_itemcode = dialog.findViewById(R.id.tv_condi_item_code);
                TextView tv_di_itemname = dialog.findViewById(R.id.tv_condi_itemname);
                TextView tv_di_itemlot = dialog.findViewById(R.id.tv_condi_lot);
                TextView tv_di_amount = dialog.findViewById(R.id.tv_condi_amount);
                TextView tv_di_take = dialog.findViewById(R.id.tv_condi_take);
                TextView tv_di_uom = dialog.findViewById(R.id.tv_condi_balance);
                TextView tv_hint = dialog.findViewById(R.id.tv_hint);
                tv_di_storage = dialog.findViewById(R.id.tv_condi_stroge);

                tv_di_datetime = dialog.findViewById(R.id.tv_condi_datatime);
                int bgcolor_head = Color.parseColor("#CC2F45");
                int bgcolor_bt_ok = Color.parseColor("#A32B95");
                int bgcolor_bt_cancel = Color.parseColor("#B0B5CB");
                tv_di_head.setBackgroundColor(bgcolor_head);
                bt_ok.setBackgroundColor(bgcolor_bt_ok);
                bt_cancel.setBackgroundColor(bgcolor_bt_cancel);

                tv_hint.setVisibility(View.INVISIBLE);
                tv_di_head.setText(typeorder);
                tv_di_itemcode.setText("Item Code : " + itemcode);
                tv_di_itemname.setText("Item Name : " + di_itemname);
                tv_di_itemlot.setText("Lot : " + lotitem);
                tv_di_take.setText("Bring In : " + ed_mi_input.getText().toString());

                df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat ddf = new SimpleDateFormat("dd/MM/yyyy");
                String currentDateandTime = df.format(new Date());
                String ccdate = ddf.format(new Date());
                cr_date = currentDateandTime;
                tv_di_datetime.setText(ccdate);
                tv_di_uom.setText("Uom : "+tv_mi_uom.getText().toString());

                tv_di_amount.setVisibility(View.INVISIBLE);
                tv_di_amount.setText("");
                //tv_di_storage.setText("นำเข้าที่ : " + di_storage);
                //bt_ok.setText("Keep at");


                bt_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(storage_true.isEmpty() || storage_true.length() == 0 || storage_true.equals("") || storage_true == null){
                            Toast.makeText(StockMIActivity_IN.this, "เลขที่ช่องเก็บไม่ถูกต้อง!!", Toast.LENGTH_SHORT).show();
                            return;
                        }else {

                            new BackgoundWorker(StockMIActivity_IN.this).execute("insertitemlot", contents,itemcode,lotitem,item_gi_code);


                        }

                    }
                });

                bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            contents = data.getStringExtra("SCAN_RESULT");
            boolean a = contents.contains("@");
            if(a==true)
            {
                itemcode = contents.substring(0, contents.indexOf("@"));
                lotitem = contents.substring(contents.indexOf("@") + 1, contents.length());
                String itemcodeMT = itemcode.substring(0,itemcode.indexOf("-"));
                if(itemcodeMT.equals("SS02"))
                {
                new BackgoundWorker(StockMIActivity_IN.this).execute("getitem", itemcode);
                new BackgoundWorker(StockMIActivity_IN.this).execute("getuomitem", itemcode);
                new BackgoundWorker(StockMIActivity_IN.this).execute("getitemigcode", itemcode);
                new BackgoundWorker(StockMIActivity_IN.this).execute("getdatasumamountIN", itemcode,lotitem);

                tv_mi_itemcode.setText("Item Code : "+itemcode);
                tv_mi_lot.setText("Lot : "+lotitem);
                }else{
                    tv_mi_itemcode.setText(null);
                    tv_mi_itemname.setText(null);
                    tv_mi_uom.setText(null);
                    tv_mi_lot.setText(null);
                    tv_mi_amount.setText(null);
                    ed_mi_input.setText("");

                    Toast.makeText(StockMIActivity_IN.this,"กรุณาสแกน ItemCode ของแผนก MT", Toast.LENGTH_SHORT).show();
                }


            }else
                {
                    tv_mi_itemcode.setText(null);
                    tv_mi_itemname.setText(null);
                    tv_mi_uom.setText(null);
                    tv_mi_lot.setText(null);
                    tv_mi_amount.setText(null);
                    ed_mi_input.setText("");

                    Toast.makeText(StockMIActivity_IN.this,"กรุณาสแกน QR Code ใหม่!!", Toast.LENGTH_SHORT).show();
                }
        }
        if (requestCode == 2 && resultCode == RESULT_OK)
        {

            storge_qlc_id = data.getStringExtra("SCAN_RESULT");
            storgeboolean = storge_qlc_id.contains("@");


            if(storgeboolean== true) {
                Toast.makeText(StockMIActivity_IN.this, "กรุณาสเกน QR Code ช่องเก็บของใหม่!!", Toast.LENGTH_SHORT).show();
                storage_true = "";
                return;
            }
            else if(storgeboolean==false){

                storage_true = storge_qlc_id;
                new BackgoundWorker(StockMIActivity_IN.this).execute("getnotestorage",storge_qlc_id);
                storage_true = storge_qlc_id;
                ; //+max_qil_sys_id+qlc_sys_id
            }
            //tv_amount222.setText(intent.getStringExtra("SCAN_RESULT"));
        }
    }

    @Override
    public void onResult(String[] result, ArrayList<Object> objectses) {

        if(result[0]== BackgoundWorker.TRUE&&result[1]=="getitem"){

             di_itemname = result[2];
            tv_mi_itemname.setText("Item Name : "+di_itemname);

        }
        if(result[0]== BackgoundWorker.TRUE&&result[1]=="getuomitem"){

             uomitem = result[2];
            tv_mi_uom.setText(uomitem);
        }
        if(result[0]== BackgoundWorker.TRUE&&result[1]=="getigcode"){

            item_gi_code = result[2];
            //tv_mi_amount.setText(item_gi_code);
            //tv_uomIN.setText(item_gi_code);

        }
        if(result[0]== BackgoundWorker.TRUE&&result[1]=="sumamountin"){

            String amountINstr = result[2];
            if(amountINstr.equals("") || amountINstr.isEmpty())
            {
                amountIN = 0;
                //tv_mi_amount.setText(null);
                //tv_mi_amount.setVisibility(View.INVISIBLE);

            }
            else
            {
                amountIN = Integer.parseInt(amountINstr);
                new BackgoundWorker(StockMIActivity_IN.this).execute("getdatasumamountOUT", itemcode,lotitem);
            }

        }
        if(result[0]== BackgoundWorker.TRUE&&result[1]=="sumamountout"){

            String amountOUTstr = result[2];
            if(amountOUTstr == "" || amountOUTstr.isEmpty()){
                amountOUT = 0;
                //tv_mi_amount.setText(null);
                //tv_mi_amount.setVisibility(View.INVISIBLE);
            }
            else {
                amountOUT = Integer.parseInt(amountOUTstr);
            }

            totalamount = amountIN - amountOUT;
            if(totalamount==0){
                tv_mi_amount.setVisibility(View.INVISIBLE);
            }else{

                tv_mi_amount.setVisibility(View.VISIBLE);
                tv_mi_amount.setText(String.valueOf("Balance : "+totalamount));
            }

        }

        if(result[0]== BackgoundWorker.TRUE&&result[1]=="notestorage"){

            storage_true = result[2];
            tv_di_storage.setText("Keep In : " +storage_true);

        }
        if(result[0]== BackgoundWorker.TRUE&&result[1]=="insertqil"){

            testinsert = result[2];
            new BackgoundWorker(StockMIActivity_IN.this).execute("getqrcitemlot",itemcode);
            //new BackgoundWorker(StockINActivity.this).execute("insetlocationitemin",max_qil_sys_id,qlc_sys_id,compcode,uomitem,headtype,typeorder,cr_uid,cr_date,upd_uid,cr_date);
            //tv_di_datetime.setText(testinsert);
//            Intent i = new Intent(StockINActivity.this, MainActivity.class);
//            Toast.makeText(StockINActivity.this, "Insert Data Success", Toast.LENGTH_SHORT).show();
//            startActivity(i);
            //tv_uomIN.setText(uomitem);
        }
        if(result[0]== BackgoundWorker.TRUE&&result[1]=="getqrcitemlot"){
            String maxqlisysid = result[2];
            insertlocaitem = result[3];
            //intmax_qil_sys_id = Integer.parseInt(maxqlisysid);
            max_qil_sys_id= maxqlisysid;

            new BackgoundWorker(StockMIActivity_IN.this).execute("insetlocationitemin",max_qil_sys_id,storge_qlc_id,compcode,uomitem,ed_mi_input.getText().toString(),typeorder,cr_uid,cr_date,upd_uid,cr_date);


            if(insertlocaitem.equals("success")) {
                Toast.makeText(StockMIActivity_IN.this, "Insert Data Success", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(StockMIActivity_IN.this, MainActivity.class);
                startActivity(i);
            }
            else{
                Toast.makeText(StockMIActivity_IN.this, "Insert ข้อมูลไม่สำเร็จ", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void materialType_IN(){

        typelist.add("IN");

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
                finish();
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
}
