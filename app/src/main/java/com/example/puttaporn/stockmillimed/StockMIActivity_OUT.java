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
import java.util.Calendar;
import java.util.Date;

public class StockMIActivity_OUT extends AppCompatActivity implements HttpRequestCallback{

    TextView tv_mi_head,tv_mi_itemcode,tv_mi_itemname,tv_mi_lot,tv_mi_storage,tv_mi_amount,tv_mi_uom,tv_di_amount,tv_di_storage;
    Button bt_mi_ok,bt_mi_scanag;
    EditText ed_input_out;
    Spinner sp_type_out;
    ArrayList<String> typelist = new ArrayList<String>();
    SimpleDateFormat df;
    String itemcode="",lotitem="",contents="",storge="",di_itemname="",uomitem="",item_gi_code="",headtype="",storage_true="",typeorder="",insertlocaitem="";
    int qli_In=0,qli_Out=0,amountTest=0,typeINOUT=0;
    Date date = Calendar.getInstance().getTime();
    int amountIN=0,amountOUT=0,totalamount=0;
    String max_qil_sys_id="",qlc_sys_id="",compcode="001",upd_uid="MILLIMED",cr_uid="MILLIMED",cr_date="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_mi_out);
        Bundle bundle = getIntent().getExtras();

        tv_mi_head = findViewById(R.id.tv_mt_headOUT);
        tv_mi_itemcode = findViewById(R.id.tv_mt_itemcodeOUT);
        tv_mi_itemname =findViewById(R.id.tv_mt_itemnameOUT);
        tv_mi_lot = findViewById(R.id.tv_mt_lotOUT);
        tv_mi_storage = findViewById(R.id.tv_mt_storage);
        tv_mi_amount = findViewById(R.id.tv_mt_amountOUT);
        tv_mi_uom = findViewById(R.id.tv_mt_uomOUT);
        bt_mi_ok = findViewById(R.id.bt_mt_ok_out);
        bt_mi_scanag = findViewById(R.id.bt_mt_scan_ag_out);
        ed_input_out = findViewById(R.id.et_mt_inoutnumber_out);
        sp_type_out = findViewById(R.id.sp_mt_type_out);

        df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat ddf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateandTime = df.format(new Date());
        String ccdate = ddf.format(new Date());
        cr_date = currentDateandTime;

        Intent intent =     new Intent("com.google.zxing.client.android.SCAN");
        startActivityForResult(Intent.createChooser(intent , "Scan with"),1);

        if(bundle!=null) {
            typeINOUT = bundle.getInt("typeINOUT");
            //compcode = bundle.getString("CompanyCOde");
        }
        if(typeINOUT==2){
            headtype = "OUT";
            tv_mi_head.setText(headtype);
            materialType_OUT();
        }

