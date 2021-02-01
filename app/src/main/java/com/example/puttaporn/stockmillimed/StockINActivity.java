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
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
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

public class StockINActivity extends AppCompatActivity implements HttpRequestCallback {
    TextView tv_itemcodeIN, tv_itemnameIN, tv_lotIN, tv_heanIN, tv_amountIN, tv_uomIN, tv_di_datetime,tv_username,tv_search;
    EditText tv_di_storage;
    Button bt_submid_di, bt_scanAG;
    EditText ed_insertnumber;
    boolean handled;
    Spinner sp_typeIN, sp_di_storge;
    ArrayList<String> typelist = new ArrayList<String>();
    Integer typeINOUT;
    String headtype;
    String storge_qlc_id, storage_true = "";
    String itemcode="", di_itemname, lotitem="", typeorder, item_gi_code,itemcode_Search,lotitem_Search;
    Calendar currentDate = Calendar.getInstance();
    Date date = Calendar.getInstance().getTime();
    String contents, testinsert, insertlocaitem,substr_igocde_item="";
    String wh_id, room_id, shv_id, col_id, row_id;
    String max_qil_sys_id, qlc_sys_id, compcode , uomitem,batch_supp, qli_in, qli_out, qli_status, cr_uid = "MILLIMED", cr_date, upd_uid = "MILLIMED", upd_date;
    String qwh_desc, qrc_room_id, qsh_name, qss_desc;
    Double amountIN, amountOUT, totalamount;
    SimpleDateFormat df;
    String username = "", userigcode = "";
    Double chk_last_balance;
    String chk_in,chk_balance;
    boolean storgeboolean = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_in);
        Bundle bundle = getIntent().getExtras();

        tv_heanIN = findViewById(R.id.tv_mt_headIN);
        tv_itemcodeIN = findViewById(R.id.tv_mt_itemcode_IN);
        tv_itemnameIN = findViewById(R.id.tv_mt_itemname_IN);
        tv_lotIN = findViewById(R.id.tv_mt_lotIN);
        tv_amountIN = findViewById(R.id.tv_mt_amountIN);
        tv_uomIN = findViewById(R.id.tv_uomIN);
        tv_username = findViewById(R.id.tv_username);
        tv_search = findViewById(R.id.tv_search);
        bt_submid_di = findViewById(R.id.bt_mt_okIN);
        bt_scanAG = findViewById(R.id.bt_mt_scan_agIN);
        ed_insertnumber = findViewById(R.id.et_mt_inoutnumberIN);
        sp_typeIN = findViewById(R.id.spn_mt_typeIN);

        //ed_insertnumber.setInputType(InputType.TYPE_CLASS_NUMBER);


        tv_search.setVisibility(View.INVISIBLE);
        if (bundle != null) {
            typeINOUT = bundle.getInt("typeINOUT");
            username = bundle.getString("Username").toUpperCase();
            userigcode = bundle.getString("UserIG_Code");

            compcode = bundle.getString("CompanyCOde");
            lotitem_Search = bundle.getString("LotItemSearch","");
            itemcode_Search = bundle.getString("ItemCodeSearch","");

            itemcode = bundle.getString("item_code","");
            lotitem = bundle.getString("lot","");

            tv_username.setText("User : "+username.toUpperCase());
            tv_lotIN.setText("Lot : " + lotitem);
        }
        if(itemcode.equals(""))
        {
            tv_search.setVisibility(View.VISIBLE);
            try {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                startActivityForResult(Intent.createChooser(intent, "Scan with"), 1);
            } catch (Exception e){
                //Toast.makeText(getBaseContext(),"Please Install Barcode Scanner",Toast.LENGTH_SHORT).show();
            }
        }else{
            tv_search.setVisibility(View.INVISIBLE);
//                    new BackgoundWorker(StockINActivity.this).execute("getitem", itemcode);
//                    new BackgoundWorker(StockINActivity.this).execute("getuomitem", itemcode);
//                    //new BackgoundWorker(StockINActivity.this).execute("getdatasumamountIN", itemcode, batch_supp,compcode);
//                    new BackgoundWorker(StockINActivity.this).execute("getitemigcode", itemcode);

            new BackgoundWorker(StockINActivity.this).execute("getdatabatchsupp", itemcode, lotitem);
            new BackgoundWorker(StockINActivity.this).execute("getitem", itemcode);

            new BackgoundWorker(StockINActivity.this).execute("getuomitem", itemcode);
            //new BackgoundWorker(StockINActivity.this).execute("getdatasumamountIN", itemcode, lotitem,compcode);
            new BackgoundWorker(StockINActivity.this).execute("getitemigcode", itemcode);
                    ed_insertnumber.setText("");

                    tv_itemcodeIN.setText("Item Code : " + itemcode);

            }



