package com.example.puttaporn.stockmillimed;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
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

import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StockOUTActivity extends AppCompatActivity implements HttpRequestCallback {

    TextView tv_itemcodeOUT, tv_itemname_OUT, tv_lotOUT, tv_heanOUT, tv_amountOUT, tv_di_storage, tv_storage, tv_uomOUT,tv_username;
    TextView tv_di_amount;
    Button bt_submid_di, bt_scanAG;
    EditText ed_insertnumber;
    Spinner sp_typeOUT, sp_storge;
    ArrayList<String> typelist = new ArrayList<String>();
    Integer typeINOUT;
    Double qli_In, qli_Out;
    String headtype,batch_supp;
    String storge = "", storage_true, locasysID, qlc_sys_id,substr_igocde_item="";
    String itemcode, di_itemname, max_qil_sys_id, lotitem, typeorder, uomitem, item_gi_code, contents, insertlocaitem, compcode = "", cr_uid = "MILLIMED", cr_date, upd_uid = "MILLIMED";
    Double amountTest = 0.000, totalINOUT;
    String chk_in ="";
    String chk_balance = "";
    String userigcode = "", username = "";
    Double chk_last_balance;
    Date date = Calendar.getInstance().getTime();
    SimpleDateFormat df;
    ArrayList<GetDateListLotInfo> getDateListLotInfoArrayList;
    //ArrayAdapter<GetDateListLotInfo> chooestype;
    DataLotAdapter dataLotAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_out);
        Bundle bundle = getIntent().getExtras();

        tv_heanOUT = findViewById(R.id.tv_mt_headOUT);
        tv_itemcodeOUT = findViewById(R.id.tv_mt_itemcodeOUT);
        tv_itemname_OUT = findViewById(R.id.tv_mt_itemnameOUT);
        tv_lotOUT = findViewById(R.id.tv_mt_lotOUT);
        tv_amountOUT = findViewById(R.id.tv_mt_amountOUT);
        tv_username = findViewById(R.id.tv_username);
        //tv_storage = findViewById(R.id.tv_storge);
        tv_uomOUT = findViewById(R.id.tv_uomOUT);
        bt_scanAG = findViewById(R.id.bt_mt_scan_ag_out);
        bt_submid_di = findViewById(R.id.bt_mt_ok_out);
        ed_insertnumber = findViewById(R.id.et_mt_inoutnumber_out);
        sp_typeOUT = findViewById(R.id.sp_mt_type_out);
        sp_storge = findViewById(R.id.sp_mi_storge);

        //ed_insertnumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat ddf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateandTime = df.format(new Date());
        String ccdate = ddf.format(new Date());
        cr_date = currentDateandTime;

