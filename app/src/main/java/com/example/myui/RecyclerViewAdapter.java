package com.example.myui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.apache.commons.lang3.text.WordUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {

    Context context;
    String[] fields;
    String filterString = null;
    private ArrayList<DataAdapter> mArrayList;
    private ArrayList<DataAdapter> mFilteredList;

    public RecyclerViewAdapter(ArrayList<DataAdapter> dataAdapter, Context context, String[] fields) {
        //super();
        this.mArrayList = dataAdapter;
        this.mFilteredList = dataAdapter;
        this.context = context;
        this.fields = fields;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_recycler, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        DataAdapter dataAdapter1 = mFilteredList.get(position);
        for (int i = 0; i < fields.length; i++) {
            try {
                Method method = dataAdapter1.getClass().getDeclaredMethod("get" + fields[i]);
                String text = WordUtils.capitalizeFully((String) method.invoke(dataAdapter1));

                Field field = holder.getClass().getDeclaredField(fields[i] + "View");
                TextView tV = (TextView) field.get(holder);

                Class cls = field.getType();
                Method method1 = cls.getDeclaredMethod("setText", new Class[]{CharSequence.class});
                method1.invoke(tV, new Object[]{text});

                field.set(holder, tV);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = mArrayList;
                } else {
                    ArrayList<DataAdapter> filteredList = new ArrayList<>();
                    for (DataAdapter dataAdapterNew : mArrayList) {
                        for (int i = 0; i < fields.length; i++) {
                            try {
                                Method method01 = dataAdapterNew.getClass().getDeclaredMethod("get" + fields[i]);
                                filterString = ((String) method01.invoke(dataAdapterNew)).toLowerCase();
                                if (filterString.contains(charString)) {
                                    filteredList.add(dataAdapterNew);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    mFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<DataAdapter>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView invNameView;

        public ViewHolder(View itemView) {
            super(itemView);
            invNameView = (TextView) itemView.findViewById(R.id.searchtext);
        }
    }
}