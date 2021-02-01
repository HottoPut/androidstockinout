package com.example.puttaporn.stockmillimed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements HttpRequestCallback {

    Button bt_stockin, bt_stockout;
    TextView tv_username;

    public static final int REQUEST_QR_SCAN = 4;
    int typeinout = 0;
    String testeee = "test", compcode = "", username = "", userigcode = "",compname="";
    Spinner sp_come_code;
    ArrayList<GetDataCompInfo> getDataCompInfoArrayList;
    DataCompAdapter dataCompAdapter;
    ArrayList<String> typelist = new ArrayList<String>();
    String typewarehouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);

        Bundle bundle = getIntent().getExtras();
        bt_stockin = findViewById(R.id.bt_stock_in);
        bt_stockout = findViewById(R.id.bt_stock_out);
        tv_username = findViewById(R.id.tv_username);
        sp_come_code = findViewById(R.id.sp_comecode);

        if (bundle != null) {
            username = bundle.getString("Username");
            userigcode = bundle.getString("UserIG_Code");
            compcode = bundle.getString("CompanyCOde");
            tv_username.setText("User : " + username.toUpperCase());
        }
        new BackgoundWorker(MainActivity.this).execute("getcomp",testeee);

        bt_stockin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setContentView(R.layout.activity_bar_code);
                //new BackgoundWorker(MainActivity.this).execute("getcustname","MCU-511002");

                Intent i = new Intent(MainActivity.this, MainScanActivity.class);
                typeinout = 1;
                i.putExtra("typeINOUT", typeinout);
                i.putExtra("Username", username);
                i.putExtra("UserIG_Code", userigcode);
                i.putExtra("CompanyCOde", compcode);
                i.putExtra("CompName", compname);


                startActivity(i);

            }
        });

        bt_stockout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, MainScanActivity.class);
                typeinout = 2;
                i.putExtra("typeINOUT", typeinout);
                i.putExtra("Username", username);
                i.putExtra("UserIG_Code", userigcode);
                i.putExtra("CompanyCOde", compcode);
                i.putExtra("CompName", compname);
                startActivity(i);

            }
        });

        sp_come_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                compcode = getDataCompInfoArrayList.get(position).comp_code.toString();
                compname = getDataCompInfoArrayList.get(position).comp_name.toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                compcode = getDataCompInfoArrayList.get(0).comp_code.toString();
                compname = getDataCompInfoArrayList.get(0).comp_name.toString();
            }
        });
    }

    @Override
    public void onResult(String[] result, ArrayList<Object> objectses) {

        if (result[1] == "getcompcode") {

            getDataCompInfoArrayList = new ArrayList<>();
            for (Object o : objectses) {
                if (o instanceof GetDataCompInfo)
                    getDataCompInfoArrayList.add((GetDataCompInfo) o);
            }

            dataCompAdapter = new DataCompAdapter(this, R.layout.testbg_layout, getDataCompInfoArrayList);
            sp_come_code.setAdapter(dataCompAdapter);
            dataCompAdapter.notifyDataSetChanged();
        }
        //Toast.makeText(this,ls.toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder dBuilder = new AlertDialog.Builder(this);
        dBuilder.setTitle("Logout");
        dBuilder.setIcon(R.drawable.ic_launcher_foreground);
        dBuilder.setCancelable(true);
        dBuilder.setMessage("Do you want to logout?");
        dBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
                //moveTaskToBack(true);
                Intent i = new Intent(MainActivity.this, LoginMWSActivity.class);
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

    private void manu() {

        typelist.add("WareHouse Millimed");
        typelist.add("WareHouse MT");

    }
}