//        try {
//            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//            startActivityForResult(Intent.createChooser(intent, "Scan with"), 1);
//        }catch (Exception e){
//            Toast.makeText(getBaseContext(),"Please Install Barcode Scanner",Toast.LENGTH_SHORT).show();
//        }


        if (bundle != null) {
            typeINOUT = bundle.getInt("typeINOUT");
            compcode = bundle.getString("CompanyCOde");
            username = bundle.getString("Username").toUpperCase();
            userigcode = bundle.getString("UserIG_Code");
            itemcode = bundle.getString("item_code","");
            lotitem = bundle.getString("lot","");
            tv_username.setText("User : "+username);
        }

        if(itemcode.equals(""))
        {
            //tv_search.setVisibility(View.VISIBLE);
            try {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                startActivityForResult(Intent.createChooser(intent, "Scan with"), 1);
            } catch (Exception e){
                //Toast.makeText(getBaseContext(),"Please Install Barcode Scanner",Toast.LENGTH_SHORT).show();
            }
        }else{
            //tv_search.setVisibility(View.INVISIBLE);
            new BackgoundWorker(StockOUTActivity.this).execute("getitem", itemcode);
            new BackgoundWorker(StockOUTActivity.this).execute("getdatabatchsupp", itemcode, lotitem);
            new BackgoundWorker(StockOUTActivity.this).execute("getuomitem", itemcode);
            new BackgoundWorker(StockOUTActivity.this).execute("getitemigcode", itemcode);
            ed_insertnumber.setText("");
            tv_itemcodeOUT.setText("Item Code : " + itemcode);

        }

        if (typeINOUT == 2) {
            headtype = "OUT";
            tv_heanOUT.setText(headtype);
            //userigcode.equals("LG") || userigcode.equals("PM") || userigcode.equals("RM") || userigcode.equals("FG")
            if (!userigcode.equals("MT")) {
                materialType_OUT();
            } else if (userigcode.equals("MT")) {
                typeout_MT();
            }
        }

        final ArrayAdapter<String> selecttype = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, typelist);
        sp_typeOUT.setAdapter(selecttype);
        sp_typeOUT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeorder = selecttype.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_storge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                storge = getDateListLotInfoArrayList.get(position).qlc_qr_code.toString();
                //tv_di_storage.setText("Storage : "+storge);
                //new BackgoundWorker(StockOUTActivity.this).execute("sumqliin", itemcode, lotitem, storge, compcode);
                new BackgoundWorker(StockOUTActivity.this).execute("sumchkout", itemcode, batch_supp,storge,compcode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                storge = getDateListLotInfoArrayList.get(0).qlc_qr_code.toString();
                //tv_di_storage.setText("Storage : "+storge);
                //new BackgoundWorker(StockOUTActivity.this).execute("sumqliin", itemcode, lotitem, storge, compcode);
                new BackgoundWorker(StockOUTActivity.this).execute("sumchkout", itemcode, batch_supp,storge,compcode);
            }
        });
        //getDateListLotInfoArrayList = new ArrayList<>();
        //final ArrayAdapter<>
        //final ArrayAdapter<GetDateListLotInfo> chooestype = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,getDateListLotInfoArrayList);
        //sp_storge.setAdapter(chooestype);
        bt_submid_di.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String test = ed_insertnumber.getText().toString();
                if (test.isEmpty() || test.length() == 0 || test.equals("") || test.startsWith(".")) {
                    Toast.makeText(StockOUTActivity.this, "คุณไม่ได้กรอกข้อมูล 'จำนวน'", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (storge.isEmpty() || storge.length() == 0 || storge.equals("")) {
                    Toast.makeText(StockOUTActivity.this, "ไม่มีข้อมูลในระบบ", Toast.LENGTH_SHORT).show();
                    return;
                }
                Double takeinoutif;
                try{
                     takeinoutif = Double.parseDouble(ed_insertnumber.getText().toString());
                }catch (Exception ex){
                    takeinoutif=0.00;
                }
                //Double balance_str = Double.parseDouble(tv_amountOUT.getText().toString());
                Double totalINOUTif = Double.parseDouble(chk_balance) - takeinoutif;
                if (totalINOUTif < 0) {
                    Toast.makeText(StockOUTActivity.this, "จำนวนที่ต้องการเบิกไม่พอ!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String test1 = tv_itemcodeOUT.getText().toString();
                if (test1.matches("")) {
                    Toast.makeText(StockOUTActivity.this, "กรุณาสแกน QR Code ใหม่", Toast.LENGTH_SHORT).show();
                    return;
                }
                String test2 = tv_itemname_OUT.getText().toString();
                if (test2.matches("")) {
                    Toast.makeText(StockOUTActivity.this, "กรุณาสแกน QR Code ใหม่", Toast.LENGTH_SHORT).show();
                    return;
                }

                final Dialog dialog = new Dialog(StockOUTActivity.this);
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
                final TextView tv_di_take = dialog.findViewById(R.id.tv_condi_take);
                TextView tv_di_balance = dialog.findViewById(R.id.tv_condi_balance);
                tv_di_storage = dialog.findViewById(R.id.tv_condi_stroge);
                TextView tv_di_datetime = dialog.findViewById(R.id.tv_condi_datatime);
                TextView tv_hint = dialog.findViewById(R.id.tv_hint);

                tv_hint.setVisibility(View.INVISIBLE);
                tv_di_head.setText(typeorder);
                tv_di_itemcode.setText("Item Code : " + itemcode);
                tv_di_itemname.setText("Item Name : " + di_itemname);
                tv_di_itemlot.setText("Lot : " + batch_supp);
                //tv_di_amount.setText("เหลือในระบบ : " + amountTest);
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = df.format(date);
                tv_di_datetime.setText(formattedDate);

                new BackgoundWorker(StockOUTActivity.this).execute("getlocitemsysid", storge);
                new BackgoundWorker(StockOUTActivity.this).execute("getqrcsysid", storge);
                Double takeinout2;
                try{
                    takeinout2 = Double.parseDouble(ed_insertnumber.getText().toString());
                }catch (Exception ex){

                    takeinout2 = 0.000;
                }


                totalINOUT = amountTest - takeinout2;
                tv_di_balance.setText("Balance : " +  chk_balance.trim());
                tv_di_storage.setText("Storage : " + storge);
                tv_di_take.setText("Take : " + takeinout2);
                //tv_di_amount.setText(" "+locasysID);
                tv_di_amount.setVisibility(View.INVISIBLE);

                bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                bt_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        new BackgoundWorker(StockOUTActivity.this).execute("sumchkout", itemcode, batch_supp,storge,compcode);

//                        if(Integer.parseInt(chk_balance) > chk_last_balance){
//
//                            Toast.makeText(StockOUTActivity.this, "จำนวนคงเหลือ เหลือน้อยกว่าจำนวนที่ต้องการจะถอน", Toast.LENGTH_SHORT).show();
//                            //ใส่ฟังชั่น Insert ข้อมูลลง Database
//                            //new BackgoundWorker(StockOUTActivity.this).execute("insertitemlot", contents, itemcode, lotitem, item_gi_code);
//                        }else{
//
//                            Toast.makeText(StockOUTActivity.this, "จำนวนคงเหลือ เหลือน้อยกว่าจำนวนที่ต้องการจะถอน", Toast.LENGTH_SHORT).show();
//                        }




                    }
                });
                dialog.show();
            }
        });

        bt_scanAG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                startActivityForResult(Intent.createChooser(intent, "Scan with"), 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {

            contents = data.getStringExtra("SCAN_RESULT");
            boolean a = contents.contains("@");
            if (a == true) {
                itemcode = contents.substring(0, contents.indexOf("@"));
                lotitem = contents.substring(contents.indexOf("@") + 1, contents.length());
                //String sunitemcode = itemcode.substring()
                String itemcodeMT = itemcode.substring(0, itemcode.indexOf("-"));
                //userigcode.equals("LG") || userigcode.equals("PM") || userigcode.equals("RM") || userigcode.equals("FG")
                if (!userigcode.equals("MT")) {
                    if (itemcodeMT.equals("SS02")) {
                        tv_itemname_OUT.setText(null);
                        tv_itemcodeOUT.setText(null);
                        ed_insertnumber.setText("");
                        tv_lotOUT.setText(null);
                        tv_amountOUT.setText(null);
                        tv_uomOUT.setText(null);
                        sp_storge.setAdapter(null);
                        Toast.makeText(StockOUTActivity.this, "กรุณาสแกน QR Code ใหม่!!", Toast.LENGTH_SHORT).show();
                    } else {
                        new BackgoundWorker(StockOUTActivity.this).execute("getitem", itemcode);
                        new BackgoundWorker(StockOUTActivity.this).execute("getdatabatchsupp", itemcode, lotitem);
                        new BackgoundWorker(StockOUTActivity.this).execute("getuomitem", itemcode);
                        new BackgoundWorker(StockOUTActivity.this).execute("getitemigcode", itemcode);
                        //new BackgoundWorker(StockOUTActivity.this).execute("sumqliin", itemcode, batch_supp, storge, compcode);
                        //new BackgoundWorker(StockOUTActivity.this).execute("sumchkout", itemcode, lotitem,storge,compcode);
                        ed_insertnumber.setText("");
                        //tv_lotOUT.setText("Lot : " + lotitem);
                        tv_itemcodeOUT.setText("Item Code : " + itemcode);
                    }
                } else if (userigcode.equals("MT")) {
                    if (!itemcodeMT.equals("SS02")) {
                        tv_itemname_OUT.setText(null);
                        tv_itemcodeOUT.setText(null);
                        ed_insertnumber.setText("");
                        tv_lotOUT.setText(null);
                        tv_amountOUT.setText(null);
                        sp_storge.setAdapter(null);
                        Toast.makeText(StockOUTActivity.this, "กรุณาสแกน QR Code ใหม่!!", Toast.LENGTH_SHORT).show();
                    } else {
                        new BackgoundWorker(StockOUTActivity.this).execute("getitem", itemcode);
                        new BackgoundWorker(StockOUTActivity.this).execute("getuomitem", itemcode);
                        new BackgoundWorker(StockOUTActivity.this).execute("getdatabatchsupp", itemcode, lotitem);
                        new BackgoundWorker(StockOUTActivity.this).execute("getitemigcode", itemcode);
                        //new BackgoundWorker(StockOUTActivity.this).execute("sumqliin", itemcode, batch_supp, storge, compcode);
                        //new BackgoundWorker(StockOUTActivity.this).execute("sumchkout", itemcode, lotitem,storge,compcode);
                        ed_insertnumber.setText("");
                        //tv_lotOUT.setText("Lot : " + lotitem);
                        tv_itemcodeOUT.setText("Item Code : " + itemcode);
                    }
                }

            } else if (a == false) {
                tv_itemname_OUT.setText(null);
                tv_itemcodeOUT.setText(null);
                ed_insertnumber.setText("");
                tv_lotOUT.setText(null);
                tv_amountOUT.setText(null);
                tv_uomOUT.setText(null);
                Toast.makeText(StockOUTActivity.this, "กรุณาสแกน QR Code ใหม่!!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    @Override
    public void onResult(String[] result, ArrayList<Object> objectses) {
        if (result[0] == BackgoundWorker.TRUE && result[1] == "getitem") {
            di_itemname = result[2];
            tv_itemname_OUT.setText("Item Name : " + di_itemname);

        }

        if (result[0] == BackgoundWorker.TRUE && result[1] == "databatchsupp") {

            batch_supp = result[2];
            tv_lotOUT.setText("Lot : " + batch_supp);
            //new BackgoundWorker(StockOUTActivity.this).execute("sumqliin", itemcode, batch_supp, storge, compcode);
            new BackgoundWorker(StockOUTActivity.this).execute("sumchkout", itemcode, lotitem,storge,compcode);

        }
        if (result[0] == BackgoundWorker.TRUE && result[1] == "getuomitem") {

            uomitem = result[2];
            tv_uomOUT.setText(uomitem);
//            new BackgoundWorker(StockOUTActivity.this).execute("getitemout", itemcode, lotitem,compcode);
        }
        if (result[0] == BackgoundWorker.TRUE && result[1] == "getamount") {

            String amountTT = result[2];

            //amountTest = Integer.parseInt(amountTT);
            //new BackgoundWorker(StockOUTActivity.this).execute("getitemout",itemcode,lotitem);
        }
        if (result[0] == BackgoundWorker.TRUE && result[1] == "locaitemsysid") {

            String loacoutid = result[2];
            locasysID = loacoutid;
            //tv_di_amount.setText(loacoutid);

        }
        //Toast.makeText(StockOUTActivity.this,result[0]+":"+result[1], Toast.LENGTH_SHORT).show();
        if (result[0] == BackgoundWorker.TRUE && result[1] == "sumchk") {
            //Toast.makeText(StockOUTActivity.this, "test", Toast.LENGTH_SHORT).show();
//           String chkbalance =;
           chk_balance =  result[2];

           if(ed_insertnumber.length()!=0){

               chk_in = ed_insertnumber.getText().toString();
               chk_last_balance = Double.parseDouble(chk_balance) - Double.parseDouble(chk_in);
               //Toast.makeText(StockOUTActivity.this, chk_last_balance+"error", Toast.LENGTH_SHORT).show();
               if(Double.parseDouble(chk_in) <= Double.parseDouble(chk_balance) ){
                   new BackgoundWorker(StockOUTActivity.this).execute("insertitemlot", contents, itemcode, batch_supp, item_gi_code);
               }else{
                   Toast.makeText(StockOUTActivity.this, "จำนวนที่ต้องการเบิกไม่พอ!!", Toast.LENGTH_SHORT).show();
               }
           }else {
               if (chk_balance == null) {
                   tv_amountOUT.setText(null);
                   tv_amountOUT.setVisibility(View.INVISIBLE);
                   //tv_amountOUT.setText("Balance : " + String.valueOf(amountTest));

               } else {
                   tv_amountOUT.setVisibility(View.VISIBLE);
                   tv_amountOUT.setText("Balance : " +String.valueOf(chk_balance).trim());
               }

           }
        }
        if (result[0] == BackgoundWorker.TRUE && result[1] == "getigcode") {

            //item_gi_code = result[2];
            String item_gi_code_select = result[2];
            substr_igocde_item = item_gi_code_select.substring(item_gi_code_select.indexOf(" ") + 1, item_gi_code_select.length());
            if(substr_igocde_item.equals(userigcode)){
                item_gi_code = item_gi_code_select;
                new BackgoundWorker(StockOUTActivity.this).execute("getitemout", itemcode, batch_supp,compcode);
            }
            else if(userigcode.equals("MT") || userigcode.equals("LG")){
                item_gi_code = item_gi_code_select;
                new BackgoundWorker(StockOUTActivity.this).execute("getitemout", itemcode, batch_supp,compcode);
            }
            else {
                Toast.makeText(StockOUTActivity.this, "You do not have access to this item.", Toast.LENGTH_SHORT).show();
                tv_itemcodeOUT.setText(null);
                tv_itemname_OUT.setText(null);
                tv_lotOUT.setText(null);
                tv_uomOUT.setText(null);
                tv_amountOUT.setText(null);
            }
            //tv_uomIN.setText(item_gi_code);
        }
        if (result[0] == BackgoundWorker.TRUE && result[1] == "qrcsysID") {

            qlc_sys_id = result[2];
            //tv_uomIN.setText(item_gi_code);
        }

        if (result[0] == BackgoundWorker.TRUE && result[1] == "sumqliin") {

            String sumqliin = result[2];
            if (sumqliin.equals("")) {
                qli_In = 0.000;
                tv_amountOUT.setText(null);
                tv_amountOUT.setVisibility(View.INVISIBLE);

            } else {
                qli_In = Double.parseDouble(sumqliin);
                new BackgoundWorker(StockOUTActivity.this).execute("sumqliout", itemcode, batch_supp, storge, compcode);
            }
        }

        if (result[0] == BackgoundWorker.TRUE && result[1] == "sumqliout") {

            String sumqliout = result[2];
            if (sumqliout == null || sumqliout.isEmpty()) {
                qli_Out = 0.0;

            } else {
                qli_Out = Double.parseDouble(sumqliout);

            }
            Double intamount = qli_In - qli_Out;
            amountTest = intamount;
            String amounbt_srt = amountTest.toString();
            if (amountTest == 0) {
                tv_amountOUT.setText(null);
                tv_amountOUT.setVisibility(View.INVISIBLE);
                //tv_amountOUT.setText("Balance : " + String.valueOf(amountTest));

            } else {

                tv_amountOUT.setVisibility(View.VISIBLE);
                tv_amountOUT.setText("Balance : " +String.valueOf(amountTest));
            }

        }

        if (result[0] == BackgoundWorker.TRUE && result[1] == "insertqil") {

            String testinsert = result[2];
            new BackgoundWorker(StockOUTActivity.this).execute("getqrcitemlot", itemcode);

        }
        if (result[0] == BackgoundWorker.TRUE && result[1] == "getqrcitemlot") {

            String maxqlisysid = result[2];
            insertlocaitem = result[3];
            //intmax_qil_sys_id = Integer.parseInt(maxqlisysid);
            String getinputnumber = ed_insertnumber.getText().toString();
            Double ed_input = Double.parseDouble(ed_insertnumber.getText().toString());
            max_qil_sys_id = maxqlisysid;
            Double total_int = amountTest - ed_input;
            String total = String.valueOf(total_int);
            new BackgoundWorker(StockOUTActivity.this).execute("insetitemout", max_qil_sys_id, qlc_sys_id, compcode, uomitem, ed_insertnumber.getText().toString(), typeorder, username, cr_date, username, cr_date);
            //tv_di_storage.setText(max_qil_sys_id);
            //tv_di_storage.setText("นำเข้าที่ : " +storage_true+max_qil_sys_id+qlc_sys_id);

        }

        if (result[0] == BackgoundWorker.TRUE && result[1] == "insetitemout") {

            String tt = result[2];

            if (tt.equals("success")) {
                Toast.makeText(StockOUTActivity.this, "Insert Data Success", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(StockOUTActivity.this, MainActivity.class);
                i.putExtra("Username", username);
                i.putExtra("UserIG_Code", userigcode);
                startActivity(i);
            } else {
                Toast.makeText(StockOUTActivity.this, "Insert ข้อมูลไม่สำเร็จ", Toast.LENGTH_SHORT).show();
            }
        }
        if (result[1] == "getitemout") {

//            getDateListLotInfoArrayList = new ArrayList<>();
//            ArrayAdapter<Object> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList);
//            sp_storge.setAdapter(adapter);
            getDateListLotInfoArrayList = new ArrayList<>();
            for (Object o : objectses) {
                if (o instanceof GetDateListLotInfo)
                    getDateListLotInfoArrayList.add((GetDateListLotInfo) o);
            }

            //dataLotAdapter = new DataLotAdapter(StockOUTActivity.this,R.layout.testbg_layout,getDateListLotInfoArrayList);
            dataLotAdapter = new DataLotAdapter(this, R.layout.testbg_layout, getDateListLotInfoArrayList);
            sp_storge.setAdapter(dataLotAdapter);
            dataLotAdapter.notifyDataSetChanged();
        }
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getDateListLotInfoArrayList);
    }

    private void materialType_OUT() {
        typelist.add("Sales");
        typelist.add("PD");
        typelist.add("Sample");
        typelist.add("Move");
    }

    private void typeout_MT() {
        typelist.add("OUT");
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder dBuilder = new AlertDialog.Builder(this);
        dBuilder.setTitle("Back");
        dBuilder.setIcon(R.drawable.ic_launcher_foreground);
        dBuilder.setCancelable(true);
        dBuilder.setMessage("Do you want to back?");
        dBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(StockOUTActivity.this, MainActivity.class);
                i.putExtra("Username", username);
                i.putExtra("UserIG_Code", userigcode);
                //i.putExtra("CompanyCOde", compcode);
                startActivity(i);
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
