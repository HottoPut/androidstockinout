package com.example.puttaporn.stockmillimed;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

    public class BackgoundWorker extends AsyncTask<String, String, String> {
    public static final String TRUE = "true", FALSE = "false";
    private HttpRequestCallback callBack;
    private String status, type;
    public static final String url_server = "http://192.168.1.17/Millimedstock/test.asmx";

    public BackgoundWorker(HttpRequestCallback callBack) {
        this.callBack = callBack;
    }

    @Override
    protected String doInBackground(String... params) {
        this.type = params[0];
        switch (type) {
            case "testconnect": {
                HashMap<String, String> param = new HashMap<>();
                param.put("test", params[1]);
                httpRequest(url_server + "/HelloWorld?", param);
                break;
            }

            case "getcustname": {
                HashMap<String, String> param = new HashMap<>();
                param.put("cust_code", params[1]);
                httpRequest(url_server + "/GetCustomer?", param);
                break;
            }

            case "getitem": {
                HashMap<String, String> param = new HashMap<>();
                param.put("item_code", params[1]);
                httpRequest(url_server + "/GetItem?", param);
                break;
            }

            case "getuomitem": {
                HashMap<String, String> param = new HashMap<>();
                param.put("item_code", params[1]);
                httpRequest(url_server + "/GetUomItem?", param);
                break;
            }

            case "getuombaseitem": {
                HashMap<String, String> param = new HashMap<>();
                param.put("item_code", params[1]);
                httpRequest(url_server + "/GetUomBaseItem?", param);
                break;
            }

            case "getuommaxitem": {
                HashMap<String, String> param = new HashMap<>();
                param.put("item_code", params[1]);
                httpRequest(url_server + "/GetUomMaxItem?", param);
                break;
            }

//            case "getaddressitem":{
//                HashMap<String,String> param = new HashMap<>();
//                param.put("item_code",params[1]);
//                param.put("item_lot",params[2]);
//                httpRequest(url_server+"/GetAddressItem?",param);
//                break;
//            }

            case "getqrcitemlot": {
                HashMap<String, String> param = new HashMap<>();
                param.put("qil_qr_code", params[1]);
                httpRequest(url_server + "/GetQRCItemLot?", param);
                break;
            }

            case "getitemigcode": {
                HashMap<String, String> param = new HashMap<>();
                param.put("item_code", params[1]);
                httpRequest(url_server + "/GetItemGiCode?", param);
                break;
            }

            case "insertitemlot": {
                HashMap<String, String> param = new HashMap<>();
                param.put("qil_qr_code", params[1]);
                param.put("qil_code", params[2]);
                param.put("qil_lot", params[3]);
                param.put("qil_ig_code", params[4]);
                httpRequest(url_server + "/InsertItemLot?", param);
                break;
            }


            case "getnotestorage": {
                HashMap<String, String> param = new HashMap<>();
                param.put("qlc_sys_id", params[1]);
                httpRequest(url_server + "/GetQRCNote?", param);
                break;
            }


            case "insetlocationitemin": {
                HashMap<String, String> param = new HashMap<>();
                param.put("qli_qil_sys_id", params[1]);
                param.put("qli_qlc_sys_id", params[2]);
                param.put("qli_comp_code", params[3]);
                param.put("qli_uom_code", params[4]);
                param.put("qli_in", params[5]);
                param.put("qli_status", params[6]);
                param.put("qli_cr_uid", params[7]);
                param.put("qli_cr_dt", params[8]);
                param.put("qli_upd_uid", params[9]);
                param.put("qli_upd_dt", params[10]);
                httpRequest(url_server + "/InsertLocationItemIN?", param);
                break;
            }

            case "insetitemout": {
                HashMap<String, String> param = new HashMap<>();
                param.put("qli_qil_sys_id", params[1]);
                param.put("qli_qlc_sys_id", params[2]);
                param.put("qli_comp_code", params[3]);
                param.put("qli_uom_code", params[4]);
                param.put("qli_out", params[5]);
                param.put("qli_status", params[6]);
                param.put("qli_cr_uid", params[7]);
                param.put("qli_cr_dt", params[8]);
                param.put("qli_upd_uid", params[9]);
                param.put("qli_upd_dt", params[10]);
                httpRequest(url_server + "/InsertLocationItemOUT?", param);
                break;
            }

            case "getdatatestdropdown": {
                HashMap<String, String> param = new HashMap<>();
                param.put("item_code", params[1]);
                httpRequest(url_server + "/GetDataTestDropdown?", param);
                break;
            }

            case "getdataamount": {
                HashMap<String, String> param = new HashMap<>();
                param.put("qlc_note", params[1]);
                httpRequest(url_server + "/GetAmount?", param);
                break;
            }

            case "getdatabatchsupp": {
                HashMap<String, String> param = new HashMap<>();
                param.put("item_code", params[1]);
                param.put("batch_substr", params[2]);
                httpRequest(url_server + "/GetBatchSupp?", param);
                break;
            }

            case "getdatasumamountIN": {
                HashMap<String, String> param = new HashMap<>();
                param.put("qil_code", params[1]);
                param.put("qil_lot", params[2]);
                param.put("qli_comp_code", params[3]);
                httpRequest(url_server + "/GetSumAmountIN?", param);
                break;
            }

            case "getdatasumamountOUT": {
                HashMap<String, String> param = new HashMap<>();
                param.put("qil_code", params[1]);
                param.put("qil_lot", params[2]);
                param.put("qli_comp_code", params[3]);
                httpRequest(url_server + "/GetSumAmountOUT?", param);
                break;
            }

            case "getitemout": {
                HashMap<String, String> param = new HashMap<>();
                param.put("qil_code", params[1]);
                param.put("qil_lot", params[2]);
                param.put("qli_comp_code", params[3]);
                httpRequest(url_server + "/GetItemOUT?", param);
                break;
            }


            case "getlocitemsysid": {
                HashMap<String, String> param = new HashMap<>();
                param.put("qlc_note", params[1]);
                httpRequest(url_server + "/GetLocItemSysID?", param);
                break;
            }

            case "getqrcsysid": {
                HashMap<String, String> param = new HashMap<>();
                param.put("qlc_note", params[1]);
                httpRequest(url_server + "/GetQlcSysID?", param);
                break;
            }

            case "getcomp": {
                HashMap<String, String> param = new HashMap<>();
                param.put("qil_code", params[1]);
                httpRequest(url_server + "/GetCompany?", param);
                break;
            }

            case "sumqliin": {
                HashMap<String, String> param = new HashMap<>();
                param.put("qil_code", params[1]);
                param.put("qil_lot", params[2]);
                param.put("qlc_note", params[3]);
                param.put("qli_comp_code", params[4]);
                httpRequest(url_server + "/SumQliIN?", param);
                break;
            }

            case "sumqliout": {
                HashMap<String, String> param = new HashMap<>();
                param.put("qil_code", params[1]);
                param.put("qil_lot", params[2]);
                param.put("qlc_note", params[3]);
                param.put("qli_comp_code", params[4]);
                httpRequest(url_server + "/SumQliOUT?", param);
                break;
            }

            case "getstorage_mi": {
                HashMap<String, String> param = new HashMap<>();
                param.put("qil_code", params[1]);
                param.put("qil_lot", params[2]);
                httpRequest(url_server + "/GetTestItemOut?", param);
                break;
            }

            case "getuserlogin": {
                HashMap<String, String> param = new HashMap<>();
                param.put("username", params[1]);
                param.put("password", params[2]);
                httpRequest(url_server + "/Getuserlogin?", param);
                break;
            }
            case "getuserigcode": {
                HashMap<String, String> param = new HashMap<>();
                param.put("userid", params[1]);
                httpRequest(url_server + "/Getuserigcode?", param);
                break;
            }

            case "getitemcodetolist": {
                HashMap<String, String> param = new HashMap<>();
                param.put("item_code", params[1]);
                param.put("item_ig_code", params[2]);
                httpRequest(url_server + "/GetItemCode?", param);
                break;
            }
            case "getitemlottolist": {
                HashMap<String, String> param = new HashMap<>();
                param.put("item_code", params[1]);
                httpRequest(url_server + "/GetItemLot?", param);
                break;
            }
            case "sumchkout": {
                HashMap<String, String> param = new HashMap<>();
                param.put("qil_code", params[1]);
                param.put("qil_lot", params[2]);
                param.put("qlc_note", params[3]);
                param.put("qli_comp_code", params[4]);
                httpRequest(url_server + "/SumchkOUT?", param);
                break;
            }

        }

        return status;
    }

    private void httpRequest(String urlLink, HashMap<String, String> param) {
        String post_data = "";
        try {
            URL url = new URL(urlLink);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);//ส่งข้อมูลให้serverไหม
            OutputStream outputStream = httpURLConnection.getOutputStream();//สร้างท่อ
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            for (String key : param.keySet()) {
                post_data += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(param.get(key), "UTF-8") + "&";
            }
            bufferedWriter.write(post_data);//เขียนใส่ท่อ
            bufferedWriter.flush();//flushไม่ต้องรอส่งเลย
            bufferedWriter.close();//ปิดท่อ
            outputStream.close();


            httpURLConnection.connect();
            int resPonseStatus = httpURLConnection.getResponseCode();

            InputStream inputStream = httpURLConnection.getInputStream();//ท่อเราดูดมา

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            status = br.readLine();//ดูดข้อมูลมา1บรรทัด

            inputStream.close();
            httpURLConnection.disconnect();

//            if (resPonseStatus == 403) {
//                status = "403";
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPostExecute(String result) {
        //super.onPostExecute(s);

        JSONObject resource = null;
        try {
            resource = new JSONObject(result);
        } catch (Exception e) {
            Log.e("JSON error", "Cann't convert to json object");
            e.printStackTrace();
            return;
        }

        switch (type) {
            case "testcon":
                ArrayList<Object> temp = new ArrayList<>();
                try {
                    JSONArray jsontestconnect = resource.getJSONArray("test");
                    for (int i = 0; i < jsontestconnect.length(); i++) {

                        jsontestconnect.getJSONObject(i).getString("test");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "getcustname": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, resource.getString("cust_name")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, resource.getString("cust_name")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "getitem": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "getitem", resource.getString("item_name")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "getitem", resource.getString("item_name")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "getuomitem": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "getuomitem", resource.getString("item_uom_code")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "getuomitem", resource.getString("item_uom_code")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "getuombaseitem": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "getuombaseitem", resource.getString("item_uom_code")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "getuombaseitem", resource.getString("item_uom_code")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "getuommaxitem": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "getuommaxitem", resource.getString("ITEM_LONG_NAME_2")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "getuommaxitem", resource.getString("ITEM_LONG_NAME_2")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "getitemigcode": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "getigcode", resource.getString("item_ig_code")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "getigcode", resource.getString("item_ig_code")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "getqrcitemlot": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "getqrcitemlot", resource.getString("qil_sys_id"), resource.getString("status")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "getqrcitemlot", resource.getString("qil_sys_id"), resource.getString("status")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "insetlocationitemIN": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "insetlocaitemIN", resource.getString("status")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "insetlocaitemIN", resource.getString("status")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "insetitemout": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "insetitemout", resource.getString("status")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "insetitemout", resource.getString("status")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "insertitemlot": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "insertqil", resource.getString("status")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "insertqil", resource.getString("status")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }


            case "getnotestorage": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "notestorage", resource.getString("qlc_note")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "notestorage", resource.getString("qlc_note")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "getdataamount": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "getamount", resource.getString("qli_in")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "getamount", resource.getString("qli_in")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "getdatasumamountIN": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "sumamountin", resource.getString("Balance")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "sumamountin", resource.getString("Balance")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "getdatasumamountOUT": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "sumamountout", resource.getString("sumOUT")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "sumamountout", resource.getString("sumOUT")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "sumqliin": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "sumqliin", resource.getString("sumqliIN")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "sumqliin", resource.getString("sumqliIN")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "sumchkout": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "sumchk", resource.getString("Balance")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "sumchk", resource.getString("Balance")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "sumqliout": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "sumqliout", resource.getString("sumqliOUT")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "sumqliout", resource.getString("sumqliOUT")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "getlocitemsysid": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "locaitemsysid", resource.getString("qli_sys_id")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "locaitemsysid", resource.getString("qli_sys_id")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "getqrcsysid": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "qrcsysID", resource.getString("qlc_sys_id")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "qrcsysID", resource.getString("qlc_sys_id")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "getstorage_mi": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "getstoragemi", resource.getString("qlc_note")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "getstoragemi", resource.getString("qlc_note")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "getuserlogin": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "userlogin", resource.getString("USERID")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "userlogin", resource.getString("USERID")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "getdatabatchsupp": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "databatchsupp", resource.getString("batch_no")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "databatchsupp", resource.getString("batch_no")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "getuserigcode": {
                String custname;
                try {
                    if (resource.getString("status").equals("success")) {
                        callBack.onResult(new String[]{TRUE, type = "userigcode", resource.getString("IG_CODE")}, null);
                    } else {
                        callBack.onResult(new String[]{FALSE, type = "userigcode", resource.getString("IG_CODE")}, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "getdatatestdropdown": {

                try {
                    ArrayList<Object> temp1 = new ArrayList<>();
                    JSONArray jsongetdatatestdropdown = resource.getJSONArray("item_code");
                    for (int i = 0; i < jsongetdatatestdropdown.length(); i++) {

                        jsongetdatatestdropdown.getJSONObject(i).getString("item_code");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "getitemout": {
                ArrayList<String> sparraylist = new ArrayList<>();
                ArrayList<Object> temp1 = new ArrayList<>();
                try {
                    JSONArray jsongetdatatestdropdown = resource.getJSONArray("QlCqrcode");
                    for (int i = 0; i < jsongetdatatestdropdown.length(); i++) {

                        GetDateListLotInfo getDateListLotInfo = new GetDateListLotInfo(jsongetdatatestdropdown.getJSONObject(i).getString("qli_qlc_sys_id"));
                        //sparraylist.add(jsongetdatatestdropdown.getJSONObject(i).getString("QLC_QR_CODE"));
                        // jsongetdatatestdropdown.getJSONObject(i).getString("QLC_QR_CODE");
                        temp1.add(getDateListLotInfo);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callBack.onResult(new String[]{TRUE, "getitemout"}, temp1);
                break;
            }

            case "getcomp": {
                ArrayList<String> sparraylist = new ArrayList<>();
                ArrayList<Object> temp1 = new ArrayList<>();
                try {
                    JSONArray jsonArray = resource.getJSONArray("Compcode");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        GetDataCompInfo getDataCompInfo = new GetDataCompInfo(jsonArray.getJSONObject(i).getString("comp_code"),
                                jsonArray.getJSONObject(i).getString("comp_name"));
                        //sparraylist.add(jsongetdatatestdropdown.getJSONObject(i).getString("QLC_QR_CODE"));
                        // jsongetdatatestdropdown.getJSONObject(i).getString("QLC_QR_CODE");
                        temp1.add(getDataCompInfo);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callBack.onResult(new String[]{TRUE, "getcompcode"}, temp1);
                break;
            }

            case "getitemcodetolist": {
                ArrayList<String> sparraylist = new ArrayList<>();
                ArrayList<Object> temp1 = new ArrayList<>();
                try {
                    JSONArray jsonArray = resource.getJSONArray("Itemcode");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        GetDataItemCodeInfo getDataItemCodeInfo = new GetDataItemCodeInfo(jsonArray.getJSONObject(i).getString("item_code"));
                        //sparraylist.add(jsongetdatatestdropdown.getJSONObject(i).getString("QLC_QR_CODE"));
                        // jsongetdatatestdropdown.getJSONObject(i).getString("QLC_QR_CODE");
                        temp1.add(getDataItemCodeInfo);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callBack.onResult(new String[]{TRUE, "getitemcodetolist"}, temp1);
                break;
            }

            case "getitemlottolist": {
                ArrayList<String> sparraylist = new ArrayList<>();
                ArrayList<Object> temp1 = new ArrayList<>();
                try {
                    JSONArray jsonArray = resource.getJSONArray("ItemLot");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        GetDataItemLotInfo getDataItemLotInfo = new GetDataItemLotInfo(jsonArray.getJSONObject(i).getString("batch_no"));
                        //sparraylist.add(jsongetdatatestdropdown.getJSONObject(i).getString("QLC_QR_CODE"));
                        // jsongetdatatestdropdown.getJSONObject(i).getString("QLC_QR_CODE");
                        temp1.add(getDataItemLotInfo);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callBack.onResult(new String[]{TRUE, "getitemlottolist"}, temp1);
                break;
            }
//            case "getaddressitem":{
//                String custname;
//                try{
//                    if (resource.getString("status").equals("success")) {
//                        callBack.onResult(new String[]{TRUE,type="addressitem",resource.getString("batch_no")}, null);
//                    } else {
//                        callBack.onResult(new String[]{FALSE,type="addressitem", resource.getString("batch_no")}, null);
//                    }
//                }catch (JSONException e){
//                    e.printStackTrace();
//                }
//            }

        }
    }
}