//        try {
//            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//            startActivityForResult(Intent.createChooser(intent, "Scan with"), 1);
//        } catch (Exception e){
//            //Toast.makeText(getBaseContext(),"Please Install Barcode Scanner",Toast.LENGTH_SHORT).show();
//        }


        if (typeINOUT == 1) {
            headtype = "IN";
            tv_heanIN.setText(headtype);
            //userigcode.equals("LG") ||userigcode.equals("PM") || userigcode.equals("RM")|| userigcode.equals("FG")
            if (!userigcode.equals("MT")) {
                materialType_IN();
            } else if (userigcode.equals("MT")) {
                typein_MT();
            }
        }

        bt_scanAG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                startActivityForResult(Intent.createChooser(intent, "Scan with"), 1);
            }
        });

        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(StockINActivity.this, SearchItemActivity.class);

                i.putExtra("Username", username);
                i.putExtra("UserIG_Code", userigcode);
                i.putExtra("CompanyCOde", compcode);
                i.putExtra("typeINOUT", typeINOUT);
                startActivity(i);
            }
        });

        bt_submid_di.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String test = ed_insertnumber.getText().toString();
                if (test.isEmpty() || test.length() == 0 || test.equals("") ||test.startsWith(".")) {
                    Toast.makeText(StockINActivity.this, "คุณไม่ได้กรอกข้อมูล 'จำนวน'", Toast.LENGTH_SHORT).show();
                    return;
                }
                String test1 = tv_itemcodeIN.getText().toString();
                if (test1.matches("") || test1.isEmpty() || test1.length() == 0 || test1.equals("")) {
                    Toast.makeText(StockINActivity.this, "กรุณาสแกน QR Code ใหม่", Toast.LENGTH_SHORT).show();
                    return;
                }
                String test2 = tv_itemnameIN.getText().toString();
                if (test2.matches("") || test2.isEmpty() || test2.length() == 0 || test2.equals("")) {
                    Toast.makeText(StockINActivity.this, "กรุณาสแกน QR Code ใหม่", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                startActivityForResult(Intent.createChooser(intent, "Scan with"), 2);
                //String di_storage = storge;

                final Dialog dialog = new Dialog(StockINActivity.this);
                dialog.setTitle("Millimed Stock");
                dialog.setContentView(R.layout.constraint_dialog_layout);
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);

                final Button bt_ok = (Button) dialog.findViewById(R.id.bt_condi_ok);
                Button bt_cancel = (Button) dialog.findViewById(R.id.bt_condi_cancel);
                TextView tv_di_head = (TextView) dialog.findViewById(R.id.tv_condi_head);
                TextView tv_di_itemcode = (TextView) dialog.findViewById(R.id.tv_condi_item_code);
                TextView tv_di_itemname = (TextView) dialog.findViewById(R.id.tv_condi_itemname);
                TextView tv_di_itemlot = (TextView) dialog.findViewById(R.id.tv_condi_lot);
                TextView tv_di_amount = (TextView) dialog.findViewById(R.id.tv_condi_amount);
                TextView tv_di_take = (TextView) dialog.findViewById(R.id.tv_condi_take);
                TextView tv_di_uom = (TextView) dialog.findViewById(R.id.tv_condi_balance);
                TextView tv_hint = dialog.findViewById(R.id.tv_hint);
                tv_di_storage = (EditText) dialog.findViewById(R.id.tv_condi_stroge);

                tv_di_datetime = dialog.findViewById(R.id.tv_condi_datatime);

                tv_hint.setVisibility(View.INVISIBLE);
                tv_di_head.setText(typeorder);
                tv_di_itemcode.setText("Item Code : " + itemcode);
                tv_di_itemname.setText("Item Name : " + di_itemname);
                tv_di_itemlot.setText("Lot : " + batch_supp);
                tv_di_take.setText("Bring In : " + ed_insertnumber.getText().toString());

                df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat ddf = new SimpleDateFormat("dd/MM/yyyy");
                String currentDateandTime = df.format(new Date());
                String ccdate = ddf.format(new Date());
                cr_date = currentDateandTime;
                tv_di_datetime.setText(ccdate);
                tv_di_uom.setText("Uom : " + tv_uomIN.getText().toString());

                tv_di_amount.setVisibility(View.INVISIBLE);
                tv_di_amount.setText("");
                //tv_di_storage.setText("นำเข้าที่ : " + di_storage);
                //bt_ok.setText("Keep at");
                tv_di_storage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        handled=false;
                        if(EditorInfo.IME_ACTION_DONE==actionId || EditorInfo.IME_ACTION_UNSPECIFIED==actionId)
                        {
                            String contents =  tv_di_storage.getText().toString().toUpperCase();
                            storgeboolean = contents.contains("@");
                            storge_qlc_id = contents;
                            if (storgeboolean == true) {
                                Toast.makeText(StockINActivity.this, "กรุณาสเกน QR Code ช่องเก็บของใหม่!!", Toast.LENGTH_SHORT).show();
                                storage_true = "";

                            } else if (storgeboolean == false) {

                                //storage_true = storge_qlc_id;
                                new BackgoundWorker(StockINActivity.this).execute("getnotestorage", contents);
                            }
                        }
                        return false;
                    }
                });

                bt_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //userigcode.equals("LG") || userigcode.equals("PM") || userigcode.equals("RM") || userigcode.equals("FG")
                        if (!userigcode.equals("MT")) {
                            if (storage_true.isEmpty() || storage_true.length() == 0 || storage_true.equals("") || storage_true == null) {
                                Toast.makeText(StockINActivity.this, "เลขที่ช่องเก็บไม่ถูกต้อง!!", Toast.LENGTH_SHORT).show();
                                return;
                            } else {

                                new BackgoundWorker(StockINActivity.this).execute("insertitemlot", contents, itemcode, batch_supp, item_gi_code);
                            }
                        }
                        if (userigcode.equals("MT")) {
                            if (storage_true.isEmpty() || storage_true.length() == 0 || storage_true.equals("") || storage_true == null) {
                                Toast.makeText(StockINActivity.this, "เลขที่ช่องเก็บไม่ถูกต้อง!!", Toast.LENGTH_SHORT).show();
                                return;
                            } else {

                                new BackgoundWorker(StockINActivity.this).execute("insertitemlot", contents, itemcode, batch_supp, item_gi_code);

                            }
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

        final ArrayAdapter<String> selecttype = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, typelist);
        sp_typeIN.setAdapter(selecttype);
        sp_typeIN.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeorder = selecttype.getItem(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                    //new BackgoundWorker(StockINActivity.this).execute("getdatabatchsupp", itemcode, lotitem);
                    String itemcodeMT = itemcode.substring(0, itemcode.indexOf("-"));
                    //userigcode.equals("LG") || userigcode.equals("PM") || userigcode.equals("RM") || userigcode.equals("FG")
                    if (!userigcode.equals("MT")) {
                        if (itemcodeMT.equals("SS02")) {
                            tv_itemnameIN.setText(null);
                            tv_itemcodeIN.setText(null);
                            ed_insertnumber.setText("");
                            tv_lotIN.setText(null);
                            tv_uomIN.setText(null);
                            tv_amountIN.setText(null);
                            Toast.makeText(StockINActivity.this, "กรุณาสแกน QR Code ใหม่!!", Toast.LENGTH_SHORT).show();
                        } else {
                           // new BackgoundWorker(StockINActivity.this).execute("getdatabatchsupp", itemcode, lotitem);
                            new BackgoundWorker(StockINActivity.this).execute("getitem", itemcode);

                            new BackgoundWorker(StockINActivity.this).execute("getuomitem", itemcode);
                            //new BackgoundWorker(StockINActivity.this).execute("getdatasumamountIN", itemcode, lotitem,compcode);
                            new BackgoundWorker(StockINActivity.this).execute("getitemigcode", itemcode);
                            tv_search.setVisibility(View.INVISIBLE);
                            ed_insertnumber.setText("");
                            tv_lotIN.setText("Lot : " + lotitem);
                            tv_itemcodeIN.setText("Item Code : " + itemcode);
                        }
                    } else if (userigcode.equals("MT")) {
                        if (!itemcodeMT.equals("SS02")) {
                            tv_itemnameIN.setText(null);
                            tv_itemcodeIN.setText(null);
                            ed_insertnumber.setText("");
                            tv_lotIN.setText(null);
                            tv_uomIN.setText(null);
                            tv_amountIN.setText(null);
                            Toast.makeText(StockINActivity.this, "กรุณาสแกน QR Code ใหม่!!", Toast.LENGTH_SHORT).show();
                        } else {
                            new BackgoundWorker(StockINActivity.this).execute("getdatabatchsupp", itemcode, lotitem);
                            new BackgoundWorker(StockINActivity.this).execute("getitem", itemcode);

                            new BackgoundWorker(StockINActivity.this).execute("getuomitem", itemcode);

                            //new BackgoundWorker(StockINActivity.this).execute("getdatasumamountIN", itemcode, lotitem,compcode);
                            new BackgoundWorker(StockINActivity.this).execute("getitemigcode", itemcode);
                            tv_search.setVisibility(View.INVISIBLE);
                            ed_insertnumber.setText("");
                            tv_lotIN.setText("Lot : " + lotitem);
                            tv_itemcodeIN.setText("Item Code : " + itemcode);
                        }
                    }
                    //tv_amountIN.setVisibility(View.INVISIBLE);
                    //tv_amountIN.setText("จำนวนคงเหลือ : " + String.valueOf(amountTest));
                } else if (a == false) {
                    tv_itemnameIN.setText(null);
                    tv_itemcodeIN.setText(null);
                    ed_insertnumber.setText("");
                    tv_lotIN.setText(null);
                    tv_uomIN.setText(null);
                    tv_amountIN.setText(null);
                    Toast.makeText(StockINActivity.this, "กรุณาสแกน QR Code ใหม่!!", Toast.LENGTH_SHORT).show();

                    return;
                }
            }

            if (requestCode == 2 && resultCode == RESULT_OK) {

                storge_qlc_id = data.getStringExtra("SCAN_RESULT");
                storgeboolean = storge_qlc_id.contains("@");

                if (storgeboolean == true) {
                    Toast.makeText(StockINActivity.this, "กรุณาสเกน QR Code ช่องเก็บของใหม่!!", Toast.LENGTH_SHORT).show();
                    storage_true = "";
                    return;
                } else if (storgeboolean == false) {

                    //storage_true = storge_qlc_id;
                    new BackgoundWorker(StockINActivity.this).execute("getnotestorage", storge_qlc_id);
                }
            }

    }

    @Override
    public void onResult(String[] result, ArrayList<Object> objectses) {
        if (result[0] == BackgoundWorker.TRUE && result[1] == "getitem") {

            di_itemname = result[2];
            tv_itemnameIN.setText("Item Name : " + di_itemname);

        }
        if (result[0] == BackgoundWorker.TRUE && result[1] == "getuomitem") {

            uomitem = result[2];
            tv_uomIN.setText(uomitem);

        }
        if (result[0] == BackgoundWorker.TRUE && result[1] == "databatchsupp") {

            batch_supp = result[2];
            tv_lotIN.setText("Lot : " + batch_supp);
            new BackgoundWorker(StockINActivity.this).execute("getdatasumamountIN", itemcode, batch_supp,compcode);


        }
        if (result[0] == BackgoundWorker.TRUE && result[1] == "getigcode") {

            String item_gi_code_select = result[2];
            substr_igocde_item = item_gi_code_select.substring(item_gi_code_select.indexOf(" ") + 1, item_gi_code_select.length());

            if(substr_igocde_item.equals(userigcode)){
                item_gi_code = item_gi_code_select;
            }
            else if(userigcode.equals("MT") || userigcode.equals("LG")){
                item_gi_code = item_gi_code_select;
            }
            else {
                tv_itemcodeIN.setText(null);
                tv_itemnameIN.setText(null);
                tv_lotIN.setText(null);
                tv_uomIN.setText(null);
                tv_amountIN.setText(null);
                Toast.makeText(StockINActivity.this, "You do not have access to this item.", Toast.LENGTH_SHORT).show();
            }
            //lotitem = contents.substring(contents.indexOf("@") + 1, contents.length());
        }
        if (result[0] == BackgoundWorker.TRUE && result[1] == "insertqil") {

            testinsert = result[2];
            new BackgoundWorker(StockINActivity.this).execute("getqrcitemlot", itemcode);
        }
        if (result[0] == BackgoundWorker.TRUE && result[1] == "insetlocaitemIN") {

            String tt = result[2];
        }

        if (result[0] == BackgoundWorker.TRUE && result[1] == "sumamountin") {
            try{
                totalamount = Double.parseDouble(result[2]);
            }catch (Exception ex){
                totalamount = 0.0;
            }

            //totalamount = Double.parseDouble(amountINstr);
            if (totalamount == 0) {
                tv_amountIN.setVisibility(View.INVISIBLE);
            }
            else if(substr_igocde_item.equals(userigcode) && totalamount !=0){

                tv_amountIN.setVisibility(View.VISIBLE);
                tv_amountIN.setText(String.valueOf("Balance : " + totalamount));
            }
            else if(userigcode.equals("MT") || userigcode.equals("LG") && totalamount != 0){

                tv_amountIN.setVisibility(View.VISIBLE);
                tv_amountIN.setText(String.valueOf("Balance : " + totalamount.toString().trim()));
            }
            else{
                tv_itemcodeIN.setText(null);
                tv_itemnameIN.setText(null);
                tv_lotIN.setText(null);
                tv_uomIN.setText(null);
                tv_amountIN.setText(null);
            }
//            if (amountINstr.equals("")) {
//                amountIN = 0.000;
//                tv_amountIN.setText(null);
//
//            } else {
//                amountIN = Double.parseDouble(amountINstr);
//                new BackgoundWorker(StockINActivity.this).execute("getdatasumamountOUT", itemcode, lotitem,compcode);
//            }
        }

        if (result[0] == BackgoundWorker.TRUE && result[1] == "insetlocaitemIN") {

            String tt = result[2];
        }

        if (result[0] == BackgoundWorker.TRUE && result[1] == "sumamountout") {

            String amountOUTstr = result[2];
            if (amountOUTstr == "" || amountOUTstr.equals("") || amountOUTstr.matches("")) {
                amountOUT = 0.000;
                tv_amountIN.setText(null);
                //tv_amountIN.setVisibility(View.INVISIBLE);
            } else {
                amountOUT = Double.parseDouble(amountOUTstr);
            }

            totalamount = amountIN - amountOUT;
            if (totalamount == 0) {
                tv_amountIN.setVisibility(View.INVISIBLE);
            }
            else if(substr_igocde_item.equals(userigcode) && totalamount !=0){

                tv_amountIN.setVisibility(View.VISIBLE);
                tv_amountIN.setText(String.valueOf("Balance : " + totalamount));
            }
            else if(userigcode.equals("MT") || userigcode.equals("LG") && totalamount != 0){

                tv_amountIN.setVisibility(View.VISIBLE);
                tv_amountIN.setText(String.valueOf("Balance : " + totalamount));
            }
            else{
                tv_itemcodeIN.setText(null);
                tv_itemnameIN.setText(null);
                tv_lotIN.setText(null);
                tv_uomIN.setText(null);
                tv_amountIN.setText(null);
            }

        }

        if (result[0] == BackgoundWorker.TRUE && result[1] == "sumchk") {
            //Toast.makeText(StockOUTActivity.this, "test", Toast.LENGTH_SHORT).show();
//           String chkbalance =;
            chk_balance =  result[2];

            if(ed_insertnumber.length()!=0){

                chk_in = ed_insertnumber.getText().toString();
                chk_last_balance = Double.parseDouble(chk_balance) - Double.parseDouble(chk_in);
                //Toast.makeText(StockOUTActivity.this, chk_last_balance+"error", Toast.LENGTH_SHORT).show();
                if(Double.parseDouble(chk_in) <= Double.parseDouble(chk_balance) ){
                    //new BackgoundWorker(StockOUTActivity.this).execute("insertitemlot", contents, itemcode, lotitem, item_gi_code);
                }else{
                    Toast.makeText(StockINActivity.this, "จำนวนที่ต้องการเบิกไม่พอ!!", Toast.LENGTH_SHORT).show();
                }
            }else {
                if (chk_balance == null) {
                    tv_amountIN.setText(null);
                    tv_amountIN.setVisibility(View.INVISIBLE);
                    //tv_amountOUT.setText("Balance : " + String.valueOf(amountTest));

                } else {
                    tv_amountIN.setVisibility(View.VISIBLE);
                    tv_amountIN.setText("Balance : " +String.valueOf(chk_balance));
                }

            }
        }

        if (result[0] == BackgoundWorker.TRUE && result[1] == "getqrcitemlot") {
            String maxqlisysid = result[2];
            insertlocaitem = result[3];
            //intmax_qil_sys_id = Integer.parseInt(maxqlisysid);
            max_qil_sys_id = maxqlisysid;

            new BackgoundWorker(StockINActivity.this).execute("insetlocationitemin", max_qil_sys_id, storge_qlc_id, compcode, uomitem, ed_insertnumber.getText().toString(), typeorder, username, cr_date, username, cr_date);

            if (insertlocaitem.equals("success")) {
                Toast.makeText(StockINActivity.this, "Insert Data Success", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(StockINActivity.this, MainActivity.class);
                i.putExtra("Username", username);
                i.putExtra("UserIG_Code", userigcode);
                startActivity(i);
            } else {
                Toast.makeText(StockINActivity.this, "Insert ข้อมูลไม่สำเร็จ", Toast.LENGTH_SHORT).show();
            }
        }
        if (result[0] == BackgoundWorker.TRUE && result[1] == "notestorage") {

            String tt = result[2];
            storage_true = result[2];
            String WhMT = "WarehouseMT";
            if (userigcode.equals("LG") || userigcode.equals("PM") || userigcode.equals("RM") || userigcode.equals("FG")) {
                if (!(storage_true.equals(WhMT))) {
                    tv_di_storage.setText("Keep In : " + storage_true);

                } else {
                    storage_true = "";
                    Toast.makeText(StockINActivity.this, "กรุณาสเกน QR Code ช่องเก็บของใหม่!!", Toast.LENGTH_SHORT).show();
                    return;
                }

            } else if (userigcode.equals("MT")) {
                if (storage_true.equals(WhMT)) {
                    tv_di_storage.setText("Keep In : " + storage_true);
                } else {
                    storage_true = "";
                    Toast.makeText(StockINActivity.this, "กรุณาสเกน QR Code ช่องเก็บของใหม่!!", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

        }

    }

    private void materialType_IN() {

        typelist.add("Sales Return");
        typelist.add("PD");
        typelist.add("Supplier");
        typelist.add("Move");


    }

    private void typein_MT() {
        typelist.add("IN");
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
                Intent i = new Intent(StockINActivity.this, MainActivity.class);
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
