package com.example.myui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    StringRequest jsonrequest;
    private RecyclerView recyclerView;
    private ArrayList<DataAdapter> detaildata;
    private Map<Integer, Object> params;
    private String dName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle bundle = getIntent().getExtras();
        String investorName = WordUtils.capitalizeFully(bundle.getString("InvestorName"));
        if (investorName != null) {
            getSupportActionBar().setTitle(investorName);
        }
        detaildata = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.detailrecycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        requestQueue = Volley.newRequestQueue(this);

        int resValue = R.layout.detail_card;
        dName = "investapp";
        String[] textField = {"folioNo"};
        JSONWork jsonWork = new JSONWork(getApplicationContext(), "first", textField, recyclerView, dName, resValue, investorName);
        params = jsonWork.jsonRequest();
        this.jsonrequest = (StringRequest) params.get(1);
        requestQueue.add(jsonrequest);
        this.detaildata = (ArrayList<DataAdapter>) params.get(2);
    }
}