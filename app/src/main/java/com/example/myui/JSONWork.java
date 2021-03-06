package com.example.myui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.myui.MainActivity.BASE_URL;

public class JSONWork {

    public static RecyclerViewAdapter bindAdapter;
    private Context context;
    private String[] fields;
    private JSONArray array = null;
    private String fileName;
    private ArrayList<DataAdapter> data;
    private RecyclerView recyclerView;
    private Map<Integer, Object> params;
    private String reqName = null;
    private int resValue;
    private String firstReq = null;

    public JSONWork(Context context, String fileName, String[] fields, RecyclerView recyclerView, String reqName, int resValue) {
        this.context = context;
        this.fileName = fileName;
        this.fields = fields;
        this.reqName = reqName;
        this.resValue = resValue;
        this.recyclerView = recyclerView;
        this.data = new ArrayList<DataAdapter>();
        this.params = new HashMap<Integer, Object>();
    }

    public JSONWork(Context context, String fileName, String[] fields, RecyclerView recyclerView, String reqName, int resValue, String firstReq) {
        this.context = context;
        this.fileName = fileName;
        this.fields = fields;
        this.reqName = reqName;
        this.firstReq = firstReq;
        this.resValue = resValue;
        this.recyclerView = recyclerView;
        this.data = new ArrayList<DataAdapter>();
        this.params = new HashMap<Integer, Object>();
    }

    public Map<Integer, Object> jsonRequest() {
        try {
            StringRequest jsonrequest = new StringRequest(Request.Method.POST, BASE_URL + fileName + ".php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        array = new JSONArray(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonSetData(array);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Error Json: " + error, Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> reqParam = new HashMap<String, String>();
                    reqParam.put("REQ", reqName);
                    if (firstReq != null) {
                        reqParam.put("firstREQ", firstReq);
                    }
                    return reqParam;
                }
            };
            params.put(1, jsonrequest);
            params.put(2, data);
            return params;
        } catch (Exception e) {
            Toast.makeText(context, "Req error" + e, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return null;
    }

    private void jsonSetData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                DataAdapter dataAdp = new DataAdapter();
                for (int j = 0; j < fields.length; j++) {
                    Field field = dataAdp.getClass().getDeclaredField(fields[j]);
                    field.set(dataAdp, jsonObject.getString(fields[j]));
                }
                data.add(dataAdp);
            }
        } catch (Exception e) {
            Toast.makeText(context, "Handle: " + e, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        bindAdapter = new RecyclerViewAdapter(data, context, fields, resValue);
        recyclerView.setAdapter(bindAdapter);
    }
}