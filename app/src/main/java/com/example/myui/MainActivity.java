package com.example.myui;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import static com.example.myui.JSONWork.bindAdapter;

public class MainActivity extends AppCompatActivity {
    public static final String BASE_URL = "http://192.168.2.4/android/";
    RequestQueue requestQueue;
    StringRequest jsonrequest;
    Method method;
    Field field;
    //String[] textField = {"address", "email", "folioNo", "folioDate", "funddesc", "fundCode", "invName", "nav", "navDate", "pan", "phone", "mobile", "trnxStatus", "trnxStatusdesc"};
    private JSONArray array = null;
    private RecyclerView recyclerView;
    private ArrayList<DataAdapter> data;
    private Map<Integer, Object> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        data = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclersearch);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        requestQueue = Volley.newRequestQueue(this);

        String[] textField = {"invName"};
        JSONWork jsonWork = new JSONWork(getApplicationContext(), "names", textField, recyclerView);
        params = jsonWork.jsonRequest();
        this.jsonrequest = (StringRequest) params.get(1);
        requestQueue.add(jsonrequest);
        this.data = (ArrayList<DataAdapter>) params.get(2);
        recyclerView.addOnItemTouchListener(new RecyclerListener(getApplicationContext(), recyclerView, new RecyclerListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                final DataAdapter newData = data.get(position);
                Toast.makeText(getApplicationContext(), newData.getinvName().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (bindAdapter != null) bindAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}