        final ArrayAdapter<String> selecttype = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,typelist);
        sp_type_out.setAdapter(selecttype);
        sp_type_out.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

                String test = ed_input_out.getText().toString();
                if(test.isEmpty() || test.length() == 0 || test.equals("")|| test.startsWith("0")) {
                    Toast.makeText(StockMIActivity_OUT.this, "คุณไม่ได้กรอกข้อมูล 'จำนวน'", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(storage_true.isEmpty()|| storage_true.length() == 0 || storage_true.equals("")) {
                    Toast.makeText(StockMIActivity_OUT.this, "ไม่มีข้อมูลในระบบ", Toast.LENGTH_SHORT).show();
                    return;
                }

                int takeinoutif = Integer.parseInt(ed_input_out.getText().toString());
                int totalINOUTif = totalamount - takeinoutif;
                if(totalINOUTif < 0)
                {
                    Toast.makeText(StockMIActivity_OUT.this, "จำนวนที่ต้องการเบิกไม่พอ!!", Toast.LENGTH_SHORT).show();
                    return;

                }
                String test1 = tv_mi_itemcode.getText().toString();
                if(test1.matches(""))
                {
                    Toast.makeText(StockMIActivity_OUT.this, "กรุณาสแกน QR Code ใหม่", Toast.LENGTH_SHORT).show();
                    return;
                }
                String test2 = tv_mi_itemname.getText().toString();
                if(test2.matches("")) {
                    Toast.makeText(StockMIActivity_OUT.this, "กรุณาสแกน QR Code ใหม่", Toast.LENGTH_SHORT).show();
                    return;
                }

                final Dialog dialog = new Dialog(StockMIActivity_OUT.this);
                dialog.setTitle("Millimed Stock");
                dialog.setContentView(R.layout.constraint_dialog_layout);
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);

                final Button bt_ok = dialog.findViewById(R.id.bt_condi_ok);
                Button bt_cancel = dialog.findViewById(R.id.bt_condi_cancel);
                TextView tv_di_head = dialog.findViewById(R.id.tv_condi_head);
                TextView tv_di_itemcode = dialog.findViewById(R.id.tv_condi_item_code);
                TextView tv_di_itemname = dialog.findViewById(R.id.tv_condi_itemname);
                TextView tv_di_itemlot = dialog.findViewById(R.id.tv_condi_lot);
                tv_di_amount = dialog.findViewById(R.id.tv_condi_amount);
                TextView tv_di_take = dialog.findViewById(R.id.tv_condi_take);
                TextView tv_di_balance = dialog.findViewById(R.id.tv_condi_balance);
                tv_di_storage = dialog.findViewById(R.id.tv_condi_stroge);
                TextView tv_di_datetime = dialog.findViewById(R.id.tv_condi_datatime);
                TextView tv_hint = dialog.findViewById(R.id.tv_hint);

                new BackgoundWorker(StockMIActivity_OUT.this).execute("getqrcsysid", storage_true);

                int bgcolor_head = Color.parseColor("#CC2F45");
                int bgcolor_bt_ok = Color.parseColor("#A32B95");
                int bgcolor_bt_cancel = Color.parseColor("#B0B5CB");
                tv_di_head.setBackgroundColor(bgcolor_head);
                bt_ok.setBackgroundColor(bgcolor_bt_ok);
                bt_cancel.setBackgroundColor(bgcolor_bt_cancel);
                //new BackgoundWorker(StockMIActivity_OUT.this).execute("getqrcsysid", storge);
                tv_hint.setVisibility(View.INVISIBLE);
                tv_di_head.setText(typeorder);
                tv_di_itemcode.setText("Item Code : " + itemcode);
                tv_di_itemname.setText("Item Name : " + di_itemname);
                tv_di_itemlot.setText("Lot : " + lotitem);
                tv_di_storage.setText("Stay at : "+storage_true);
                tv_di_balance.setText("Balance : "+totalamount);
                tv_di_take.setText("Take : "+ed_input_out.getText().toString());
                tv_di_amount.setVisibility(View.INVISIBLE);



                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = df.format(date);
                tv_di_datetime.setText(formattedDate);

                bt_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new BackgoundWorker(StockMIActivity_OUT.this).execute("insertitemlot", contents,itemcode,lotitem,item_gi_code);
                    }
                });

                bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

        bt_mi_scanag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                startActivityForResult(Intent.createChooser(intent , "Scan with"),1);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {

            contents = data.getStringExtra("SCAN_RESULT");
            boolean a = contents.contains("@");
            if(a==true) {
                itemcode = contents.substring(0, contents.indexOf("@"));
                lotitem = contents.substring(contents.indexOf("@") + 1, contents.length());
                String itemcodeMT = itemcode.substring(0,itemcode.indexOf("-"));
                //String sunitemcode = itemcode.substring()
                if(itemcodeMT.equals("SS02"))
                {
                    new BackgoundWorker(StockMIActivity_OUT.this).execute("getitem", itemcode);
                    new BackgoundWorker(StockMIActivity_OUT.this).execute("getuomitem", itemcode);
                    new BackgoundWorker(StockMIActivity_OUT.this).execute("getitemigcode", itemcode);
                    new BackgoundWorker(StockMIActivity_OUT.this).execute("getstorage_mi", itemcode,lotitem);
                    new BackgoundWorker(StockMIActivity_OUT.this).execute("getdatasumamountIN", itemcode,lotitem);
                    ed_input_out.setText("");
                    tv_mi_lot.setText("Lot : " + lotitem);
                    tv_mi_itemcode.setText("Item Code : " + itemcode);
                }else
                    {
                        tv_mi_itemname.setText(null);
                        tv_mi_itemcode.setText(null);

                        tv_mi_lot.setText(null);
                        tv_mi_amount.setText(null);
                        tv_mi_storage.setText(null);
                        ed_input_out.setText("");
                        Toast.makeText(StockMIActivity_OUT.this, "กรุณาสแกน ItemCode ของแผนก MT", Toast.LENGTH_SHORT).show();
                        return;
                    }


            }else if(a==false)
            {
                tv_mi_itemname.setText(null);
                tv_mi_itemcode.setText(null);
                ed_input_out.setText("");
                tv_mi_lot.setText(null);
                tv_mi_amount.setText(null);
                tv_mi_storage.setText(null);
                Toast.makeText(StockMIActivity_OUT.this, "กรุณาสแกน QR Code ใหม่!!", Toast.LENGTH_SHORT).show();
                return;
            }
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
            //new BackgoundWorker(StockMIActivity_OUT.this).execute("getitemout",itemcode,lotitem);
        }
        if(result[0]== BackgoundWorker.TRUE&&result[1]=="getstoragemi"){

            storage_true = result[2];
            tv_mi_storage.setText("Stay at : "+storage_true);
            new BackgoundWorker(StockMIActivity_OUT.this).execute("sumqliin",itemcode,lotitem,storge);
            //new BackgoundWorker(StockMIActivity_OUT.this).execute("getitemout",itemcode,lotitem);
        }
        if(result[0]== BackgoundWorker.TRUE&&result[1]=="getigcode") {

            item_gi_code = result[2];
            //tv_uomIN.setText(item_gi_code);
            //new BackgoundWorker(StockMIActivity_OUT.this).execute("sumqliout",itemcode,lotitem,storge);
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
                new BackgoundWorker(StockMIActivity_OUT.this).execute("getdatasumamountOUT", itemcode,lotitem);
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

        if(result[0]== BackgoundWorker.TRUE&&result[1]=="insertqil"){

            String testinsert = result[2];
            new BackgoundWorker(StockMIActivity_OUT.this).execute("getqrcitemlot",itemcode);

        }
        if(result[0]== BackgoundWorker.TRUE&&result[1]=="getqrcitemlot"){

            String maxqlisysid = result[2];
            insertlocaitem = result[3];
            //intmax_qil_sys_id = Integer.parseInt(maxqlisysid);
            String getinputnumber = ed_input_out.getText().toString();
            int ed_input = Integer.parseInt(ed_input_out.getText().toString());
            max_qil_sys_id= maxqlisysid;
            int total_int = amountTest - ed_input;
            String total = String.valueOf(total_int);
            new BackgoundWorker(StockMIActivity_OUT.this).execute("insetitemout",max_qil_sys_id,qlc_sys_id,compcode,uomitem,getinputnumber,typeorder,cr_uid,cr_date,upd_uid,cr_date);


            //tv_di_storage.setText(max_qil_sys_id);
            //tv_di_storage.setText("นำเข้าที่ : " +storage_true+max_qil_sys_id+qlc_sys_id);

        }
        if(result[0]== BackgoundWorker.TRUE&&result[1]=="qrcsysID"){

            qlc_sys_id = result[2];
            //tv_uomIN.setText(item_gi_code);


        }
        if(result[0]== BackgoundWorker.TRUE&&result[1]=="insetitemout"){


            String tt = result[2];

            if(tt.equals("success")) {
                Toast.makeText(StockMIActivity_OUT.this, "Insert Data Success", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(StockMIActivity_OUT.this, MainActivity.class);
                startActivity(i);
            }
            else{
                Toast.makeText(StockMIActivity_OUT.this, "Insert ข้อมูลไม่สำเร็จ", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void materialType_OUT(){

        typelist.add("OUT");
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
