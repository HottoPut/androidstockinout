package com.example.puttaporn.stockmillimed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchItemActivity extends AppCompatActivity implements HttpRequestCallback {

    EditText ed_itemcode;
    TextView tv_username;
    TextInputLayout til_itemcode;
    Spinner sp_itemcode,sp_itemlot;
    Button bt_submit,bt_cancel;
    String editemcode="",itemcode_search="",lotitem_search="",item_ig_code="FG",username="",userigcode="",compcode="";
    int typeINOUT;

    ArrayList<GetDataItemCodeInfo> getDataItemCodeInfos;
    DataItemCodeAdapter dataItemCodeAdapter;
    ArrayList<GetDataItemLotInfo> getDataItemLotInfos;
    DataItemLotAdapter dataItemLotAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);
        Bundle bundle = getIntent().getExtras();

        ed_itemcode = findViewById(R.id.et_itemcodeoritemname);
        til_itemcode = findViewById(R.id.til_itemcode);
        sp_itemcode = findViewById(R.id.sp_item);
        sp_itemlot = findViewById(R.id.sp_lot);
        bt_submit = findViewById(R.id.bt_ok);
        bt_cancel = findViewById(R.id.bt_cancel);
        tv_username = findViewById(R.id.tv_username2);

        if (bundle != null) {
            typeINOUT = bundle.getInt("typeINOUT");
            username = bundle.getString("Username").toUpperCase();
            userigcode = bundle.getString("UserIG_Code");
            compcode = bundle.getString("CompanyCOde");

            tv_username.setText("User : "+username.toUpperCase());
        }


        ed_itemcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editemcode = ed_itemcode.getText().toString().toUpperCase();
                new BackgoundWorker(SearchItemActivity.this).execute("getitemcodetolist",editemcode,userigcode);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editemcode = ed_itemcode.getText().toString().toUpperCase();
                new BackgoundWorker(SearchItemActivity.this).execute("getitemcodetolist",editemcode,userigcode);
            }

            @Override
            public void afterTextChanged(Editable s) {
                editemcode = ed_itemcode.getText().toString().toUpperCase();
                new BackgoundWorker(SearchItemActivity.this).execute("getitemcodetolist",editemcode,userigcode);
            }
        });

        sp_itemcode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemcode_search = getDataItemCodeInfos.get(position).item_code.toString();
                new BackgoundWorker(SearchItemActivity.this).execute("getitemlottolist",itemcode_search);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                itemcode_search = getDataItemCodeInfos.get(0).item_code.toString();
                new BackgoundWorker(SearchItemActivity.this).execute("getitemlottolist",itemcode_search);
            }
        });

        sp_itemlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lotitem_search = getDataItemLotInfos.get(position).batch_no.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                lotitem_search = getDataItemLotInfos.get(0).batch_no.toString();
            }
        });
        //new BackgoundWorker(SearchItemActivity.this).execute("getcomp",);


        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchItemActivity.this, StockINActivity.class);
                i.putExtra("Username", username);
                i.putExtra("UserIG_Code", userigcode);
                i.putExtra("CompanyCOde", compcode);
                i.putExtra("typeINOUT",typeINOUT);
                startActivity(i);
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchItemActivity.this, StockINActivity.class);
                i.putExtra("Username", username);
                i.putExtra("UserIG_Code", userigcode);
                i.putExtra("typeINOUT",typeINOUT);
                i.putExtra("CompanyCOde", compcode);
                i.putExtra("ItemCodeSearch", itemcode_search);
                i.putExtra("LotItemSearch", lotitem_search);
                startActivity(i);
            }
        });
    }

    @Override
    public void onResult(String[] result, ArrayList<Object> objectses) {

        if (result[1] == "getitemcodetolist") {

            getDataItemCodeInfos = new ArrayList<>();
            for (Object o : objectses) {
                if (o instanceof GetDataItemCodeInfo)
                    getDataItemCodeInfos.add((GetDataItemCodeInfo) o);
            }

            dataItemCodeAdapter = new DataItemCodeAdapter(this, R.layout.testbg_layout, getDataItemCodeInfos);
            sp_itemcode.setAdapter(dataItemCodeAdapter);
            dataItemCodeAdapter.notifyDataSetChanged();
        }

        if (result[1] == "getitemlottolist") {

            getDataItemLotInfos = new ArrayList<>();
            for (Object o : objectses) {
                if (o instanceof GetDataItemLotInfo)
                    getDataItemLotInfos.add((GetDataItemLotInfo) o);
            }

            dataItemLotAdapter = new DataItemLotAdapter(this, R.layout.testbg_layout, getDataItemLotInfos);
            sp_itemlot.setAdapter(dataItemLotAdapter);
            dataItemLotAdapter.notifyDataSetChanged();
        }

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

                Intent i = new Intent(SearchItemActivity.this, StockINActivity.class);
                i.putExtra("Username", username);
                i.putExtra("UserIG_Code", userigcode);
                i.putExtra("CompanyCOde", compcode);
                i.putExtra("typeINOUT",typeINOUT);
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
